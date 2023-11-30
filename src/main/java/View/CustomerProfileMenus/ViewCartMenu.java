package View.CustomerProfileMenus;

import Controller.CustomerProfileManager;
import Controller.ProductsManager;
import Model.Account.Customer;
import Model.Product.Product;
import View.Menu;
import View.ProductMenus.ProductMenu;


import java.util.ArrayList;
import java.util.HashMap;

public class ViewCartMenu extends Menu {
    private Customer customer;
    private CustomerProfileManager customerProfileManager;
    private CustomerProfileMenu customerProfileMenu;
    public ViewCartMenu(Customer customer, Menu parentMenu) {
        super("View Cart Menu", parentMenu);
        this.customer = customer;
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getShowProductsMenu());
        subMenus.add(new ProductMenu(this));
        subMenus.add(getIncreaseMenu());
        subMenus.add(getDecreaseMenu());
        subMenus.add(getShowTotalPriceMenu());
        subMenus.add(new PurchaseMenu(customer, parentMenu));
        this.setSubMenus(subMenus);
    }

    private Menu getShowProductsMenu() {
        return new Menu("Show Products Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                HashMap<Product, Integer> cart = productsManager.getCart();
                for (Product product : cart.keySet()) {
                    product.digest();
                    System.out.println("you have " + cart.get(product) + " of this product in your cart\n");
                }
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

    private Menu getIncreaseMenu() {
        return new Menu("Increase Product", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter the number you want to increase:");
            }
            @Override
            public void execute() {
                show();
                Product product = customerProfileMenu.getProduct(parentMenu);
                System.out.printf("You just have %d number of this product now!\n", customerProfileManager.getNumberOfProductInCart(product, customer));
                String increaseNumber = customerProfileMenu.getIncreaseProductInView(parentMenu, product);
                productsManager.addProductToCart(product, Integer.parseInt(increaseNumber));
                System.out.println("Product number increased successfully");
                parentMenu.execute();
            }
        };
    }

    private Menu getDecreaseMenu() {
        return new Menu("Decrease Product", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter the number you want to decrease:");
            }
            @Override
            public void execute() {
                show();
                Product product = customerProfileMenu.getProduct(parentMenu);
                System.out.printf("You just have %d number of this product now!\n", customerProfileManager.getNumberOfProductInCart(product, customer));
                String decreaseNumber = customerProfileMenu.getDecreaseProductInView(parentMenu, product);
                productsManager.addProductToCart(product, -Integer.parseInt(decreaseNumber));
                System.out.println("Product number decreased successfully");
                parentMenu.execute();
            }
        };
    }

    private Menu getShowTotalPriceMenu() {
        return new Menu("Show TotalPrice", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println("Total price of your order: " + ProductsManager.getTotalPrice() + "$");
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

}
