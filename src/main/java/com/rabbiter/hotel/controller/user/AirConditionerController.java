package com.rabbiter.hotel.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.ConstantCode;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.*;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.TypeService;
import com.rabbiter.hotel.service.manager.QueueManager;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.sse.SseEmitterServer;
import com.rabbiter.hotel.service.manager.RecordManager;
import com.rabbiter.hotel.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/user/conditioner")
public class AirConditionerController {
    private final Logger logger = LoggerFactory.getLogger(SseEmitterServer.class);
    @Resource
    private QueueManager queueManager;

    @Resource
    private RecordManager recordManager;

    @Resource
    private OrderService orderService;

    @Resource
    private TypeService typeService;

    @Resource
    private RoomService roomService;


    @GetMapping(value = "/getRoomId")
    public CommonResult<Integer> getRoomId(@RequestParam(value = "userId", required = false) Integer userId) {

        CommonResult<Integer> commonResult = new CommonResult<>();
        if (userId == null) {
            User user = (User) WebUtils.getSession().getAttribute("loginUser");
            userId = user.getId();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("flag", 1);
        Order orders = orderService.getBaseMapper().selectOne(queryWrapper);
        if (orders != null) {
            commonResult.setData(orders.getRoomId());
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        }
        return commonResult;
    }

    @GetMapping(value = "/status")
    public CommonResult<AirConditionerStatusDTO> status(@RequestParam("roomId") Integer roomId) {
        CommonResult<AirConditionerStatusDTO> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(0, roomId);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(1, roomId);
        } else {
            commonResult.setData(airConditionerStatusDTO);
            airConditionerStatusDTO.setReason(-1);
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            return commonResult;
        }

        commonResult.setData(airConditionerStatusDTO);
        if (airConditionerStatusDTO == null) {
            AirConditionerStatusDTO nullDTO = new AirConditionerStatusDTO();
            nullDTO.setPowerOn(false);
            commonResult.setData(nullDTO);
        } else {
            commonResult.setData(airConditionerStatusDTO);
            airConditionerStatusDTO.setReason(-3);
        }
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        logger.info("/status: " + commonResult.toString());
        return commonResult;
    }


    @PostMapping(value = "/turnOn")
    public CommonResult<String> turnOn(@RequestBody AirConditionerStatusDTO airConditionerStatusDTO) {
        System.out.println(airConditionerStatusDTO.toString());
        Order order = getLatestOrder(airConditionerStatusDTO.getRoomId());
        System.out.println(order.toString());

        airConditionerStatusDTO.setUserId(order.getUserId());
        CommonResult<String> commonResult = new CommonResult<>();
        if (queueManager.enQueue(airConditionerStatusDTO) == QueueManager.SERVICE) {
            commonResult.setData("加入服务队列");
        } else {
            commonResult.setData("加入等待队列");
        }
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        logger.info("/turnOn: " + commonResult.toString());
        return commonResult;
    }


