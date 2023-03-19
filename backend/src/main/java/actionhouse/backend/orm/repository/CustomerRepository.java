package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Customer;
import jakarta.persistence.EntityManager;

public class CustomerRepository extends BaseRepository<Customer> {
    public CustomerRepository(EntityManager entityManager) {
        super(Customer.class, entityManager);
    }
}
