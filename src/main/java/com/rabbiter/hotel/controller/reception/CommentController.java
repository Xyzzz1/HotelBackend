package com.rabbiter.hotel.controller.reception;

import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Comment;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.ReturnCommentDTO;
import com.rabbiter.hotel.dto.ReturnUserDTO;
import com.rabbiter.hotel.service.CommentService;
import com.rabbiter.hotel.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：rabbiter
 * @date：2022/01/07 1:23
 * Description：
 */
@RestController("adminCommentController")
@RequestMapping("/reception")
public class CommentController {

    @Resource
    private CommentService commentService;
    @Resource
    private UserService userService;

    @GetMapping("/listComment")
    public CommonResult<List<ReturnCommentDTO>> listComment() {
        CommonResult<List<ReturnCommentDTO>> commonResult = new CommonResult<>();
        List<ReturnCommentDTO> returnCommentList = new ArrayList<>();

        List<Comment> commentList = commentService.list();

        for (Comment comment : commentList) {
            ReturnCommentDTO commentDTO = new ReturnCommentDTO();
            User user = userService.getById(comment.getUserId());
            ReturnUserDTO returnUserDTO = new ReturnUserDTO();
            BeanUtils.copyProperties(user, returnUserDTO);

            commentDTO.setComment(comment);
            commentDTO.setUser(returnUserDTO);

            returnCommentList.add(commentDTO);
        }

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(returnCommentList);

        return commonResult;
    }
}
