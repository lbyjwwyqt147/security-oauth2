package com.example.oauth.config;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableResourceServer   //注解来开启资源服务器
public class ResourceConfiguration  extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource_id";
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private TokenStore tokenStore;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(true).tokenServices(tokenServices);
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {

        //如果 启用 http.addFilterBefore(oAuth2AuthenticationFilter,AbstractPreAuthenticatedProcessingFilter.class) 代码 则需要启用下面被注释的代码
        OAuth2AuthenticationProcessingFilter oAuth2AuthenticationFilter = new OAuth2AuthenticationProcessingFilter();
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setExceptionTranslator(webResponseExceptionTranslator());
        oAuth2AuthenticationFilter.setAuthenticationEntryPoint(oAuth2AuthenticationEntryPoint);
        OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        oAuth2AuthenticationManager.setTokenServices(defaultTokenServices);
        oAuth2AuthenticationFilter.setAuthenticationManager(oAuth2AuthenticationManager);

        // 配置那些资源需要保护的
        http.csrf().disable()
                .requestMatchers().antMatchers("/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        http.addFilterBefore(oAuth2AuthenticationFilter,AbstractPreAuthenticatedProcessingFilter.class); // 这种方式也可以达到token校验失败后自定义返回数据格式  使用此方式需要将上面的代码启用
    }

    /**
     * 重写 token 验证失败后自定义返回数据格式
     * @return
     */
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity translate(Exception e) throws Exception {
                ResponseEntity responseEntity = super.translate(e);
                OAuth2Exception body = (OAuth2Exception) responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                // do something with header or response
                if(401==responseEntity.getStatusCode().value()){
                    //自定义返回数据格式
                    Map<String,String> map =  new HashMap<>();
                    map.put("status","401");
                    map.put("message",e.getMessage());
                    map.put("timestamp", String.valueOf(LocalDateTime.now()));
                    return new ResponseEntity(JSON.toJSONString(map), headers, responseEntity.getStatusCode());
                }else{
                    return new ResponseEntity(body, headers, responseEntity.getStatusCode());
                }
            }
        };
    }
}
