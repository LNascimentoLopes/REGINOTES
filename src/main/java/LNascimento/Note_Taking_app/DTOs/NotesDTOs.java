package LNascimento.Note_Taking_app.DTOs;

import LNascimento.Note_Taking_app.Models.Tags;
import LNascimento.Note_Taking_app.Models.Users;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class NotesDTOs {
    public record NoteRequest(String title, String content) {}
    public record PatchNoteRequest(Optional<String> title, Optional<String> content){}
    public record getNotesResponse(
            String title,
            String contentMarkdown,
            String contentHtml,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            UUID id,
            Set<TagDTOs.TagResponseDTO> tags // Ou Set<TagDTO> tags
    ) {}
}
