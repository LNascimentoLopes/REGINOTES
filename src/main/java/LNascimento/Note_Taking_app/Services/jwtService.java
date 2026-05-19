package LNascimento.Note_Taking_app.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class jwtService {

    @Value("${api.security.token.secret}")
    private String SECRET;
    @Value("${api.security.token.expiration:7200000}")
    private String EXPIRATION;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Integer.parseInt(EXPIRATION)))
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
    }

    public String extractUsername(String token){
        return getBody(token).getSubject();
    }

    private boolean isTokenExpired(String token){
        Date exp = getBody(token).getExpiration();
        return exp.before(new Date());

    }
    public boolean isValid(String token, UserDetails details){
        String username = extractUsername(token);
        return username.equals(details.getUsername()) && !isTokenExpired(token);
    }

    private Claims getBody (String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

}
