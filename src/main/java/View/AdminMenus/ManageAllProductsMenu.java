package View.AdminMenus;

import Controller.AdminProfileManager;
import View.Menu;

import java.util.ArrayList;

public class ManageAllProductsMenu extends Menu {
    private AdminProfileManager adminProfileManager;

    public ManageAllProductsMenu(Menu parentMenu, AdminProfileManager adminProfileManager) {
        super("Manage All Products Menu", parentMenu);
        this.adminProfileManager = adminProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getRemoveProductMenu());
        this.setSubMenus(submenus);
    }

    private Menu getRemoveProductMenu() {
        return new Menu("Remove Product", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter product id to remove product or (Back) to return:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        adminProfileManager.removeProduct(input);
                        System.out.println("Product removed successfully.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no product with this id.");
                    }
                    this.execute();
                }
            }
        };
    }
}
