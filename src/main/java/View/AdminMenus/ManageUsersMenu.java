package View.AdminMenus;

import Controller.AdminProfileManager;
import Model.Account.Account;
import View.Menu;

import java.util.ArrayList;

public class ManageUsersMenu extends Menu {
    private AdminProfileManager adminProfileManager;

    public ManageUsersMenu(Menu parentMenu, AdminProfileManager adminProfileManager) {
        super("Manage Users Menu", parentMenu);
        this.adminProfileManager = adminProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getViewUserMenu());
        submenus.add(getDeleteUserMenu());
        submenus.add(getCreateManagerProfileMenu());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        ArrayList<Account> allUsers = adminProfileManager.getAllUsers();
        for (Account user : allUsers) {
            System.out.println(user.getUsername());
        }
        super.show();
    }

    public Menu getViewUserMenu() {
        return new Menu("View User", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter username or (Back) to return:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        String userInfo = adminProfileManager.viewUser(input);
                        System.out.println(userInfo);
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no account with this username.");
                    }
                    this.execute();
                }
            }
        };
    }

    public Menu getDeleteUserMenu() {
        return new Menu("Delete User", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter Username or (Back) to return:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        adminProfileManager.deleteUser(input);
                        System.out.println("User deleted successfully.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no account with this username.");
                    }
                    this.execute();
                }
            }
        };
    }

//todo: adding exceptions to this menu
    public Menu getCreateManagerProfileMenu() {
        return new Menu("Create Manager Profile", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter Following data to create manager profile:");
                System.out.println("Enter username:");
                String username = scanner.nextLine();
                System.out.println("Enter password:");
                String password = scanner.nextLine();
                System.out.println("Enter name:");
                String name = scanner.nextLine();
                System.out.println("Enter lastName:");
                String lastName = scanner.nextLine();
                System.out.println("Enter email:");
                String email = scanner.nextLine();
                System.out.println("Enter phone number:");
                String phoneNumber = scanner.nextLine();
                adminProfileManager.registerAdmin(username, password, name, lastName, email, phoneNumber);
                System.out.println("Enter (Back) to return or (Create Manager Profile) to create another manager profile:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    this.execute();
                }
            }
        };
    }

}
