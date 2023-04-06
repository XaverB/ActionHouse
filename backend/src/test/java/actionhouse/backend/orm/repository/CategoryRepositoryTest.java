package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Category;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.RollbackException;
import org.dbunit.dataset.DataSetException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;

public class CategoryRepositoryTest extends BaseRepositoryTest {

    private CategoryRepository categoryRepository;

    public CategoryRepositoryTest() throws Exception {

        entityManager = JpaUtil.getTransactionalEntityManager();
        categoryRepository = new CategoryRepository(entityManager);
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
    @ValueSource(ints = {1, 2, 3, 4})
    public void getByIdWithValidIdReturnsCategory(int categoryId) throws MalformedURLException, DataSetException {
        var expected = getDataSetCategory(categoryId, "full.xml");
        var actual = categoryRepository.getById(categoryId);
        
        assertCategory(expected, actual);
    }

    @Test
    public void saveCategoryWithValidCategorySavesCategory() throws MalformedURLException, DataSetException {
        var expected = getDataSetCategory(6, "new_category.xml");

        categoryRepository.save(new Category(null, "New Category"));

        JpaUtil.commit(entityManager);
        entityManager = JpaUtil.getTransactionalEntityManager();
        categoryRepository = new CategoryRepository(entityManager);

        var actual = categoryRepository.getById(5);
        entityManager.getTransaction().commit();

        assertCategory(expected, actual);
    }

    @Test
    public void deleteWithValidCategoryDeletesCategory()
    {
        Category c = categoryRepository.getById(5L);
        Assert.assertNotNull(c);

        categoryRepository.delete(c);
        Category actual = categoryRepository.getById(5L);
        commit();

        Assert.assertNull(actual);
    }

    @Test
    public void deleteWithInvalidCategoryThrowsForeignKeyConstraintException()
    {
        Category c = categoryRepository.getById(1L);
        Assert.assertNotNull(c);

        categoryRepository.delete(c);
        Category actual = categoryRepository.getById(1L);
        Assert.assertThrows(RollbackException.class, () -> commit());
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