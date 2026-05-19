package LNascimento.Note_Taking_app.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTOs {
    public record registerRequest(

            @NotBlank(message = "Name must not be blank")
            String username,
            @NotBlank(message = "Email must not be blank")
            @Email(message = "Insert a valid Email")
            String email,
            @NotBlank(message = "Password must not be blank")
            @Size(min = 6, message = "Password cannot be less than 6 characters")
            String password){}

    public record loginRequest(
            @Email(message = "Insert a valid Email")
            String email,
            @NotBlank(message = "Name can not be blank")
            @Size(min = 6, message = "Password cannot be less than 6 characters")
            String password){}
}
