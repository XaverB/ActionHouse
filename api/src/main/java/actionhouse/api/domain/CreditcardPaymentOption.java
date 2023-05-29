package actionhouse.api.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreditcardPaymentOption extends PaymentOption {
    private String creditCardNumber;
    private LocalDate creditCardValidTo;
    private String cardVerificationValue;


    public CreditcardPaymentOption(Long Id, String owner, Customer customer, String creditCardNumber, LocalDate creditCardValidTo, String cardVerificationValue) {
        super(Id, owner, customer);
        this.creditCardNumber = creditCardNumber;
        this.creditCardValidTo = creditCardValidTo;
        this.cardVerificationValue = cardVerificationValue;

    }
}
