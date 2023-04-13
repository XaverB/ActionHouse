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
    @TableGenerator(
            name = "Cat_Gen",
            initialValue = 50,
            allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Cat_Gen")
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

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Category other = (Category) obj;
        return id != null && id.equals(other.id);
    }
}
