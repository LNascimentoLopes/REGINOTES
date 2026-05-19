package LNascimento.Note_Taking_app.DTOs;

import java.util.UUID;

public class TagDTOs {
    public record CreateTagRequest(String tagName, String tagColor){}
    public record TagResponseDTO(UUID id, String tagName, String tagColor){}
}
