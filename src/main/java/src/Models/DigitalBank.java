package src.Models;


import src.Dao.AccountDAO;
import src.Dao.CustomerDAO;
import src.Dao.TransactionDAO;
import src.Enum.TypeTransaction;
import src.Exception.AccountNumberNotValidException;
import src.Exception.AmountException;
import src.Exception.CustomerIdNotValidException;
import src.Main;
import src.Service.RegexCheck;
import src.Service.Service;

import java.time.Instant;
import java.util.*;

// quản lý ngân hàng tiềm năng
public class DigitalBank extends Bank {
    public DigitalBank() {
        this.setId(String.valueOf(UUID.randomUUID()));
    }

    public static void showCustomers(){
        // lấy dữ liệu
        List<Customer> customerList_1 = Main.getCustomerData();
        List<Account> accountList_1 = Main.getAccountData();
        List<Customer> result  = Service.joinAccountToCustomer(customerList_1,accountList_1);
        if (result.size()==0){
            System.out.println("Chưa có khách hàng nào trong danh sách!");
        }else {
            result.stream().forEach(s->s.displayInformation());
        }
    }

    public static Customer isCustomerExisted(String ID,List<Customer> customerList)throws CustomerIdNotValidException{

        // Tìm một khách hàng bất kỳ có ID bằng với ID được truyền vào
        Optional<Customer> optionalCustomer = customerList.stream().filter(s->s.getCustomerId().getId().equals(ID)).findAny();

        // Trả về khách hàng đó, nếu không tìm thấy thì trả về null
        Customer customer = optionalCustomer.orElseGet(()->null);
        if (customer==null){
            throw new CustomerIdNotValidException("Không tìm thấy khách hàng, thao tác không thành công!");
        }
        return customer;
    }

    public static Account isAccountExisted(String accountNumber) throws AccountNumberNotValidException {
        List<Account> accountList = Main.getAccountData();
        // Tìm một khách hàng bất kỳ có ID bằng với ID được truyền vào
        Optional<Account> accountOptional = accountList.stream().filter(s->s.getAccountNumber().equals(accountNumber)).findAny();
        // Trả về khách hàng đó, nếu không tìm thấy thì trả về null
        Account account = accountOptional.orElseGet(()->null);
        if (!accountOptional.isPresent()){
            throw new AccountNumberNotValidException("Khách hàng không tồn tại trên hệ thống !");
        }
        return account;
    }

    public static Account isAccountExisted(String accountNumber, String id,List<Account> accountList,List<Customer> customerList) throws AccountNumberNotValidException {
        // Tìm một khách hàng bất kỳ có ID bằng với ID được truyền vào
        Optional<Account> accountOptional = accountList.stream().filter(s->s.getAccountNumber().equals(accountNumber)).findAny();
        Optional<Customer> optionalCustomer = customerList.stream().filter(s->s.getCustomerId().getId().equals(id)).findAny();
        if (!accountOptional.isPresent()){
            throw new AccountNumberNotValidException("Số tài khoản không tồn tại trên hệ thống !");
        }
        if (!optionalCustomer.isPresent()){
            throw new AccountNumberNotValidException("Khách hàng không tồn tại trên hệ thống !");
        }
        Account account = accountOptional.get();
        Customer customer = optionalCustomer.get();
        if (account.getCustomerId().getId().equals(customer.getCustomerId().getId())){
            return account;
        }else {
            throw new AccountNumberNotValidException("Số tài khoản thuộc một khách hàng khác !");
        }
    }


