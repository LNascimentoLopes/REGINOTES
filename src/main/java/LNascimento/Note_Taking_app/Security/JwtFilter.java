package LNascimento.Note_Taking_app.Security;

import LNascimento.Note_Taking_app.Services.CustomUserDetailsService;
import LNascimento.Note_Taking_app.Services.jwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final jwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;


    public JwtFilter(jwtService jwtService, CustomUserDetailsService userDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/auth/login") || path.equals("/auth/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
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
        }catch ( ExpiredJwtException ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
            return;
        }catch (JwtException ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);

            return;
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request,response,null,e);
            return;
        }

        filterChain.doFilter(request,response);
    }
}
