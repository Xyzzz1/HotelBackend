package com.rabbiter.hotel.service.manager;

import com.rabbiter.hotel.common.Configuration;
import com.rabbiter.hotel.component.TimerManager;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:调度策略，用信号量保证互斥
 */
@Service
public class QueueManager {

    public static final int WAIT = 1;
    public static final int SERVICE = 2;
    private final Semaphore semaphoreWait = new Semaphore(1);
    private final Semaphore semaphoreService = new Semaphore(1);
    private final Semaphore semaphoreTask = new Semaphore(1);

    private final TimerManager timerManager = new TimerManager();
    private RecordManager recordManager = new RecordManager();

    @Resource
    private PowerManager powerManager;

    /**
     * 提出服务请求时的调度策略
     *
     * @param airConditionerStatusDTO
     * @return 标识，从服务/等待队列中移除
     */
    public int enQueue(AirConditionerStatusDTO airConditionerStatusDTO) {
        if (!QueueDTO.isFull()) { //资源数够用之间加入服务队列
            addToService(airConditionerStatusDTO);
            return SERVICE;
        }
        int windSpeed = airConditionerStatusDTO.getWindSpeed();
        int lowestServiceQueueWindSpeed = getLowestServiceQueueWindSpeed();
        if (windSpeed < lowestServiceQueueWindSpeed) { //小于最低风速的情况，直接移入等待队列,不分配时间片
            try {
                semaphoreWait.acquire();
                QueueDTO.waitQueue.add(airConditionerStatusDTO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphoreWait.release();
            }
            return WAIT;
        } else if (windSpeed > lowestServiceQueueWindSpeed) { //优先级调度
            AirConditionerStatusDTO removeDTO = priorityDispatch(airConditionerStatusDTO); //将被抢占的服务队列对象
            removeFromService(removeDTO);
            addToWait(removeDTO);
            addToService(airConditionerStatusDTO);
            return SERVICE;
        } else { //轮询调度
            addToWait(airConditionerStatusDTO);
            return WAIT;
        }
    }


    /**
     * 用户关机或到达房间温度时的调度策略
     *
     * @param airConditionerStatusDTO
     * @param reason
     * @return 标识，从服务/等待队列中移除
     */
    public int deQueue(AirConditionerStatusDTO airConditionerStatusDTO, int reason) {
        try {
            powerManager.powerOff(airConditionerStatusDTO, reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (QueueDTO.waitQueue.contains(airConditionerStatusDTO)) {
            removeFromWait(airConditionerStatusDTO);
            return WAIT;
        } else {
            removeFromService(airConditionerStatusDTO);
            AirConditionerStatusDTO newDTO = selectNew();
            if (newDTO != null) {
                removeFromWait(newDTO);
                addToService(newDTO);
            }
            return SERVICE;
        }
    }

    /**
     * 移入等待队列的对象时间片到期后执行的任务
     *
     * @param airConditionerStatusDTO
     */
    private void enQueueTask(AirConditionerStatusDTO airConditionerStatusDTO) {
        if (!QueueDTO.isFull()) { //资源数够用之间加入服务队列
            addToService(airConditionerStatusDTO);
        }
        int windSpeed = airConditionerStatusDTO.getWindSpeed();
        int lowestServiceQueueWindSpeed = getLowestServiceQueueWindSpeed();
        if (windSpeed < lowestServiceQueueWindSpeed) { //小于最低风速的情况，直接移入等待队列,不分配时间片
            ;//do nothing
        } else if (windSpeed > lowestServiceQueueWindSpeed) { //优先级调度
            AirConditionerStatusDTO removeDTO = priorityDispatch(airConditionerStatusDTO); //将被抢占的服务队列对象
            removeFromService(removeDTO);
            addToWait(removeDTO);
            removeFromWait(airConditionerStatusDTO);
            addToService(airConditionerStatusDTO);
        } else { //轮询调度
            AirConditionerStatusDTO seizedDTO = null;
            for (AirConditionerStatusDTO removedDTO : QueueDTO.serviceQueue) {
                if (removedDTO.getWindSpeed() == windSpeed) {
                    seizedDTO = removedDTO;
                    break;
                }
            }
            removeFromService(seizedDTO);
            removeFromWait(airConditionerStatusDTO);
            addToWait(seizedDTO);
            addToService(airConditionerStatusDTO);
        }
    }


    /**
     * 服务时长到期执行的任务：从等待队列选择一个优先级最高，若有多个选择等待时间最长的
     *
     * @param dto
     * @return
     */
    Runnable taskForService(AirConditionerStatusDTO dto) {
        return () -> {
            try {
                semaphoreTask.acquire();
                if (!QueueDTO.serviceQueue.contains(dto))
                    return;
                removeFromService(dto);
                try {
                    powerManager.powerOff(dto, 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AirConditionerStatusDTO newDTO = selectNew();
                if (newDTO != null) {
                    removeFromWait(newDTO);
                    addToService(newDTO);
                }

                //test
                System.out.println("SERVICE" + dto.getRoomId() + ": time slice expire hit, current time:" + new Date());
                System.out.println("service queue: " + QueueDTO.serviceQueue);
                System.out.println("wait queue: " + QueueDTO.waitQueue);
                System.out.println(timerManager.getTimers().keySet());

                System.out.println("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphoreTask.release();
            }
        };
    }


    /**
     * 被抢占的对象移入等待队列，时间片到期执行的任务: 按照优先级策略找出服务队列风速最低的对象，若有多个，则找到服务时长最长(在队头)的作为牺牲品，移入等待队列
     *
     * @param dto
     * @return
     */
    Runnable taskForWait(AirConditionerStatusDTO dto) {
        return () -> {
            try {
                semaphoreTask.acquire();
                if (QueueDTO.serviceQueue.contains(dto))
                    return;
                enQueueTask(dto);

                //test
                System.out.println("WAIT" + dto.getRoomId() + ": time slice expire hit, current time:" + new Date());
                System.out.println("service queue: " + QueueDTO.serviceQueue);
                System.out.println("wait queue: " + QueueDTO.waitQueue);
                System.out.println(timerManager.getTimers().keySet());
                System.out.println("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphoreTask.release();
            }
        };
    }

    /**
     * 从等待队列中移除，并移除计时器
     *
     * @param dto
     */
    private void removeFromWait(AirConditionerStatusDTO dto) {
        timerManager.removeObject(dto);
        try {
            semaphoreWait.acquire();
            if (!QueueDTO.waitQueue.contains(dto))
                return;
            QueueDTO.waitQueue.remove(dto);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphoreWait.release();
        }
    }

    /**
     * 从服务队列移除，并更新targetDuration，移除计时器
     *
     * @param dto
     */
    private void removeFromService(AirConditionerStatusDTO dto) {
        if (dto.getTargetDuration() != -1) {
            long startTime = timerManager.getStartTime(dto);
            long timeGap = (System.currentTimeMillis() / 1000 - startTime) * Configuration.timeChangeRate; //当前已经服务的(模拟)时间
            dto.setTargetDuration(dto.getTargetDuration() - (int) timeGap);
        }
        timerManager.removeObject(dto);

        try {
            semaphoreService.acquire();
            if (!QueueDTO.serviceQueue.contains(dto))
                return;
            QueueDTO.serviceQueue.remove(dto);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphoreService.release();
        }

    }

    /**
     * 加入服务队列并新建一个定时器，定时器到期任务为taskForService
     *
     * @param dto
     */
    private void addToService(AirConditionerStatusDTO dto) {
        try {
            if (QueueDTO.serviceQueue.contains(dto))
                return;
            semaphoreService.acquire();
            dto.setPowerOnTime(new Date());
            QueueDTO.serviceQueue.add(dto);
            try {
                powerManager.powerOn(dto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphoreService.release();
        }
        long delay = Long.MAX_VALUE;
        if (dto.getTargetDuration() != -1) //考虑没有设置定时器的情况
            delay = dto.getTargetDuration() / Configuration.timeChangeRate;
        timerManager.addObjectWithTimer(dto, delay, TimeUnit.SECONDS, taskForService(dto));
    }


    /**
     * 优先级较低，轮询时加入等待队列会调度，新建一个定时器，到期任务分别执行taskForWait，taskForPoll
     *
     * @param dto
     */
    private void addToWait(AirConditionerStatusDTO dto) {
        try {
            if (QueueDTO.waitQueue.contains(dto) || dto.getTargetDuration() == 0)
                return;
            semaphoreWait.acquire();
            QueueDTO.waitQueue.add(dto);
            try {
                powerManager.waiting(dto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphoreWait.release();
        }

        timerManager.addObjectWithTimer(dto, Configuration.timeSlice / Configuration.timeChangeRate, TimeUnit.SECONDS, taskForWait(dto));


    }


    /**
     * 从等待队列选择一个优先级最高，若有多个选择等待时间最长的
     * 三种情况会调度：用户关机，达到设定的服务时长，达到设定的目标温度
     *
     * @return
     */
    public AirConditionerStatusDTO selectNew() {
        AirConditionerStatusDTO addDTO = QueueDTO.waitQueue.peek();
        if (addDTO == null)
            return null;

        for (AirConditionerStatusDTO dto : QueueDTO.waitQueue) {
            if (dto.getWindSpeed() > addDTO.getWindSpeed()) {
                addDTO = dto;
            }
        }

        return addDTO;
    }


    /**
     * 按照优先级返回被抢占的对象
     *
     * @param airConditionerStatusDTO
     * @return
     */
    private AirConditionerStatusDTO priorityDispatch(AirConditionerStatusDTO airConditionerStatusDTO) {
        List<AirConditionerStatusDTO> lowerPriorityDTO = new ArrayList<>();

        int windSpeed = airConditionerStatusDTO.getWindSpeed();
        for (AirConditionerStatusDTO serviceDTO : QueueDTO.serviceQueue) {
            if (serviceDTO.getWindSpeed() < windSpeed)
                lowerPriorityDTO.add(serviceDTO);
        }

        if (lowerPriorityDTO.size() == 1) { //只有一个，直接返回
            return lowerPriorityDTO.get(0);
        } else {
            boolean isAllEqual = true;
            int lowestWindSpeed = lowerPriorityDTO.get(0).getWindSpeed();
            for (AirConditionerStatusDTO dto : lowerPriorityDTO) {
                if (dto.getWindSpeed() != lowestWindSpeed) {
                    isAllEqual = false;
                    if (dto.getWindSpeed() < lowestWindSpeed) {
                        lowestWindSpeed = dto.getWindSpeed();
                    }
                }
            }
            if (isAllEqual) { //所有优先级较低的对象风速都相等
                AirConditionerStatusDTO maxDurationdto = lowerPriorityDTO.get(0);
                for (AirConditionerStatusDTO dto : lowerPriorityDTO) {
                    if (dto.getTargetDuration() == -1) { //未设定服务时长，认为无穷
                        maxDurationdto = dto;
                        break;
                    }
                    if (dto.getTargetDuration() > maxDurationdto.getTargetDuration()) {
                        maxDurationdto = dto;
                    }
                }
                return maxDurationdto;
            } else {
                for (AirConditionerStatusDTO dto : lowerPriorityDTO) {
                    if (dto.getWindSpeed() == lowestWindSpeed) //返回最低的风速的服务对象
                        return dto;
                }
            }
        }
        return null;
    }

    /**
     * 得到当前服务队列的最低风速
     *
     * @return
     */
    private int getLowestServiceQueueWindSpeed() {
        int lowestWindSpeed = QueueDTO.serviceQueue.peek().getWindSpeed();
        for (AirConditionerStatusDTO serviceDTO : QueueDTO.serviceQueue) {
            if (serviceDTO.getWindSpeed() < lowestWindSpeed)
                lowestWindSpeed = serviceDTO.getWindSpeed();
        }
        return lowestWindSpeed;
    }

    /**
     * 更新空调对象的用户设定的时间，需要考虑-1的情况，以及在等待/服务队列
     */
    public boolean updateDuration(Integer roomId, Integer targetDuration) {
        for (AirConditionerStatusDTO updateDTO : QueueDTO.waitQueue) {
            if (updateDTO.getRoomId() == roomId) {
                updateDTO.setTargetDuration(targetDuration);
                recordManager.updateAndAdd(updateDTO);
                return true;
            }
        }

        for (AirConditionerStatusDTO updateDTO : QueueDTO.serviceQueue) {
            if (updateDTO.getRoomId() == roomId) {
                updateDTO.setTargetDuration(targetDuration);
                recordManager.updateAndAdd(updateDTO);
                timerManager.removeObject(updateDTO);
                long delay = Long.MAX_VALUE;
                if (targetDuration != -1) //考虑没有设置定时器的情况
                    delay = targetDuration / Configuration.timeChangeRate;
                timerManager.addObjectWithTimer(updateDTO, delay, TimeUnit.SECONDS, taskForService(updateDTO));
                return true;
            }
        }

        return false;
    }

}
