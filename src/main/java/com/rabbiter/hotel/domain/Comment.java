package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author：rabbiter
 * @date：2022/01/01 11:57
 * Description：
 */
@TableName(value = "comment")
public class Comment {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "information")
    private String information;
    @TableField(value = "type")
    private Integer type;

    public Comment() {
    }

    public Comment(Integer id, Integer userId, String information, Integer type) {
        this.id = id;
        this.userId = userId;
        this.information = information;
        this.type = type;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", information='" + information + '\'' +
                ", type=" + type +
                '}';
    }
}
