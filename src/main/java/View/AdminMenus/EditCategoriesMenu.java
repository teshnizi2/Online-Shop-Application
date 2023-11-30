package View.AdminMenus;

import Controller.AdminProfileManager;
import Model.Product.Category;
import View.Menu;

import java.util.ArrayList;

public class EditCategoriesMenu extends Menu {
    private AdminProfileManager adminProfileManager;
    private Category category;

    public EditCategoriesMenu(Menu parentMenu, AdminProfileManager adminProfileManager) {
        super("Edit Category Menu", parentMenu);
        this.adminProfileManager = adminProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getEditCategoryNameMenu());
        submenus.add(getEditCategorySpecialFeaturesMenu());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        System.out.println("Enter the name of the category you want to edit, (back) to return or (logout) to log out:");
        String categoryName = scanner.nextLine();
        if (AdminProfileManager.isCategoryWithThisName(categoryName)) {
            this.category = Category.getCategoryByName(categoryName);
            super.show();
        }
        else if (categoryName.equalsIgnoreCase("back")) {
            parentMenu.execute();
        }
        else if (categoryName.equalsIgnoreCase("logout")) {
            //todo: add logout to this
        }
        else {
            System.out.println("There is no Category with this name.");
            this.show();
        }
    }

    private Menu getEditCategoryNameMenu() {
        return new Menu("Edit Category Name Menu", this) {
            @Override
            public void execute() {
                System.out.println("Enter new Name:");
                String newCategoryName = scanner.nextLine();
                try {
                    adminProfileManager.editCategoryName(category, newCategoryName);
                    System.out.println("Category name successfully changed to " + newCategoryName);
                }
                catch (IllegalArgumentException e) {
                    System.out.println("There is another category with this name.");
                }
                this.parentMenu.execute();
            }
        };
    }

    //todo: edit subCategory

    private Menu getEditCategorySpecialFeaturesMenu() {
        return new Menu("Edit Category Special Features Menu", this) {
            @Override
            public void execute() {
                System.out.println("Select one of these options or enter (back) to return:");
                System.out.println("1. Remove Special Feature");
                System.out.println("2. Add Special Feature");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else if (input.equals("1")) {
                    System.out.println("Enter the special Feature you want to remove:");
                    String specialFeature = scanner.nextLine();
                    try {
                        adminProfileManager.removeCategorySpecialFeature(category, specialFeature);
                        System.out.println("Special Feature " + specialFeature + " successfully removed.");
                    }
                    catch (NullPointerException e) {
                        System.out.println("This category doesn't have this special feature.");
                    }
                }
                else if (input.equals("2")) {
                    System.out.println("Enter special feature you want to add:");
                    String specialFeature = scanner.nextLine();
                    try {
                        adminProfileManager.addSpecialFeature(category, specialFeature);
                        System.out.println("Special Feature " + specialFeature + " successfully added.");
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("This special feature is already in this category.");
                    }
                }
                else {
                    System.out.println("You must enter either 1, 2 or back.");
                }
                this.execute();
            }
        };
    }
}
