package com.rabbiter.hotel.facility.clock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/14
 * Description:后端维护的每个房间的温度
 */


@Component
public class TemperatureController {
    @Scheduled(cron ="*/10 * * * * ?")
    public void adjustTemperature() {
        System.out.println("world");
    }
}
