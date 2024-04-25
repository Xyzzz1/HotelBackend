package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.mapper.OrderMapper;
import com.rabbiter.hotel.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:04
 * Description：
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


}

