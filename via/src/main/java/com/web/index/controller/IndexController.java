package com.web.index.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Url controller of NAVBAR
 */

@Controller
public class IndexController {

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

}
