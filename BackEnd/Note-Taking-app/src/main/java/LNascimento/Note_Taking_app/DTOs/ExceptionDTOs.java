package LNascimento.Note_Taking_app.DTOs;

import java.time.Instant;
import java.util.List;

public class ExceptionDTOs {
    public record StandardErrorDTO(
            Instant timestamp,
            Integer status,
            String error,
            String message,
            String path){}

    public record FieldMessageDTO(
            String field,
            String message) {}

    public record ValidationErrorDTO(
            Instant timestamp,
            Integer status,
            String error,
            String message,
            String path,
            List<FieldMessageDTO> errors // A lista mágica que o Front-end ama
    ) {}
}
