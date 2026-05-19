package LNascimento.Note_Taking_app.Security;

import LNascimento.Note_Taking_app.Services.CustomUserDetailsService;
import LNascimento.Note_Taking_app.Services.jwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class JwtFilter extends OncePerRequestFilter {
    private final jwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(jwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/auth/login") || path.equals("/auth/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = header.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null){
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (jwtService.isValid(token, user)){
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("--- RAIO-X DO FILTRO ---");
            System.out.println("URL Tentada: " + request.getRequestURI());
            System.out.println("Usuário logado: " + auth.getName());
            System.out.println("Autoridades EXATAS: " + auth.getAuthorities());
            System.out.println("------------------------");
        }

        filterChain.doFilter(request,response);
    }
}
