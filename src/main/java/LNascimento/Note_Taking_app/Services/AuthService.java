package LNascimento.Note_Taking_app.Services;


import LNascimento.Note_Taking_app.DTOs.AuthDTOs.registerRequest;
import LNascimento.Note_Taking_app.Models.Users;
import LNascimento.Note_Taking_app.Repositories.UserRepository;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Utils.Mapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final jwtService jwtService;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final Mapper mapper;

    public AuthService(AuthenticationManager authManager, jwtService jwtService, PasswordEncoder encoder, UserRepository repository, Mapper mapper) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.repository = repository;
        this.mapper = mapper;
    }

    public void register(registerRequest request){

        if (repository.findByEmail(request.email()).isPresent()){
            throw new EntityExistsException("User already exists");
        }
        Users users = mapper.RegisterDtoToEntity(request, encoder);
        repository.save(users);
    }

    public String login (String email, String password){

        Users user = repository.findByEmail(email).orElseThrow();

        authManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        UserDetails usd = new CustomUserDetails(user);
        return jwtService.generateToken(usd);
    }

}
