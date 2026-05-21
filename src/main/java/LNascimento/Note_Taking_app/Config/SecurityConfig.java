package LNascimento.Note_Taking_app.Config;

import LNascimento.Note_Taking_app.Security.JwtFilter;
import LNascimento.Note_Taking_app.Services.CustomUserDetailsService;
import LNascimento.Note_Taking_app.Services.jwtService; // Importe o serviço
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final jwtService jwtService;
    private final JwtFilter jwtFilter;// Adicione esta dependência

    // Injete ambos no construtor
    public SecurityConfig(CustomUserDetailsService userDetailsService, jwtService jwtService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    // Remova o "JwtFilter filter" dos parâmetros
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Crie o filtro manualmente. Isso garante que ele exista APENAS dentro do Spring Security

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/notes/**").hasRole("USER") // Ajuste conforme o nome no banco
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Ajuste conforme o nome no banco
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}