package View.CustomerProfileMenus;

import Controller.CustomerProfileManager;
import Controller.ProductsManager;
import Model.Account.Customer;
import Model.Product.Product;
import View.Menu;
import View.ViewPersonalInfoMenu;

import java.util.ArrayList;

public class CustomerProfileMenu extends Menu {
    private Customer customer;
    private CustomerProfileManager customerProfileManager;

    public CustomerProfileMenu(Customer customer, Menu parentMenu) {
        super("CustomerProfile Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        this.customer = customer;
        this.customerProfileManager = new CustomerProfileManager(customer);
        subMenus.add(new ViewPersonalInfoMenu(this, customerProfileManager));
        subMenus.add(new ViewCartMenu(customer,this));
        subMenus.add(new ViewOrdersMenu(customer,this));
        subMenus.add(getViewBalanceMenu());
        subMenus.add(getViewDiscountCodesMenu());
        this.setSubMenus(subMenus);
    }

    private Menu getViewBalanceMenu() {
        return new Menu("View Balance", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(customerProfileManager.viewBalance());
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

    private Menu getViewDiscountCodesMenu() {
        return new Menu("Discount Codes" , this) {
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(customerProfileManager.getDiscountCodes());
                System.out.println("1. Logout");
                System.out.println("2. Back");
            }
        };
    }

    public String getProductIdInView(Menu parentMenu) {
        while(true) {
            while (true) {
                String input = scan(parentMenu);
                if (!isNumericAndPositive(input)) {
                    break;
                }
                if (!ProductsManager.isValidIDForProductID(input)) {
                    System.out.println("There is no product with this ID");
                    break;
                }
                return input;
            }
        }

    }

    public String getProductRateInView(Menu parentMenu) {
        while(true) {
            while(true) {
                String input = scan(parentMenu);
                if (!isNumericAndPositive(input)) {
                    break;
                }
                if (Integer.parseInt(input) > 5 && Integer.parseInt(input) < 1) {
                    System.out.println("please enter the number in range(1-5):");
                    break;
                }
                return input;
            }
        }
    }

    public String getBuyLogInView(Menu parentMenu) {
        while(true) {
            while(true) {
                String input = scan(parentMenu);
                if (!isNumericAndPositive(input)) {
                    break;
                }
                if (!customerProfileManager.isInputValidForBuyLogID(input)) {
                    System.out.println("There is no order with this ID");
                    break;
                }
                return input;
            }
        }
    }

    public String getIncreaseProductInView(Menu parentMenu, Product product) {
        while(true) {
            while(true) {
                String input = scan(parentMenu);
                if (!isNumericAndPositive(input)) {
                    break;
                }
                int existingNumberInStore = CustomerProfileManager.getExistingNumberOfProductInStore(product, Integer.parseInt(input));
                if (existingNumberInStore < Integer.parseInt(input)) {
                    System.out.printf("we have just %d of this Product. Please enter another number:\n", existingNumberInStore);
                    break;
                }
                return input;
            }
        }
    }

    public String getDecreaseProductInView(Menu parentMenu, Product product) {
        while(true) {
            while(true) {
                String input = scan(parentMenu);
                if (!isNumericAndPositive(input)) {
                    break;
                }
                return input;
            }
        }
    }

    public static boolean isNumericAndPositive(String input) {
        if (!input.matches("\\A\\d+\\z")) {
            System.out.println("please enter valid number:");
            return false;
        }
        return true;
    }

    public Product getProduct(Menu parentMenu) {
        while(true) {
            while (true) {
                System.out.println("Enter product Id");
                Product product = productsManager.getProductByID(scan(parentMenu));
                if (product == null) {
                    System.out.println("There is no product with this Id");
                    break;
                }
                if (!productsManager.hasProductInCart(product)) {
                    System.out.println("You don't have this Product in your cart");
                    break;
                }
                return product;
            }
        }

    }


}
