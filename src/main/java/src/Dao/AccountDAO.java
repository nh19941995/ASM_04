package src.Dao;
import src.Models.Account;
import src.Service.Service;
import java.util.List;


public class AccountDAO implements DAO<Account>{
    private final static String FILE_PATH = "src/main/java/src/Stores/Accounts.dat";
    @Override
    public void save(List<Account> accountList) {
        Service.writeFile(FILE_PATH,accountList);
    }

    @Override
    public List<Account> List() {
        return Service.readFile(FILE_PATH);
    }

    public static AccountDAO getInstance(){
        return new AccountDAO();
    }

}
