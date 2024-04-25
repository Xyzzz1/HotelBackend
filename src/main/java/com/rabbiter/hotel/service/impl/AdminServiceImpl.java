package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Admin;
import com.rabbiter.hotel.mapper.AdminMapper;
import com.rabbiter.hotel.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:04
 * Description：
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    
}

