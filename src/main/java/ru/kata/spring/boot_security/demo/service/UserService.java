package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void save(User user);
    void deleteById(int id);
    User getById(int id);
    User getByEmail(String email);
    List<User> getAll();
}
