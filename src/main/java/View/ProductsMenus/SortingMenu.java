package View.ProductsMenus;

import View.Menu;

import java.util.ArrayList;

public class SortingMenu extends Menu {
    public SortingMenu(Menu parentMenu) {
        super("Sorting Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getShowAvailableSortsOrFiltersMenu("Show Available Sorts", this));
        subMenus.add(new SetSortTypeMenu(this));
        subMenus.add(getCurrentSortMenu());
        subMenus.add(getDisableSortMenu());
        this.setSubMenus(subMenus);
    }

    public static Menu getShowAvailableSortsOrFiltersMenu(String name, Menu parentMenu){
        return new Menu(name, parentMenu) {
            @Override
            public void execute() {
                int i = 1;
                for (Menu menu : parentMenu.getSubmenus().get(1).getSubmenus()) {
                    System.out.println(i++ + ": " + menu.getName());
                }
                System.out.println();
                parentMenu.execute();
            }
        };
    }

    private Menu getCurrentSortMenu (){
        return new Menu("Current Sort", this) {
            @Override
            public void execute() {
                System.out.println(productsManager.getCurrentSort());
                System.out.println();
                parentMenu.execute();
            }
        };
    }

    private Menu getDisableSortMenu(){
        return new Menu("Disable Current Sort", this) {
            @Override
            public void execute() {
                productsManager.useSortByVisit();
                parentMenu.execute();
            }
        };
    }
}
