package LNascimento.Note_Taking_app.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class TagDTOs {
    public record CreateTagRequest(

            @NotBlank(message = "Tag name must not be blank")
            String tagName,
            @NotBlank(message = "Tag name must not be blank")
            @Pattern(
                    regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                    message = "A cor deve ser um código hexadecimal válido (ex: #FFF ou #FFFFFF)"
            )
            String tagColor){}
    public record TagResponseDTO(
            UUID id,
            String tagName,
            String tagColor){}
}
