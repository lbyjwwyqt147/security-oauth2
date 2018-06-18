package com.example.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
