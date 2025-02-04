package com.example.JWT.controller;

import com.example.JWT.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password){
        if ("user".equals(username) && "password".equals(password)) {
            return jwtUtil.generateToken(username);
        }
        else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
