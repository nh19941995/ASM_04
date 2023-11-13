package src.Service;

import src.Models.Account;
import src.Models.Customer;
import src.Models.Transaction;

import java.io.*;
import java.util.*;

public class Service {

    // đọc file nhị phân

    public static <T> List<T> readFile(String fileName){
        List<T> objects = new ArrayList<>();
        File oldFile = new File(fileName);

        if (!oldFile.exists()) {
            // Tạo mới tệp nếu không tồn tại
            try {
                oldFile.createNewFile();
            } catch (IOException io) {
                System.out.println("IOException: " + io.getMessage());
            }
        }

        try(ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))){
            boolean eof = false;
            while (!eof){
                try {
                    T object = (T) file.readObject();

                    objects.add(object);
                }catch (EOFException e){
                    eof = true;
                }
            }

        }catch (EOFException e){
            return new ArrayList<>();
        }catch (IOException io){
            System.out.println("IOException: " + io.getMessage());
        }catch (ClassNotFoundException cnfe){
            System.out.println("ClassNotFoundException: "+ cnfe.getMessage());
        }
        return objects;
    }

    public static <T> void writeFile(String fileName, List <T> objects){
        try(ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))){
            for (T object : objects){
                file.writeObject(object);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static List<Account> joinTransactionToAccount(List<Account> accountList , List<Transaction> transactionList){
        // map các listAccount có key là id
        Map<String, List<Transaction>> accountMap = new HashMap<>();
        for (Transaction transaction: transactionList) {
            if (accountMap.containsKey(transaction.getAccountNumber())){
                List<Transaction> t = accountMap.get(transaction.getAccountNumber());
                t.add(transaction);
                accountMap.replace(transaction.getAccountNumber(),t);
            }else {
                List<Transaction> t = new ArrayList<>();
                t.add(transaction);
                accountMap.putIfAbsent(transaction.getAccountNumber(),t);
            }
        }

        for (Account account: accountList) {
            for (String s: accountMap.keySet()) {
                if (account.getAccountNumber().equals(s)){
                    account.setTransactionList(accountMap.get(s));
                }
            }
        }
        return accountList;
    }

}


