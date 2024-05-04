package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import org.junit.*;

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
        AirConditionerStatusDTO airConditionerUser1=new AirConditionerStatusDTO(302,true,1,2,3,null);
        AirConditionerStatusDTO airConditionerUser2=new AirConditionerStatusDTO(402,true,4,5,6,null);
        AirConditionerStatusDTO airConditionerUser3=new AirConditionerStatusDTO(502,true,7,8,9,null);

        QueueDTO.enqueue(airConditionerUser1);
        QueueDTO.enqueue(airConditionerUser2);
        QueueDTO.enqueue(airConditionerUser3);

        System.out.println(QueueDTO.dequeue().toString());
        System.out.println(QueueDTO.dequeue().toString());
        System.out.println(QueueDTO.dequeue().toString());
    }

}
