package com.rabbiter.hotel.controller.reception;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.SpecificBillService;
import com.rabbiter.hotel.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public CommonResult<List<SpecificBill>> getSpecificBills(@RequestParam("userId") Integer userId){
        CommonResult<List<SpecificBill>> commonResult = new CommonResult<>();
        QueryWrapper<SpecificBill> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<SpecificBill> bilList= specificBillService.getBaseMapper().selectList(queryWrapper);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(bilList);
        return commonResult;
    }

    @PostMapping("/checkOut")
    public CommonResult<String> checkOut(@RequestParam("roomId") Integer roomId,@RequestParam("realPrice") Double realPrice){
        CommonResult<String>commonResult = new CommonResult<>();

        QueryWrapper<Order> orderQueryWrapper=new QueryWrapper();
        orderQueryWrapper.eq("room_id",roomId);
        orderQueryWrapper.eq("flag",1);
        Order order=orderService.getOne(orderQueryWrapper);
        order.setFlag(3);
        order.setRealPrice(realPrice);
        order.setLeaveTime(new Date());

        orderQueryWrapper.eq("id",order.getId());
        orderService.update(order,orderQueryWrapper);

        QueryWrapper<Room> roomQueryWrapper=new QueryWrapper<>();
        roomQueryWrapper.eq("id",roomId);
        Room room=roomService.getOne(roomQueryWrapper);
        room.setState(0);
        roomService.update(room,roomQueryWrapper);

        if(order!=null&&room!=null){
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("退房成功！");
        }else{
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("数据库t_order,room表修改错误！");
        }

        return commonResult;

    }
}
