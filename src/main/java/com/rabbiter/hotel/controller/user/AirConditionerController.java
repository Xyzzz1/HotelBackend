package com.rabbiter.hotel.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.ConstantCode;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.facility.airconditioner.PollQueue;
import com.rabbiter.hotel.facility.airconditioner.PriorityQueue;
import com.rabbiter.hotel.facility.airconditioner.QueueController;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.sse.SseEmitterServer;
import com.rabbiter.hotel.util.WebUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.json.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/service/conditioner")
public class AirConditionerController {
    private final Logger logger = LoggerFactory.getLogger(SseEmitterServer.class);
    @Resource
    private OrderService orderService;

    @GetMapping(value = "/getRoomID")
    public CommonResult<Integer> getRoomID() {
        CommonResult<Integer> commonResult = new CommonResult<>();
        User user = (User) WebUtils.getSession().getAttribute("loginUser");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", user.getId());
        Date currentDate = new Date();
        Integer roomID = null;
        List<Order> orders = orderService.getBaseMapper().selectList(queryWrapper);
        for (Order order : orders) {
            if (currentDate.before(order.getLeaveTime()) && currentDate.after(order.getInTime())) {
                roomID = order.getRoomId();
                break;
            }
        }
        if (roomID != null) {
            commonResult.setData(roomID);
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        }
        return commonResult;
    }

    @GetMapping(value = "/status")
    public CommonResult<AirConditionerStatusDTO> status(@RequestParam("roomId") Integer roomID) {
        CommonResult<AirConditionerStatusDTO> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(QueueDTO.SERVICE_QUEUE, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(QueueDTO.WAIT_QUEUE, roomID);
        } else {
            commonResult.setData(airConditionerStatusDTO);
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            return commonResult;
        }

        commonResult.setData(airConditionerStatusDTO);
        if (airConditionerStatusDTO == null) {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        } else {
            commonResult.setCode(ConstantCode.WAITING); //在等待队列
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        logger.info("/status: " + commonResult.toString());
        return commonResult;
    }


    @PostMapping(value = "/turnOn")
    public CommonResult<String> turnOn(@RequestBody AirConditionerStatusDTO airConditionerStatusDTO)  {
        User user = (User) WebUtils.getSession().getAttribute("loginUser");
        airConditionerStatusDTO.setUserID(user.getId());
        CommonResult<String> commonResult = new CommonResult<>();
        QueueDTO.setQueueType(QueueDTO.SERVICE_QUEUE);
        if (!QueueDTO.isFull()) {
            QueueDTO.enqueue(airConditionerStatusDTO);
            commonResult.setData("加入服务队列");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());

            //插入记录


        } else {
            QueueDTO.setQueueType(QueueDTO.WAIT_QUEUE);
            QueueDTO.enqueue(airConditionerStatusDTO);
            commonResult.setData("加入等待队列");
            commonResult.setCode(ConstantCode.WAITING);
        }

        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        logger.info("/turnOn: " + commonResult.toString());
        return commonResult;
    }


