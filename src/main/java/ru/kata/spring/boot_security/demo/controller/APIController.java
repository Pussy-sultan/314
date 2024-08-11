package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class APIController {

    private final UserService userService;

    @Autowired
    public APIController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    @ResponseBody
    public ResponseEntity<User> readUserById(@PathVariable("id") int id) {
        User user = userService.getById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/roles")
    @ResponseBody
    public ResponseEntity<Set<Role>> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByName(userDetails.getUsername());
        return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByName(userDetails.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseEntity<HttpStatus> create(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
