package com.rabbiter.hotel.controller.user;

import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Type;
import com.rabbiter.hotel.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author：rabbiter
 * @date：2022/01/01 18:47
 * Description：
 */
@RestController
@RequestMapping("/user")
public class TypeController {

    @Resource
    private TypeService typeService;

    @GetMapping("/listTypes")
    public CommonResult<List<Type>> listTypes() {
        CommonResult<List<Type>> commonResult = new CommonResult<>();

        List<Type> list = typeService.list();

        commonResult.setData(list);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }
}
