package com.example.JWT.controller;

import com.example.JWT.entity.Staff;
import com.example.JWT.service.StaffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class WebController {
    @Autowired
    private StaffService staffService;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "signup";
    }
    @PostMapping("/signup")
    public String handleSignUp(Staff staff, Model model) {
        if (staffService.findByUsername(staff.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "signup";
        }

        staffService.saveUser(staff);

        return "redirect:/login";
    }

    @RequestMapping("/")
    public void notLoginRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        if (authResult == null || authResult.getPrincipal() == null || authResult.getPrincipal().equals("anonymousUser")) {
            response.sendRedirect("/login");
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

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

