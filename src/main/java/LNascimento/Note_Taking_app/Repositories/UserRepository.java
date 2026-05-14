package LNascimento.Note_Taking_app.Repositories;

import LNascimento.Note_Taking_app.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID>{
    Users findByUsername(String username);
    Optional<Users> findByEmail(String email);
}
