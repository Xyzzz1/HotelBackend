package com.rabbiter.hotel.controller.reception;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.*;
import com.rabbiter.hotel.service.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author：rabbiter
 * @date：2022/01/01 13:08
 * Description：
 */
@RestController("adminOrderController")
@RequestMapping("/reception")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;
    @Resource
    private SpecificBillService specificBillService;
    @Resource
    private TypeService typeService;

    @GetMapping("/listOrders")
    public CommonResult<List<Order>> listOrders(@RequestParam("orderFlags") List<Integer> flags) {
        CommonResult<List<Order>> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.in("flag", flags);
        List<Order> userList = orderService.list(queryWrapper);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(userList);

        return commonResult;
    }

    @PostMapping("/unsubscribe")
    public CommonResult<String> unsubscribe(@RequestParam("orderId") Integer orderId) {
        CommonResult<String> commonResult = new CommonResult<>();

        Order order = orderService.getById(orderId);
        order.setFlag(2);
        boolean result = orderService.updateById(order);

        if (result) {
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("退订成功");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("退订失败");
        }

        return commonResult;
    }

    @PostMapping("/handle")
    public CommonResult<String> handle(@RequestParam("orderId") Integer orderId) {
        CommonResult<String> commonResult = new CommonResult<>();

        Order order = orderService.getById(orderId);
        order.setFlag(1);
        boolean result = orderService.updateById(order);

        if (result) {
            roomService.bookRoom(order.getRoomId());
            User user = userService.getById(order.getUserId());
            int jifen = (int) (user.getJifen() + order.getRealPrice());
            user.setJifen(jifen);
            userService.updateById(user);

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("办理入住成功");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("办理入住失败");
        }

        return commonResult;
    }

    @GetMapping("/specificBills")
    public CommonResult<List<SpecificBill>> getSpecificBills(@RequestParam("userId") Integer userId) {
        CommonResult<List<SpecificBill>> commonResult = new CommonResult<>();
        QueryWrapper<SpecificBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<SpecificBill> bilList = specificBillService.getBaseMapper().selectList(queryWrapper);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(bilList);
        return commonResult;
    }

    @PostMapping("/checkOut")
    public CommonResult<String> checkOut(@RequestParam("roomId") Integer roomId) {
        CommonResult<String> commonResult = new CommonResult<>();

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("room_id", roomId);
        orderQueryWrapper.eq("flag", 1);
        Order order = orderService.getOne(orderQueryWrapper);
        order.setFlag(3);

        orderQueryWrapper.eq("id", order.getId());
        orderService.update(order, orderQueryWrapper);

        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("id", roomId);
        Room room = roomService.getOne(roomQueryWrapper);
        room.setState(0);
        roomService.update(room, roomQueryWrapper);


        if (order != null && room != null) {
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("退房成功！");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("数据库t_order,room表修改错误！");
        }

        return commonResult;

    }

    @PostMapping("/generateBill")
    public CommonResult<Map<String, Object>> generateBill(@RequestParam("userId") Integer userId) {
        CommonResult<Map<String, Object>> commonResult = new CommonResult<>();
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId);
        orderQueryWrapper.eq("flag", 1);
        Order currentOrder = orderService.getOne(orderQueryWrapper);
        result.put("roomId", currentOrder.getRoomId());

        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("id", currentOrder.getRoomId());
        Room room = roomService.getOne(roomQueryWrapper);
        result.put("roomNumber", room.getNumber());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", currentOrder.getUserId());
        User user = userService.getOne(userQueryWrapper);
        result.put("userName", user.getUserName());

        Map<String, Object> accommodationFee = new HashMap<>();
        accommodationFee.put("inTime", currentOrder.getInTime());
        accommodationFee.put("leaveTime", currentOrder.getLeaveTime());
        int dayDiff = getDaysDifference(currentOrder.getLeaveTime(), currentOrder.getInTime());
        accommodationFee.put("dayCount", dayDiff);

        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("id", room.getType());
        Type type = typeService.getOne(typeQueryWrapper);
        accommodationFee.put("unitPrice", type.getPrice());

        accommodationFee.put("totalPrice", currentOrder.getRealPrice());

        result.put("accommodationFee", accommodationFee);

        Map<String, Object> airConditionerFee = new HashMap<>();
        QueryWrapper<SpecificBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<SpecificBill> bilList = specificBillService.getBaseMapper().selectList(queryWrapper);
        airConditionerFee.put("specificCount", bilList.size());

        QueryWrapper<SpecificBill> specificBillQueryWrapper = new QueryWrapper<>();
        specificBillQueryWrapper.eq("user_id", userId);
        specificBillQueryWrapper.orderByDesc("id");
        specificBillQueryWrapper.last("LIMIT 1");
        SpecificBill pre_specificBill = specificBillService.getOne(specificBillQueryWrapper); //最近的一条记录

        Float conditionerFee=0f;
        if(pre_specificBill!=null) {
            Date currentOrderDate=currentOrder.getCreateTime();
            if(pre_specificBill.getStartTime().getTime()>currentOrderDate.getTime()){ //对应记录是上一次的订单
                conditionerFee=pre_specificBill.getCurrentFee();
            }
        }
        airConditionerFee.put("totalPrice", conditionerFee);

        result.put("airConditionerFee", airConditionerFee);
        result.put("totalFee", currentOrder.getRealPrice() + conditionerFee);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(result);

        return commonResult;
    }


    private int getDaysDifference(Date currentDate, Date preDate) {
        LocalDate localDate1 = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = preDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(localDate2, localDate1);

        // 向上取整天数差异
        int roundedDays =  (int)Math.ceil((double) daysBetween);

        return roundedDays;
    }



}
