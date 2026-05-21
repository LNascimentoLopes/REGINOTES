package LNascimento.Note_Taking_app.Services;


import LNascimento.Note_Taking_app.DTOs.AuthDTOs.*;
import LNascimento.Note_Taking_app.Models.RefreshToken;
import LNascimento.Note_Taking_app.Models.Users;
import LNascimento.Note_Taking_app.Repositories.RefreshTokenRepository;
import LNascimento.Note_Taking_app.Repositories.UserRepository;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Utils.Mapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final jwtService jwtService;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final Mapper mapper;
    private final RefreshTokenServices services;

    public AuthService(AuthenticationManager authManager, jwtService jwtService, PasswordEncoder encoder, UserRepository repository, Mapper mapper, RefreshTokenServices services) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.repository = repository;
        this.mapper = mapper;
        this.services = services;
    }

    public void register(registerRequest request){

        if (repository.findByEmail(request.email()).isPresent()){
            throw new EntityExistsException("User already exists");
        }
        Users users = mapper.RegisterDtoToEntity(request, encoder);
        repository.save(users);
    }

    public loginResponse login (String email, String password){

        Users user = repository.findByEmail(email).orElseThrow();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        CustomUserDetails usd = new CustomUserDetails(user);
        String token = jwtService.generateToken(usd);

        loginResponse response = new loginResponse(token,services.createRefreshToken(usd).getToken());
        return response;
    }


}
