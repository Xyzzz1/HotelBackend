package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author：rabbiter
 * @date：2022/01/01 11:49
 * Description：
 */
@TableName(value = "t_order")
public class Order {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "room_id")
    private Integer roomId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "in_time")
    private Date inTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "leave_time")
    private Date leaveTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private Date createTime = new Date();
    @TableField(value = "real_price")
    private Double realPrice;
    @TableField(value = "real_people")
    private Integer realPeople;
    @TableField(value = "fapiao")
    private Integer fapiao;
    @TableField(value = "flag")
    private Integer flag = 0;

    public Order() {
    }

    public Order(Integer id, Integer userId, Integer roomId, Date inTime, Date leaveTime, Date createTime, Double realPrice, Integer realPeople, Integer fapiao, Integer flag) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.inTime = inTime;
        this.leaveTime = leaveTime;
        this.createTime = createTime;
        this.realPrice = realPrice;
        this.realPeople = realPeople;
        this.fapiao = fapiao;
        this.flag = flag;
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

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
    }

    public Integer getRealPeople() {
        return realPeople;
    }

    public void setRealPeople(Integer realPeople) {
        this.realPeople = realPeople;
    }

    public Integer getFapiao() {
        return fapiao;
    }

    public void setFapiao(Integer fapiao) {
        this.fapiao = fapiao;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", inTime=" + inTime +
                ", leaveTime=" + leaveTime +
                ", createTime=" + createTime +
                ", realPrice=" + realPrice +
                ", realPeople=" + realPeople +
                ", fapiao=" + fapiao +
                ", flag=" + flag +
                '}';
    }
}
