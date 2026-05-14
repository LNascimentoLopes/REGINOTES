package LNascimento.Note_Taking_app.Repositories;
import LNascimento.Note_Taking_app.DTOs.NotesDTOs.*;
import LNascimento.Note_Taking_app.Models.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Notes, UUID> {

    @Query("SELECT n FROM Notes n LEFT JOIN FETCH n.tags WHERE n.users.id = :userId AND n.deletedAt IS NULL AND n.id = :id")
    Optional<Notes> findByIdAndUserId (@Param("id") UUID id, @Param("userId") UUID userId);

    @Query("SELECT n FROM Notes n LEFT JOIN FETCH n.tags WHERE n.users.id = :userId AND n.deletedAt IS NULL")
    Page<Notes> findByUserId (@ Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT n FROM Notes n WHERE n.users.id = :userId AND n.deletedAt IS NOT NULL")
    Page<Notes> findTrashedByUserId (@ Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT n FROM Notes n WHERE n.users.id = :userId AND n.deletedAt IS NOT NULL AND n.id = :noteId")
    Optional<Notes> findTrashedByIdAndUserId (@Param("noteId") UUID id, @Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM Notes n WHERE n.id = :id AND n.users.id = :UserId AND n.deletedAt IS NOT NULL")
    void permaDelete (@Param("id") UUID id, @Param("UserId") UUID userId);

    @Modifying
    @Query("UPDATE Notes n SET n.deletedAt = CURRENT_TIMESTAMP WHERE n.id = :id AND n.users.id = :UserId")
    void softDelete (@Param("id") UUID id, @Param("UserId") UUID userId);
}
