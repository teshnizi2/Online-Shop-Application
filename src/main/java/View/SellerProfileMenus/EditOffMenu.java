package View.SellerProfileMenus;

import Controller.SellerProfileManager;
import Model.Request.EditOffRequest;
import View.Menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;

import static View.SellerProfileMenus.AddOffMenu.getDate;

public class EditOffMenu extends Menu {
    private SellerProfileManager sellerProfileManager;
    private EditOffRequest editOffRequest;

    public EditOffMenu(Menu parentMenu, SellerProfileManager sellerProfileManager) {
        super("Edit Off Menu", parentMenu);
        this.sellerProfileManager = sellerProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getEditOffID());
        submenus.add(getEditOffStartTime());
        submenus.add(getEditOffEndTime());
        submenus.add(getEditOffAmount());
        submenus.add(getEditOffProductsMenu());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        System.out.println("Enter ID of the Off you want to edit, (back) to return or (logout) to log out:");
        String offID = scanner.nextLine();
        if (offID.equalsIgnoreCase("back")) {
            parentMenu.execute();
        } else if (offID.equalsIgnoreCase("logout")) {
            loginAndRegisterManager.logoutUser();
        }
        try {
            this.editOffRequest = sellerProfileManager.makeNewEditOffRequest(offID);
            super.show();
        }
        catch (NullPointerException e) {
            System.out.println("There is no Off with this ID.");
            this.show();
        }
        catch (IllegalArgumentException e) {
            System.out.println("You don't have this off.");
            this.show();
        }
    }

    private Menu getEditOffID() {
        return new Menu("Edit Off ID", this) {
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
                    sellerProfileManager.setOffId(editOffRequest, offID);
                    System.out.println("Off ID " + offID + " successfully added to your request.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("There is already an Off with this ID.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditOffStartTime() {
        return new Menu("Edit Off Start Time", this) {
            @Override
            public void execute() {
                System.out.println("Enter Off Start Time:");
                Date offStartTime = getDate(parentMenu);
                if (offStartTime == null){
                    execute();
                }
                sellerProfileManager.setOffStartTime(editOffRequest, offStartTime);
                System.out.println("Off Start Time " + offStartTime + " successfully added to your request.");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditOffEndTime() {
        return new Menu("Edit Off End Time", this) {
            @Override
            public void execute() {
                System.out.println("Enter Off End Time:");
                Date offEndTime = getDate(parentMenu);
                if (offEndTime == null){
                    execute();
                }
                sellerProfileManager.setOffEndTime(editOffRequest, offEndTime);
                System.out.println("Off End Time " + offEndTime + " successfully added to your request.");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditOffAmount() {
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
                    sellerProfileManager.setOffAmount(editOffRequest, offAmount);
                    System.out.println("Off Amount " + offAmount + " successfully added to your request.");
                }
                catch (InputMismatchException e) {
                    System.out.println("You should enter an integer number.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditOffProductsMenu() {
        return new Menu("Edit Off Products Menu", this) {
            @Override
            public void execute() {
                System.out.println("Select one of these options or enter (back) to return:");
                System.out.println("1. Remove Product");
                System.out.println("2. Add Product");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else if (input.equals("1")) {
                    System.out.println("Enter ID of Product:");
                    String productId = scanner.nextLine();
                    try {
                        sellerProfileManager.removeProductInOffRequest(editOffRequest, productId);
                        System.out.println("Product " + productId + " removed successfully in request.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no product with this ID.");
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("This Product isn't in off");
                    }
                    this.execute();
                }
                else if (input.equals("2")) {
                    System.out.println("Enter ID of Product:");
                    String productId = scanner.nextLine();
                    try {
                        //sellerProfileManager.setOffProduct(editOffRequest, productId);
                        System.out.println("Product " + productId + " added successfully to your request.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no product with this ID");
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("You don't have this product.");
                    }
                    this.execute();
                }
                else {
                    System.out.println("Wrong input");
                    this.execute();
                }
            }
        };
    }
}
