package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getWithoutAdmin() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().stream()
                        .filter(role -> role.getName().equals("ROLE_ADMIN"))
                        .toList().isEmpty()
                )
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String getRoleListByUser(User user) {
        return user.getRoles().stream().reduce(
                "", (partialAgeResult, item) -> partialAgeResult + ((partialAgeResult.isEmpty() ? "" : " ") + item.getName()), String::concat
        );
    }

    @Transactional(readOnly = true)
    @Override
    public User getByName(String email) {
        return userRepository.findByName(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found exception");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
