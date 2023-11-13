package src.Models;

import src.Enum.TypeTransaction;
import src.Exception.AmountException;
import src.Main;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// tài khoản ATM (SavingsAccount)
public class SavingsAccount extends Account implements ReportService, Withdraw,ITransfer {
    private final double SAVINGS_ACCOUNT_MAX_WITHDRAW = 5000000d;
    private final double SAVINGS_ACCOUNT_MIN_WITHDRAW = 50000d;

    public SavingsAccount() {
    }

    public SavingsAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void withdraw(double amount,boolean status) {
        // trừ tiền người rút
        this.setBalance(this.getBalance() - amount);
        // tạo lịch sử giao dịch
        Transaction transactionWithdrawer = this.createTransaction(-amount, Instant.now(),status, TypeTransaction.WITHDRAW_MONEY);
        // cập nhật dữ liệu
        Account.update(this);
        logWithdraw(transactionWithdrawer);
    }

    @Override
    public void isAcceptedWithdraw(double amount) {
        if (amount < SAVINGS_ACCOUNT_MIN_WITHDRAW) {
            throw new AmountException("Giá trị giao dịch phải >= "+Main.formatMoney(SAVINGS_ACCOUNT_MIN_WITHDRAW));
        }

        if (amount > SAVINGS_ACCOUNT_MAX_WITHDRAW) {
            throw new AmountException("Giá trị giao dịch phải < "+Main.formatMoney(SAVINGS_ACCOUNT_MAX_WITHDRAW));
        }

        // số dư người gửi
        double senderBalace = this.getBalance() - amount;

        if (senderBalace < SAVINGS_ACCOUNT_MIN_WITHDRAW ){
            throw new AmountException("Số dư không đủ, giao dịch không thành công!");
        }
    }


    @Override
    public void transfer(Account receiveAccount, double amount,boolean status) {
        if (status){
            // trừ tiền người gửi
            this.setBalance(this.getBalance() - amount);
            // tạo lịch sử giao dịch
            Transaction transactionSender = this.createTransaction(-amount, Instant.now(),status, TypeTransaction.TRANSFER_MONEY);
            // cập nhật dữ liệu
            Account.update(this);
            // thêm tiền người nhận
            receiveAccount.setBalance(receiveAccount.getBalance()+amount);
            // tạo lịch sử giao dịch
            Transaction transactionreceive = receiveAccount.createTransaction(amount, Instant.now(),status,TypeTransaction.TRANSFER_MONEY);
            // cập nhật dữ liệu
            Account.update(receiveAccount);
            logTranfers(transactionSender,transactionreceive);
        }else {
            // tạo giao dịch lỗi
            this.createTransaction(0, Instant.now(),status, TypeTransaction.TRANSFER_MONEY);
            receiveAccount.createTransaction(0, Instant.now(),status,TypeTransaction.TRANSFER_MONEY);
        }

    }

    @Override
    public void isAcceptedTransfer(Account receiveAccount, double amount) {
        if (amount < SAVINGS_ACCOUNT_MIN_WITHDRAW) {
            throw new AmountException("Giá trị giao dịch phải >= "+Main.formatMoney(SAVINGS_ACCOUNT_MIN_WITHDRAW));
        }
        // số dư người gửi
        double senderBalace = this.getBalance() - amount;
        // số dư người nhận
        double receiverBalace = receiveAccount.getBalance() + amount;
        if (senderBalace < SAVINGS_ACCOUNT_MIN_WITHDRAW || receiverBalace < SAVINGS_ACCOUNT_MIN_WITHDRAW){
            throw new AmountException("Số dư không đủ, giao dịch không thành công!");
        }
    }

    @Override
    public void logTranfers(Transaction sender,Transaction receiver) {
        // Định dạng ngày giờ thành chuỗi
        // Chuyển đổi Instant sang LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(sender.getTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String time = dateTime.format(formatter);
        System.out.println("+--------------------+-----------------------+--------------------+");
        System.out.println("|                    BIEN LAI GIAO DICH SAVING                    |");
        System.out.printf("| NGÀY G/D : %52s |%n",  time);
        System.out.printf("| ATM ID   : %52s |%n", " DIGITAL-BANK-ATM-2022");
        System.out.printf("| SỐ TK    : %52s |%n", sender.getAccountNumber());
        System.out.printf("| SỐ NHẬN  : %52s |%n", receiver.getAccountNumber());
        System.out.printf("| SỐ TIỀN  : %52s |%n", Main.formatMoney(sender.getAmount()));
        System.out.printf("| PHÍ + VAT: %52s |%n", "0 đ");
        System.out.printf("| SO DU TK : %52s |%n", Main.formatMoney(sender.getBalance()));
        System.out.println("+--------------------+-----------------------+--------------------+");
        System.out.println("");
        System.out.println("");
    }

    @Override
    public void logWithdraw(Transaction withdrawer) {
        // Định dạng ngày giờ thành chuỗi
        // Chuyển đổi Instant sang LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(withdrawer.getTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String time = dateTime.format(formatter);
        System.out.println("+--------------------+-----------------------+--------------------+");
        System.out.println("|                    BIEN LAI GIAO DICH SAVING                    |");
        System.out.printf("| NGÀY G/D : %52s |%n",  time);
        System.out.printf("| ATM ID   : %52s |%n", " DIGITAL-BANK-ATM-2022");
        System.out.printf("| SỐ TK    : %52s |%n", withdrawer.getAccountNumber());
        System.out.printf("| SỐ TIỀN  : %52s |%n", Main.formatMoney(withdrawer.getAmount()));
        System.out.printf("| PHÍ + VAT: %52s |%n", "0 đ");
        System.out.printf("| SO DU TK : %52s |%n", Main.formatMoney(withdrawer.getBalance()));
        System.out.println("+--------------------+-----------------------+--------------------+");
        System.out.println("");
        System.out.println("");
    }
}
