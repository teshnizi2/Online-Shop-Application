package View.ProductsMenus;

import Controller.FilteringType;
import View.Menu;

import java.util.ArrayList;

public class SetFilteringTypeMenu extends Menu {
    public SetFilteringTypeMenu(Menu parentMenu) {
        super("Set Filtering Type Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getCategoryFilterMenu());
        subMenus.add(getNameFilteringMenu());
        subMenus.add(getMaximumPriceFilteringMenu());
        subMenus.add(getMinimumPriceFilteringMenu());
        subMenus.add(getExistenceFilteringMenu());
        this.setSubMenus(subMenus);
    }

    private Menu getCategoryFilterMenu(){
        return new Menu(FilteringType.CATEGORY_FILTER.getFilterType(), this) {
            @Override
            public void execute() {
                System.out.println("Categories:");
                productsManager.showAllCategories();
                System.out.println("\nEnter category name.");
                String categoryName = scanner.nextLine().trim();
                try {
                    productsManager.addCategoryFilter(categoryName);
                }
                catch (IllegalArgumentException e){
                    System.out.println("there is no such a category!");
                }
                finally {
                    parentMenu.execute();
                }
            }
        };
    }

    private Menu getNameFilteringMenu() {
        return new Menu(FilteringType.NAME_FILTER.getFilterType(), this) {
            @Override
            public void execute() {
                productsManager.addNameFiltering(scanner.nextLine().trim());
                parentMenu.execute();
            }
        };
    }

    private Menu getMaximumPriceFilteringMenu(){
        return new Menu(FilteringType.MAXIMUM_PRICE_FILTER.getFilterType(), this) {
            @Override
            public void execute() {
                System.out.println("Enter maximum price:");
                String max = scanner.nextLine().trim();
                int maxPrice;
                if (max.matches("\\d+") && (maxPrice = Integer.parseInt(max)) > 0){
                    productsManager.addMaximumPriceFilter(maxPrice);
                }
                else {
                    System.out.println("Wrong Input");
                }
                parentMenu.execute();
            }
        };
    }

    private Menu getMinimumPriceFilteringMenu(){
        return new Menu(FilteringType.MINIMUM_PRICE_FILTER.getFilterType(), this) {
            @Override
            public void execute() {
                System.out.println("Enter minimum price:");
                String min = scanner.nextLine().trim();
                int minPrice;
                if (min.matches("\\d+") && (minPrice = Integer.parseInt(min)) > 0){
                    productsManager.addMinimumPriceFilter(minPrice);
                }
                else {
                    System.out.println("Wrong Input");
                }
                parentMenu.execute();
            }
        };
    }

    private Menu getExistenceFilteringMenu(){
        return new Menu(FilteringType.EXISTENCE_FILTER.getFilterType(), this) {
            @Override
            public void execute() {
                System.out.println("1. only existing files");
                System.out.println("2. all of files");
                String command = scanner.nextLine().trim();
                if (command.equals("1") || command.equalsIgnoreCase("only existing files")){
                    productsManager.addExistenceFilter(true);
                }
                else if (command.equalsIgnoreCase("all of files") || command.equals("2")){
                    productsManager.addExistenceFilter(false);
                }
                else {
                    System.out.println("wrong input!");
                }
                parentMenu.execute();
            }
        };
    }
}
