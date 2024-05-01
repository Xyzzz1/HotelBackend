package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SearchRoomDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date inTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date leaveTime;
    private String roomType;
    private int maxPeople;
    private String minPrice;
    private String maxPrice;

    public SearchRoomDTO() {
    }

    public SearchRoomDTO(Date inTime, Date leaveTime, String roomType, int maxPeople, String minPrice, String maxPrice) {
        this.inTime = inTime;
        this.leaveTime = leaveTime;
        this.roomType = roomType;
        this.maxPeople = maxPeople;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
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

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getMaxPeople() {
        return this.maxPeople;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        if(this.roomType.equals("所有房型"))
            return "";
        return this.roomType;
    }

    public int getMinPrice() {
        if (this.minPrice=="") {
            return  -1;
        } else {
           return  Integer.valueOf(this.minPrice);
        }
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        if (this.maxPrice=="") {
            return  -1;
        } else {
            return  Integer.valueOf(this.maxPrice);
        }
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "不想写了";
    }
}
