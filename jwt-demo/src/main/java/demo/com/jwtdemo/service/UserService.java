package demo.com.jwtdemo.service;

import demo.com.jwtdemo.models.AppUser;
import demo.com.jwtdemo.models.Role;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);

    Role saveRole(Role role);

    void addRoleToUser(String email, String roleName);

    AppUser getUser(String email);

    List<AppUser> getUsers();
}