package View.SellerProfileMenus;

import Controller.SellerProfileManager;
import Model.Account.Seller;
import Model.Product.Category;
import View.Menu;
import View.ViewPersonalInfoMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class SellerProfileMenu extends Menu {
    private Seller seller;
    private SellerProfileManager sellerProfileManager;

    public SellerProfileMenu(Seller seller, Menu parentMenu) {
        super("Seller Profile Menu", parentMenu);
        this.seller = seller;
        this.sellerProfileManager = new SellerProfileManager(seller);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(new ViewPersonalInfoMenu(this, sellerProfileManager));
        subMenus.add(getViewCompanyInformationMenu());
        subMenus.add(getViewSalesHistoryMenu());
        subMenus.add(new ManageProductsMenu(this, sellerProfileManager));
        subMenus.add(getShowCategoriesMenu());
        subMenus.add(new ViewOffsMenu(this, sellerProfileManager));
        subMenus.add(getViewBalanceMenu());
        this.setSubMenus(subMenus);
    }

    //todo: does this work right?
    public Menu getViewCompanyInformationMenu() {
        return new Menu("View graphics.SellerProfile.Company Information Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(sellerProfileManager.getCompanyInformation());
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

    public Menu getViewSalesHistoryMenu() {
        return new Menu("View Sales History Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                for (String saleHistory : sellerProfileManager.getSalesHistory()) {
                    System.out.print(saleHistory + ", ");
                    System.out.println("1. Logout");
                    System.out.println("2. Back");
                }
            }
        };
    }

    public Menu getShowCategoriesMenu() {
        return new Menu("Show Categories Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                for (Category category : sellerProfileManager.getAllCategories()) {
                    System.out.println(category);
                }
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

    public Menu getViewBalanceMenu() {
        return new Menu("View Balance Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("your balance is: " + seller.getBalance());
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

}
