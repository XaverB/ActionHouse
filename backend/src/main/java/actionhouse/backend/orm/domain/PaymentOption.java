package actionhouse.backend.orm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@NoArgsConstructor
@ToString @Getter @Setter
public class PaymentOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Customer customer;

    public PaymentOption(Long id, String owner, Customer customer) {
        this.id = id;
        this.owner = owner;
        this.customer = customer;
    }

    @Override
    public int hashCode() { return 42; }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        PaymentOption other = (PaymentOption) obj;
        return id != null && id.equals(other.id);
    }
}
