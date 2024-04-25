package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author：rabbiter
 * @date：2022/01/03 13:05
 * Description：
 */
public class ReturnUserDTO {

    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    private String email;
    private String userName;
    private Integer sex;
    private String phone;
    private Integer jifen = 0;
    private Integer state = 0;

    public ReturnUserDTO() {
    }

    public ReturnUserDTO(Integer id, Date createTime, String email, String userName, Integer sex, String phone, Integer jifen, Integer state) {
        this.id = id;
        this.createTime = createTime;
        this.email = email;
        this.userName = userName;
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
        return "ReturnUserDTO{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", jifen=" + jifen +
                ", state=" + state +
                '}';
    }
}
