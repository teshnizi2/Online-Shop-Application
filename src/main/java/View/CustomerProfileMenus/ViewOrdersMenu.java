package View.CustomerProfileMenus;

import Controller.CustomerProfileManager;
import Controller.ProductsManager;
import Model.Account.Customer;
import View.Menu;

import java.util.ArrayList;

public class ViewOrdersMenu extends Menu {
    private CustomerProfileManager customerProfileManager;
    private CustomerProfileMenu customerProfileMenu;
    public ViewOrdersMenu(Customer customer, Menu parentMenu) {
        super("View Orders Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getShowOrderMenu());
        subMenus.add(getRateMenu());
        this.setSubMenus(subMenus);
    }

    @Override
    public void show() {
        try {
//            System.out.println(customerProfileManager.showOrdersSellerNameAndDate()); // todo: wrong input if (null)
        } catch (Exception e) {
        }
        super.show();
    }

    private Menu getShowOrderMenu() {
        return new Menu("Show Order Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter orderID to view order:");
            }
            @Override
            public void execute() {
                show();
                String input = customerProfileMenu.getBuyLogInView(parentMenu);
                System.out.println(customerProfileManager.showOrder(input));
                parentMenu.execute();
            }
        };
    }

    private Menu getRateMenu() {
        return new Menu("Get Rate Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter productID:");
            }
            @Override
            public void execute() {
                show();
                String productID = customerProfileMenu.getProductIdInView(parentMenu);
                String score = customerProfileMenu.getProductRateInView(this);
                customerProfileManager.rateProduct(productID, score);
                System.out.println("Your rate submitted");
            }
        };
    }
}
