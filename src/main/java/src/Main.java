package src;


import src.Dao.AccountDAO;
import src.Dao.CustomerDAO;
import src.Dao.TransactionDAO;
import src.Models.*;
import src.Service.Display;
import src.Service.RegexCheck;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    private static List<Customer> customerList = CustomerDAO.getInstance().List();
    private static List<Account> accountList = AccountDAO.getInstance().List();
    private static List<Transaction> transactionList = TransactionDAO.getInstance().List();

    public static List<Customer> getCustomerData() {
        return customerList;
    }
    public static List<Account> getAccountData() {
        return accountList;
    }
    public static List<Transaction> getTransactionData() {
        return transactionList;
    }

    public static void setCustomerList(List<Customer> customerList) {
        Main.customerList = customerList;
    }

    public static void setAccountList(List<Account> accountList) {
        Main.accountList = accountList;
    }

    public static void setTransactionList(List<Transaction> transactionList) {
        Main.transactionList = transactionList;
    }

    public static String formatMoney(double value){
        // Tạo một đối tượng DecimalFormat với định dạng thường
        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        // Sử dụng đối tượng DecimalFormat để định dạng giá trị double
        String formattedValue = decimalFormat.format(value);
        return formattedValue;
    }

    public static int getUserChoice(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Lựa chọn sai, vui lòng nhập lại !");
                System.out.print("Chọn chức năng: ");
                scanner.nextLine(); // Đọc và bỏ qua giá trị không hợp lệ
            }
        }
    }


    public static void main(String[] args) {
        // tạo một ngân hàng
        DigitalBank bank = new DigitalBank();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            String author = "FX23013";
            String version = "4.0.0";
            Display.menu(author,version); // Hiển thị menu
            choice = getUserChoice(scanner); // Lấy lựa chọn từ người dùng
            scanner.nextLine(); // Đọc ký tự Enter thừa

            switch (choice) {
                case 1:
                    bank.showCustomers();
                    break;
                case 2:
                    // src/main/java/src/Stores/Customers.txt
                    String urlCustomersTxt;
                    while (true) {
                        System.out.print("Nhập đường dẫn tệp: ");
                        urlCustomersTxt = scanner.nextLine();

                        if (!urlCustomersTxt.trim().isEmpty()) {
                            break;
                        } else {
                            System.out.println("Đường dẫn không được để trống. Vui lòng nhập lại.");
                        }
                    }
                    bank.addCustomers(urlCustomersTxt);
                    break;
                case 3:
                    bank.addSavingAccount(scanner);
                    break;
                case 4:
                    try {
                        // nhập ID
                        String depositorsID = RegexCheck.getInput("Nhập mã số của người gửi tiền: ", RegexCheck.CCCDNumber, scanner);
                        bank.tranfers(scanner,depositorsID,Main.getCustomerData());
                    }catch (Exception e){
                        System.out.println(e.getMessage());;
                        break;
                    }
                    break;
                case 5:
                    try {
                        // nhập ID
                        String withdrawID = RegexCheck.getInput("Nhập mã số của người rút tiền: ", RegexCheck.CCCDNumber, scanner);
                        bank.withdraw(scanner,withdrawID,Main.getCustomerData());
                    }catch (Exception e){
                        System.out.println(e.getMessage());;
                        break;
                    }

                    break;
                case 6:
                    List<Customer> customerList = Main.getCustomerData();
                    for (Customer customer: customerList
                         ) {
                        customer.displayInformation();
                        for (Account account: customer.getAccounts()) {
                            List<Transaction> transactionList = account.getTransactionList();
                            for (Transaction transaction:  transactionList) {
                                transaction.display();
                            }
                        }
                        System.out.println("+------------------------------------------------------------------+");
                    }
                    break;
//                case 7:
//                    try {
//                        // đường dẫn tệp Customers.txt
//                        String urlCustomersTxt_7 = "src/main/java/src/Stores/Customers.txt";
//                        CustomerDAO.creatCustomerTxt(urlCustomersTxt_7,scanner);
//
//                        // đọc file Customers.txt
//                        List<Customer> customerListTxt_7 = CustomerDAO.readTxt(urlCustomersTxt_7);
//
//                        // lấy dữ liệu
//                        List<Customer> customerListDat_7 = Main.getCustomerData();
//
//                        // các khách hàng mới đạt yêu cầu
//                        List<Exception> exceptionList = new ArrayList<>();
//                        List<Customer> newValidCustomers_7 = Bank.checkFileInputCustomers(customerListTxt_7,exceptionList);
//
//                        // list chứa toàn bộ
//                        customerListDat_7.addAll(newValidCustomers_7);
//
//                        // Lưu vào file nhị phân
//                        if (customerListDat_7!=null){
//                            CustomerDAO.getInstance().save(customerListDat_7);
//                        }
//
//                        // đọc file nhị phân dat
//                        List<Customer> end_7 =  CustomerDAO.getInstance().List();
//
//                        end_7.stream().forEach(s->s.displayInformation());
//
//                    }catch (Exception e){
//                        System.out.println(e.getMessage());
//                        break;
//                    }
//                case 8:
//                    try {
//                        DigitalBank.addXAccount(scanner);
//                    }catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
                case 0:
                    System.out.println("exit");
                    break;
                default:
                    System.out.println("Lựa chọn sai, vui lòng nhập lại lựa chọn !");
            }
        }while (choice!=0);

    }



}
