package src.Models;

import src.Dao.AccountDAO;
import src.Dao.CustomerDAO;
import src.Dao.TransactionDAO;
import src.Enum.TypeTransaction;
import src.Main;
import src.Service.RegexCheck;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


public class Customer extends User implements Serializable {
    static final long serialVersionUID = 1l;
    // danh sách các tài khoản của 1 khách hàng
    private List<Account> accounts = new ArrayList<>();

    public List<Account> getAccounts() {
        // Lấy dữ liệu toàn bộ
        List<Account> allAccountList = Main.getAccountData();

        // Kiểm tra null cho allTransactionList
        if (allAccountList == null) {
            return null;
        }

        // Kiểm tra null cho accountNumber
        if (Objects.isNull(this.getCustomerId())) {
            return null;
        }

        // Lọc danh sách các giao dịch liên quan
        List<Account> accountList = allAccountList.stream()
                .filter(s -> Objects.equals(s.getCustomerId().getId(), this.getCustomerId().getId()))
                .collect(Collectors.toList());
        return accountList;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


    public Customer(String name, CCCD customerId) {
        super(name, customerId);
    }


    public Customer() {
    }

    // tổng số dư của tất cả các tài khoản trong danh sách tài khoản
    public double getBalance(){
        double sum = 0;
        if (this.getAccounts()!=null){
            for (Account a: this.getAccounts()) {
                sum += a.getBalance();
            }
            return sum;
        }
        return 0;
    }


    // in ra thông tin khách hàng
    public void displayInformation(){
        if (this==null){
            System.out.println("Customer: Null");
        }else {
            // Tạo một đối tượng DecimalFormat với định dạng thường
            DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
            // Sử dụng đối tượng DecimalFormat để định dạng giá trị double
            String balance = decimalFormat.format(this.getBalance());
            String id = this.getCustomerId().getId();
            String name = this.getName();
            String type = (this.isPremiumAccount()?"Premium":"Nomal");
            System.out.printf("| %12s | %-20s | %-7s | %16s |%n",  id,name,type,balance);
            int n = 0;
            if (this.getAccounts() !=null){
                for (Account a: this.getAccounts()) {
//                    System.out.println("| "+(++n)+" "+a.toString());
                    System.out.printf("| %-3s%-20s \n", ++n, a.toString());

                }
            }
        }
    }

    // kiểm tra loại tài khoản
    public boolean isPremiumAccount(){
        if (this.getAccounts()!=null){
            return this.getAccounts().stream().anyMatch(Account::isPremiumAccount);
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        //   kiểm tra xem đối tượng hiện tại có phải là cùng đối tượng với đối tượng được truyền vào làm tham số hay
        //   không. Nếu đúng, thì hai đối tượng rõ ràng là bằng nhau và phương thức trả về true.
        if (this == obj)  return true;

        // kiểm tra xem đối tượng được truyền vào làm tham số có phải là null hay không hoặc nếu nó không cùng lớp với
        // đối tượng hiện tại. Nếu một trong hai điều kiện này là đúng, thì hai đối tượng không thể bằng nhau và phương
        // thức trả về false.
        if (obj == null || this.getClass() != obj.getClass()){
            System.out.println("vào 1");
            return false;
        }

        // Dòng này chuyển đổi đối tượng được truyền vào làm tham số thành đối tượng Customer.
        // Điều này là cần thiết vì phương thức equals() được triển khai cụ thể cho lớp Customer.
        Customer customer = (Customer) obj;

        // so sánh ID khách hàng của hai đối tượng. Nếu ID khách hàng bằng nhau,
        // thì hai đối tượng bằng nhau và phương thức trả về true.
        if (this.getCustomerId().getId().equals(customer.getCustomerId().getId()) ){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId().getId());
    }

//    public Customer getCustmer(String ID){
//        // lấy dữ liệu
//        List<Customer> customerList = Main.getCustomerData();
//
//        // Tìm một khách hàng bất kỳ có ID bằng với ID được truyền vào
//        Optional<Customer> optionalCustomer = customerList.stream().filter(s->s.getCustomerId().equals(ID)).findAny();
//
//        // Trả về khách hàng đó, nếu không tìm thấy thì trả về null
//        Customer customer = optionalCustomer.orElseGet(()->null);
//        return customer;
//    }

    public void tranfers( Scanner scanner) throws Exception{
        // nhập tài khoản dùng để chuyển tiền,
        // nhập tài khoản nhận tiền
        // (kiểm tra tính hợp lệ của từng tài khoản),

        // sau đó yêu cầu nhập số tiền rút, xác nhận việc chuyển tiền và sau khi thỏa mãn hết các điều kiện sẽ
        // gọi hàm transfer của account gửi.

        try {
            // nhập thông tin người gửi --------------------------------------------------------------------------------

            // nhập số TK
            String senderAccountNumber = RegexCheck.getInput("Nhập số tài khoản của người gửi gồm 6 chữ số: ",RegexCheck.accountNumber,scanner);
            SavingsAccount senderAccount =  (SavingsAccount) DigitalBank.isAccountExisted(senderAccountNumber,this.getCustomerId().getId(),Main.getAccountData(),Main.getCustomerData());

            // nhập thông tin người nhận -------------------------------------------------------------------------------

            // nhập số TK
            SavingsAccount receiverAccount;
            do {
                // nhập số tk người nhận
                String receiverAccountNumber = RegexCheck.getInput("Nhập số tài khoản của nhận gồm 6 chữ số: ",RegexCheck.accountNumber,scanner);
                receiverAccount =  (SavingsAccount) DigitalBank.isAccountExisted(receiverAccountNumber);
            }while (receiverAccount==null);

            Customer receiverCustomer = DigitalBank.isCustomerExisted(receiverAccount.getCustomerId().getId(),Main.getCustomerData());
            System.out.println("Gửi tiền đến tài khoản: " + receiverAccount.getAccountNumber()+" | "+receiverCustomer.getName());

            // nhập số tiền
            String amount = RegexCheck.getInput("Nhập số tiền cần chuyển: ","Số tiền giao dịch là số nguyên dương, vui lòng nhập lại !",RegexCheck.balanceRegex,scanner);

            // xác nhận giao dịch
            System.out.print("Xác nhận thực hiện chuyển "
                    + Main.formatMoney(Double.parseDouble(amount))
                    + " từ tài khoản [" + senderAccountNumber
                    + "] đến tài khoản [" + receiverAccount.getAccountNumber() +"] (y,n): ");
            String flag = scanner.nextLine();
            if (!flag.equals("y")){
                throw new RuntimeException("Hủy giao dịch !");
            }

            // check có chuyển khoản dc không
            try {
                senderAccount.isAcceptedTransfer(receiverAccount,Double.parseDouble(amount));
            }catch (Exception e){
                // tạo giao dịch lỗi
                senderAccount.transfer(receiverAccount,Double.parseDouble("0"),false);
                throw e;
            }
            // bắt đầu chuyển khoản
            senderAccount.transfer(receiverAccount,Double.parseDouble(amount),true);
            // update dữ liệu
        }catch (Exception e){
            throw e;
        }
    }

    public void withdraw(Scanner scanner) throws Exception{
        try {
            // nhập thông tin người rút --------------------------------------------------------------------------------

            // nhập số TK
            String senderAccountNumber = RegexCheck.getInput("Nhập số tài khoản của người rút gồm 6 chữ số: ",RegexCheck.accountNumber,scanner);
            SavingsAccount senderAccount =  (SavingsAccount) DigitalBank.isAccountExisted(senderAccountNumber,this.getCustomerId().getId(),Main.getAccountData(),Main.getCustomerData());
            // nhập số tiền
            String amount = RegexCheck.getInput("Nhập số tiền cần rút: ","Số tiền giao dịch là số nguyên dương, vui lòng nhập lại !",RegexCheck.balanceRegex,scanner);
            // xác nhận giao dịch
            System.out.print("Xác nhận thực hiện rút "
                    + Main.formatMoney(Double.parseDouble(amount)) + " từ tài khoản [" + senderAccount.getAccountNumber() + "] (y,n): ");
            String flag = scanner.nextLine();
            if (!flag.equals("y")){
                throw new RuntimeException("Hủy giao dịch !");
            }

            // check có rút dc không
            try {
                senderAccount.isAcceptedWithdraw(Double.parseDouble(amount));
            }catch (Exception e){
                // tạo giao dịch lỗi
                senderAccount.withdraw(Double.parseDouble("0"),false);
                throw e;
            }
            // bắt đầu rút
            senderAccount.withdraw(Double.parseDouble(amount),true);
        }catch (Exception e){
            throw e;
        }
    }

}


