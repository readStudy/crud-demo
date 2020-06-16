package com.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
    @GetMapping("/user/login")
    public String logout_page() {
        return "login";
    }
}
