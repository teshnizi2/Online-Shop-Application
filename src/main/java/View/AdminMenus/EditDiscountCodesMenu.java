package View.AdminMenus;

import Controller.AdminProfileManager;
import Model.Account.Admin;
import View.Menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;

import static View.SellerProfileMenus.AddOffMenu.getDate;

public class EditDiscountCodesMenu extends Menu {
    private AdminProfileManager adminProfileManager;

    public EditDiscountCodesMenu(Menu parentMenu, AdminProfileManager adminProfileManager) {
        super("Edit Discount Code Menu", parentMenu);
        this.adminProfileManager = adminProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getEditDiscountCode());
        submenus.add(getEditStartTime());
        submenus.add(getEditEndTime());
        submenus.add(getEditDiscountPercent());
        submenus.add(getEditMaxPossibleDiscount());
        submenus.add(getEditDiscountPerCustomer());
        this.setSubMenus(submenus);
    }

    private Menu getEditDiscountCode() {
        return new Menu("Edit Discount Code", this) {
            @Override
            public void execute() {
                String discountCode;
                String newDiscountCode;
                while(true) {
                    System.out.println("Enter the code of discount you want to change:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isDiscountWithThisID(input)){
                        discountCode = input;
                        break;
                    }
                    System.out.println("There is no discount with previous code.");
                }
                while(true) {
                    System.out.println("Enter new Discount Code:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountCode(input)){
                        newDiscountCode = input;
                        break;
                    }
                    System.out.println("There is discount with this code.");
                }
                try {
                    adminProfileManager.editDiscountCode(discountCode, newDiscountCode);
                    System.out.println("Discount code " + discountCode + " successfully changed to " + newDiscountCode);
                }
                catch (NullPointerException n) {
                    System.out.println("There is no discount with previous code.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("There is discount with this code.");
                }
                this.parentMenu.execute();
            }
        };
    }

    //todo: make StartTime a Date instead of String
    private Menu getEditStartTime() {
        return new Menu("Edit Start Time", this) {
            @Override
            public void execute() {
                String discountCode;
                while(true){
                    System.out.println("Enter the code of discount you want to change:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountCode(input)){
                        discountCode = input;
                        break;
                    }
                    System.out.println("There is no discount with previous code.");
                }
                System.out.println("Enter new Start Time");
                Date startTime = getDate(parentMenu);
                if (startTime == null){
                    execute();
                }
                try {
                    adminProfileManager.editDiscountStartTime(discountCode, startTime);
                    System.out.println("Discount Start Time successfully changed to " + startTime);
                }
                catch (NullPointerException n) {
                    System.out.println("There is no discount with this code.");
                }
                this.parentMenu.execute();
            }
        };
    }

    //todo: make EndTime a Date instead of String
    private Menu getEditEndTime() {
        return new Menu("Edit End Time", this) {
            @Override
            public void execute() {
                String discountCode;
                while(true){
                    System.out.println("Enter the code of discount you want to change, (back) to return or (logout) to log out:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountCode(input)){
                        discountCode = input;
                        break;
                    }
                    System.out.println("There is no discount with previous code.");
                }
                System.out.println("Enter new End Time");
                Date endTime = getDate(parentMenu);
                if (endTime == null){
                    execute();
                }
                try {
                    adminProfileManager.editDiscountEndTime(discountCode, endTime);
                    System.out.println("Discount end time successfully changed to " + endTime);
                }
                catch (NullPointerException n) {
                    System.out.println("There is no discount with this code.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditDiscountPercent() {
        return new Menu("Edit Discount Percent", this) {
            @Override
            public void execute() {
                String discountCode;
                while(true){
                    System.out.println("Enter the code of discount you want to change, (back) to return or (logout) to log out:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountCode(input)){
                        discountCode = input;
                        break;
                    }
                    System.out.println("There is no discount with previous code.");
                }
                String discountPercent = "00";
                while(true){
                    System.out.println("Enter new Discount Percent:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountPercent(input)){
                        discountPercent = input;
                        break;
                    }
                    System.out.println("You must enter a 2 digit number.");
                }
                try {
                    adminProfileManager.editDiscountPercent(discountCode, discountPercent);
                    System.out.println("Discount percent successfully changed to " + discountPercent);
                }
                catch (NullPointerException n) {
                    System.out.println("There is no discount with this code.");
                }
                catch (IllegalArgumentException i) {
                    System.out.println("You must enter a 2 digit number.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditMaxPossibleDiscount() {
        return new Menu("Edit Maximum Possible Discount", this) {
            @Override
            public void execute() {
                String discountCode;
                while(true){
                    System.out.println("Enter the code of discount you want to change, (back) to return or (logout) to log out:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountCode(input)){
                        discountCode = input;
                        break;
                    }
                    System.out.println("There is no discount with previous code.");
                }
                String maxPossibleDiscount = "0";
                while(true){
                    System.out.println("Enter new maximum possible discount, (back) to return or (logout) to log out:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (input.matches("\\A\\d+\\z")){
                        discountCode = input;
                        break;
                    }
                    System.out.println("You must enter a number.");
                }
                try {
                    adminProfileManager.editDiscountMaxPossibleDiscount(discountCode, maxPossibleDiscount);
                    System.out.println("Discount maximum Possible discount successfully changed to " + maxPossibleDiscount);
                }
                catch (NullPointerException n) {
                    System.out.println("There is no discount with this code.");
                }
                catch (InputMismatchException i) {
                    System.out.println("You must enter a number.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditDiscountPerCustomer() {
        return new Menu("Edit Number Of Customers That Can Use This Discount", this) {
            @Override
            public void execute() {
                String discountCode;
                while(true){
                    System.out.println("Enter the code of discount you want to change, (back) to return or (logout) to log out:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (AdminProfileManager.isInputValidForDiscountCode(input)){
                        discountCode = input;
                        break;
                    }
                    System.out.println("There is no discount with previous code.");
                }
                String discountPerCustomer;
                while(true){
                    System.out.println("Enter new number of customers that can use this discount:");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("Back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("Logout")) {
                        loginAndRegisterManager.logoutUser();
                    } else if (input.matches("\\A\\d+\\z")){
                        discountPerCustomer = input;
                        break;
                    }
                    System.out.println("You must enter an integer number.");
                }
                try {
                    adminProfileManager.editDiscountPerCustomer(discountCode, discountPerCustomer);
                }
                catch (NullPointerException n) {
                    System.out.println("There is no discount with this code.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("You must enter an integer number.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.parentMenu.execute();
            }
        };
    }

}
