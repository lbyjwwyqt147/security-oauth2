package com.example.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping(value = "users/list")
    public String listUser(){
        return "user";
    }

    @GetMapping(value = "opt/list")
    public String optList(){
        return "optList";
    }


    @GetMapping("/user")
    public Principal user(Principal user){
        System.out.println(".. 进入　获取用户信息　方法   ..........  ");
        return user;
    }
}
