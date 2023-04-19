package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Category;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import org.dbunit.dataset.DataSetException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

public class CategoryRepositoryTest extends BaseRepositoryTest {

    private CategoryRepository categoryRepository;

    public CategoryRepositoryTest() throws Exception {
        onSetUp();
    }

    @BeforeAll
    public static void init()  {
        BaseRepositoryTest.init();
    }

    @AfterAll
    public static void cleanup() {
        JpaUtil.closeEntityManagerFactory();
    }

    @BeforeEach
    public void setUp() throws Exception {
        entityManager = JpaUtil.getTransactionalEntityManager();
        categoryRepository = new CategoryRepository(entityManager);
        refreshDatabase();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if(entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void getByIdWithValidIdReturnsCategory(int categoryId) throws MalformedURLException, DataSetException {
        var expected = getDataSetCategory(categoryId, "full.xml");
        var actual = categoryRepository.getById(categoryId);
        
        assertCategory(expected, actual);
    }

    @Test
    public void saveCategoryWithValidCategorySavesCategory() throws MalformedURLException, DataSetException {
        var expected = getDataSetCategory(5, "new_category.xml");

        categoryRepository.save(new Category(null, "New Category"));

        JpaUtil.commit(entityManager);
        entityManager = JpaUtil.getTransactionalEntityManager();
        categoryRepository = new CategoryRepository(entityManager);

        // table values for index starts at 50
        var actual = categoryRepository.getById(50);
        entityManager.getTransaction().commit();
    }

    @Test
    public void insertWithNewCategoryReturnsCategoryWithId() {
        var category = categoryRepository.save(new Category(null,"Antiquitäten " + LocalDateTime.now().toLocalTime().toString()));
        entityManager.getTransaction().commit();

        System.out.println("Inserted category: " + category.getId());
        Assert.assertTrue(category.getId() > 0);
    }

    @Test
    public void deleteWithValidCategoryDeletesCategory()
    {
        Category c = categoryRepository.getById(5L);
        Assert.assertNotNull(c);

        categoryRepository.delete(c);
        commit();

        entityManager = JpaUtil.getTransactionalEntityManager();
        categoryRepository = new CategoryRepository(entityManager);

        Category actual = categoryRepository.getById(5L);


        Assert.assertNull(actual);
    }

    @Test
    public void deleteWithInvalidCategoryThrowsForeignKeyConstraintException()
    {
        Category c = categoryRepository.getById(1L);
        Assert.assertNotNull(c);

        categoryRepository.delete(c);

        Assert.assertThrows(PersistenceException.class, () -> commit());
    }

    @Test
    public void updateWithValidCategoryUpdatesCategory() throws MalformedURLException, DataSetException {
        Category c = categoryRepository.getById(1L);
        c.setName("Updated Category");
        categoryRepository.update(c);
        commit();

        entityManager = JpaUtil.getTransactionalEntityManager();
        categoryRepository = new CategoryRepository(entityManager);

        var actual = categoryRepository.getById(1L);
        Assert.assertEquals("Updated Category", actual.getName());
    }

    private void assertCategory(Category expected, Category actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
    }

}