package src.Dao;

import src.Models.Account;
import src.Models.Transaction;
import src.Service.Service;

import java.util.List;

public class TransactionDAO implements DAO<Transaction>{
    private final static String FILE_PATH = "src/main/java/src/Stores/Transactions.dat";
    public static TransactionDAO getInstance(){
        return new TransactionDAO();
    }

    @Override
    public void save(List<Transaction> transactionList) {
        Service.writeFile(FILE_PATH,transactionList);
    }

    @Override
    public List<Transaction> List() {
        return Service.readFile(FILE_PATH);
    }
}
