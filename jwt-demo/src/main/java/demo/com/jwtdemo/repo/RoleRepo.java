package demo.com.jwtdemo.repo;

import demo.com.jwtdemo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName (String name);
}
