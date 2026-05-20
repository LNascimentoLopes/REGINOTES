package LNascimento.Note_Taking_app.Repositories;

import LNascimento.Note_Taking_app.Models.RefreshToken;
import LNascimento.Note_Taking_app.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUser(Users user);

}
