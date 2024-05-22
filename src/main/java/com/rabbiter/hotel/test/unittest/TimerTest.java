package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.component.TimerManager;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:
 */
public class TimerTest {

    @Test
    public void test(){

        TimerManager timerManager = new TimerManager();
        AirConditionerStatusDTO airConditionerUser1=new AirConditionerStatusDTO(1, 1, true, 21, 3, 0, 60, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser2= new AirConditionerStatusDTO(2, 2, true, 23, 2, 0, 60, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser3=new AirConditionerStatusDTO(3, 3, true, 24, 1, 0, 60, new Date(), 1);

        Runnable task1 = () -> {
            System.out.println("Task for " + airConditionerUser1 + " executed at " + new Date());
            timerManager.removeObject(airConditionerUser1);  // 任务执行后移除对象及其定时器
        };

        Runnable task2 = () -> {
            System.out.println("Task for " + airConditionerUser2 + " executed at " + new Date());
            timerManager.removeObject(airConditionerUser2);  // 任务执行后移除对象及其定时器
        };



        timerManager.addObjectWithTimer(airConditionerUser1, 5, TimeUnit.SECONDS, task1);
        timerManager.addObjectWithTimer(airConditionerUser2, 10, TimeUnit.SECONDS, task2);

        try {
            Thread.sleep(60000);  // 等待 60 秒以观察任务执行情况
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 关闭调度器
        timerManager.shutdown();




    }
}
