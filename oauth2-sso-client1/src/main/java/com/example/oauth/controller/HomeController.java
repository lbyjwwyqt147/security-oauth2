package com.example.oauth.controller;

import com.alibaba.fastjson.JSON;
import com.example.oauth.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
public class HomeController {

    @Value("${oauth2-server}")
    private String serverUrl;

    @Autowired
    IMessageService messageService;

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;


    @RequestMapping("/getMessages")
    public List<String> getMessages(){
        List<String> list = oAuth2RestTemplate.getForObject(serverUrl+"/api/messages",List.class);
        list.stream().forEach(item ->{
            System.out.println(item);
        });
        return list;
    }

    @RequestMapping("/postMessages")
    public String postMessage(){
        String s = "s";
        String str = oAuth2RestTemplate.postForObject(serverUrl+"api/messages",s,String.class);
        System.out.println(str);
        return str;
    }

    @GetMapping("api/user")
    public Principal user(Principal user){
        System.out.println(".. 进入　获取用户信息　方法   ..........  ");
        System.out.println(JSON.toJSONString(user));
        return user;
    }
}
