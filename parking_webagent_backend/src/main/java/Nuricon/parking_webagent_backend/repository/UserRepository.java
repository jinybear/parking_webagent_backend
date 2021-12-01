package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.util.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String id);
}
