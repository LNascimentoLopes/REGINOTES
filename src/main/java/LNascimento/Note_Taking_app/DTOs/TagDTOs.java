package LNascimento.Note_Taking_app.DTOs;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class TagDTOs {
    public record CreateTagRequest(

            @NotBlank(message = "Tag name must not be blank")
            String tagName,
            @NotBlank(message = "Tag name must not be blank")
            String tagColor){}
    public record TagResponseDTO(
            UUID id,
            String tagName,
            String tagColor){}
}
