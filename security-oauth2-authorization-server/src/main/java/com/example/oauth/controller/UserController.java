package com.example.oauth.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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




    @RequestMapping(path = "api/messages", method = RequestMethod.GET)
    List<String> getMessages(Principal principal) {
        List<String> list = new LinkedList<>();
        list.add("俏如来");
        list.add("帝如来");
        list.add("鬼如来");
        return list;
    }

    @RequestMapping(path = "api/messages", method = RequestMethod.POST)
    String postMessage(Principal principal) {

        return "POST -> 默苍离 ";
    }
}
