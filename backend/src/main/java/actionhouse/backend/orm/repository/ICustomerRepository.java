package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Customer;

import java.util.List;

public interface ICustomerRepository extends IBaseRepository<Customer> {
    List<Customer> getTopSellers(int count);
}
