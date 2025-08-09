package com.toysmarket.toysmarket.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String targetUrl,
                        Model model, HttpSession session, HttpServletRequest request) {
        session.setAttribute("targetUrl", targetUrl);
        model.addAttribute("targetUrl", targetUrl);
        return "login";
    }
}