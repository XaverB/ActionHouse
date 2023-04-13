package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.*;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.PersistenceException;
import org.dbunit.dataset.DataSetException;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public class CustomerRepositoryTest extends BaseRepositoryTest {
    CustomerRepository customerRepository;

    public CustomerRepositoryTest() throws Exception {
        onSetUp();
    }

    @BeforeAll
    public static void init() throws Exception {
        JpaUtil.getEntityManagerFactory();
    }

    @AfterAll
    public static void cleanup() {
        JpaUtil.closeEntityManagerFactory();
    }

    @BeforeEach
    public void setUp() throws Exception {
        entityManager = JpaUtil.getTransactionalEntityManager();
        customerRepository = new CustomerRepository(entityManager);
        refreshDatabase();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if(entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
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
    public void deleteWithInvalidCustomerThrowsConstraintViolationException()
    {
        Customer c = customerRepository.getById(1l);
        Assert.assertNotNull(c);

        customerRepository.delete(c);
        Customer actual = customerRepository.getById(1l);

        Assert.assertThrows(PersistenceException.class, () -> commit());
    }

    @Test
    public void deleteWithValidCustomerDeletesCustomer()
    {
        Customer c = customerRepository.getById(9l);
        Assert.assertNotNull(c);

        customerRepository.delete(c);
        Customer actual = customerRepository.getById(9l);
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

    @Test
    public void updateWithRemovedPaymentOptionsRemovesPaymentOptions()
    {
        Customer c = customerRepository.getById(1l);
        Assert.assertNotNull(c);
        Assert.assertTrue(c.getPaymentOptions().size() > 0);

        c.getPaymentOptions().clear();
        customerRepository.update(c);
        commit();

        entityManager = JpaUtil.getTransactionalEntityManager();
        customerRepository = new CustomerRepository(entityManager);
        Customer actual = customerRepository.getById(1l);

        Assert.assertTrue(actual.getPaymentOptions().size() == 0);
    }

    @Test
    public void updateWithAddedPaymentOptionsAddsPaymentOptions()
    {
        Customer c = customerRepository.getById(1l);
        Assert.assertNotNull(c);
        Assert.assertTrue(c.getPaymentOptions().size() == 2);

        c.getPaymentOptions().add(
                new BankPaymentOption(null, "newBank", c, "newBank", "newBank")
        );
        customerRepository.update(c);
        commit();

        entityManager = JpaUtil.getTransactionalEntityManager();
        customerRepository = new CustomerRepository(entityManager);
        Customer actual = customerRepository.getById(1l);

        PaymentOption newPaymentOption = actual.getPaymentOptions().stream()
                .filter(p -> p instanceof BankPaymentOption)
                .filter(p -> ((BankPaymentOption) p).getBankIdentifier().equals("newBank"))
                .findFirst()
                .orElse(null);

        Assert.assertTrue(actual.getPaymentOptions().size() == 3);
        Assert.assertNotNull(newPaymentOption);
        Assert.assertTrue(newPaymentOption.getId() != null);
    }

    @Test
    public void getTopSellersWithOneReturnsTopSeller() throws MalformedURLException, DataSetException {
        // Arrange
        var expected = getDataSetCustomer(1, "full.xml");

        // Act
        var actual = customerRepository.getTopSellers(1)
                .stream()
                .findFirst()
                .orElse(null);
        commit();

        // Assert
        Assert.assertEquals(expected, actual);
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
        assertArrayContainsInAnyOrder(expected.getPaymentOptions(), actual.getPaymentOptions());
        Assert.assertEquals(expected.getShippingAddress(), actual.getShippingAddress());
        Assert.assertEquals(expected.getPaymentAddress(), actual.getPaymentAddress());
    }

}