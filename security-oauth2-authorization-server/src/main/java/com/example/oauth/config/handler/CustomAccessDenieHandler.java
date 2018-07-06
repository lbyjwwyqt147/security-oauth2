package com.example.oauth.config.handler;

import com.example.oauth.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAccessDenieHandler extends OAuth2AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException authException) throws IOException, ServletException {
        log.info(" ====================================================== ");
        log.info("请求url：" +httpServletRequest.getRequestURI());
        log.info("  ============ 权限认证失败..................... ");
        log.info(authException.getMessage());
        log.info(authException.getLocalizedMessage());
        authException.printStackTrace();
        Map<String,String> map =  new HashMap<>();
        map.put("status","403");
        map.put("message",authException.getMessage());
        map.put("path", httpServletRequest.getServletPath());
        map.put("timestamp", String.valueOf(LocalDateTime.now()));
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResultUtil.writeJavaScript(httpServletResponse,map);
    }
}
