package View;

import Controller.CustomerProfileManager;
import Controller.LoginAndRegisterManager;
import Controller.ProductsManager;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class Menu {
    private String name;
    protected ArrayList<Menu> submenus;
    protected Menu parentMenu;
    public static Scanner scanner;
    protected static ProductsManager productsManager;
    protected static LoginAndRegisterManager loginAndRegisterManager;
    protected static ArrayList<Menu> allMenus = new ArrayList<>();
    protected static Menu loginAndRegisterMenu;

    public Menu (String name, Menu parentMenu) {
        this.name = name;
        this.parentMenu = parentMenu;
        allMenus.add(this);
    }

    public static void setScanner(Scanner scanner) {
        Menu.scanner = scanner;
    }

    public static void setProductsManager(ProductsManager productsManager) {
        Menu.productsManager = productsManager;
    }

    public static void setLoginAndRegisterManager(LoginAndRegisterManager loginAndRegisterManager) {
        Menu.loginAndRegisterManager = loginAndRegisterManager;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public ArrayList<Menu> getSubmenus() {
        return submenus;
    }

    public String getName() {
        return name;
    }

    public void setSubMenus(ArrayList<Menu> submenus) {
        this.submenus = submenus;
    }

    public static void setLoginAndRegisterMenu(Menu loginAndRegisterMenu) {
        Menu.loginAndRegisterMenu = loginAndRegisterMenu;
    }

    public void show() {
        System.out.println(this.name + ":");
        for (int i = 0; i < submenus.size(); i++) {
            System.out.println(i + 1 + ". " + submenus.get(i).getName());
        }
        System.out.println();
        if (loginAndRegisterManager.isLogin()) {
            System.out.println(submenus.size() + 1 + ". Logout");
        }
        else {
            System.out.println(submenus.size() + 1 + ". Login");
        }
        if (this.parentMenu != null) {
            System.out.println((submenus.size() + 2) + ". Back");
        }
        else {
            System.out.println((submenus.size() + 2) + ". Exit");
        }
    }

    public void execute() {
        boolean flag = true;
        show();
        try {
            Menu nextMenu = null;
            int chosenMenu = Integer.parseInt(scanner.nextLine());
            if (submenus == null) {
                if (chosenMenu == 1) {
                    loginAndRegisterManager.logoutUser();
                    nextMenu = this;
                } else if (chosenMenu == 2) {
                    nextMenu = this.parentMenu;
                }
            } else if (chosenMenu == submenus.size() + 1){
                if (loginAndRegisterManager.isLogin()) {
                    loginAndRegisterManager.logoutUser();
                } else {
                    loginAndRegisterMenu.execute();
                }
                nextMenu = this;
            } else if (chosenMenu == submenus.size() + 2) {
                if (this.parentMenu == null) {
                    flag = false;
                } else {
                    nextMenu = this.parentMenu;
                }
            }
            else {
                nextMenu = submenus.get(chosenMenu - 1);
            }

            if (flag) {
                nextMenu.execute();
            }
        }
        catch (Exception e) {
            System.out.println("Wrong input\n");
            execute();
        }
    }

    public static String scan(Menu parentMenu) {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("back")) {
            parentMenu.execute();
        } else if (input.equalsIgnoreCase("logout")) {
            loginAndRegisterManager.logoutUser();
        }
        return input;
    }

    public static MyResult scanAdvance(int pageNumber, int startPage, Menu parentMenu) {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("back")) {
            if (pageNumber == startPage) {
                parentMenu.execute();
            }
            return new MyResult(true, "-1", "");
        } else if (input.equalsIgnoreCase("logout")) {
            loginAndRegisterManager.logoutUser();
        } else if (input.equalsIgnoreCase("next")) {
            return new MyResult(true, "1", "");
        }
        return new MyResult(false, input, "");
    }

    public static final class MyResult {
        private final boolean valid;
        private final String  message;
        private final String secondMessage;

        public MyResult(boolean valid, String message, String secondMessage) {
            this.valid = valid;
            this.message = message;
            this.secondMessage = secondMessage;
        }

        public boolean getValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        public String getSecondMessage() {
            return secondMessage;
        }
    }

}
