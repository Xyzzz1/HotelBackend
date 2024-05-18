package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:
 */

@TableName(value = "room_temp")
public class RoomTemp {
    @TableField(value = "room_id")
    private Integer roomID;

    @TableField(value = "init_temp")
    private double initTemp;

    public RoomTemp(){}

    public RoomTemp(Integer roomID,double initTemp){
        this.roomID=roomID;
        this.initTemp=initTemp;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public double getInitTemp() {
        return initTemp;
    }

    public void setInitTemp(Integer initTemp) {
        this.initTemp = initTemp;
    }

    @Override
    public String toString() {
        return "RoomTemperature{" +
                "roomID=" + roomID +
                ", initTemp=" + initTemp +
                '}';
    }
}
