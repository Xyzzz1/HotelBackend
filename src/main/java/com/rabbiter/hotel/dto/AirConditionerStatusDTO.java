package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 等待队列中空调的基本属性
 */
public class AirConditionerStatusDTO {

    private int roomID;
    private boolean powerOn;
    private int currentWindSpeed;
    private int targetTemperature; //设定的温度
    private int targetDuration; //设定的空调使用时长(秒)

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date powerOnTime;

    public AirConditionerStatusDTO() {

    }

    public AirConditionerStatusDTO(int roomID, boolean PowerOn,int windSpeed, int temp, int targetDuration, Date powerOnTime) {
        this.roomID = roomID;
        this.powerOn=PowerOn;
        this.currentWindSpeed = windSpeed;
        this.targetTemperature = temp;
        this.targetDuration = targetDuration;
        this.powerOnTime=powerOnTime;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomID() {
        return this.roomID;
    }

    public void setWindSpeed(int windSpeed) {
        this.currentWindSpeed = windSpeed;
    }

    public int getWindSpeed() {
        return this.currentWindSpeed;
    }

    public void setTemp(int temp) {
        this.targetTemperature = temp;
    }

    public int getTemp() {
        return this.targetTemperature;
    }

    public void setSeconds(int hours) {
        this.targetDuration = hours;
    }

    public int getSeconds() {
        return this.targetDuration;
    }


    public Date getPowerOnTime(){return this.powerOnTime;}

    public void setPowerOnTime(Date date){this.powerOnTime=date;}


    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", powerOn=" + powerOn +
                ", currentWindSpeed=" + currentWindSpeed +
                ", targetTemperature=" + targetTemperature +
                ", targetDuration=" + targetDuration +
                ", powerOnTime=" + powerOnTime +
                '}';
    }
}
