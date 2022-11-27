package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping(params = "logout")
    public String logout() {
        return "redirect:/logout";
    }

    @GetMapping
    public String showHomePage() {
        return "index";
    }

    @PostMapping
    public String onlyGetMapping(Model model) {
        return "index";
    }
}
