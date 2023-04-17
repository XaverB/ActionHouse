package actionhouse.app.bl;

import actionhouse.backend.orm.domain.Address;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static actionhouse.app.util.MenuHelper.promptFor;

public class CustomerBl {

    /**
     * Reads a customer from the stdin, creates a new customer and saves it to the database.
     */
    public void createCustomer() {

        // read customer from stdin
        var customer = readCustomerFromStdin();

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                CustomerRepository customerRepository = new CustomerRepository(em);
                customer = customerRepository.save(customer);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🆕 Created customer: " + customer.getId());
        }catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    public void updateCustomer() {
        // read customer from stdin
        var customerId = readCustomerIdFromStdin();
        var customer = readCustomerFromStdin();
        customer.setId(customerId);

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                CustomerRepository customerRepository = new CustomerRepository(em);
                customer = customerRepository.update(customer);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🆕 Updated customer: " + customer.getId());
        }catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    public void deleteCustomer() {
        // read customer from stdin
        var customerId  = readCustomerIdFromStdin();

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try
            {
                CustomerRepository customerRepository = new CustomerRepository(em);
                Customer customer = customerRepository.getById(customerId);
                if(customer == null) {
                    System.out.println("Customer not found");
                    return;
                }

                customerRepository.delete(customer);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🆕 Deleted customer: " + customerId);
        }catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    public void showCustomer() {
        var customerId  = readCustomerIdFromStdin();

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try
            {
                CustomerRepository customerRepository = new CustomerRepository(em);
                Customer customer = customerRepository.getById(customerId);
                if(customer == null) {
                    System.out.println("Customer not found");
                    return;
                }

                PrintCustomer(customer);
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    public void showCustomers() {
        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try
            {
                CustomerRepository customerRepository = new CustomerRepository(em);
                var customers = customerRepository.getAll();
                for (Customer customer : customers) {
                    PrintCustomer(customer);
                }
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    private static void PrintCustomer(Customer customer) {
        System.out.println("🆕 Customer: " + customer.getId());
        System.out.println("Firstname: " + customer.getFirstname());
        System.out.println("Lastname: " + customer.getLastname());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Shipping address: " + customer.getShippingAddress());
        System.out.println("Billing address: " + customer.getPaymentAddress());
    }

    private Customer readCustomerFromStdin() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("🚢 Enter customer shipping address:");
        var shippingAddress = new Address(
                promptFor(in, "Street"),
                promptFor(in, "City"),
                promptFor(in, "State"),
                promptFor(in, "Zip"),
                promptFor(in, "Country")
        );

        System.out.println("💵 Enter customer billing address:");
        var billingAddress = new Address(
                promptFor(in, "Street"),
                promptFor(in, "City"),
                promptFor(in, "State"),
                promptFor(in, "Zip"),
                promptFor(in, "Country")
        );

        System.out.println("🦲 Enter customer data:");
        var customer = new Customer(null,
                promptFor(in, "Firstname"),
                promptFor(in, "Lastname"),
                promptFor(in, "Email"),
                shippingAddress,
                billingAddress
        );
        // read customer from stdin
        return customer;
    }

    private Long readCustomerIdFromStdin() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("🦲 Enter customer id:");
        // read customer from stdin
        return Long.parseLong(promptFor(in, "Id"));
    }
}
