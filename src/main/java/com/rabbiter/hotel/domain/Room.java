package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author：rabbiter
 * @date：2022/01/01 11:42
 * Description：
 */
@TableName(value = "room")
public class Room {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "number")
    private String number;
    @TableField(value = "type")
    private Integer type;
    @TableField(value = "state")
    private Integer state = 0;
    @TableField(value = "max_people")
    private Integer maxPeople;
    @TableField(value = "introduce")
    private String introduce;

    public Room() {
    }

    public Room(Integer id, String number, Integer type, Integer state, Integer maxPeople, String introduce) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.state = state;
        this.maxPeople = maxPeople;
        this.introduce = introduce;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", state=" + state +
                ", maxPeople=" + maxPeople +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}
