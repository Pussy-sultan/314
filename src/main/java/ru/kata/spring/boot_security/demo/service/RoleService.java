package ru.kata.spring.boot_security.demo.service;

import java.util.Set;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleService {
    Set<Role> findAll();
    Role findByName(String name);
    void saveIfExists(String name);
}
