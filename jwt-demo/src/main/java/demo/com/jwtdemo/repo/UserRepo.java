package demo.com.jwtdemo.repo;

import demo.com.jwtdemo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByEmail (String email);
}
