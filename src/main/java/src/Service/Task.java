package src.Service;

import src.Models.Account;

import java.util.ArrayList;
import java.util.List;

public class Task implements Runnable {
    // Lớp Task có một trường taskId để lưu trữ id của tác vụ
    private List<Account> accountList = new ArrayList<>();
    private int start;
    private int end;
    private Account account;
    private static boolean accountFound;
    private boolean taskCompleted = false;

    public static void setAccountFound(boolean accountFound) {
        Task.accountFound = accountFound;
    }

    public Task(int num, int thread, Account account, List<Account> accountList ) {
        this.account = account;
        this.accountList = accountList;
        // số phần tử cần duyệt qua
        int total = accountList.size() / thread;
        // index phần tử kết thúc
        this.end = total * num;
        // index phần tử bắt đầu
        this.start = total * num - total;
        setAccountFound(false);
    }

    @Override
    public void run() {
        try {
            for (int i = start; i < end; i++) {
//                System.out.println("Thread: " + Thread.currentThread().getName() + " phần tử: " + i);
                // Kiểm tra xem tài khoản đã được tìm thấy hay chưa
                if (accountFound) {
                    break;
                }

                // Nếu tìm thấy tài khoản, đánh dấu là hoàn thành và dừng luồng
                if (accountList.get(i).equals(account)) {
                    accountFound = true;
                    taskCompleted = true;
//                    System.out.println("Thay thế thành công !, dừng luồng");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
