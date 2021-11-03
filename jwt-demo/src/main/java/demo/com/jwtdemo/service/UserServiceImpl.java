package demo.com.jwtdemo.service;

import demo.com.jwtdemo.models.AppUser;
import demo.com.jwtdemo.models.Role;
import demo.com.jwtdemo.repo.RoleRepo;
import demo.com.jwtdemo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
private final UserRepo userRepo;
private final RoleRepo roleRepo;
private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepo.findByEmail(email);
        if (user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found: {}", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(user.getName(), user.getPassword(), authorities);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        AppUser user = userRepo.findByEmail(email);
        Role role = roleRepo.findByName(roleName);
        log.info("Adding role {} to user {}", role.getName(), user.getName());
        user.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepo.findByEmail(email);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
}
