package src.Service;

import src.Models.Account;
import src.Models.Customer;

import java.text.DecimalFormat;

public class Display {

    public static void menu(String AUTHOR ,String VERSION ){
        System.out.println("+--------------+----------------------------+----------------------+");
        System.out.println("| NGAN HANG SO | "+AUTHOR+"@v"+VERSION+"                                    |");
        System.out.println("+--------------+----------------------------+----------------------+");
        System.out.println("| 1. Xem danh sách khách hàng                                      |");
        System.out.println("| 2. Nhập danh sách khách hàng                                     |");
        System.out.println("| 3. Thêm tài khoản ATM                                            |");
        System.out.println("| 4. Chuyển tiền                                                   |");
        System.out.println("| 5. Rút tiền                                                      |");
        System.out.println("| 6. Tra cứu lịch sử giao dịch                                     |");
//        System.out.println("| 7. Thêm khách hàng thủ công                                      |");
//        System.out.println("| 8. Thêm số tài khoản ngẫu nhiên tự động (để test)                |");
        System.out.println("| 0. Thoát                                                         |");
        System.out.println("+--------------+----------------------------+----------------------+");
        System.out.print("Chọn chức năng: ");
    }




}
