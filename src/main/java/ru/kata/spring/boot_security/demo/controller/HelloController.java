package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositorey.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class HelloController {

    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public HelloController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "show";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String deleteUser(Model model) {

        model.addAttribute("user", new User());

        List<Role> allRolesTU = roleService.getAllRoles();

        model.addAttribute("allRoles", allRolesTU);

        return "CreeteTu";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user) {

        userService.add(user);

        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam Long id, Model model) {

        model.addAttribute("user", userService.getUserById(id));

        model.addAttribute("allRoles", roleService.getAllRoles());

        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/details")
    public String detals(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "userDetails";
    }
}