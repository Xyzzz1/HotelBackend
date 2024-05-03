package com.rabbiter.hotel.dto;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 等待队列中空调的基本属性
 */
public class AirConditionerUserDTO {

    private int roomID;
    private int windSpeed;
    private int temp;
    private int hours=-1; //设定的空调使用时长

    public AirConditionerUserDTO() {

    }

    public AirConditionerUserDTO(int roomID, int windSpeed, int temp, int hours) {
        this.roomID = roomID;
        this.windSpeed = windSpeed;
        this.temp = temp;
        this.hours = hours;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomID() {
        return this.roomID;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindSpeed() {
        return this.windSpeed;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTemp() {
        return this.temp;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return this.hours;
    }


    @Override
    public String toString() {
        return "AirConditionerDTO{" +
                "roomID=" + roomID +
                ", windSpeed=" + windSpeed +
                ", temp=" + temp +
                ", hours=" + hours +
                '}';
    }
}
