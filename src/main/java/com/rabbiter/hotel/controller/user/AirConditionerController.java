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
import com.rabbiter.hotel.sse.SseEmitterServer;
import com.rabbiter.hotel.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/service/conditioner")
public class AirConditionerController {
    private final Logger logger = LoggerFactory.getLogger(SseEmitterServer.class);
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
        AirConditionerStatusDTO airConditionerStatusDTO = new AirConditionerStatusDTO();
        airConditionerStatusDTO = findServer(0, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(1, roomID);
        } else {
            commonResult.setData(airConditionerStatusDTO);
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            return commonResult;
        }

        commonResult.setData(airConditionerStatusDTO);
        if (airConditionerStatusDTO == null) {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        } else {
            commonResult.setCode(ConstantCode.WAITING); //在等待队列
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        logger.info("/status: "+commonResult.toString());
        return commonResult;
    }


    @PostMapping(value = "/turnOn")
    public CommonResult<String> turnOn(@RequestBody AirConditionerStatusDTO airConditionerStatusDTO) {
        CommonResult<String> commonResult = new CommonResult<>();
        QueueDTO.setQueueType(0);
        if (!QueueDTO.isFull()) {
            QueueDTO.enqueue(airConditionerStatusDTO);
            commonResult.setData("加入服务队列");
            SseEmitterServer.sendMessage(Integer.toString(airConditionerStatusDTO.getRoomID()),"service");
        } else {
            QueueDTO.setQueueType(1);
            QueueDTO.enqueue(airConditionerStatusDTO);
            commonResult.setData("加入等待队列");
            SseEmitterServer.sendMessage(Integer.toString(airConditionerStatusDTO.getRoomID()),"waiting");
        }
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        logger.info("/turnOn: "+commonResult.toString());
        return commonResult;
    }

    @PostMapping(value="turnOff")
    public CommonResult<String> turnOff(@RequestParam("roomID") Integer roomID){
        CommonResult<String> commonResult=new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO=findServer(0,roomID);
        if(airConditionerStatusDTO==null){
            airConditionerStatusDTO=findServer(1,roomID);
            if(airConditionerStatusDTO==null){
                commonResult.setData("当前空调并未开机。");
                commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
                commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
                return commonResult;
            }else{
                commonResult.setData("已从等待队列中移除。");
            }
        }else{
            commonResult.setData("已从服务队列中移除。");
        }
        QueueDTO.remove(airConditionerStatusDTO);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        logger.info("/turnOff: "+commonResult.toString());
        return commonResult;
    }

    @GetMapping(value = "/subscribe")
    public SseEmitter subscribe(@RequestParam("roomID") Integer roomID) {
        return SseEmitterServer.connect(Integer.toString(roomID));
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
