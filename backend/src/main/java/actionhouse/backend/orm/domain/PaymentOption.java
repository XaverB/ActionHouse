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
    @GeneratedValue
    private Long id;

    private String owner;

    // we do not want to delete customers when we delete a payment option,
    // but cascading PERSIST and MERGE is fine
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Customer customer;
}
