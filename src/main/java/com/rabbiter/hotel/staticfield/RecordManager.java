package com.rabbiter.hotel.staticfield;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rabbiter.hotel.common.Configuration;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.service.SpecificBillService;

import javax.annotation.Resource;
import java.util.Date;


public class RecordManager {

    @Resource
    private SpecificBillService specificBillService;

    public RecordManager() {

    }

    /**
     * 加入服务队列开始服务时调用
     * 新插入一条新的开机记录，start_time为当前时间，end_time,extra_fee,reason应为null,current_fee为0
     *
     * @param dto
     */
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

    /**
     * 移除服务队列/用户主动关机/服务时长到期/到达指定温度调用
     * 找到该用户最近最近的一条记录，更新end_time为当前时间(此前应为null)，更新reason(应为null),按照当前风速更新current_fee+=计算出的费用
     *
     * @param dto
     * @param reason
     */
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
     * 调整风速时调用
     * 找到该用户最近最近的一条记录，若end不为空，说明已经服务完毕，直接返回，否则更新end_time为当前时间，按照当前风速更新current_fee+=计算出的费用
     * 新建一条新记录，request_time，start_time为当前时间，end_time,extra_fee,reason应为null,current_fee为刚刚更新后的费用
     *
     * @param dto
     */
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


    /**
     * 调整温度时调用
     * 找到该用户最近最近的一条记录，更新当前温度
     *
     * @param dto
     */
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

    /**
     * 调整duration时调用
     * 找到该用户最近最近的一条记录，更新duration
     *
     * @param dto
     */
    public void DurationAdjust(AirConditionerStatusDTO dto) {
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
}
