package com.rabbiter.hotel.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author：rabbiter
 * @date：2022/01/01 13:08
 * Description：
 */
@RestController("adminOrderController")
@RequestMapping("/admin")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;

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
}
