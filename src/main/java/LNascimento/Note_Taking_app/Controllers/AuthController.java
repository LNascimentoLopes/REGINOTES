package LNascimento.Note_Taking_app.Controllers;

import LNascimento.Note_Taking_app.DTOs.AuthDTOs.registerRequest;
import LNascimento.Note_Taking_app.DTOs.AuthDTOs.loginRequest;
import LNascimento.Note_Taking_app.Services.AuthService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("register")
    public ResponseEntity Register (@RequestBody @Valid registerRequest request){
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }
    @PostMapping("login")
    public ResponseEntity Login(@RequestBody @Valid loginRequest request){
        String token = service.login(request.email(),request.password());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
