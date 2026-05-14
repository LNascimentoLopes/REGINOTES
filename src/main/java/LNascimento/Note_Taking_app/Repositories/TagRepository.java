package LNascimento.Note_Taking_app.Repositories;

import LNascimento.Note_Taking_app.Models.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tags, UUID> {
    Optional<Tags> findByTagNameAndUserId(String tagName, UUID userId);
}
