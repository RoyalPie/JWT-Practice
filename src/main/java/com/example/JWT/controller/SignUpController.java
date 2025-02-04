package com.example.JWT.controller;

import com.example.JWT.entity.Staff;
import com.example.JWT.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class SignUpController {
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
}
