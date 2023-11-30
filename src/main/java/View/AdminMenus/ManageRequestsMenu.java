package View.AdminMenus;

import Controller.AdminProfileManager;
import View.Menu;

import java.util.ArrayList;

public class ManageRequestsMenu extends Menu {
    private AdminProfileManager adminProfileManager;

    public ManageRequestsMenu(Menu parentMenu, AdminProfileManager adminProfileManager) {
        super("Manage Requests Menu", parentMenu);
        this.adminProfileManager = adminProfileManager;
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getDetailsOfRequestMenu());
        subMenus.add(getAcceptRequestMenu());
        subMenus.add(getDeclineRequestMenu());
        this.setSubMenus(subMenus);
    }

    @Override
    public void show() {
        ArrayList<String> allRequests = adminProfileManager.getAllRequests();
        for (String request : allRequests) {
            System.out.println("Request ID: " + request);
        }
        super.show();
    }

    private Menu getDetailsOfRequestMenu() {
        return new Menu("Details of request", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter requestId to show details or (back) to return:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        String requestDetails = adminProfileManager.getDetailsOfRequest(input);
                        System.out.println(requestDetails);
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no request with this id.");
                    }
                    this.execute();
                }
            }
        };
    }

    private Menu getAcceptRequestMenu() {
        return new Menu("Accept Request", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter requestId to accept or (back) to return:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        adminProfileManager.acceptRequest(input);
                        System.out.println("Request accepted.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no request with this id.");
                    }
                    catch (IllegalArgumentException i) {
                        System.out.println("Request cannot be accepted because there is another Object with this id.");
                    }
                    this.execute();
                }
            }
        };
    }

    private Menu getDeclineRequestMenu() {
        return new Menu("Decline Request", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ":");
                System.out.println("Enter requestId to decline or (back) to return:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        adminProfileManager.declineRequest(input);
                        System.out.println("Request declined successfully.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("There is no request with this id.");
                    }
                    this.execute();
                }
            }
        };
    }

}
