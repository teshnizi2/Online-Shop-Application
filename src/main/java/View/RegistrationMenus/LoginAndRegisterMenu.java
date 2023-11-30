package View.RegistrationMenus;

import View.Menu;

import java.util.ArrayList;

public class LoginAndRegisterMenu extends Menu {
    public LoginAndRegisterMenu(Menu parentMenu) {
        super("Login Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(new RegistrationMenu(this));
        subMenus.add(getLoginMenu());
        this.setSubMenus(subMenus);
    }

    @Override
    public void show() {
        System.out.println("Login Menu :");
        for (int i = 0; i < submenus.size(); i++) {
            System.out.println(i + 1 + ". " + submenus.get(i).getName());
        }
        System.out.println();
        System.out.println((submenus.size() + 1) + ". Back");
    }

    @Override
    public void execute() {
        boolean flag = true;
        show();
        try {
            Menu nextMenu = null;
            int chosenMenu = Integer.parseInt(scanner.nextLine());
            if (chosenMenu == submenus.size() + 1) {
                    flag = false;
            } else {
                nextMenu = submenus.get(chosenMenu - 1);
            }
            if (flag) {
                nextMenu.execute();
            }
        }
        catch (Exception e){
            System.out.println("Wrong input\n");
            execute();
        }
    }

    private Menu getLoginMenu(){
        return new Menu("Login", this) {
            @Override
            public void execute() {
                System.out.println("Enter your username:");
                String userName = scanner.nextLine().trim();
                System.out.println("Enter your password:");
                String password = scanner.nextLine().trim();
                try {
                    loginAndRegisterManager.loginUser(userName, password);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
    }

}
