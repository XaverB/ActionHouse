package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.orm.repository.ICustomerRepository;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

public class CustomerRepositoryStub implements ICustomerRepository {
    @Override
    public Customer getById(long id) {
        throw new NotYetImplementedException("Not yet implemented");
    }

    @Override
    public Customer save(Customer entity) {
        throw new NotYetImplementedException("Not yet implemented");
    }

    @Override
    public Customer update(Customer entity) {
        throw new NotYetImplementedException("Not yet implemented");
    }

    @Override
    public void delete(Customer entity) {
        throw new NotYetImplementedException("Not yet implemented");
    }

    @Override
    public List<Customer> getTopSellers(int count) {
        throw new NotYetImplementedException("Not yet implemented");
    }
}
