package actionhouse.backend.tools;

import actionhouse.backend.orm.domain.Category;
import actionhouse.backend.orm.repository.CategoryRepository;
import actionhouse.backend.util.JpaUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TestInsert {

    @Test
    public void TestInsert() {
        JpaUtil.getEntityManagerFactory();

        var em = JpaUtil.getTransactionalEntityManager();
        CategoryRepository categoryRepository = new CategoryRepository(em);

        var category = categoryRepository.save(new Category(null,"Antiquitäten " + LocalDateTime.now().toLocalTime().toString()));
        em.getTransaction().commit();

        System.out.println("Inserted category: " + category.getId());


        JpaUtil.closeEntityManagerFactory();
    }

}
