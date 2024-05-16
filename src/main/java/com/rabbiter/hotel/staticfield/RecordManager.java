package com.rabbiter.hotel.staticfield;

/**
 * @author
 * @date: 2024/5/16
 * Description:
 */


import com.rabbiter.hotel.dto.AirConditionerStatusDTO;

/**
 DROP TABLE IF EXISTS `specific_bill`;
 CREATE TABLE `specific_bill` (
 `id` int(0) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 `user_id` int(0) NOT NULL,
 `request_time` datetime NOT NULL,
 `start_time` datetime NOT NULL,
 `room_id` int(0) NOT NULL,
 `end_time` datetime DEFAULT NULL,
 `wind_speed` int(0) NOT NULL,
 `temperature` float NOT NULL,
 `duration` int(0) NOT NULL,
 `reason` int(0) DEFAULT NULL,
 `extra_fee` int(0) DEFAULT NULL,
 `current_fee` float NOT NULL,
 `fee_rate` float NOT NULL
 );

 */
public class RecordManager {


    /**
     * 加入服务队列开始服务时调用
     * 新插入一条新的开机记录，start_time为当前时间，end_time,extra_fee,reason应为null,current_fee为0
     *
     * @param dto
     */
    public static void powerOn(AirConditionerStatusDTO dto){}

    /**
     * 移除服务队列/用户主动关机/服务时长到期/到达指定温度调用
     * 找到该用户最近最近的一条记录，更新end_time为当前时间(此前应为null)，更新reason(应为null),按照当前风速更新current_fee+=计算出的费用
     *
     * @param dto
     * @param reason
     */
    public static void powerOff(AirConditionerStatusDTO dto,int reason){}


    /**
     * 调整风速时调用
     * 找到该用户最近最近的一条记录，更新end_time为当前时间(此前应为null)，按照当前风速更新current_fee+=计算出的费用
     * 新建一条新记录，request_time，start_time为当前时间，end_time,extra_fee,reason应为null,current_fee为刚刚更新后的费用
     *
     * @param dto
     */
    public static void windAdjust(AirConditionerStatusDTO dto){}

}
