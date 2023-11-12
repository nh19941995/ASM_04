package src.Dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import src.Main;
import src.Models.Account;
import src.Models.Bank;
import src.Models.CCCD;
import src.Models.Customer;
import src.Service.InstantAdapter;
import src.Service.RegexCheck;
import src.Service.Service;
import java.io.*;
import java.time.Instant;
import java.util.*;

public class CustomerDAO implements DAO<Customer>{
    private final static String FILE_PATH = "src/main/java/src/Stores/Customers.dat";
    public static CustomerDAO getInstance(){
        return new CustomerDAO();
    }
    @Override
    public void save(List<Customer> list) {
        Service.writeFile(FILE_PATH,list);
    }

    @Override
    public List<Customer> List() {
        // đọc file Accounts.dat
        List<Account> accountList_6 = AccountDAO.getInstance().List();

        // đọc file nhị phân Customers.dat
        List<Customer> customerListDat_6 = Service.readFile(FILE_PATH);

        // map các listAccount có key là id
        Map<String, List<Account>> customerMap = new HashMap<>();
        for (Account account: accountList_6) {
            if (customerMap.containsKey(account.getCustomerId().getId())){
                List<Account> accountList = customerMap.get(account.getCustomerId().getId());
                accountList.add(account);
                customerMap.replace(account.getCustomerId().getId(),accountList);
            }else {
                List<Account> accountList = new ArrayList<>();
                accountList.add(account);
                customerMap.putIfAbsent(account.getCustomerId().getId(),accountList);
            }
        }
        for (Customer customer: customerListDat_6) {
            for (String s: customerMap.keySet()) {
                if (customer.getCustomerId().getId().equals(s)){
                    customer.setAccounts(customerMap.get(s));
                }
            }
        }
        return customerListDat_6;
    }

    public static List<Customer> readTxt(String urlCustomersTxt)  throws FileNotFoundException{
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();

        // Đọc file txt
        List<Customer> customerList = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(urlCustomersTxt))) {
            String jsonContent = reader.readLine();

            TypeToken<List<Customer>> typeToken = new TypeToken<List<Customer>>() {};

            customerList = gson.fromJson(jsonContent, typeToken.getType());
        } catch (FileNotFoundException e) {
            // Ném ra ngoại lệ mới với thông báo
            throw new FileNotFoundException("Không tìm thấy tệp tin!");
        } catch (IOException e) {
            // Xử lý ngoại lệ IOException nếu có lỗi khác
            e.printStackTrace();
        }

        return customerList;
    }


    // hàm tạo file Customer.txt
    public static void creatCustomerTxt( String urlCustomersTxt,Scanner scanner){
        List<Customer> customerList = new ArrayList<>();
        String flag;

        do {
            String name = RegexCheck.getInput("Nhập vào tên khách hàng: ","Tên chỉ bao gồm chữ cái và khoảng trắng, vui lòng nhập lại!",RegexCheck.nameRegex,scanner);
            String id = RegexCheck.getInput("Nhập vào id khách hàng: ","Id chỉ bao gồm 12 số, vui lòng nhập lại !",RegexCheck.CCCDNumber,scanner);
            Customer customer = new Customer(name,new CCCD(id));
            customerList.add(customer);
            System.out.print("Nhập khách hàng khác y/n: ");
            flag = scanner.nextLine();
        }while (flag.equals("y"));

        // Tạo đối tượng Gson
        // Sử dụng Gson để chuyển đối tượng thành chuỗi JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
        // Deserialize JSON thành đối tượng MyAccount
        String json = gson.toJson(customerList);
        System.out.println(json);
        // Lưu vào file
        try {
            File file = new File(urlCustomersTxt);
            if (!file.exists()) {
                file.createNewFile();
            }
            // Sử dụng BufferedWriter để ghi dữ liệu vào tệp
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                // Đối số thứ hai của FileWriter là true để cho phép ghi thêm vào tệp (append)
                String content = json;
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
