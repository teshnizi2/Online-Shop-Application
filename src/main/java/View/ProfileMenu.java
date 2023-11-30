package View;

import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.Customer;
import Model.Account.Seller;
import View.AdminMenus.AdminProfileMenu;
import View.CustomerProfileMenus.CustomerProfileMenu;
import View.SellerProfileMenus.SellerProfileMenu;

import java.util.ArrayList;

public class ProfileMenu extends Menu {

    public ProfileMenu(Menu parentMenu) {
        super("Profile Menu", parentMenu);
        submenus = new ArrayList<>();
    }

    @Override
    public void execute() {
        Account account = Account.getLoggedInAccount();
        if (account == null) {
            System.out.println("You must login first.");
            loginAndRegisterMenu.execute();
            parentMenu.execute();
        }
        else if (account instanceof Admin) {
            AdminProfileMenu adminProfileMenu = new AdminProfileMenu(parentMenu, (Admin) account);
            adminProfileMenu.execute();
        }
        else if (account instanceof Customer) {
            CustomerProfileMenu customerProfileMenu = new CustomerProfileMenu((Customer) account, parentMenu);
            customerProfileMenu.execute();
        }
        else if (account instanceof Seller) {
            SellerProfileMenu sellerProfileMenu = new SellerProfileMenu((Seller) account, parentMenu);
            sellerProfileMenu.execute();
        }
    }
}
