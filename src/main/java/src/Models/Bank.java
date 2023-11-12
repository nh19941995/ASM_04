package src.Models;

import src.Exception.CustomerIdNotValidException;
import src.Main;
import java.util.*;

public abstract class Bank {
    private String Id;
    private String bankName;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Bank(String id, String bankName) {
        Id = id;
        this.bankName = bankName;
    }

    public Bank() {
        this.Id = String.valueOf(UUID.randomUUID());
    }



    public static List<Customer> checkFileInputCustomers(List<Customer> customerListTxt, List<Exception> exceptionList) throws CustomerIdNotValidException{
        // lấy dữ liệu
        List<Customer> customerListDat = Main.getCustomerData();
        // chứa các số CCCD của tất cả khách hàng trong Customers.dat
        List<String> existingCustomerIds = new ArrayList<>();

        for (Customer customerDat : customerListDat) {
            existingCustomerIds.add(customerDat.getCustomerId().getId());
        }

        // chứa các số CCCD mới trong Customers.txt sau khi bỏ các số lặp
        List<Customer> newCustomerListDat = new ArrayList<>();
        for (Customer customerTxt : customerListTxt) {
            try {
                if (!CCCD.check(customerTxt.getCustomerId().getId())){
                    throw new CustomerIdNotValidException("Khách hàng " + customerTxt.getCustomerId().getId() + " có ID không đúng định dạng, thêm khách hàng không thành công !");
                }
                if (existingCustomerIds.contains(customerTxt.getCustomerId().getId())) {
                    throw new CustomerIdNotValidException("Khách hàng " + customerTxt.getCustomerId().getId() + " đã tồn tại, thêm khách hàng không thành công !");
                } else {
                    newCustomerListDat.add(customerTxt);
                    throw new CustomerIdNotValidException("Khách hàng " + customerTxt.getCustomerId().getId() + " đã thêm thành công !");

                }
            }catch (Exception e){
                exceptionList.add(e);
            }
        }
        return newCustomerListDat;
    }
}
