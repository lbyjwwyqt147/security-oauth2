package com.example.oauth.service.impl;

import com.example.oauth.entity.SysAccount;
import com.example.oauth.repository.SysAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private SysAccountRepository repository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysAccount user = repository.findByUserAccount(username);
        if(user == null){
            log.info("登录用户【"+username + "】不存在.");
            throw new UsernameNotFoundException("登录用户【"+username + "】不存在.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserAccount(), user.getUserPwd(), getAuthority());
    }

    private List getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


}
