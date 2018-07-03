package com.example.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomLoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("================= 登录失败 =======================");
        Map<String,String> map =  new HashMap<>();
        map.put("status","1");
        log.info(e.getLocalizedMessage());
        //用户登录时身份认证未通过
        if (e instanceof BadCredentialsException){
            map.put("message","用户名或者密码错误.");
            log.info("用户登录时：用户名或者密码错误.");
        }else{
            log.info("登录失效.");
            map.put("message","登录失效.");
        }
        httpServletResponse.sendRedirect("/login");
    }
}
