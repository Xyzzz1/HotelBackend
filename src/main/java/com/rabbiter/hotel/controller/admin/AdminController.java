package com.rabbiter.hotel.controller.admin;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Admin;
import com.rabbiter.hotel.dto.AdminLoginDTO;
import com.rabbiter.hotel.service.AdminService;
import com.rabbiter.hotel.util.WebUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/21
 * Description:
 */
@RestController("adminAdminController")
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    @PostMapping(value = "/login")
    public CommonResult<Admin> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        CommonResult<Admin> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("email", adminLoginDTO.getEmail());
        String md5Password = SecureUtil.md5(adminLoginDTO.getPassword());
        queryWrapper.eq("password", md5Password);
        Admin admin = adminService.getOne(queryWrapper);

        if (null != admin) {

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData(admin);
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData(null);
        }

        return commonResult;
    }

    @GetMapping("/logout")
    public CommonResult<String> logout(){
        CommonResult<String> commonResult = new CommonResult<>();

        WebUtils.getSession().removeAttribute("loginAdmin");

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData("登出成功!");

        return commonResult;
    }

}
