package src.Models;

import src.Dao.AccountDAO;
import src.Dao.TransactionDAO;
import src.Enum.TypeTransaction;
import src.Main;
import src.Service.Task;
import src.Service.Task_2;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public abstract class Account implements Serializable {
    static final long serialVersionUID = 1l;
    private String accountNumber;  // số tài khoản
    private double balance;  // số dư

    private CCCD customerId;

    private List<Transaction> transactions; // lịch sử giao dịch

    public List<Transaction> getTransactionList() {
        // Lấy dữ liệu toàn bộ
        List<Transaction> allTransactionList = Main.getTransactionData();

        // Kiểm tra null cho allTransactionList
        if (allTransactionList == null) {
            return null;
        }

        // Kiểm tra null cho accountNumber
        if (Objects.isNull(this.accountNumber)) {
            return null;
        }

        // Lọc danh sách các giao dịch liên quan
        List<Transaction> transactionList = allTransactionList.stream()
                .filter(s -> Objects.equals(s.getAccountNumber(), this.accountNumber))
                .collect(Collectors.toList());
        return transactionList;
    }

    public Account() {
        this.balance=0;
    }

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public CCCD getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CCCD customerId) {
        this.customerId = customerId;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactions = transactionList;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isPremiumAccount() {
        return (this.balance >= 10000000);
    }

    @Override
    public String toString() {
        // Tạo một đối tượng DecimalFormat với định dạng thường
        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        // Sử dụng đối tượng DecimalFormat để định dạng giá trị double
        String balance = decimalFormat.format(getBalance());
        String AccountNumber = this.getAccountNumber();
        String type = "SAVINGS";
        return String.format(": %-8s| %-7s  %40s |",  AccountNumber,type,balance);
    }


    public Transaction createTransaction(double amount, Instant time, boolean status, TypeTransaction type){
        Transaction transaction = Transaction.addTransactionRecord(amount,this.getAccountNumber(),this.getBalance(),0,status, type,time);
        transaction.display();
        return transaction;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)  return true;

        if (obj == null || this.getClass() != obj.getClass()){
            return false;
        }
        Account account = (Account) obj;

        if (this.getAccountNumber().equals(account.getAccountNumber()) ){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountNumber());
    }

    public static void update(Account account) {
        // cập nhật dữ liệu vào file
        List<Account> accountList = Main.getAccountData();
        int numOfThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numOfThread);
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 1; i <= numOfThread; i++) {
                Runnable task = new Task(i, numOfThread, account, accountList);
                futures.add(executor.submit(task));
            }

            // Đợi cho tất cả các tác vụ hoàn thành
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
        } finally {
            executor.shutdown();
            AccountDAO.getInstance().save(accountList);
        }
    }





}
