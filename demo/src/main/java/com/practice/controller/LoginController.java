package com.practice.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
    @GetMapping("/user/login")
    public String login_page() {
        return "login";
    }
    
    @GetMapping("/user/login/email")
    public String loginByEmail_page() {
        return "email_login";
    }
    
    @GetMapping("/user/emailcode")
    public String getEmailCodePage() {
        return "get_email_code";
    }
    
    @PostMapping("/user/emailcode")
    public String getEmailCodePage(RedirectAttributes redirectAttributes, HttpSession session, @Param("username") String username) {
    	
    	String emailCode = UUID.randomUUID().toString();
    	Map<String, String> data = new HashMap<>();
    	data.put("emailCode", emailCode);
    	data.put("username", username);
    	session.setAttribute("emailCode", data);
    	
    	redirectAttributes.addFlashAttribute("emailCode", emailCode);
    	
    	
        return "redirect:/user/login/email";
    }
    
}
