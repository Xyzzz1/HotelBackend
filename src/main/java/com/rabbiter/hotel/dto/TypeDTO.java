package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author：rabbiter
 * @date：2022/01/04 13:00
 * Description：
 */
public class TypeDTO {

    private Integer typeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date inTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date leaveTime;

    public TypeDTO() {
    }

    public TypeDTO(Integer typeId, Date inTime, Date leaveTime) {
        this.typeId = typeId;
        this.inTime = inTime;
        this.leaveTime = leaveTime;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    @Override
    public String toString() {
        return "TypeDTO{" +
                "typeId=" + typeId +
                ", inTime=" + inTime +
                ", leaveTime=" + leaveTime +
                '}';
    }
}
