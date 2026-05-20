package LNascimento.Note_Taking_app.Controllers;


import LNascimento.Note_Taking_app.DTOs.AuthDTOs.*;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import LNascimento.Note_Taking_app.Services.AuthService;
import LNascimento.Note_Taking_app.Services.CustomUserDetailsService;
import LNascimento.Note_Taking_app.Services.RefreshTokenServices;
import LNascimento.Note_Taking_app.Services.jwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService service;
    private final RefreshTokenServices refreshTokenServices;
    private final jwtService jwtService;

    public AuthController(AuthService service, RefreshTokenServices refreshTokenServices, jwtService jwtService) {
        this.service = service;
        this.refreshTokenServices = refreshTokenServices;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    public ResponseEntity Register (@RequestBody @Valid registerRequest request){
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }
    @PostMapping("login")
    public ResponseEntity<loginResponse> Login(@RequestBody @Valid loginRequest request){
        loginResponse response = service.login(request.email(),request.password());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("refresh")
    public ResponseEntity Refresh (@RequestBody @Valid refreshRequest request, @AuthenticationPrincipal CustomUserDetails user){

        loginResponse response = refreshTokenServices.generateNewTokens(request.refreshToken(), user.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @DeleteMapping("logout")
    public ResponseEntity Logout(@RequestBody @Valid logoutRequest request, @AuthenticationPrincipal CustomUserDetails user, HttpServletRequest servletRequest){

        refreshTokenServices.deleteRefreshToken(request.refreshToken());

        String authHeader = servletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);

            jwtService.invalidateToken(accessToken);
        }

        return ResponseEntity.ok().build();
    }

}
