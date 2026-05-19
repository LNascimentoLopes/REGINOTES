package LNascimento.Note_Taking_app.Repositories;

import LNascimento.Note_Taking_app.Models.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tags, UUID> {
    Optional<Tags> findByTagNameAndUserId(String tagName, UUID userId);

    Optional<Tags> findByIdAndUserId(UUID id, UUID userId);

    Optional<Page<Tags>> findAllByUserId(UUID userID, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM note_tags WHERE tag_id  = :tagId AND note_id = :noteId ",nativeQuery = true)
    void removeTagFromNote(@Param("id") UUID tagId, @Param("noteId") UUID noteId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM note_tags WHERE tag_id = :tagId)", nativeQuery = true)
    boolean isTagAssignedToAnyNote(@Param("tagId") UUID tagId);
}