    @PostMapping(value = "/adjustTargetTemperature")
    public CommonResult<String> adjustTargetTemperature(@RequestParam("roomId") Integer roomID, @RequestParam("targetTemperature") Integer temp) {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(0, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(1, roomID);
        }

        if (airConditionerStatusDTO == null) {
            commonResult.setData("空调温度调节发生错误！");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        } else {
            airConditionerStatusDTO.setTargetTemperature(temp);
            recordManager.updateAndAdd(airConditionerStatusDTO);
            commonResult.setData("roomID" + roomID + ": 温度修改成功");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        return commonResult;
    }

    @PostMapping(value = "/adjustWindSpeed")
    public CommonResult<String> adjustWindSpeed(@RequestParam("roomId") Integer roomID, @RequestParam("windSpeed") Integer windSpeed) {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(0, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(1, roomID);
        }

        if (airConditionerStatusDTO == null) {
            commonResult.setData("空调风速调节发生错误！");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        } else {
            airConditionerStatusDTO.setWindSpeed(windSpeed);
            recordManager.updateAndAdd(airConditionerStatusDTO);
            commonResult.setData("roomID" + roomID + ": 风速修改成功");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        }
        return commonResult;
    }


    @PostMapping(value = "turnOff")
    public CommonResult<String> turnOff(@RequestParam("roomId") Integer roomID) {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO airConditionerStatusDTO = findServer(0, roomID);
        if (airConditionerStatusDTO == null) {
            airConditionerStatusDTO = findServer(1, roomID);
            if (airConditionerStatusDTO == null) {
                commonResult.setData("当前空调并未开机。");
                commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
                commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
                return commonResult;
            }
        }

        Order order = getLatestOrder(roomID);
        Date currentLeaveData = order.getLeaveTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentLeaveData);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        order.setLeaveTime(calendar.getTime());//离店日期加一天

        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", roomID);
        Room room = roomService.getOne(queryWrapper);
        Type type = typeService.getById(room.getType());

        order.setRealPrice(order.getRealPrice() + type.getPrice()); //加一天的房价
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("id", order.getId());
        orderService.update(order, orderQueryWrapper);

        if (queueManager.deQueue(airConditionerStatusDTO, 2) == QueueManager.WAIT)
            commonResult.setData("已从等待队列中移除。");
        else
            commonResult.setData("已从服务队列中移除。");

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }


    @GetMapping(value = "/subscribe")
    public SseEmitter subscribe(@RequestParam("roomId") Integer roomID) {
        return SseEmitterServer.connect(Integer.toString(roomID));
    }

    public AirConditionerStatusDTO findServer(int type, int roomID) {
        if (type == 0)//查服务队列
            for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.serviceQueue) {
                if (airConditionerStatusDTO.getRoomId() == roomID) {
                    return airConditionerStatusDTO;
                }
            }
        else
            for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.waitQueue) {
                if (airConditionerStatusDTO.getRoomId() == roomID) {
                    return airConditionerStatusDTO;
                }
            }

        return null;
    }

    @PostMapping(value = "/adjustTargetDuration")
    public CommonResult<String> adjustTargetDuration(@RequestParam("targetDuration") Integer targetDuration, @RequestParam("roomId") Integer roomId) {
        CommonResult<String> commonResult = new CommonResult<>();
        AirConditionerStatusDTO updateDTO= queueManager.updateDuration(roomId, targetDuration);
        if (updateDTO!=null) {
            recordManager.updateAndAdd(updateDTO);
            commonResult.setData("调整运行时间成功！");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        } else {
            commonResult.setData("调整失败！空调未开启服务！");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        }

        return commonResult;

    }

    @PostMapping(value = "/adjustMode")
    public CommonResult<String> adjustMode(@RequestParam("roomId") Integer roomId, @RequestParam("mode") Integer mode) {
        boolean isFind = false;
        for (AirConditionerStatusDTO updateDTO : QueueDTO.waitQueue) {
            if (updateDTO.getRoomId() == roomId) {
                updateDTO.setMode(mode);
                recordManager.updateAndAdd(updateDTO);
                isFind = true;
            }
        }
        if (!isFind)
            for (AirConditionerStatusDTO updateDTO : QueueDTO.serviceQueue) {
                if (updateDTO.getRoomId() == roomId) {
                    updateDTO.setMode(mode);
                    recordManager.updateAndAdd(updateDTO);
                    isFind = true;
                }
            }

        CommonResult<String> commonResult = new CommonResult<>();

        if (isFind) {
            commonResult.setData("调整模式成功！");
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        } else {
            commonResult.setData("调整失败！空调未开启服务！");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
        }

        return commonResult;
    }

    private Order getLatestOrder(Integer roomId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("flag", 1);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 1");
        Order order = orderService.getOne(queryWrapper); //最近的一条订单记录
        return order;
    }


}
