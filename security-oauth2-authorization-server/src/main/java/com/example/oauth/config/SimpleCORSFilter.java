package com.example.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @FileName: SimpleCORSFilter
 * @Company:
 * @author    ljy
 * @Date      2018年05月11日
 * @version   1.0.0
 * @remark:   自定义跨域访问过滤器
 *
 */
@Slf4j
@Component
public class SimpleCORSFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletRequest.setCharacterEncoding("utf-8");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");//允许所以域名访问，
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");//允许的访问方式
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,Authorization");
        httpServletResponse.setHeader("Access-Control-Request-Headers", "x-requested-with,content-type,Accept,Authorization");
        httpServletResponse.setHeader("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}