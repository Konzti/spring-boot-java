package demo.com.jwtdemo;

import demo.com.jwtdemo.models.AppUser;
import demo.com.jwtdemo.models.Role;
import demo.com.jwtdemo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class JwtDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtDemoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MEMBER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));

			userService.saveUser(new AppUser(null,"John", "john@email.com","password", new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Ash", "ash@email.com","password", new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Alex", "alex@email.com","password", new ArrayList<>()));

			userService.addRoleToUser("john@email.com", "ROLE_USER");
			userService.addRoleToUser("john@email.com", "ROLE_MEMBER");
			userService.addRoleToUser("john@email.com", "ROLE_ADMIN");
			userService.addRoleToUser("ash@email.com", "ROLE_USER");
			userService.addRoleToUser("alex@email.com", "ROLE_USER");
		};
	}

}
