package src.Models;

public interface Withdraw {
    void withdraw(double amount, boolean status);
    // xác định xem giá trị có thoả điều kiện rút tiền hay không
    void isAcceptedWithdraw(double amount);
}
