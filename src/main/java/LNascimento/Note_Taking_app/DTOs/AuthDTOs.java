package LNascimento.Note_Taking_app.DTOs;

public class AuthDTOs {
    public record registerRequest(String username, String email, String password){}
    public record loginRequest(String email,String password){}
}
