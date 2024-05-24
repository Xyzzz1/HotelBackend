package com.rabbiter.hotel.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.domain.Type;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.*;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.TypeService;
import com.rabbiter.hotel.service.UserService;
import com.rabbiter.hotel.util.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/user")
public class RoomController {

    @Resource
    private RoomService roomService;
    @Resource
    private OrderService orderService;
    @Resource
    private TypeService typeService;
    @Resource
    private UserService userService;


    @GetMapping(value = "/listRoom")
    public CommonResult<List<ReturnRoomDTO>> listRoom() {
        CommonResult<List<ReturnRoomDTO>> commonResult = new CommonResult<>();
        Date earliestDate = new Date(0);
        Date latestDate = new Date(Long.MAX_VALUE);
        DateSectionDTO dateSectionDTO = new DateSectionDTO(earliestDate, latestDate);
        List<ReturnRoomDTO> list = roomService.listRooms(dateSectionDTO);

        commonResult.setData(list);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

    @PostMapping(value = "/roomDetail")
    public CommonResult<ReturnRoomDTO> roomDetail(@RequestParam("roomId") Integer roomId) {
        CommonResult<ReturnRoomDTO> commonResult = new CommonResult<>();

        ReturnRoomDTO returnRoomDTO = roomService.roomDetail(roomId);
//        System.out.println(returnRoomDTO);
        commonResult.setData(returnRoomDTO);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

    @GetMapping(value = "/roomTypes")
    public CommonResult<String[]> roomTypes() {
        CommonResult<String[]> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("type_name").groupBy("type_name");
        List<Object> typeNames = typeService.getBaseMapper().selectObjs(queryWrapper);
        String[] types = new String[typeNames.size()];
        for (int i = 0; i < typeNames.size(); i++) {
            types[i] = String.valueOf(typeNames.get(i));
        }
        commonResult.setData(types);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }


    @PostMapping("/listRoomsByTypeId")
    public CommonResult<List<ReturnRoomDTO>> listRoomsByTypeId(@RequestBody TypeDTO typeDTO) {
        CommonResult<List<ReturnRoomDTO>> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();

        DateSectionDTO dateSectionDTO = new DateSectionDTO();
        BeanUtils.copyProperties(typeDTO, dateSectionDTO);
        List<ReturnRoomDTO> roomList = roomService.listRooms(dateSectionDTO);

        List<ReturnRoomDTO> returnRoomList = new ArrayList<>();
        if (0 != roomList.size()) {
            for (ReturnRoomDTO room : roomList) {
                if (typeDTO.getTypeId().equals(room.getType().getId())) {
                    returnRoomList.add(room);
                }
            }
        }

        commonResult.setData(returnRoomList);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

}
