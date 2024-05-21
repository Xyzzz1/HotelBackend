package com.rabbiter.hotel.test.unittest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.common.Configuration;
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
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = HotelManagerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordManagerTest {

    @Resource
    private SpecificBillService specificBillService;

    @Test
    public void powerOntest() throws ParseException {
        Date currentTime = new Date(); //当前时间

        //进行开机服务，用户id为2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 25, 1, 0, 60, currentTime, 1);
        dto.setPowerOnTime(new Date());
        powerOn(dto);

        /*
        //用户2调风速
        user2.setWindSpeed(4);
        recordManager.windAdjust(user2);


        //用户1重新申请开机
        user1.setRequestTime(currentTime);
        recordManager.powerOn(user1);

        //调整温度
        user1.setTargetTemperature(16);
        recordManager.temperAdjust(user1);

        //调整duration
        user1.setTargetDuration(233333333);
        recordManager.DurationAdjust(user1);

         */

    }

    @Test
    public void powerOffTest() throws ParseException {
        Date currentTime = new Date(); //当前时间
        //进行关机服务,用户2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 25, 3, 0, 60, currentTime, 1);
        powerOff(dto, 1);
    }


    @Test
    public void decimalTest() {
        float floatValue = 123.45678f;
        float roundedValue = Math.round(floatValue * 100.0f) / 100.0f;
        System.out.println("Rounded float: " + roundedValue);
    }


    public void powerOn(AirConditionerStatusDTO dto) {
        SpecificBill specificBill = new SpecificBill(null, dto.getUserID(), dto.getRequestTime(), dto.getPowerOnTime(), dto.getRoomID(),
                null, dto.getWindSpeed(), dto.getTargetTemperature(), dto.getTargetDuration(), null, dto.getAdditionalFee(), 0f, 1f);

        boolean success = specificBillService.save(specificBill);
        if (success) {
            System.out.println("开机插入成功");
        } else {
            System.out.println("开机插入失败");
        }
    }

    public void powerOff(AirConditionerStatusDTO dto, int reason) {
        Date currentTime = new Date(); // 当前时间

        QueryWrapper<SpecificBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dto.getUserID());
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 1");
        SpecificBill pre_specificBill = specificBillService.getOne(queryWrapper);

        if (pre_specificBill.getEndTime() != null) {
            return;
        }

        pre_specificBill.setEndTime(currentTime);
        float current_fee = pre_specificBill.getCurrentFee() + calFee(pre_specificBill);
        pre_specificBill.setReason(reason);
        pre_specificBill.setCurrentFee(current_fee);


        UpdateWrapper<SpecificBill> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", pre_specificBill.getId());

        boolean flag = specificBillService.update(pre_specificBill, updateWrapper);
        if (flag) {
            System.out.println("关机更新成功");
        } else {
            System.out.println("关机更新失败");
        }


    }

    /**
     * 计算规则：高：1度/1分钟， 中：1度/2分钟，低：1度/3分钟,1元/1度
     * 不满一分钟向上取整
     * 系统时间倍率：Configuration.timeChangeRate
     *
     * @param preSpecificBill
     * @return
     */
    private float calFee(SpecificBill preSpecificBill) {
        int duration = (int) (preSpecificBill.getEndTime().getTime() - preSpecificBill.getStartTime().getTime()) * Configuration.timeChangeRate / 1000; //总秒数
        int minutes = (duration + 59) / 60;

        if (preSpecificBill.getWindSpeed() == 3) {
            return roundToTwoVector((float) minutes * preSpecificBill.getFeeRate());
        } else if (preSpecificBill.getWindSpeed() == 2) {
            return roundToTwoVector((float) minutes * preSpecificBill.getFeeRate() / 2);
        } else {
            return roundToTwoVector((float) minutes * preSpecificBill.getFeeRate() / 3);
        }

    }

    /**
     * 规约到2位小数
     *
     * @param num
     * @return
     */
    private float roundToTwoVector(float num) {
        return Math.round(num * 100.0f) / 100.0f;
    }

    @Test
    public void windAdjustTest() {
        Date currentTime = new Date(); //当前时间
        //进行关机服务,用户2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 25, 3, 0, 60, currentTime, 1);
        windAdjust(dto);
    }


    public void windAdjust(AirConditionerStatusDTO dto) {
        Date currentTime = new Date(); // 当前时间
        QueryWrapper<SpecificBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dto.getUserID());
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 1");
        SpecificBill update = specificBillService.getOne(queryWrapper);
        if (update.getEndTime() != null)//服务完毕
            return;

        update.setEndTime(currentTime);
        float current_fee = update.getCurrentFee() + calFee(update);
        update.setCurrentFee(current_fee);

        UpdateWrapper<SpecificBill> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", update.getId());
        boolean flag1 = specificBillService.update(update, updateWrapper);
        if (flag1) {
            System.out.println("风速更新成功");
        } else {
            System.out.println("风速更新失败");
        }

        queryWrapper.clear();

        SpecificBill specificBill = new SpecificBill(null, dto.getUserID(), currentTime, currentTime, dto.getRoomID(),
                null, dto.getWindSpeed(), dto.getTargetTemperature(), dto.getTargetDuration(), null, dto.getAdditionalFee(), current_fee, 1f);
        boolean flag2 = specificBillService.save(specificBill);
        if (flag2) {
            System.out.println("风速新建成功");
        } else {
            System.out.println("风速新建失败");
        }
    }




    @Test
    public void temperAdjustTest() {
        Date currentTime = new Date(); //当前时间
        //进行关机服务,用户2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 100, 3, 0, 60, currentTime, 1);
        temperAdjust(dto);
    }


    public void temperAdjust(AirConditionerStatusDTO dto) {
        QueryWrapper<SpecificBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dto.getUserID());
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 1");

        SpecificBill update = specificBillService.getOne(queryWrapper);

        update.setTemperature(dto.getTargetTemperature());


        UpdateWrapper<SpecificBill> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", update.getId());
        boolean flag1 = specificBillService.update(update, updateWrapper);
        if (flag1) {
            System.out.println("温度更新成功");
        } else {
            System.out.println("温度更新失败");
        }

    }

    @Test
    public void durationAdjustTest() {
        Date currentTime = new Date(); //当前时间
        //进行关机服务,用户2
        AirConditionerStatusDTO dto = new AirConditionerStatusDTO(202, 2, true, 20, 3, 0, -1, currentTime, 1);
        durationAdjust(dto);
    }

    public void durationAdjust(AirConditionerStatusDTO dto) {
        QueryWrapper<SpecificBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dto.getUserID());
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 1");

        SpecificBill update = specificBillService.getOne(queryWrapper);

        //更新时间
        update.setDuration(dto.getTargetDuration());


        UpdateWrapper<SpecificBill> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", update.getId());
        boolean flag1 = specificBillService.update(update, updateWrapper);
        if (flag1) {
            System.out.println("duration更新成功");
        } else {
            System.out.println("duration更新失败");
        }


    }

}
