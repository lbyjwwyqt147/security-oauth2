package com.example.oauth.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {


    @RequestMapping(value = { "/", "/home" })
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "Hello friend!");
        return "home";
    }

    @RequestMapping(value = "/hello")
    public String adminPage(Model model) {
        return "hello";
    }


    @RequestMapping(value = "/loginPage")
    public String loginPage(Model model ) {
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login(Model model ) {
        return "login";
    }


}
