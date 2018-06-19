package com.example.oauth.controller;

import com.example.oauth.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class HomeController {

    @Value("${oauth2-server}")
    private String serverUrl;

    @Autowired
    IMessageService messageService;

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    @RequestMapping("/")
    String home(Model model) {
        model.addAttribute("messages", messageService.getMessage());
        return "index";
    }

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

}
