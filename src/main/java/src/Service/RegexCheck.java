package src.Service;

import java.util.Scanner;

public class RegexCheck {
    public static final String accountNumber = "^[0-9]{6}$";
    public static final String CCCDNumber = "^[0-9]{12}$";
    public static final String balanceRegex = "^[1-9]\\d*$";
    public static final String nameRegex = "^[a-zA-ZÀ-Ỹà-ỹ ]+$";

    public static String getInput(String mess, String regex, Scanner scanner) {
        String result = null;
        while (true) {
            System.out.print(mess);
            result = scanner.nextLine();

            if (result.matches(regex)) {
                break;
            } else {
                System.out.println("Sai định dạng, vui lòng nhập lại!");
            }
        }
        return result;
    }

    public static String getInput(String mess,String error, String regex, Scanner scanner) {
        String result = null;
        while (true) {
            System.out.print(mess);
            result = scanner.nextLine();

            if (result.matches(regex)) {
                break;
            } else {
                System.out.println(error);
            }
        }
        return result;
    }




}
