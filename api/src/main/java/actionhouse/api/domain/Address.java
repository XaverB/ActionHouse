package actionhouse.api.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@Getter @Setter
@ToString
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}
