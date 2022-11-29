package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> usersList = userService.getList();
        model.addAttribute("allUsers", usersList);
        return "admin";
    }

    @GetMapping("/addNewUser")
    public String createForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        return "new";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            return "new";
        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "new";
        }
        if (userService.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "new";
        } else {
            userService.add(user);
            return "redirect:/admin";
        }
    }

    @GetMapping("/{id}")
    public String getUserById(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("user", userService.findById(id));
        return "user";
    }

    @GetMapping("/change/{id}")
    public String changeForm(Model model, @PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        return "change";
    }

    @PatchMapping("/{id}")
    public String changeUser(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.change(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