    @PostMapping(value = "/adjustTargetTemperature")
    public CommonResult<String> adjustTargetTemperature(@RequestParam("roomId") Integer roomID, @RequestParam("targetTemperature") Integer temp) {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(QueueDTO.SERVICE_QUEUE, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(QueueDTO.WAIT_QUEUE, roomID);
        }

        if (airConditionerStatusDTO == null) {
            commonResult.setData("空调温度调节发生错误！");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        } else {
            airConditionerStatusDTO.setTargetTemperature(temp);
            commonResult.setData("roomID" + roomID + ": 温度修改成功");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        return commonResult;
    }

    @PostMapping(value = "/adjustWindSpeed")
    public CommonResult<String> adjustWindSpeed(@RequestParam("roomId") Integer roomID, @RequestParam("windSpeed") Integer windSpeed) {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(QueueDTO.SERVICE_QUEUE, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(QueueDTO.WAIT_QUEUE, roomID);
        }

        if (airConditionerStatusDTO == null) {
            commonResult.setData("空调风速调节发生错误！");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        } else {
            airConditionerStatusDTO.setWindSpeed(windSpeed);
            commonResult.setData("roomID" + roomID + ": 风速修改成功");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        return commonResult;
    }


    @PostMapping(value = "turnOff")
    public CommonResult<String> turnOff(@RequestParam("roomId") Integer roomID) throws JSONException {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(QueueDTO.SERVICE_QUEUE, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(QueueDTO.WAIT_QUEUE, roomID);
            if (airConditionerStatusDTO == null) {
                commonResult.setData("当前空调并未开机。");
                commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
                commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
                return commonResult;
            } else {
                commonResult.setData("已从等待队列中移除。");
            }
        } else {
            commonResult.setData("已从服务队列中移除。");
        }
        QueueDTO.remove(airConditionerStatusDTO);

        String message = createSSEMessage(airConditionerStatusDTO.getRoomID(), false, 2);
        SseEmitterServer.sendMessage(Integer.toString(airConditionerStatusDTO.getRoomID()), message);


        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        logger.info("/turnOff: " + commonResult.toString());
        return commonResult;
    }


    @GetMapping(value = "/subscribe")
    public SseEmitter subscribe(@RequestParam("roomId") Integer roomID) {
        return SseEmitterServer.connect(Integer.toString(roomID));
    }

    public AirConditionerStatusDTO findServer(int type, int roomID) {
        QueueDTO.setQueueType(type);
        if (!QueueDTO.isEmpty()) {
            for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.getQueue()) {
                if (airConditionerStatusDTO.getRoomID() == roomID) {
                    return airConditionerStatusDTO;
                }
            }
        }
        return null;
    }

    private String createSSEMessage(Integer roomID, boolean isTurnOn, Integer reason) throws JSONException {
        /*
        reason 枚举类型
        1 计时器到时自动关闭
        2 用户主动关闭
        3 到达指定温度
        4 被抢占关闭
        5 意外关闭
        6 用户改变参数
        -1 开机
        */
        JSONObject innerObj = new JSONObject();
        QueueDTO.setQueueType(0);
        innerObj.put("serviceQueueLength", QueueDTO.getSize());
        QueueDTO.setQueueType(1);
        innerObj.put("requestQueueSize", QueueDTO.getSize());

        JSONObject obj = new JSONObject();
        obj.put("controllerType", "status-update");
        obj.put("source", innerObj);
        obj.put("timestamp", System.currentTimeMillis());
        obj.put("roomId", roomID);
        obj.put("powerOn", isTurnOn);
        obj.put("reason", reason);

        return obj.toString();
    }


    private void processQueue() throws JSONException{
        List<AirConditionerStatusDTO> currentServiceQueue;
        if (QueueDTO.MODE == QueueDTO.PRIORITY) {
            QueueController priorityQueueController = new PriorityQueue();
            currentServiceQueue = priorityQueueController.getUser();
        }else{
            QueueController pollQueueController = new PollQueue();
            currentServiceQueue=pollQueueController.getUser();
        }
        List<AirConditionerStatusDTO> startServiceList=getStartService(currentServiceQueue);
        List<AirConditionerStatusDTO> removedServiceList=getRemovedService(currentServiceQueue);
        QueueDTO.lastServiceQueue=currentServiceQueue;

        // 更新调度
        for( AirConditionerStatusDTO startDto:startServiceList ){
            String message = createSSEMessage(startDto.getRoomID(), true, -1);
            SseEmitterServer.sendMessage(Integer.toString(startDto.getRoomID()), message);
            //插入数据库开机记录
        }

        for(AirConditionerStatusDTO removedDto:removedServiceList){
            String message = createSSEMessage(removedDto.getRoomID(), false, -1);
            SseEmitterServer.sendMessage(Integer.toString(removedDto.getRoomID()), message);
            //插入关机记录
        }
    }


    private List<AirConditionerStatusDTO> getStartService(List<AirConditionerStatusDTO> currentServiceQueue) {
        List<AirConditionerStatusDTO> startServiceList = new ArrayList<>();
        for (AirConditionerStatusDTO dto : currentServiceQueue) {
            boolean isIn = false;
            for (AirConditionerStatusDTO lastDto : QueueDTO.lastServiceQueue) {
                if (dto.equals(lastDto)) {
                    isIn = true;
                    break;
                }
            }
            if(!isIn)
                startServiceList.add(dto);
        }
        return startServiceList;
    }

    private List<AirConditionerStatusDTO> getRemovedService(List<AirConditionerStatusDTO> currentServiceQueue) {
        List<AirConditionerStatusDTO> removedServiceList = new ArrayList<>();
        for (AirConditionerStatusDTO lastDto :  QueueDTO.lastServiceQueue) {
            boolean isIn = false;
            for (AirConditionerStatusDTO dto : currentServiceQueue) {
                if (lastDto.equals(dto)) {
                    isIn = true;
                    break;
                }
            }
            if(!isIn)
                removedServiceList.add(lastDto);
        }
        return removedServiceList;
    }

}
