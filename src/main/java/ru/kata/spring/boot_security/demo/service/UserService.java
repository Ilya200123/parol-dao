package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.domain.Sort;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll(Sort sort);
    Optional<User> findById(Long id);
    User findByUsername(String username);
    User save(User user);
    User update(Long id, User updatedUser);
    void deleteById(Long id);
}