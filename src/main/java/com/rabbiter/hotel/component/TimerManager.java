package com.rabbiter.hotel.component;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:定时器
 */
public class TimerManager {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<AirConditionerStatusDTO, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final Map<AirConditionerStatusDTO, Long> startTimes = new ConcurrentHashMap<>();

    public void addObjectWithTimer(AirConditionerStatusDTO obj, long delay, TimeUnit unit, Runnable task) {
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(task, delay, delay, unit);
        timers.put(obj, scheduledFuture);
        startTimes.put(obj, System.currentTimeMillis() / 1000); //转换为s
    }

    public void removeObject(AirConditionerStatusDTO obj) {
        ScheduledFuture<?> scheduledFuture = timers.remove(obj);
        if (startTimes.containsKey(obj))
            startTimes.remove(obj);

        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    public Long getStartTime(AirConditionerStatusDTO obj) {
        return startTimes.get(obj);
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public  Map<AirConditionerStatusDTO, ScheduledFuture<?>> getTimers(){return  timers;}


}
