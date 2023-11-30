package View.AdminMenus;

import Controller.AdminProfileManager;
import Model.Account.Admin;
import View.Menu;
import View.ViewPersonalInfoMenu;

import java.util.ArrayList;

public class AdminProfileMenu extends Menu {
    protected Admin admin;
    private AdminProfileManager adminProfileManager;

    public AdminProfileMenu(Menu parentMenu, Admin admin) {
        super("Admin Profile Menu", parentMenu);
        this.admin = admin;
        this.adminProfileManager = new AdminProfileManager(admin);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(new ViewPersonalInfoMenu(this, adminProfileManager));
        subMenus.add(new ManageUsersMenu(this, adminProfileManager));
        subMenus.add(new ManageAllProductsMenu(this, adminProfileManager));
        subMenus.add(new ManageDiscountCodesMenu(this, adminProfileManager));
        subMenus.add(new ManageRequestsMenu(this, adminProfileManager));
        subMenus.add(new ManageCategoriesMenu(this, adminProfileManager));
        this.setSubMenus(subMenus);
    }
}
