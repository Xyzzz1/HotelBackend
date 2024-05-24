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

    @PostMapping(value = "/listAllSpareRoom")
    public CommonResult<List<ReturnRoomDTO>> listRoom(@RequestBody SearchRoomDTO searchRoomDTO) {
        CommonResult<List<ReturnRoomDTO>> commonResult = new CommonResult<>();
        DateSectionDTO dateSectionDTO = new DateSectionDTO(searchRoomDTO.getInTime(), searchRoomDTO.getLeaveTime());
        List<ReturnRoomDTO> list = roomService.listRooms(dateSectionDTO);

        String roomType = searchRoomDTO.getRoomType();
        int maxPeople = searchRoomDTO.getMaxPeople();
        int minPrice = searchRoomDTO.getMinPrice();
        int maxPrice = searchRoomDTO.getMaxPrice();

        Iterator<ReturnRoomDTO> iterator = list.iterator();
        while (iterator.hasNext()) {
            ReturnRoomDTO returnRoom = iterator.next();
            if (!roomType.equals("") && !returnRoom.getType().getTypeName().equals(roomType)) {
                iterator.remove();
                continue;
            }
            if (returnRoom.getMaxPeople() < maxPeople) {
                iterator.remove();
                continue;
            }
            if (minPrice != -1 && returnRoom.getType().getPrice() < minPrice) {
                iterator.remove();
                continue;
            }
            if (maxPrice != -1 && returnRoom.getType().getPrice() > maxPrice) {
                iterator.remove();
                continue;
            }
        }
        commonResult.setData(list);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

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


    @PostMapping("/bookRoom")
    public CommonResult<String> bookRoom(@RequestBody BookDTO bookDTO) {
        CommonResult<String> commonResult = new CommonResult<>();


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone",bookDTO.getPhone());
        User user=userService.getBaseMapper().selectOne(queryWrapper);

        if (user == null) {
            commonResult.setData("不存在该用户!");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            return commonResult;
        }

        Room room =roomService.getById(bookDTO.getRoomId());
        room.setState(1);
        roomService.updateById(room);

        Type type = typeService.getById(room.getType());
        Order order = new Order();
        BeanUtils.copyProperties(bookDTO, order);
        order.setUserId(user.getId());
        order.setFlag(1); //已经办理入住
        order.setLeaveTime(order.getInTime()); //刚开始离店=入住时间


        //int days = (int) Math.ceil((bookDTO.getLeaveTime().getTime() - bookDTO.getInTime().getTime()) / (60 * 60 * 24 * 1000 * 1.0));
        // System.out.println(days);

        order.setRealPrice((double)0); //刚开始为0
        // System.out.println(order);

        orderService.save(order);

        user.setState(1);
        userService.updateById(user);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData("预订成功!");

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
