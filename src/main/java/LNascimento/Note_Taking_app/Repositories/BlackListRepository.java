package LNascimento.Note_Taking_app.Repositories;

import LNascimento.Note_Taking_app.Models.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, UUID> {
    boolean existsByToken(String token);

    void deleteByExpiryDateBefore(Date date);
}
