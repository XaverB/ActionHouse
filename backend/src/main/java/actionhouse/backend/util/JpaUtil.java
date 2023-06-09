package actionhouse.backend.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import actionhouse.backend.orm.jpa.EntityManagerTask;
import actionhouse.backend.orm.jpa.EntityManagerTaskWithResult;

public class JpaUtil {

    private static EntityManagerFactory emFactory;

    public static String PU_Name = "AuctionHousePU";

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emFactory == null)
            emFactory = Persistence.createEntityManagerFactory(PU_Name);
        return emFactory;
    }

    public static synchronized void closeEntityManagerFactory() {
        if (emFactory != null) {
            emFactory.close();
            emFactory = null;
        }
    }

    public static EntityManager getTransactionalEntityManager() {
        var em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    public static void commit(EntityManager em) {
        var tx = em.getTransaction();
        if (tx.isActive()) tx.commit();
    }

    public static void rollback(EntityManager em) {
        var tx = em.getTransaction();
        if (tx.isActive()) tx.rollback();
    }

    public static void executeTransactional(EntityManager entityManager, EntityManagerTask task) {
        try (var em = entityManager) {
            try {
                task.execute(em);
                JpaUtil.commit(em);
            } catch (Exception ex) {
                JpaUtil.rollback(em);
                throw ex;
            }
        }
    }

    public static <T> T executeWithResultTransactional(EntityManager entityManager, EntityManagerTaskWithResult<T> task) {
        try (var em = entityManager) {
            try {
                var result = task.execute(em);
                JpaUtil.commit(em);
                return result;
            } catch (Exception ex) {
                JpaUtil.rollback(em);
                throw ex;
            }
        }
    }
}
