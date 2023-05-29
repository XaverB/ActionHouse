package actionhouse.api.domain;

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

    public BankPaymentOption(Long Id, String owner, Customer customer, String bankAccountNumber, String bankIdentifier) {
        super(Id, owner, customer);
        this.bankAccountNumber = bankAccountNumber;
        this.bankIdentifier = bankIdentifier;
    }
}
