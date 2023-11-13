package src.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import src.Exception.AmountException;
import src.Main;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    private static SavingsAccount account1;
    private static SavingsAccount account2;
    @BeforeEach
    public void setUp() {
        account1 = new SavingsAccount("111111",10000000d);
        account2 = new SavingsAccount("111111",10000000d);
    }


    @ParameterizedTest
    @CsvSource({
            "100000 ,9900000,true"
            , "2000000,8000000, true"
    })
    void withdraw_test(double amount,double balance,boolean status) {
        account1.withdraw(amount,status);
        assertEquals(balance,account1.getBalance());
    }

    @ParameterizedTest
    @CsvSource({
            "10000000  , 100000 ,"
            , "10000000, 5000000, "
            , "10000000, 0,'Giá trị giao dịch phải >= 50,000 đ'"
            , "10000000, 10000000, 'Giá trị giao dịch phải < 5,000,000 đ'"
            , "10000000, 10000000, 'Giá trị giao dịch phải < 5,000,000 đ'"
            , "1000000, 2000000, 'Số dư không đủ, giao dịch không thành công!'"
    })
    void isAcceptedWithdraw_test(double balace,double amount,String expectedMessage) {
        SavingsAccount account = new SavingsAccount("",balace);
        if (expectedMessage!=null){
            AmountException exception = assertThrows(AmountException.class, () -> {
                // kiểm tra khi có ngoại lệ
                account.isAcceptedWithdraw(amount);
            });
            assertEquals(expectedMessage, exception.getMessage());
        }else {
            // kiểm tra khi không có ngoại lệ
            assertDoesNotThrow(() -> {
                account.isAcceptedWithdraw(amount);
            });
        }

    }

    @ParameterizedTest
    @CsvSource({
            "1000000 ,0,100000,900000,100000,true"
            ,"1000000 ,0,100000,1000000,0,false"
    })
    void transfer(double senderBalace,double receiveBalace,double amount,double expectedSenderBalace,double expectedReceiveBalace,boolean status) {
        SavingsAccount  senderAccount = new SavingsAccount("",senderBalace);
        SavingsAccount  receiveAccount = new SavingsAccount("",receiveBalace);

        senderAccount.transfer(receiveAccount,amount,status);
        // người gửi
        assertEquals(expectedSenderBalace,senderAccount.getBalance());
        // người nhận
        assertEquals(expectedReceiveBalace,receiveAccount.getBalance());
    }

    @ParameterizedTest
    @CsvSource({
            "10000000  , 100000 ,"
            , "10050000, 10000000, "
            , "10000000, 10000000, 'Số dư không đủ, giao dịch không thành công!'"
            , "10000000, 0,'Giá trị giao dịch phải >= 50,000 đ'"
            , "10000000, 20000000, 'Số dư không đủ, giao dịch không thành công!'"
    })
    void isAcceptedTransfer(double receiveBalace,double amount,String expectedMessage) {
        // gửi
        SavingsAccount senderAccount = new SavingsAccount("",receiveBalace);
        // nhận
        SavingsAccount receiveAccount = new SavingsAccount("",100000);

        if (expectedMessage!=null){
            AmountException exception = assertThrows(AmountException.class, () -> {
                // kiểm tra khi có ngoại lệ
                senderAccount.isAcceptedTransfer(receiveAccount,amount);
            });
            assertEquals(expectedMessage, exception.getMessage());
        }else {
            // kiểm tra khi không có ngoại lệ
            assertDoesNotThrow(() -> {
                senderAccount.isAcceptedTransfer(receiveAccount,amount);
            });
        }





    }
}