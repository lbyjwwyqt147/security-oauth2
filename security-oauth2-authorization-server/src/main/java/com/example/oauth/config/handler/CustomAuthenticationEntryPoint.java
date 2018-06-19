package com.example.oauth.config.handler;

import com.example.oauth.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("      ========================================= 身份认证失败..................... ");
        Map<String,String> map =  new HashMap<>();
        map.put("status","401");
        map.put("message","Access Denied.");
        ResultUtil.writeJavaScript(httpServletResponse,map);
       // httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Access Denied");
    }
}
