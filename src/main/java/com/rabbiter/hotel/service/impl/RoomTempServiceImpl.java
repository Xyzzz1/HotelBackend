package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.RoomTemp;
import com.rabbiter.hotel.mapper.RoomTempMapper;
import com.rabbiter.hotel.service.RoomTempService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoomTempServiceImpl extends ServiceImpl<RoomTempMapper, RoomTemp> implements RoomTempService {
}
