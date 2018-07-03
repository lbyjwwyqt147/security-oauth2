package com.example.oauth.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sun.plugin.liveconnect.SecurityContextHelper;

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
        System.out.println(JSON.toJSONString(user));
        return user;
    }




    @RequestMapping(path = "api/messages", method = RequestMethod.GET)
    public List<String> getMessages(Principal principal) {
        List<String> list = new LinkedList<>();
        list.add("俏如来");
        list.add("帝如来");
        list.add("鬼如来");
        return list;
    }

    @RequestMapping(path = "api/messages", method = RequestMethod.POST)
   public String postMessage(Principal principal) {
        return "POST -> 默苍离 ";
    }

    /**
     * 当前登录人信息
     * @return
     */
    @RequestMapping(path = "api/user", method = RequestMethod.GET)
    public UserDetails currentlyLoginUser(){
         UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return  userDetails;
    }


}
