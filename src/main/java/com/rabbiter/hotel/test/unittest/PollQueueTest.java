package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.facility.airconditioner.PollQueue;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

/**
 * @author hejiaqi
 * @date: 2024/5/14

 */

public class PollQueueTest {
    public PollQueueTest(){
    }

    @Test
    public void test(){
        PollQueue pollQueue=new PollQueue();
        QueueDTO.setQueueType(1);
        AirConditionerStatusDTO airConditionerUser1=new AirConditionerStatusDTO(1, 1, true, 21, 3, 0, 60, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser2= new AirConditionerStatusDTO(2, 2, true, 23, 2, 0, 60, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser3=new AirConditionerStatusDTO(3, 3, true, 24, 1, 0, 60, new Date(), 1);

        QueueDTO.enqueue(airConditionerUser1);
        QueueDTO.enqueue(airConditionerUser2);
        QueueDTO.enqueue(airConditionerUser3);

        System.out.println("测试等待队列");
        System.out.println(QueueDTO.getQueue());

        QueueDTO.setQueueType(0);
        System.out.println("测试服务队列");
        System.out.println(QueueDTO.getQueue());


        System.out.println("############################################");
        List<AirConditionerStatusDTO> user1 = pollQueue.getUser();
        System.out.println("一次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("QueueDTO中的队列结果");
        System.out.println("服务队列");
        QueueDTO.setQueueType(0);
        System.out.println(QueueDTO.getQueue());
        System.out.println("等待队列");
        QueueDTO.setQueueType(1);
        System.out.println(QueueDTO.getQueue());


        System.out.println("############################################");
        user1 = pollQueue.getUser();
        System.out.println("二次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("QueueDTO中的队列结果");
        System.out.println("服务队列");
        QueueDTO.setQueueType(0);
        System.out.println(QueueDTO.getQueue());
        System.out.println("等待队列");
        QueueDTO.setQueueType(1);
        System.out.println(QueueDTO.getQueue());

        System.out.println("############################################");
        user1 = pollQueue.getUser();
        System.out.println("三次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("QueueDTO中的队列结果");
        System.out.println("服务队列");
        QueueDTO.setQueueType(0);
        System.out.println(QueueDTO.getQueue());
        System.out.println("等待队列");
        QueueDTO.setQueueType(1);
        System.out.println(QueueDTO.getQueue());


        System.out.println("############################################");
        user1 = pollQueue.getUser();
        System.out.println("四次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("QueueDTO中的队列结果");
        System.out.println("服务队列");
        QueueDTO.setQueueType(0);
        System.out.println(QueueDTO.getQueue());
        System.out.println("等待队列");
        QueueDTO.setQueueType(1);
        System.out.println(QueueDTO.getQueue());


        System.out.println("############################################");
        user1 = pollQueue.getUser();
        System.out.println("五次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("QueueDTO中的队列结果");
        System.out.println("服务队列");
        QueueDTO.setQueueType(0);
        System.out.println(QueueDTO.getQueue());
        System.out.println("等待队列");
        QueueDTO.setQueueType(1);
        System.out.println(QueueDTO.getQueue());

    }
}
