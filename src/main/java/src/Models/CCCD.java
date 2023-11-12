package src.Models;
import src.Exception.CustomerIdNotValidException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CCCD implements Serializable{
    private static List<Gender> listGenders;
    private static String[] addressName;
    private static String[] addressID;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static List<Gender> getListGenders() {
        return listGenders;
    }

    public static void setListGenders(List<Gender> listGenders) {
        CCCD.listGenders = listGenders;
    }

    public static String[] getAddressName() {
        return addressName;
    }

    public static void setAddressName(String[] addressName) {
        CCCD.addressName = addressName;
    }

    public static String[] getAddressID() {
        return addressID;
    }

    public static void setAddressID(String[] addressID) {
        CCCD.addressID = addressID;
    }

    public CCCD() {
    }

    public CCCD(String id) {
        this.id = id;
    }

    static {
        // address
        String[] arrName = new String[] {
                "Hà Nội", "Hà Giang", "Cao Bằng", "Bắc Kạn", "Tuyên Quang", "Lào Cai",
                "Điện Biên", "Lai Châu", "Sơn La", "Yên Bái", "Hòa Bình", "Thái Nguyên",
                "Lạng Sơn", "Quảng Ninh", "Bắc Giang", "Phú Thọ", "Vĩnh Phúc", "Bắc Ninh",
                "Hải Dương", "Hải Phòng", "Hưng Yên", "Thái Bình", "Hà Nam", "Nam Định",
                "Ninh Bình", "Thanh Hóa", "Nghệ An", "Hà Tĩnh", "Quảng Bình", "Quảng Trị",
                "Thừa Thiên Huế", "Đà Nẵng", "Quảng Nam", "Quảng Ngãi", "Bình Định", "Phú Yên",
                "Khánh Hòa", "Ninh Thuận", "Bình Thuận", "Kon Tum", "Gia Lai", "Đắk Lắk",
                "Đắk Nông", "Lâm Đồng", "Bình Phước", "Tây Ninh", "Bình Dương", "Đồng Nai",
                "Bà Rịa - Vũng Tàu", "Hồ Chí Minh", "Long An", "Tiền Giang", "Bến Tre",
                "Trà Vinh", "Vĩnh Long", "Đồng Tháp", "An Giang", "Kiên Giang",
                "Cần Thơ", "Hậu Giang", "Sóc Trăng", "Bạc Liêu", "Cà Mau"
        };
        String[] arrID = new String[] {
                "001", "002", "004", "006", "008", "010", "011", "012", "014", "015",
                "017", "019", "020", "022", "024", "025", "026", "027", "030", "031",
                "033", "034", "035", "036", "037", "038", "040", "042", "044", "045",
                "046", "048", "049", "051", "052", "054", "056", "058", "060", "062",
                "064", "066", "067", "068", "070", "072", "074", "075", "077", "079",
                "080", "082", "083", "084", "086", "087", "089", "091", "092", "093",
                "094", "095", "096"
        };
        CCCD.setAddressName(arrName);
        CCCD.setAddressID(arrID);
        // gender + centuries
        ArrayList<Gender> listGenders = new ArrayList<>();
        listGenders.add(new Gender(20,0,1));
        listGenders.add(new Gender(21,2,3));
        listGenders.add(new Gender(22,4,5));
        listGenders.add(new Gender(23,6,7));
        listGenders.add(new Gender(24,8,9));
        CCCD.setListGenders(listGenders);
    }

    // kiểm tra chiều dài
    private static boolean lengthCheck (String valueCCCD) {
        return valueCCCD.matches("^[0-9]{12}$");
    }

    // kiểm tra địa chỉ
    private static boolean checkAddress(String valueCCCD){
        // Tách chuỗi thành các phần tử bằng chỉ số
        String addressID = valueCCCD.substring(0, 3);
        String[] arrID = CCCD.getAddressID();
        if (Arrays.asList(arrID).contains(addressID)){
            return true;
        }else {
            return false;
        }
    }

    // kiểm tra tổng thể
    public static boolean check(String CCCD){
        if (lengthCheck(CCCD) && checkAddress(CCCD)) {
            return true;
        } else {
            return false;
        }
    }

    // nhập dữ liệu
    public static CCCD getInput(){
        Scanner scanner = new Scanner(System.in);
        String valueCCCD="";
        int flag = 0;

        do {
            System.out.print("Nhập số CCCD: ");
            valueCCCD  = scanner.nextLine();

            if(valueCCCD.equals("0")&&flag==1)break;
            if (check(valueCCCD)){
                return new CCCD(valueCCCD);
            }else {
                flag=1;
            }
        }while (true);
        return null;
    }
}


class Gender {
    private int arrCenturies ;
    private int male;
    private int female;

    public Gender(int arrCenturies, int male, int female) {
        this.arrCenturies = arrCenturies;
        this.male = male;
        this.female = female;
    }

    public int getArrCenturies() {
        return arrCenturies;
    }

    public void setArrCenturies(int arrCenturies) {
        this.arrCenturies = arrCenturies;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
    }

    @Override
    public String toString() {
        return "Centuries: "+this.arrCenturies + " | Female: "+this.female + " | Male: "+this.male;
    }
}
