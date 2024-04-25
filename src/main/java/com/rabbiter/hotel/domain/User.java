package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author：rabbiter
 * @date：2022/01/01 11:36
 * Description：
 */
@TableName(value = "`user`")
public class User {

    @TableId(value = "id")
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private Date createTime = new Date();
    @TableField(value = "email")
    private String email;
    @TableField(value = "user_name")
    private String userName;
    @TableField(value = "password")
    private String password;
    @TableField(value = "sex")
    private Integer sex;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "jifen")
    private Integer jifen = 0;
    @TableField(value = "state")
    private Integer state = 0;

    public User() {
    }

    public User(Integer id, Date createTime, String email, String userName, String password, Integer sex, String phone, Integer jifen, Integer state) {
        this.id = id;
        this.createTime = createTime;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.sex = sex;
        this.phone = phone;
        this.jifen = jifen;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getJifen() {
        return jifen;
    }

    public void setJifen(Integer jifen) {
        this.jifen = jifen;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", jifen=" + jifen +
                ", state=" + state +
                '}';
    }
}
