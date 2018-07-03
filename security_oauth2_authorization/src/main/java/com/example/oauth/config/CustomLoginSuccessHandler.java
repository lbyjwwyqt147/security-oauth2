package com.example.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> map =  new HashMap<>();
        map.put("status","0");
        //获得授权后可得到用户信息
        User userDetails =  (User) authentication.getPrincipal();
        //将身份 存储到SecurityContext里
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        log.info(securityContext.getAuthentication().getPrincipal().toString());
        StringBuffer msg = new StringBuffer("用户：");
        msg.append(userDetails.getUsername()).append(" 成功登录系统.");
        log.info(msg.toString());
        map.put("message","登录成功.");
        map.put("userDetails",userDetails);
        httpServletResponse.sendRedirect("/oauth/confirm_access");

    }

}
