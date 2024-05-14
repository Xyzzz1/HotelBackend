package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import org.junit.*;

import java.util.Date;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 运行该类，如果能正常工作会有以下输出：
 * AirConditionerDTO{roomID=302, windSpeed=1, temp=2, hours=3}
 * AirConditionerDTO{roomID=402, windSpeed=4, temp=5, hours=6}
 * AirConditionerDTO{roomID=502, windSpeed=7, temp=8, hours=9}
 *
 * Process finished with exit code 0
 */

public class QueueDTOTest {


    public QueueDTOTest(){
    }

    @Test
    public void test(){
        QueueDTO.setQueueType(1);
        AirConditionerStatusDTO airConditionerUser1=new AirConditionerStatusDTO(1, 1, true, 21, 3, 0, 60, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser2=new AirConditionerStatusDTO(2, 2, true, 23, 2, 0, 360, new Date(), 1);
        AirConditionerStatusDTO airConditionerUser3=new AirConditionerStatusDTO(3, 3, true, 24, 1, 0, 1800, new Date(), 1);

        QueueDTO.enqueue(airConditionerUser1);
        QueueDTO.enqueue(airConditionerUser2);
        QueueDTO.enqueue(airConditionerUser3);

        System.out.println(QueueDTO.dequeue().toString());
        System.out.println(QueueDTO.dequeue().toString());
        System.out.println(QueueDTO.dequeue().toString());

        System.out.println(QueueDTO.MODE);
    }

}
