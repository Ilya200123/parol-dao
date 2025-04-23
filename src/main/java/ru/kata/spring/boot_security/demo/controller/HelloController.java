package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositorey.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class HelloController {

    private final UserService userService;
    private final RoleService roleService;
    private UserRepository userRepository;

    @Autowired
    public HelloController(UserService userService, RoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listUsers(Authentication authentication, Model model) {

            UserDetails userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User userTu = userRepository.findByUsername(userDetails.getUsername());

            model.addAttribute("userTu", userTu);
            model.addAttribute("users", userService.listUsers());


        model.addAttribute("user", new User()); // Добавить это
        model.addAttribute("allRoles", roleService.getAllRoles()); // Добавить это
        return "show";
    }

    @GetMapping("/delete-confirm")
    public String confirmDelete(@RequestParam Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "fragments/delete-form :: deleteForm";
    }

    @PostMapping("/delete")
    public String deleteUserConfirmed(@RequestParam Long id) {
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
        return "fragments/edit-form :: editForm"; // Изменили возвращаемое значение
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/details")
    public String details(@RequestParam("id") Long id, Model model, Authentication authentication) {
        // Получаем текущего аутентифицированного пользователя
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        // Получаем пользователя, информацию о котором нужно показать
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
        model.addAttribute("userTu", currentUser);
        return "user-details";
    }
}