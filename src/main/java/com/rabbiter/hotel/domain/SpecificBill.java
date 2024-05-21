package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 */

@TableName(value = "specific_bill")
public class SpecificBill {
    @TableId(value = "id")
    private Integer id;

    @TableField(value = "user_id")
    private Integer userId;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "request_time")
    private Date requestTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "room_id")
    private Integer roomId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "end_time")
    private Date endTime;

    @TableField(value = "wind_speed")
    private Integer windSpeed;

    @TableField(value = "temperature")
    private Integer temperature;

    @TableField(value = "duration")
    private Integer duration;

    @TableField(value = "reason")
    private Integer reason;

    @TableField(value = "extra_fee")
    private Integer extraFee;

    @TableField(value = "current_fee")
    private Float currentFee;

    @TableField(value = "fee_rate")
    private Float feeRate;


    public SpecificBill() {
    }

    public SpecificBill(Integer id, Integer userId, Date requestTime,Date startTime,Integer roomId, Date endTime, Integer windSpeed,
                        Integer temperature, Integer duration, Integer reason, Integer extraFee,Float currentFee,Float feeRate) {
        this.id = id;
        this.userId = userId;
        this.requestTime=requestTime;
        this.startTime = startTime;
        this.roomId = roomId;
        this.endTime = endTime;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.duration = duration;
        this.reason = reason;
        this.extraFee = extraFee;
        this.currentFee=currentFee;
        this.feeRate=feeRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public Integer getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(Integer extraFee) {
        this.extraFee = extraFee;
    }

    public Float getCurrentFee() {
        return currentFee;
    }

    public void setCurrentFee(Float currentFee) {
        this.currentFee = currentFee;
    }

    public Float getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(Float feeRate) {
        this.feeRate = feeRate;
    }



    @Override
    public String toString() {
        return "SpecificBill{" +
                "id=" + id +
                ", userId=" + userId +
                ", requestTime=" + requestTime +
                ", startTime=" + startTime +
                ", roomId=" + roomId +
                ", endTime=" + endTime +
                ", windSpeed=" + windSpeed +
                ", temperature=" + temperature +
                ", duration=" + duration +
                ", reason=" + reason +
                ", extraFee=" + extraFee +
                ", currentFee=" + currentFee +
                ", feeRate=" + feeRate +
                '}';
    }
}
