package com.rabbiter.hotel.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.ConstantCode;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.util.WebUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/service/conditioner")
public class AirConditionerController {


    @Resource
    private OrderService orderService;

    @GetMapping(value = "/getRoomID")
    public CommonResult<Integer> getRoomID() {
        CommonResult<Integer> commonResult = new CommonResult<>();
        User user = (User) WebUtils.getSession().getAttribute("loginUser");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", user.getId());
        Date currentDate = new Date();
        Integer roomID = null;
        List<Order> orders = orderService.getBaseMapper().selectList(queryWrapper);
        for (Order order : orders) {
            if (currentDate.before(order.getLeaveTime()) && currentDate.after(order.getInTime())) {
                roomID = order.getRoomId();
                break;
            }
        }
        if (roomID != null) {
            commonResult.setData(roomID);
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        }
        return commonResult;
    }

    @GetMapping(value = "/status")
    public CommonResult<AirConditionerStatusDTO> status(@RequestParam("roomID") Integer roomID) {
        CommonResult<AirConditionerStatusDTO> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO=new AirConditionerStatusDTO();
        airConditionerStatusDTO=findServer(0,roomID);
        if(airConditionerStatusDTO==null){
            airConditionerStatusDTO=findServer(1,roomID);
        }else{
            commonResult.setData(airConditionerStatusDTO);
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            return commonResult;
        }

        commonResult.setData(airConditionerStatusDTO);
        if(airConditionerStatusDTO==null){
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        }else{
            commonResult.setCode(ConstantCode.WAITING); //在等待队列
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        System.out.println(commonResult.toString());
        return commonResult;
    }


    public AirConditionerStatusDTO findServer(int type, int roomID) {
        QueueDTO.setQueueType(type);
        if (!QueueDTO.isEmpty()) {
            for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.getQueue()) {
                if (airConditionerStatusDTO.getRoomID() == roomID) {
                 return airConditionerStatusDTO;
                }
            }
        }
        return null;
    }
}
