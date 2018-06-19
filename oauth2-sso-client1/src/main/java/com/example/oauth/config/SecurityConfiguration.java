package com.example.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${oauth2-server}/exit")
    private String logoutUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/oauth/**","/index","/logout/**","/home","/login").permitAll()
                .and()
                .formLogin().loginPage("/index")
                .and()
                .logout()
                .logoutSuccessUrl(logoutUrl)
                .and().authorizeRequests().anyRequest().authenticated();
    }

}
