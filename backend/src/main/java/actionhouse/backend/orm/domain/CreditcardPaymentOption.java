package actionhouse.backend.orm.domain;

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
}
