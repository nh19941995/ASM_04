package src.Models;

// dùng để In ra biên lai theo từng loại tài khoản mỗi khi rút tiền
public interface ReportService {
    void logTranfers(Transaction deposit,Transaction receiver);
    void logWithdraw(Transaction withdrawer);
}
