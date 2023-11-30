package View.SellerProfileMenus;

import Controller.ProductsManager;
import Controller.SellerProfileManager;
import View.Menu;

import java.util.ArrayList;

public class ManageProductsMenu extends Menu {
    private SellerProfileManager sellerProfileManager;

    public ManageProductsMenu(Menu parentMenu, SellerProfileManager sellerProfileManager) {
        super("Manage Products Menu", parentMenu);
        this.sellerProfileManager = sellerProfileManager;
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getViewMenu());
        subMenus.add(getViewBuyersMenu());
        subMenus.add(new AddProductMenu(this, sellerProfileManager));
        subMenus.add(new EditProductMenu(this, sellerProfileManager));
        subMenus.add(getRemoveProductMenu());
        this.setSubMenus(subMenus);
    }

    @Override
    public void show() {
        //System.out.println(sellerProfileManager.getSellerProductsNameAndID());
        super.show();
    }

    public Menu getViewMenu() {
        return new Menu("View Product Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter ProductId to view the product:");
            }

            @Override
            public void execute() {
                show();
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Back")) {
                    this.parentMenu.execute();
                } else if (input.equals("Logout")) {
                    loginAndRegisterManager.logoutUser();
                } else if (ProductsManager.isValidIDForProductID(input)){
                    System.out.println(sellerProfileManager.getProductByID(input));
                    this.execute();
                }
                System.out.println("There is no product with this productID");
                this.execute();
            }
        };
    }

    public Menu getViewBuyersMenu() {
        return new Menu("View Buyers Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter ProductId to view the product buyers:");
            }

            @Override
            public void execute() {
                show();
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Back")) {
                    this.parentMenu.execute();
                } else if (input.equals("Logout")) {
                    loginAndRegisterManager.logoutUser();
                } else if (ProductsManager.isValidIDForProductID(input)) {
                    System.out.println(sellerProfileManager.getProductBuyers(input));
                    this.execute();
                }
                System.out.println("There is no product with this productID");
                this.execute();
            }
        };
    }

    public Menu getRemoveProductMenu() {
        return new Menu("Remove Product Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter (productId) to remove a product, (Back) to return or (Logout) to leave your account:");
            }

            @Override
            public void execute() {
                show();
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Back")) {
                    this.parentMenu.execute();
                } else if (input.equalsIgnoreCase("Logout")) {
                    loginAndRegisterManager.logoutUser();
                } else {
                    if (SellerProfileManager.isProductIdFormatValid(input)) {
                        sellerProfileManager.removeProductRequest(input);
                        System.out.println("Your product removed successfully");
                        parentMenu.execute();
                    } else {
                        System.out.println("ProductID is Invalid");
                        this.execute();
                    }
                }
            }
        };
    }
}
