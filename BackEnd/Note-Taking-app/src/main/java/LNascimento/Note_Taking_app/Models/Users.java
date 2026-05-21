package LNascimento.Note_Taking_app.Models;

import LNascimento.Note_Taking_app.Utils.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "app_user")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 75)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notes> notes = new ArrayList<>();

}
