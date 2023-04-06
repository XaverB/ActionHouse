package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Address;
import actionhouse.backend.orm.domain.BankPaymentOption;
import actionhouse.backend.orm.domain.CreditcardPaymentOption;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.util.JpaUtil;
import org.dbunit.dataset.DataSetException;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.time.LocalDate;


public class CustomerRepositoryTest extends BaseRepositoryTest {
    CustomerRepository customerRepository;

    public CustomerRepositoryTest() throws Exception {

        entityManager = JpaUtil.getTransactionalEntityManager();
        customerRepository = new CustomerRepository(entityManager);
        onSetUp();
    }

    @BeforeClass
    public static void init() throws Exception {
        JpaUtil.getEntityManagerFactory();
    }

    @AfterClass
    public static void cleanup() {
        JpaUtil.closeEntityManagerFactory();
    }

    @BeforeEach
    public void setUp() throws Exception {
        refreshDatabase();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void getByIdWithValidIdReturnsCustomer(int customerId) throws MalformedURLException, DataSetException {
        // Arrange
        var expected = getDataSetCustomer(customerId, "full.xml");

        // Act
        var actual = customerRepository.getById(customerId);
        commit();

        // Assert
        assertCustomer(expected, actual);
    }

    @Test
    public void saveWithValidCustomerWithPaymentOptionsGeneratesIds() throws MalformedURLException, DataSetException {
        // Arrange
        Customer c = createDummyCustomer();
        Customer expected = getDataSetCustomer(4, "new_customer.xml");

        // Act
        var actual = customerRepository.save(c);
        commit();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteWithValidCustomerDeletesCustomer()
    {
        Customer c = customerRepository.getById(1l);
        Assert.assertNotNull(c);

        customerRepository.delete(c);
        Customer actual = customerRepository.getById(1l);
        commit();

        Assert.assertNull(actual);
    }

    @Test
    public void updateWithValidCustomerUpdatesCustomer()
    {
        Customer c = customerRepository.getById(1l);
        Assert.assertNotNull(c);

        c.setFirstname("newFirstName");
        customerRepository.update(c);
        commit();

        entityManager = JpaUtil.getTransactionalEntityManager();
        customerRepository = new CustomerRepository(entityManager);
        Customer actual = customerRepository.getById(1l);

        Assert.assertEquals("newFirstName", actual.getFirstname());
    }

    private static Customer createDummyCustomer() {
        var billingAddress = new Address("a", "a", "a", "a", "a");
        var shippingAddress = new Address("b", "b", "b", "b", "b");

        var c = new Customer(null, "a", "a", "a", shippingAddress, billingAddress);

        CreditcardPaymentOption creditCardPayment = new CreditcardPaymentOption(null, "a", c,"a", LocalDate.of(2023, 1, 1), "a");
        BankPaymentOption bankPayment = new BankPaymentOption(null, "b", c, "b", "b");

        c.addPaymentOption(creditCardPayment);
        c.addPaymentOption(bankPayment);
        return c;
    }

    private static void assertCustomer(Customer expected, Customer actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.getPaymentOptions().size(), actual.getPaymentOptions().size());
        Assert.assertTrue(expected.getPaymentOptions().containsAll(actual.getPaymentOptions()));
        Assert.assertTrue(actual.getPaymentOptions().containsAll(expected.getPaymentOptions()));
        Assert.assertEquals(expected.getShippingAddress(), actual.getShippingAddress());
        Assert.assertEquals(expected.getPaymentAddress(), actual.getPaymentAddress());
    }
}