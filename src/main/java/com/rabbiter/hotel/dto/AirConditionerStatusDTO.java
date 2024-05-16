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

    private int roomID;
    private int userID;
    private boolean powerOn;
    private int targetTemperature; //设定的温度
    private int windSpeed;
    private int additionalFee;
    private int targetDuration; //设定的空调使用时长(秒)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date requestTime;

    private int mode;//0制热，1制冷Ï


    public AirConditionerStatusDTO() {

    }

    // 构造方法
    public AirConditionerStatusDTO(int roomID, int userID, boolean powerOn, int targetTemperature,
                                   int windSpeed, int additionalFee, int targetDuration,
                                   Date requestTime, int mode) {
        this.roomID = roomID;
        this.userID = userID;
        this.powerOn = powerOn;
        this.targetTemperature = targetTemperature;
        this.windSpeed = windSpeed;
        this.additionalFee = additionalFee;
        this.targetDuration = targetDuration;
        this.requestTime = requestTime;
        this.mode = mode;
    }
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(int targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public int getTargetDuration() {
        return targetDuration;
    }

    public void setTargetDuration(int targetDuration) {
        this.targetDuration = targetDuration;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public int getPowerOnTime() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
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
                powerOn == other.powerOn &&
                targetTemperature == other.targetTemperature &&
                windSpeed == other.windSpeed &&
                additionalFee == other.additionalFee &&
                targetDuration == other.targetDuration &&
                mode == other.mode &&
                Objects.equals(requestTime, other.requestTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, userID, powerOn, targetTemperature, windSpeed, additionalFee, targetDuration, requestTime, mode);
    }
}
