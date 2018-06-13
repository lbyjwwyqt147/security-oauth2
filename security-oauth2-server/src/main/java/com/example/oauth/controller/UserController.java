package com.example.oauth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(value="/user", method = RequestMethod.GET)
    public String listUser(){
        return "user";
    }
}
