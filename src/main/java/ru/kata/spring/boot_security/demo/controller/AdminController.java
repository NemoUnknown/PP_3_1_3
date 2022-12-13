package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/panel")
    public String showAllUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("allUsers", userService.getList());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "admin";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") User user) {
        user.setRoles(roleService.getCurrentRoles(user));
        userService.add(user);
        return "redirect:/admin/panel";
    }

    @GetMapping("/panel/{id}/change")
    public String changeForm(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("editUser", userService.findById(id));
        return "admin";
    }

    @PatchMapping("/panel/{id}")
    public String changeUser(@ModelAttribute("editUser") User user, @PathVariable("id") Long id) {
        user.setRoles(roleService.getCurrentRoles(user));
        userService.change(id, user);
        return "redirect:/admin/panel";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/panel";
    }

}
