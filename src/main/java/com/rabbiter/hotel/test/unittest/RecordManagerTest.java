package com.rabbiter.hotel.test.unittest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.domain.Bill;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnSpecificBillDTO;
import com.rabbiter.hotel.service.BillService;
import com.rabbiter.hotel.service.SpecificBillService;
import com.rabbiter.hotel.staticfield.RecordManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@SpringBootTest(classes = HotelManagerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordManagerTest {

    @Resource
    private SpecificBillService specificBillService;
    private RecordManager rm;

    @Test
    public void test() throws ParseException {
        rm=new RecordManager(specificBillService);

        //先插入一条记录 ,用户id为1的记录
        String dateString = "2024-05-02 15:30";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date createTime = dateFormat.parse(dateString);
        Date currentTime=new Date(); //当前时间
        SpecificBill specificBill = new SpecificBill(1, 1, createTime, currentTime, 111,
                null, 2, 25, 120, null, 50, 0f, 1f);

        QueryWrapper queryWrapper = new QueryWrapper();
        // where子句
        queryWrapper.eq("id", 1);
        specificBillService.save(specificBill);


        //进行开机服务，用户id为2
        AirConditionerStatusDTO user2=new AirConditionerStatusDTO(222,2,false,25,6,0,60,currentTime,1);
        rm.powerOn(user2);

        //进行关机服务,用户1
        AirConditionerStatusDTO user1=new AirConditionerStatusDTO(111,1,true,25,6,0,120,createTime,1);
        rm.powerOff(user1,1);

        //用户2调风速
        user2.setWindSpeed(4);
        rm.windAdjust(user2);


        //用户1重新申请开机
        user1.setRequestTime(currentTime);
        rm.powerOn(user1);

        //调整温度
        user1.setTargetTemperature(16);
        rm.temperAdjust(user1);

        //调整duration
        user1.setTargetDuration(233333333);
        rm.DurationAdjust(user1);

    }

}