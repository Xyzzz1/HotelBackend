package com.rabbiter.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.dto.AdminReturnRoomDTO;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnRoomDTO;

import java.util.List;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:02
 * Description：
 */
public interface RoomService extends IService<Room> {

    Boolean bookRoom(Integer roomId);

    Boolean finishRoom(Integer roomId);

    List<ReturnRoomDTO> listRooms(DateSectionDTO dateSectionDTO);

    ReturnRoomDTO roomDetail(Integer roomId);

    AdminReturnRoomDTO adminRoomDetail(Integer roomId);
}
