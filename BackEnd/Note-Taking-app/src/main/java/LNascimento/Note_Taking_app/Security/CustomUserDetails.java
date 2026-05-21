package LNascimento.Note_Taking_app.Security;

import LNascimento.Note_Taking_app.Models.Users;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Users user;

    public CustomUserDetails(Users user) {this.user = user;}

    public UUID getId(){
        return this.user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_"+user.getRole().name())
        );
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
