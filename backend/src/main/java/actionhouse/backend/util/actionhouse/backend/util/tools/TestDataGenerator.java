package actionhouse.backend.util.actionhouse.backend.util.tools;

import actionhouse.backend.orm.domain.*;
import actionhouse.backend.orm.repository.CategoryRepository;
import actionhouse.backend.util.JpaUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

    public static void main(String[] args) {
        JpaUtil.getEntityManagerFactory();

        var em = JpaUtil.getTransactionalEntityManager();
        CategoryRepository categoryRepository = new CategoryRepository(em);

        var category = categoryRepository.save(new Category(null,"Antiquitäten"));
        em.getTransaction().commit();

        System.out.println("Inserted category: " + category.getId());


        JpaUtil.closeEntityManagerFactory();
    }
}
