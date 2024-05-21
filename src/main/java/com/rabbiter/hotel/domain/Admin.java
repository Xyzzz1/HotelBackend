package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author：rabbiter
 * @date：2022/01/01 11:55
 * Description：
 */
@TableName(value = "`admin`")
public class Admin {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "email")
    private String email;
    @TableField(value = "admin_name")
    private String admin_name;
    @TableField(value = "password")
    private String password;
    @TableField(value = "phone")
    private String phone;

    @TableField(value = "type")
    private Integer type;

    public Admin() {
    }

    public Admin(Integer id, String email, String admin_name, String password, String phone,Integer type) {
        this.id = id;
        this.email = email;
        this.admin_name = admin_name;
        this.password = password;
        this.phone = phone;
        this.type=type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(Integer type){
        this.type=type;
    }

    public Integer getType(){
        return type;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", admin_name='" + admin_name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
