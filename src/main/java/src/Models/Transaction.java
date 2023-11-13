package src.Models;



import src.Dao.TransactionDAO;
import src.Enum.TypeTransaction;
import src.Main;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Transaction implements Serializable {
    static final long serialVersionUID = 1l;
    private String id;
    private String accountNumber;
    private double amount;

    private double fee;

    private double  balance;
    private Instant time;
    private boolean status;
    private TypeTransaction typeTransaction;

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Transaction(String id, String accountNumber, double amount, double fee, double balance, Instant time, boolean status, TypeTransaction typeTransaction) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.fee = fee;
        this.balance = balance;
        this.time = time;
        this.status = status;
        this.typeTransaction = typeTransaction;
    }

    public void display(){
        Instant time = this.time;

        // Chuyển đối tượng Instant sang múi giờ cụ thể (ví dụ: UTC)
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = time.atZone(zoneId);

        // Định dạng đối tượng ZonedDateTime thành chuỗi "yyyy/MM/dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = zonedDateTime.format(formatter);

        System.out.printf("[GD] %-10s| %14s | %-20s | %19s |\n",
        this.accountNumber,this.typeTransaction, Main.formatMoney(this.amount), formattedDateTime);
    }

    // tạo lịch sử giao dịch
    public static Transaction addTransactionRecord(double amount, String accountNumber, double balance, double fee, boolean status, TypeTransaction typeTransaction,Instant time){
        // lấy dữ liệu
        List<Transaction> transactionsData = TransactionDAO.getInstance().List();

        if (transactionsData==null){
            transactionsData = new ArrayList<>();
        }
        String id = String.valueOf(UUID.randomUUID());
        // dữ liệu mới
        Transaction transactionRecord = new Transaction(id,accountNumber,amount,fee,balance, time,status,typeTransaction);
        transactionsData.add(transactionRecord);
        // lưu file
        TransactionDAO.getInstance().save(transactionsData);
        Main.setTransactionList(transactionsData);
        return transactionRecord;
    }

}



