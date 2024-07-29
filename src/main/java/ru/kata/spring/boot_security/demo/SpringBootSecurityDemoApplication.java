package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
		initApplicationDefault(context);
	}

	public static void initApplicationDefault(ConfigurableApplicationContext context) {
		UserService userService = context.getBean(UserServiceImp.class);
		RoleService roleService = context.getBean(RoleServiceImp.class);

		roleService.saveIfExists("ROLE_ADMIN");
		roleService.saveIfExists("ROLE_USER");

		User user = userService.getByEmail("admin@mail.ru");
		if (user == null) {
			List<Role> roleList = roleService.findAll();
			User newUser = new User(
					"Admin",
					"admin@mail.ru",
					"admin"
			);
			newUser.setRoleList(roleList);
			userService.save(newUser);

		}
		context.close();
	}
}
