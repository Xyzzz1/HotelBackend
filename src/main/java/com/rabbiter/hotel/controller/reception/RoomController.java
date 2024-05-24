package com.rabbiter.hotel.controller.reception;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author：rabbiter
 * @date：2022/01/01 13:08
 * Description：
 */
@RestController("adminRoomController")
@RequestMapping("/reception")
public class RoomController {

    @Resource
    private RoomService roomService;
    @Resource
    private OrderService orderService;
    @Resource
    private TypeService typeService;
    @Resource
    private UserService userService;

    @PostMapping (value = "/listRoom")
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

    @PostMapping(value = "/roomDetail")
    public CommonResult<AdminReturnRoomDTO> roomDetail(@RequestParam("roomId") Integer roomId) {
        CommonResult<AdminReturnRoomDTO> commonResult = new CommonResult<>();

        AdminReturnRoomDTO returnRoomDTO = roomService.adminRoomDetail(roomId);

        commonResult.setData(returnRoomDTO);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

    @PostMapping("/checkIn")
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

    @PostMapping("/addRoom")
    public CommonResult<String> addRoom(@RequestBody Room room) {
        CommonResult<String> commonResult = new CommonResult<>();
        Room roomByNumber = roomService.getOne(new QueryWrapper<Room>().eq("number", room.getNumber()));
        if(roomByNumber != null) {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("房间号已存在");
            return commonResult;
        }
        boolean result = roomService.save(room);

        if (result) {
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("添加房间成功");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("添加房间失败");
        }

        return commonResult;
    }

    @PostMapping("/updateRoom")
    public CommonResult<String> updateRoom(@RequestBody Room room) {
        CommonResult<String> commonResult = new CommonResult<>();
        Room roomByNumber = roomService.getOne(new QueryWrapper<Room>().eq("number", room.getNumber()).ne("id", room.getId()));
        if(roomByNumber != null) {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("房间号已存在");
            return commonResult;
        }

        boolean result = roomService.updateById(room);

        if (result) {
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("修改房间信息成功");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("修改房间信息失败");
        }

        return commonResult;
    }

    @PostMapping("/deleteRoom")
    public CommonResult<String> deleteRoom(@RequestParam("roomId") Integer roomId) {
        CommonResult<String> commonResult = new CommonResult<>();


        // 移除关联订单
        orderService.remove(
                new QueryWrapper<Order>().eq("room_id", roomId)
        );

        boolean result = roomService.removeById(roomId);

        if (result) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("room_id", roomId);
            orderService.remove(queryWrapper);

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("删除房间成功");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("删除房间失败");
        }

        return commonResult;
    }

}
