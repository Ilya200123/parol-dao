
package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositorey.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/users")
@Validated // Для валидации параметров в методах (например, @RequestParam)
public class UserRestControllerJPA {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

        @Autowired
        public UserRestControllerJPA(PasswordEncoder passwordEncoder, UserRepository userRepository) {
            this.passwordEncoder = passwordEncoder;
            this.userRepository = userRepository;
        }

        // ✅ Получить список всех пользователей
        @GetMapping
        public ResponseEntity<List<User>> getAllUsers() {
            try {
                List<User> users = userRepository.findAll(Sort.by("lastName").ascending());
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        // ✅ Получить пользователя по ID
        @GetMapping("/{id}")
        public ResponseEntity<User> getUser(@PathVariable Long id) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();

            }
        }

        // ✅ Создать нового пользователя
        @PostMapping
        public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }

    // ✅ Обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Обновляем username, если он был изменён
                    if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                        existingUser.setUsername(user.getUsername());
                    }

                    // Обновляем пароль только если он был изменён
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }

                    // Обновляем остальные поля
                    if (user.getLastName() != null) {
                        existingUser.setLastName(user.getLastName());
                    }

                    if (user.getAge() != null) {
                        existingUser.setAge(user.getAge());
                    }

                    // Обновляем роли (если они пришли в запросе)
                    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                        existingUser.setRoles(user.getRoles());
                    }

                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

        // ✅ Удалить пользователя
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/current")
        public ResponseEntity<User> getCurrentUser(Principal principal) {
            User user = userRepository.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        }
    }