    public static void addCustomers(String fileName) {
        List<Exception> exceptions = new ArrayList<>();
        // đọc file Customers.txt
        List<Customer> customerListTxt = new ArrayList<>();
        try {
            customerListTxt = CustomerDAO.readTxt(fileName);
        } catch (Exception e) {
            // Bắt ngoại lệ chung Exception ở cuối cùng
            System.out.println(e.getMessage());
        }

        // lấy dữ liệu
        List<Customer> customerList = Main.getCustomerData();

        // lọc các khách hàng đạt yêu cầu
        List<Customer> newValidCustomers = new ArrayList<>();

        newValidCustomers = Bank.checkFileInputCustomers(customerListTxt,exceptions);

        customerList.addAll(newValidCustomers);
        if (customerList != null) {
            try {
                CustomerDAO.getInstance().save(customerList);
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        // Xử lý tất cả các ngoại lệ sau khi đã thực hiện xong các công việc
        if (!exceptions.isEmpty()) {
            for (Exception e : exceptions) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("");
        customerList.stream().forEach(s -> s.displayInformation());
    }

    public static boolean addSavingAccount(Scanner scanner){
        List<Customer> customerList = Main.getCustomerData();
        List<Account> accountList = Main.getAccountData();
        String CCCDnumber = RegexCheck.getInput("Nhập mã số của khách hàng: ",RegexCheck.CCCDNumber,scanner);
        // kiểm tra khách hàng có tồn tại không
        if (!customerList.contains(new Customer("",new CCCD(CCCDnumber)))){
            System.out.println("Không tìm thấy khách hàng "+CCCDnumber+", tác vụ không thành công");
            return false;
        }

        String accountNumber = RegexCheck.getInput("Nhập số tài khoản gồm 6 chữ số: ",RegexCheck.accountNumber,scanner);
        String balance;
        do {
            balance = RegexCheck.getInput("Nhập số dư tài khoản: ",RegexCheck.balanceRegex,scanner) ;
            if (Integer.parseInt(balance)>50000){
                break;
            }else {
                System.out.println("Số dư tài khoản phải lớn hơn 50.000đ !");
            }
        }while (true);

        final String accountNumberFN = accountNumber;

        // Kiểm tra xem số tài khoản ATM đã tồn tại trong ngân hàng hay chưa
        boolean accountExists = accountList.stream()
                .anyMatch(a -> a.getAccountNumber().equals(accountNumberFN));
        if (accountExists){
            System.out.println("Số tài khoản đã tồn tại, không thể thêm mới !");
            return false;
        }else {
            Account account = new SavingsAccount(accountNumber,Double.parseDouble(balance));
            account.setCustomerId(new CCCD(CCCDnumber));
            accountList.add(account);
            // lưu file
            AccountDAO.getInstance().save(accountList);
            System.out.println("Tạo tài khoản thành công");
            return true;
        }
    }



    public static boolean addXAccount(Scanner scanner){
        List<Customer> customerList = Main.getCustomerData();
        List<Account> accountList = Main.getAccountData();
        String CCCDnumber = RegexCheck.getInput("Nhập mã số của khách hàng: ",RegexCheck.CCCDNumber,scanner);
        // kiểm tra khách hàng có tồn tại không
        if (!customerList.contains(new Customer("",new CCCD(CCCDnumber)))){
            System.out.println("Không tìm thấy khách hàng "+CCCDnumber+", tác vụ không thành công");
            return false;
        }
        int x = scanner.nextInt();
        for (int i= 0;i < x;i++){
            // Tạo đối tượng Random
            Random random = new Random();
            // Tạo số ngẫu nhiên với 6 chữ số

            String balance = Integer.toString(100000 + random.nextInt(900000)) ;
            final String accountNumberFN = Integer.toString(100000 + i) ;
            // Kiểm tra xem số tài khoản ATM đã tồn tại trong ngân hàng hay chưa
            boolean accountExists = accountList.stream()
                    .anyMatch(a -> a.getAccountNumber().equals(accountNumberFN));
            if (accountExists){
                System.out.println("Số tài khoản đã tồn tại, không thể thêm mới !");

            }else {
                Account account = new SavingsAccount(accountNumberFN,Double.parseDouble(balance));
                account.setCustomerId(new CCCD(CCCDnumber));
                accountList.add(account);
                // lưu file
                AccountDAO.getInstance().save(accountList);
            }
        }
        return true;
    }

    public static void tranfers(Scanner scanner, String customerId, List<Customer> customerList) throws Exception{
        // check và in thông tin người gửi
        Customer senderCustomer = isCustomerExisted(customerId,customerList);
        senderCustomer.displayInformation();
        try {
            senderCustomer.tranfers(scanner);
        }catch (Exception e){
            throw e;
        }
    }

    public static void withdraw(Scanner scanner, String customerId,List<Customer> customerList) throws Exception{
        // check và in thông tin người gửi
        Customer withdrawerCustomer = isCustomerExisted(customerId,customerList);
        withdrawerCustomer.displayInformation();
        try {
            withdrawerCustomer.withdraw(scanner);
        }catch (Exception e){
            throw e;
        }
    }





}
