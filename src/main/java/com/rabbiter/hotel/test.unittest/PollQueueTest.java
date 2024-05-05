package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.dto.AirConditionerUserDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.facility.airconditioner.PollQueue;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author hejiaqi
 * @date: 2024/5/5
 
 */

public class PollQueueTest {
    public PollQueueTest(){
    }

    @Test
    public void test(){
        QueueDTO dt=new QueueDTO();
        dt.setQueueType(1);
        AirConditionerUserDTO airConditionerUser1=new AirConditionerUserDTO(302,1,2,3);
        AirConditionerUserDTO airConditionerUser2=new AirConditionerUserDTO(402,4,5,2);
        AirConditionerUserDTO airConditionerUser3=new AirConditionerUserDTO(502,7,8,1);

        dt.enqueue(airConditionerUser1);
        dt.enqueue(airConditionerUser2);
        dt.enqueue(airConditionerUser3);

        System.out.println("测试等待队列");
        System.out.println(dt.getQueue());

        dt.setQueueType(0);
        System.out.println("测试服务队列");
        System.out.println(dt.getQueue());

        List<AirConditionerUserDTO> user1 = PollQueue.getUser1(dt);
        System.out.println("一次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("DTO中的队列结果");
        System.out.println("服务队列");
        dt.setQueueType(0);
        System.out.println(dt.getQueue());
        System.out.println("等待队列");
        dt.setQueueType(1);
        System.out.println(dt.getQueue());

        user1 = PollQueue.getUser1(dt);
        System.out.println("二次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("DTO中的队列结果");
        System.out.println("服务队列");
        dt.setQueueType(0);
        System.out.println(dt.getQueue());
        System.out.println("等待队列");
        dt.setQueueType(1);
        System.out.println(dt.getQueue());

        user1 = PollQueue.getUser1(dt);
        System.out.println("三次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("DTO中的队列结果");
        System.out.println("服务队列");
        dt.setQueueType(0);
        System.out.println(dt.getQueue());
        System.out.println("等待队列");
        dt.setQueueType(1);
        System.out.println(dt.getQueue());



        user1 = PollQueue.getUser1(dt);
        System.out.println("四次时间片后的结果");
        System.out.println(user1.toString());
        System.out.println("DTO中的队列结果");
        System.out.println("服务队列");
        dt.setQueueType(0);
        System.out.println(dt.getQueue());
        System.out.println("等待队列");
        dt.setQueueType(1);
        System.out.println(dt.getQueue());

    }
}
