package com.rabbiter.hotel.dto;

import com.rabbiter.hotel.domain.Order;

/**
 * @author：rabbiter
 * @date：2022/01/03 12:46
 * Description：
 */
public class ReturnOrderDTO {

    private Order order;
    private ReturnUserDTO user;
    private ReturnRoomDTO room;

    public ReturnOrderDTO() {
    }

    public ReturnOrderDTO(Order order, ReturnUserDTO user, ReturnRoomDTO room) {
        this.order = order;
        this.user = user;
        this.room = room;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ReturnUserDTO getUser() {
        return user;
    }

    public void setUser(ReturnUserDTO user) {
        this.user = user;
    }

    public ReturnRoomDTO getRoom() {
        return room;
    }

    public void setRoom(ReturnRoomDTO room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "ReturnOrderDTO{" +
                "order=" + order +
                ", user=" + user +
                ", room=" + room +
                '}';
    }
}
