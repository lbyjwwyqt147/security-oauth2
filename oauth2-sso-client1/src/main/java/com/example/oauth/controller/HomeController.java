package com.example.oauth.controller;

import com.alibaba.fastjson.JSON;
import com.example.oauth.config.TokenContextHolder;
import com.example.oauth.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("api/test")
    public String test(){
        Map<String,String> map = new HashMap<>();
        map.put("code","0");
        map.put("msg","测试权限信息成功");
        System.out.println(JSON.toJSONString(map));
        return JSON.toJSONString(map);
    }

    @RequestMapping("/postMessages")
    public String postMessage(){
        String token = TokenContextHolder.getToken();
        String str = oAuth2RestTemplate.postForObject(serverUrl+"api/messages?access_token="+token,null,String.class);
        Map<String,String> map = new HashMap<>();
        map.put("msg",str);
        System.out.println(JSON.toJSONString(map));
        return JSON.toJSONString(map);
    }

    @GetMapping("api/user")
    public String user(){
        System.out.println(".. 进入　获取用户信息　方法   ..........  ");
        String token = TokenContextHolder.getToken();
        String str = oAuth2RestTemplate.getForObject(serverUrl+"api/user?access_token="+token,String.class);
        System.out.println(JSON.toJSONString(str));
        return JSON.toJSONString(str);
    }
}
