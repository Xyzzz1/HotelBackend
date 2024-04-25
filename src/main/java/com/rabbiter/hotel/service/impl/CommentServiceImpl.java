package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Comment;
import com.rabbiter.hotel.mapper.CommentMapper;
import com.rabbiter.hotel.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:04
 * Description：
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


}

