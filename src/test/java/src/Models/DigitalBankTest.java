package src.Models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import src.Dao.CustomerDAO;
import src.Exception.AccountNumberNotValidException;
import src.Exception.AmountException;
import src.Models.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static src.Models.DigitalBank.isCustomerExisted;

class DigitalBankTest {
    private static List<Customer> customerList;
    private static List<Account> accountListA;
    private static List<Account> accountListB;


    @BeforeAll
    public static void setUp() {
        accountListA = new ArrayList<>();
        Account account1 = new SavingsAccount("111111",10000d);
        account1.setCustomerId(new CCCD("037153000251"));
        Account account2 = new SavingsAccount("222222",20000d);
        account2.setCustomerId(new CCCD("037153000251"));
        Account account3 = new SavingsAccount("333333",20000d);
        account3.setCustomerId(new CCCD("037153000999"));

        accountListA.add(account1);
        accountListA.add(account2);
        accountListA.add(account3);


        // tạo dữ liệu
        customerList = new ArrayList<>();
        Customer customer1 = new Customer("a",new CCCD("037153000251"));
        Customer customer2 = new Customer("b",new CCCD("037153000252"));

        customerList.add(customer1);
        customerList.add(customer2);
    }

    @ParameterizedTest
    @CsvSource({
            "037153000251 ,true,"
            , "222222222, false,'Không tìm thấy khách hàng, thao tác không thành công!'"
            , "037153000252, true,"
    })
    void isCustomerExisted_test(String id, boolean expected,String expectedException) {
        try {
            Customer customer = DigitalBank.isCustomerExisted(id, customerList);
            boolean result = customerList.contains(customer);
            assertEquals(expected,result);
        } catch (Exception e) {
            assertEquals(expectedException,e.getMessage());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "111111 ,037153000251,true,"
            , "222222,037153000251, true,"
            , "333333,037153000251, false,'Số tài khoản thuộc một khách hàng khác !'"
            , "99999,037153000251, false,'Số tài khoản không tồn tại trên hệ thống !'"
    })
    void isAccountExisted_test(String number,String id, boolean expected,String expectedMessage) {
        // kiểm tra giá trị
        try {
            Account account = DigitalBank.isAccountExisted(number,id, accountListA,customerList);
            assertEquals(account.getAccountNumber(),number);
            assertEquals(account.getCustomerId().getId(),id);
            assertEquals(expected,customerList.contains(new Customer("",new CCCD(id))));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // kiểm tra ngoại lệ
        if (expectedMessage!=null){
            AccountNumberNotValidException exception = assertThrows(AccountNumberNotValidException.class, () -> {
                // kiểm tra khi có ngoại lệ
                DigitalBank.isAccountExisted(number,id, accountListA,customerList);
            });
            assertEquals(expectedMessage, exception.getMessage());
        }else {
            // kiểm tra khi không có ngoại lệ
            assertDoesNotThrow(() -> {
                DigitalBank.isAccountExisted(number,id, accountListA,customerList);
            });
        }
    }
}