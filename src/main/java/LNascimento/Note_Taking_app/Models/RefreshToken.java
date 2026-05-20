package LNascimento.Note_Taking_app.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate; // Substitui o TIMESTAMP do banco

    @ManyToOne(fetch = FetchType.LAZY) // LAZY por performance, só carrega o user se precisarmos
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // Sua entidade User atual


}
