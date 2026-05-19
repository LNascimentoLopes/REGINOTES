package LNascimento.Note_Taking_app.Models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table (name = "tags")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "tagColor")
    private String tagColor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToMany(mappedBy = "tags")
    private Set<Notes> notes = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tags tag = (Tags) o;
        // Só compara pelo ID! Se o ID for igual, é a mesma tag.
        return Id != null && Id.equals(tag.Id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
