package actionhouse.backend.orm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
public class Customer {

    @Id @GeneratedValue
    private Long Id;

    private String firstname;
    private String lastname;
    private String email;

    @AttributeOverride(name="street", column = @Column(name="ship_street"))
    @AttributeOverride(name="city", column = @Column(name="ship_city"))
    @AttributeOverride(name="state", column = @Column(name="ship_state"))
    @AttributeOverride(name="zipCode", column = @Column(name="ship_zipcode"))
    @AttributeOverride(name="country", column = @Column(name="ship_country"))
    @Embedded
    private Address shippingAddress;

    @AttributeOverride(name="street", column = @Column(name="pay_street"))
    @AttributeOverride(name="city", column = @Column(name="pay_city"))
    @AttributeOverride(name="state", column = @Column(name="pay_state"))
    @AttributeOverride(name="zipCode", column = @Column(name="pay_zipcode"))
    @AttributeOverride(name="country", column = @Column(name="pay_country"))
    @Embedded
    private Address paymentAddress;

    // cascading all operations is fine here, because we want to delete
    // payment options when we delete a customer and so on
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<PaymentOption> paymentOptions = new HashSet<>();

    // we do not want to delete articles if we delete a customer
    // therefore we are just cascading PERSIST and MERGE
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @ToString.Exclude
    private Set<Article> soldArticles = new HashSet<>();

    // we do not want to delete articles if we delete a customer
    // therefore we are just cascading PERSIST and MERGE
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @ToString.Exclude
    private Set<Article> boughtArticles = new HashSet<>();

    // I thought about not declaring this property, but all bids of a customer could be quite interesting
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bid> bids = new HashSet<>();
}
