package LNascimento.Note_Taking_app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table( name = "notes")
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contentMarkdown;

    @Column(columnDefinition = "TEXT")
    private String contentHtml;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    @ManyToMany
    @JoinTable(name = "note_tags", joinColumns = @JoinColumn(name = "noteId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))

    private Set<Tags> tags = new HashSet<>();


    @JsonProperty("userId")
    public UUID getUserIdForJson() {
       return users != null ? users.getId() : null;
    }

}
