package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.PaymentOption;
import jakarta.persistence.EntityManager;

public class PaymentRepository extends BaseRepository<PaymentOption> {
    public PaymentRepository(EntityManager entityManager) {
        super(PaymentOption.class, entityManager);
    }
}
