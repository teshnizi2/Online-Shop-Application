package Model.Account;

import java.util.ArrayList;

public class Admin extends Account{
    private static ArrayList<Admin> allAdmins;
    private String storeBankID;

    private static int minWalletBalance = 10;
    private static int bankingFeePercent = 5;

    static {
        allAdmins = new ArrayList<>();
    }

    public Admin(String username, String password, String name, String lastName, String email, String phoneNumber) {
        super(username, password, name, lastName, email, phoneNumber);
        allAdmins.add(this);
    }

    public Admin() {
        this("", "", "", "", "", "");
    }

    public void setStoreBankID(String storeBankID) {
        this.storeBankID = storeBankID;
    }

    public String getStoreBankID() {
        return storeBankID;
    }

    public static void setAllAdmins(ArrayList<Admin> allAdmins) {
        Admin.allAdmins = allAdmins;
    }

    public static ArrayList<Admin> getAllAdmins() {
        return allAdmins;
    }

    public static Admin getAdminByUserName(String username){
        for (Admin admin : allAdmins) {
            if (admin.getUsername().equalsIgnoreCase(username)){
                return admin;
            }
        }
        return null;
    }

    public static void setAllStoreBankIDs(String storeBankID){
        for (Admin admin : allAdmins) {
            admin.storeBankID = storeBankID;
        }
    }

    public static String getBankIDOfStore(){
        return allAdmins.get(0).storeBankID;
    }

    public static int getMinWalletBalance() {
        return minWalletBalance;
    }

    public static void setMinWalletBalance(int minWalletBalance) {
        Admin.minWalletBalance = minWalletBalance;
    }

    public static int getBankingFeePercent() {
        return bankingFeePercent;
    }

    public static void setBankingFeePercent(int bankingFeePercent) {
        Admin.bankingFeePercent = bankingFeePercent;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username = '" + username + '\'' +
                ", password = '" + password + '\'' +
                ", name = '" + name + '\'' +
                ", lastName = '" + lastName + '\'' +
                ", email = '" + email + '\'' +
                ", phoneNumber = '" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public void removeUser() {
        allAdmins.remove(this);
    }
}
