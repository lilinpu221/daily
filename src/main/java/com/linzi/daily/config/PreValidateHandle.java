package com.linzi.daily.config;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class PreValidateHandle extends OncePerRequestFilter implements InitializingBean {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //设置允许跨域的配置
        String origin = request.getHeader("Origin");
        if(CharSequenceUtil.isNotBlank(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }else {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        // 允许的访问方法
        response.setHeader("Access-Control-Allow-Methods","*");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        String allowHeaders = "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization";
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request,response);
    }
}
