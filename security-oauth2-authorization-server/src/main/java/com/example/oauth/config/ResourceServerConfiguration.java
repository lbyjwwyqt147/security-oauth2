package com.example.oauth.config;

import com.example.oauth.config.handler.CustomAuthenticationEntryPoint;
import com.example.oauth.config.handler.CustomLoginFailHandler;
import com.example.oauth.config.handler.CustomLoginSuccessHandler;
import com.example.oauth.config.handler.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/***
 * 资源服务认证配置
 */
@Configuration
@EnableResourceServer   //注解来开启资源服务器
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "user";
  /*  @Autowired
    private ResourceServerTokenServices tokenServices;*/

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
       // resources.resourceId(RESOURCE_ID).stateless(false).tokenServices(tokenServices);
        resources.resourceId(RESOURCE_ID).stateless(true);
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/**","/login","/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint())  //认证失败的业务处理
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(customLoginFailHandler())
                .successHandler(customLoginSuccessHandler())
                .permitAll();

       /* http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/oauth/**","/loginPage","/logout/**","/home").permitAll()
                //.antMatchers("/users/**").authenticated() //配置users访问控制，必须认证过后才可以访问
                .antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint())  //认证失败的业务处理
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/loginPage")
                .defaultSuccessUrl("/home")
                .failureHandler(customLoginFailHandler())
                .successHandler(customLoginSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler());*/
    }


    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationFailureHandler customLoginFailHandler(){
        return new CustomLoginFailHandler();
    }

    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }
}
