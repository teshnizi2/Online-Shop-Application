package View;

import Controller.ProfileManager;

import java.util.ArrayList;

public class ViewPersonalInfoMenu extends Menu {
    private ProfileManager profileManager;

    public ViewPersonalInfoMenu(Menu parentMenu, ProfileManager profileManager) {
        super("View Personal Info Menu", parentMenu);
        this.profileManager = profileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getEditFieldMenu());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        String personalInfo = profileManager.viewPersonalInfo();
        System.out.println(personalInfo);
        super.show();
    }

    //todo: editing other fields like companyName, other than just normal account fields?
    private Menu getEditFieldMenu() {
        return new Menu("Edit Field", this) {
            @Override
            public void execute() {
                System.out.println(this.getName() + ": ");
                System.out.println("Enter field you want to edit or (Back) to return");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                }
                else {
                    System.out.println("Enter the change you want to make:");
                    String fieldChange = scanner.nextLine();
                    String fieldName = input;
                    try {
                        profileManager.editFieldOfProfile(fieldName, fieldChange);
                        System.out.println(fieldName + " successfully changed to " + fieldChange);
                    }
                    catch (IllegalAccessException e) {
                        System.out.println("This field cannot be changed.");
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("Wrong field name.");
                    }
                    this.execute();
                }
            }
        };
    }
}
