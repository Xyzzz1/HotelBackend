package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.service.manager.RecordManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = HotelManagerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordManagerTest {

    @Resource
    private RecordManager recordManager;

    @Test
    public void updateAndAddTest() {
        Date currentTime = new Date(); //当前时间
        //进行关机服务,用户2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 20, 1, 0, 18, currentTime, 1);
        recordManager.updateAndAdd(dto);

    }

    @Test
    public void powerOntest(){
        Date currentTime = new Date(); //当前时间
        //进行关机服务,用户2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 20, 1, 0, 18, currentTime, 1);
        dto.setPowerOnTime(currentTime);
        recordManager.powerOn(dto);
    }

}
