package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleService roleService, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = user.getRoles();
        Set<Role> rolesEmpty = new HashSet<>();
        for (Role role : roles) {
            rolesEmpty.add(roleService.findByName(role.getName()));
        }
        user.setRoles(rolesEmpty);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void updateUser(User updateUser) {
        User user = userRepository.findById(updateUser.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String currentPassword = user.getPassword();
        String newPassword = updateUser.getPassword();
        if (!currentPassword.equals(newPassword)) {
            updateUser.setPassword((bCryptPasswordEncoder.encode(updateUser.getPassword())));
        }
        userRepository.save(updateUser);
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
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String getRoleListByUser(User user) {
        return user.getRoles()
                .stream()
                .reduce(
                "", (partialAgeResult, item) -> partialAgeResult + ((partialAgeResult.isEmpty() ? "" : " ") + item.getName()), String::concat
        );
    }

    @Override
    public boolean isAdmin(User user) {
        boolean isAdmin = false;
        for (Role role :user.getRoles()) {
            if (role.getName().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        return isAdmin;
    }

    @Transactional(readOnly = true)
    @Override
    public User getByName(String email) {
        return userRepository.findByName(email);
    }



    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
