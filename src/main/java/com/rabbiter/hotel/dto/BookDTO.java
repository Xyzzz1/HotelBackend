package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author：rabbiter
 * @date：2022/01/01 14:21
 * Description：
 */
public class BookDTO {

    private Integer roomId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date inTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date leaveTime;

    private Integer realPeople;
    private Integer fapiao;

    public BookDTO() {
    }

    public BookDTO(Integer roomId, Date inTime, Date leaveTime, Integer realPeople, Integer fapiao) {
        this.roomId = roomId;
        this.inTime = inTime;
        this.leaveTime = leaveTime;
        this.realPeople = realPeople;
        this.fapiao = fapiao;
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

    @Override
    public String toString() {
        return "BookDTO{" +
                "roomId=" + roomId +
                ", inTime=" + inTime +
                ", leaveTime=" + leaveTime +
                ", realPeople=" + realPeople +
                ", fapiao=" + fapiao +
                '}';
    }
}
