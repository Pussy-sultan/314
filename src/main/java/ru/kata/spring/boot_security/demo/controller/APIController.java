package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class APIController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public APIController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<User> readUserById(@PathVariable("id") int id) {
        User user = userService.getById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/roles", produces = "application/json")
    public ResponseEntity<Set<Role>> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByName(userDetails.getUsername());
        return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByName(userDetails.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<HttpStatus> create(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping(value = "/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
