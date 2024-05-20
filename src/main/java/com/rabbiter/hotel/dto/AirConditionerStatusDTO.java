package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 等待队列中空调的基本属性
 */
public class AirConditionerStatusDTO {

    private Integer roomID;
    private Integer userID;
    private boolean powerOn;
    private Integer targetTemperature; //设定的温度
    private Integer windSpeed;
    private Integer additionalFee;
    private Integer targetDuration; //设定的空调使用时长(秒)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date requestTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date powerOnTime;


    private Integer mode;//0制热，1制冷


    public AirConditionerStatusDTO() {

    }

    // 构造方法
    public AirConditionerStatusDTO(Integer roomID, Integer userID, boolean on, Integer targetTemperature,
                                   Integer windSpeed, Integer additionalFee, Integer targetDuration,
                                   Date requestTime, Integer mode) {
        this.roomID = roomID;
        this.userID = userID;
        this.powerOn = on;
        this.targetTemperature = targetTemperature;
        this.windSpeed = windSpeed;
        this.additionalFee = additionalFee;
        this.targetDuration = targetDuration;
        this.requestTime = requestTime;
        this.mode = mode;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public Integer getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(Integer targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public Integer getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(Integer additionalFee) {
        this.additionalFee = additionalFee;
    }

    public void setPowerOnTime(Date powerOnTime){
        this.powerOnTime=powerOnTime;
    }
    public Date getPowerOnTime(){
        return this.powerOnTime;
    }

    public Integer getTargetDuration() {
        return targetDuration;
    }

    public void setTargetDuration(Integer targetDuration) {
        this.targetDuration = targetDuration;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getMode() {
        return this.mode;
    }


    @Override
    public String toString() {
        return "AirConditionerStatusDTO{" +
                "roomID=" + roomID +
                ", userID=" + userID +
                ", powerOn=" + powerOn +
                ", targetTemperature=" + targetTemperature +
                ", windSpeed=" + windSpeed +
                ", additionalFee=" + additionalFee +
                ", targetDuration=" + targetDuration +
                ", requestTime=" + requestTime +
                ", powerOnTime=" + powerOnTime +
                ", mode=" + mode +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AirConditionerStatusDTO other = (AirConditionerStatusDTO) obj;
        return roomID == other.roomID &&
                userID == other.userID &&
                Objects.equals(requestTime, other.requestTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, userID, requestTime);
    }
}
