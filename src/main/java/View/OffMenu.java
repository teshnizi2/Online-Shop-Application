package View;

import Model.Account.Off;
import Model.Product.Product;
import View.CustomerProfileMenus.ViewCartMenu;
import View.ProductMenus.ProductMenu;
import View.ProductsMenus.ProductsMenu;

import java.util.ArrayList;

public class OffMenu extends Menu{

    public OffMenu(Menu parentMenu) {
        super("Off Menu", parentMenu);
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(new ProductMenu(this));
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        ArrayList<String> allOffs = Off.getAllOffsStatus();
        for (String off : allOffs) {
            System.out.println(off);
        }
        System.out.println();
        super.show();
    }

    @Override
    public void execute() {
        show();
        ProductsMenu.goToProductMenu(this, parentMenu, submenus, 0);
    }
}
