import actionhouse.backend.util.JpaUtil;
import org.junit.Test;

public class justsumtest {

    @Test
    public void hi() {
        JpaUtil.getEntityManagerFactory();

        try(var em = JpaUtil.getTransactionalEntityManager())
        {
            System.out.println("hi");
        }

        JpaUtil.closeEntityManagerFactory();
    }
}
