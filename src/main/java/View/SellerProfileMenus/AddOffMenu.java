package View.SellerProfileMenus;

import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Request.AddOffRequest;
import View.Menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;

public class AddOffMenu extends Menu {
    private SellerProfileManager sellerProfileManager;
    private AddOffRequest addOffRequest;

    public AddOffMenu(Menu parentMenu, SellerProfileManager sellerProfileManager) {
        super("Add Off Menu", parentMenu);
        this.sellerProfileManager = sellerProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getAddOffID());
        submenus.add(getAddOffStartTime());
        submenus.add(getAddOffEndTime());
        submenus.add(getAddOffAmount());
        submenus.add(getAddOffProductsMenu());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        if (addOffRequest == null) {
            //this.addOffRequest = sellerProfileManager.addOffRequest();
        }
        super.show();
    }

    private Menu getAddOffID() {
        return new Menu("Add Off ID", this) {
            @Override
            public void execute() {
                System.out.println("Enter Off ID, (back) to return or (logout) to log out::");
                String offID = scanner.nextLine();
                if (offID.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (offID.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setOffId(addOffRequest, offID);
                    System.out.println("Off ID " + offID + " successfully added to your request.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("There is already an Off with this ID.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddOffStartTime() {
        return new Menu("Add Off Start Time", this) {
            @Override
            public void execute() {
                System.out.println("Enter Off Start Time:");
                Date offStartTime = getDate(parentMenu);
                if (offStartTime == null){
                    execute();
                }
                sellerProfileManager.setOffStartTime(addOffRequest, offStartTime);
                System.out.println("Off Start Time " + offStartTime + " successfully added to your request.");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddOffEndTime() {
        return new Menu("Add Off End Time", this) {
            @Override
            public void execute() {
                System.out.println("Enter Off End Time:");
                Date offEndTime = getDate(parentMenu);
                if (offEndTime == null){
                    execute();
                }
                sellerProfileManager.setOffEndTime(addOffRequest, offEndTime);
                System.out.println("Off End Time " + offEndTime + " successfully added to your request.");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddOffAmount() {
        return new Menu("Add Off Amount", this) {
            @Override
            public void execute() {
                System.out.println("Enter Off Amount, (back) to return or (logout) to log out::");
                String offAmount = scanner.nextLine();
                if (offAmount.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (offAmount.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setOffAmount(addOffRequest, offAmount);
                    System.out.println("Off Amount " + offAmount + " successfully added to your request.");
                }
                catch (InputMismatchException e) {
                    System.out.println("You should enter an integer number.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddOffProductsMenu() {
        return new Menu("Add Off Products Menu", this) {
            @Override
            public void execute() {
                System.out.println("Enter ID of product you want to add, or (back) to return:");
                String productId = scanner.nextLine();
                if (productId.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        //sellerProfileManager.setOffProduct(addOffRequest, productId);
                        System.out.println("Product " + productId + " successfully added to your request.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no product with this ID.");
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("You don't have this product.");
                    }
                    this.execute();
                }
            }
        };
    }

    public static Date getDate(Menu parentMenu){
        int year, month, day;
        try {
            while (true) {
                System.out.println("Enter year");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (input.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                } else if (!input.matches("\\A\\d+\\z")) {
                    System.out.println("please enter number");
                } else {
                    year = Integer.parseInt(input);
                    break;
                }
            }
            while (true) {
                System.out.println("Enter month");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (input.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                } else if (!input.matches("\\A\\d+\\z")) {
                    System.out.println("please enter number");
                } else if (!(Integer.parseInt(input) > 12 || Integer.parseInt(input) < 1)) {
                    System.out.println("Please enter a number between 1-12 for month");
                } else {
                    month = Integer.parseInt(input);
                    break;
                }
            }
            while (true) {
                System.out.println("Enter day");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (input.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                } else if (!input.matches("\\A\\d+\\z")) {
                    System.out.println("please enter number");
                } else if (!(Integer.parseInt(input) > 31 || Integer.parseInt(input) < 1)) {
                    System.out.println("Please enter a number between 1-31 for day");
                } else {
                    day = Integer.parseInt(input);
                    break;
                }
            }
            return java.sql.Date.valueOf(LocalDate.of(year, month, day));
        } catch (Exception e) {
            System.out.println("Wrong format of Date. Please try again later");
            return null;
        }
    }
}
