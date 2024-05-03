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

@TableName(value = "bill")
public class Bill {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "user_id")
    private Integer userID;
    @TableField(value = "user_name")
    private String userName;
    @TableField(value = "fee")
    private Integer fee;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private Date createTime;

    public Bill() {
    }

    public Bill(Integer id, Integer user_id, String user_name, Integer fee, Date create_time) {
        this.id = id;
        this.userID = user_id;
        this.userName = user_name;
        this.fee = fee;
        this.createTime = create_time;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return userID;
    }

    public void setUser_id(Integer user_id) {
        this.userID = user_id;
    }

    public String getUser_name() {
        return userName;
    }

    public void setUser_name(String user_name) {
        this.userName = user_name;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Date getCreate_time() {
        return createTime;
    }

    public void setCreate_time(Date create_time) {
        this.createTime = create_time;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", user_id=" + userID +
                ", user_name='" + userName + '\'' +
                ", fee=" + fee +
                ", create_time=" + createTime +
                '}';
    }
}
