package View.RegistrationMenus;

import Model.Account.AccountType;
import View.Menu;


import java.util.ArrayList;

public class RegistrationMenu extends Menu {
    private AccountType accountType;

    public RegistrationMenu(Menu parentMenu) {
        super("Create Account", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getCreateCustomerAccountMenu());
        subMenus.add(getCreateSellerAccountMenu());
        subMenus.add(getCreateAdminAccountMenu());
        this.setSubMenus(subMenus);
    }

    private Menu getCreateCustomerAccountMenu(){
        return new Menu("Create Customer Account", this) {
            @Override
            public void execute() {
                accountType = AccountType.CUSTOMER;
                registerNewUser();
            }
        };
    }

    private Menu getCreateSellerAccountMenu(){
        return new Menu("Create Seller Account", this) {
            @Override
            public void execute() {
                accountType = AccountType.SELLER;
                registerNewUser();
            }
        };
    }

    private Menu getCreateAdminAccountMenu(){
        return new Menu("Create Admin Account", this) {
            @Override
            public void execute() {
                accountType = AccountType.ADMIN;
                if (loginAndRegisterManager.canCreateAdminManually()) {
                    registerNewUser();
                }
                else {
                    System.out.println("You can't create admin accounts any more");
                }
            }
        };
    }

    private String getUserName(){
        System.out.println("Enter username:");
        System.out.println("Enter back to cancel");
        String userName = scanner.nextLine().trim();
        if (userName.equalsIgnoreCase("back")){
            execute();
        }
        if (userName.matches("[a-zA-Z0-9.]+") && !userName.contains("..")){
            if (!loginAndRegisterManager.isThereAccountWithUserName(userName)) {
                return userName;
            }
            else {
                System.out.println("This username already exists.");
            }
        }
        else {
            System.out.println("Invalid Username");
            System.out.println("UserNames can only contain letters (a-z), numbers (0-9), and periods (.)");
            System.out.println("(UserNames cannot contain ore than one dot in a row)\n");
        }
        return getUserName();
    }

    private String getPassword(){
        System.out.println("Enter password:");
        return scanner.nextLine().trim();
    }

    private String getEmail() {
        System.out.println("Enter email:");
        System.out.println("Enter back to cancel");
        String email = scanner.nextLine().trim();
        if (email.equalsIgnoreCase("back")){
            execute();
        }
        if (email.matches(".+?@\\w+\\.\\w+")){
            return email;
        }
        else {
            System.out.println("Invalid email");
        }
        return getEmail();
    }

    private String getNameOfUser(boolean lastName){
        System.out.println("Enter" + (lastName ? " last" : "") + " name:");
        System.out.println("Enter back to cancel");
        String name = scanner.nextLine().trim();
        if (name.equalsIgnoreCase("back")){
            execute();
        }
        if (name.matches("[a-zA-Z ]+")){
            return name;
        }
        else {
            System.out.println("Invalid name");
        }
        return getNameOfUser(lastName);
    }

    private String getPhoneNumber(){
        System.out.println("Enter phone number:");
        System.out.println("Enter back to cancel");
        String phoneNumber = scanner.nextLine().trim();
        if (phoneNumber.equalsIgnoreCase("back")){
            execute();
        }
        if (phoneNumber.matches("\\d+")){
            return phoneNumber;
        }
        else {
            System.out.println("Invalid phone number");
        }
        return getPhoneNumber();
    }

    private String getCompanyName(){
        System.out.println("Enter company name");
        return scanner.nextLine().trim();
    }

    private void registerNewUser(){
        if (accountType.equals(AccountType.CUSTOMER)){
            loginAndRegisterManager.registerCustomer(getUserName(), getPassword(), getNameOfUser(false),
                    getNameOfUser(true), getEmail(), getPhoneNumber());
        }
        else if (accountType.equals(AccountType.SELLER)){
            loginAndRegisterManager.registerSeller(getUserName(), getPassword(), getNameOfUser(false),
                    getNameOfUser(true), getEmail(), getPhoneNumber(), getCompanyName());
            System.out.println("Your request has been sent to Admins. Please wait for verification\n");
        }
        else {
            loginAndRegisterManager.registerAdmin(getUserName(), getPassword(), getNameOfUser(false),
                    getNameOfUser(true), getEmail(), getPhoneNumber());
        }
    }
}
