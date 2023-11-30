package View.ProductsMenus;

import Controller.FilteringType;
import View.Menu;

import java.util.ArrayList;

public class FilteringMenu extends Menu {
    public FilteringMenu(Menu parentMenu) {
        super("Filtering Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(SortingMenu.getShowAvailableSortsOrFiltersMenu("Show Available Filters", this));
        subMenus.add(new SetFilteringTypeMenu(this));
        subMenus.add(getCurrentFiltersMenu());
        subMenus.add(new DisablingFiltersMenu(this));
        this.setSubMenus(subMenus);
    }

    private Menu getCurrentFiltersMenu(){
        return new Menu("CurrentFilters", this) {
            @Override
            public void execute() {
                ArrayList<FilteringType> currentFilters = productsManager.getCurrentFilters();
                System.out.println("Active Filters:");
                for (FilteringType filter : currentFilters) {
                    System.out.println("\t" + filter.getFilterType());
                }
                System.out.println();
                parentMenu.execute();
            }
        };
    }
}
