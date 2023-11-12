package src.Service;

import src.Models.Account;

import java.util.ArrayList;
import java.util.List;

public class Task_2 implements Runnable {
    // Lớp Task có một trường taskId để lưu trữ id của tác vụ
    private List<Account> accountList = new ArrayList<>();
    private  int start;
    private  int end;
    private Account account;

    private boolean taskCompleted = false;
    public Task_2(int num,int thread,Account account, List<Account> accountList) {
        this.account = account;
        this.accountList = accountList;
        // số phần tử cần duyệt qua
        int total = accountList.size()/thread;
        // index phần tử kết thúc
        this.end = total*num;
        // index phần tử bắt đầu
        this.start = total*num -total;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread: " + Thread.currentThread().getName() + " bắt đầu: " + start + "kết thúc: "+end );
            for (int i = start; i < end; i++) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " phần tử: " + i);
                if (accountList.get(i).equals(account)) {
                    taskCompleted = true;
                    System.out.println("Thay thế thành công !, dừng luồng");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println( e.getMessage());
        }
    }

    public boolean isTaskCompleted() {
        return taskCompleted;
    }
}

