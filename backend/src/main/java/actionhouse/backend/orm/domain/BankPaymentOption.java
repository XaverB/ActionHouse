package actionhouse.backend.orm.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BankPaymentOption extends PaymentOption {
    private String bankAccountNumber;
    private String bankIdentifier;
}
