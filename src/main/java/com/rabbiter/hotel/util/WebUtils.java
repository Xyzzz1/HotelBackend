package com.rabbiter.hotel.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:10
 * Description：
 */
public class WebUtils {
    /**
     * 得到request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request;
    }

    /**
     * 得到session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }
}
