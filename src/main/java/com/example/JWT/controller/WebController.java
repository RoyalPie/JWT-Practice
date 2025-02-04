package com.example.JWT.controller;

import com.example.JWT.entity.Staff;
import com.example.JWT.service.StaffService;
import com.example.JWT.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class WebController {
//    @Autowired
//    private JWTUtil jwtUtil;

    @RequestMapping("/")
    public void notLoginRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        if (authResult == null || authResult instanceof AnonymousAuthenticationToken) {
            response.sendRedirect("/auth/login");
        } else {
            response.sendRedirect("/success");
        }
    }

    @RequestMapping("/success")
    public void loginSuccessRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult, HttpSession session) throws IOException {
        String role = authResult.getAuthorities().toString();

        String username = authResult.getName();
        session.setAttribute("username", username);

        if (role.contains("ADMIN")) {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/admin/home"));
        } else if (role.contains("USER")) {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/user/home"));
        }
    }

//    @GetMapping("/auth/login")
//    public String loginPage() {
//        return "login";
//    }
//    @PostMapping("/auth/login")
//    public String login(@RequestParam String username, @RequestParam String password){
//        if ("user".equals(username) && "password".equals(password)) {
//            return jwtUtil.generateToken(username);
//        }
//        else {
//            throw new RuntimeException("Invalid credentials");
//        }
//    }
    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "adminHome";
    }

    @GetMapping("/user/home")
    public String userHome(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "userHome";
    }
}

