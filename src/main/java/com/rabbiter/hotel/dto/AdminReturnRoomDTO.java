package com.rabbiter.hotel.dto;

import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.domain.Type;

/**
 * @author：rabbiter
 * @date：2022/01/07 10:20
 * Description：
 */
public class AdminReturnRoomDTO {

    private Room room;
    private Type type;

    public AdminReturnRoomDTO() {
    }

    public AdminReturnRoomDTO(Room room, Type type) {
        this.room = room;
        this.type = type;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AdminReturnRoomDTO{" +
                "room=" + room +
                ", type=" + type +
                '}';
    }
}
