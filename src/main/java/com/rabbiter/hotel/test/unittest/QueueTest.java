package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.queue.QueueController;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:
 */
public class QueueTest {

    private QueueController queueController=new QueueController();

    @Test
    public void test(){
        AirConditionerStatusDTO airConditionerUser1=new AirConditionerStatusDTO(1, 1, true, 21, 3, 0, 240, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser2= new AirConditionerStatusDTO(2, 2, true, 23, 1, 0, 600, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser3=new AirConditionerStatusDTO(3, 3, true, 24, 1, 0, 600, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser4=new AirConditionerStatusDTO(4, 4, true, 24, 1, 0, 600, new Date(), 1);

        List<AirConditionerStatusDTO> dtoList=new ArrayList<>();
        //dtoList.add(airConditionerUser1);
        dtoList.add(airConditionerUser2);
        dtoList.add(airConditionerUser3);
        //dtoList.add(airConditionerUser4);

        for(AirConditionerStatusDTO dto:dtoList){
            int result=queueController.enQueue(dto);
            if(result==QueueController.WAIT){
                System.out.println("roomID"+dto.getRoomID()+": into wait queue");
            }else{
                System.out.println("roomID"+dto.getRoomID()+": into service queue");
            }
        }

        System.out.println(QueueDTO.serviceQueue);
        System.out.println(QueueDTO.waitQueue);
        System.out.println();

        try {
            Thread.sleep(60000);  // 等待 60 秒以观察任务执行情况
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
