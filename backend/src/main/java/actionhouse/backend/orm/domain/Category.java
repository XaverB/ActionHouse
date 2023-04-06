package actionhouse.backend.orm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    // we could use "name" as the id, but I prefer artificial ids
    @Column(unique = true)
    private String name;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    private Set<Article> articles = new HashSet<>();

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
