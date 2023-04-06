package actionhouse.backend.util.actionhouse.backend.util.tools;

import actionhouse.backend.orm.domain.Address;
import actionhouse.backend.orm.domain.BankPaymentOption;
import actionhouse.backend.orm.domain.CreditcardPaymentOption;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.util.JpaUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

    public static void main(String[] args) {
        JpaUtil.getEntityManagerFactory();



        JpaUtil.closeEntityManagerFactory();
    }
}
