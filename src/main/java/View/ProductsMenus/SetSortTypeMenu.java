package View.ProductsMenus;

import Controller.FilteringType;
import Controller.SortType;
import Model.Product.Category;
import View.Menu;

import java.util.ArrayList;

public class SetSortTypeMenu extends Menu {
    public SetSortTypeMenu(Menu parentMenu) {
        super("Set Sort Type Menu", parentMenu);
        ArrayList<Menu> subMenus = new ArrayList<>();
        subMenus.add(getSortByTimeMenu());
        subMenus.add(getSortByScore());
        subMenus.add(getSortByVisitNumber());
        subMenus.add(getSortByProductFeaturesMenu());
        this.setSubMenus(subMenus);
    }

    private Menu getSortByTimeMenu(){
        return new Menu(SortType.SORT_BY_TIME.getSortType(), this) {
            @Override
            public void execute() {
                productsManager.useSortByTime();
                parentMenu.execute();
            }
        };
    }

    private Menu getSortByScore(){
        return new Menu(SortType.SORT_BY_SCORE.getSortType(), this) {
            @Override
            public void execute() {
                productsManager.useSortByScore();
                parentMenu.execute();
            }
        };
    }

    private Menu getSortByVisitNumber() {
        return new Menu(SortType.SORT_BY_VISIT.getSortType(), this) {
            @Override
            public void execute() {
                productsManager.useSortByVisit();
                parentMenu.execute();
            }
        };
    }

    private Menu getSortByProductFeaturesMenu(){
        return new Menu("Sort By Product Special Features", this) {
            @Override
            public void execute() {
                if (productsManager.getCurrentFilters().contains(FilteringType.CATEGORY_FILTER)) {
                    Category category = productsManager.getCategoryFilter();
                    System.out.println("Features you can sort based on them:");
                    int i = 1;
                    for (String feature : category.getSpecialFeatures()) {
                        System.out.println(i++ + ": " + feature);
                    }
                    System.out.println("\nEnter number of feature: (Enter back to cancel)");
                    String num = scanner.nextLine().trim();
                    int number;
                    if (num.equalsIgnoreCase("back")){
                        parentMenu.execute();
                    }
                    else if (num.matches("\\d+") &&
                            (number = Integer.parseInt(num)) <= category.getSpecialFeatures().size() && number > 0){
                            productsManager.useSortBySpecialFeature((category.getSpecialFeatures().get(number - 1)));
                    }
                    else {
                        System.out.println("Wrong Input!\n");
                        execute();
                    }
                }
                else {
                    System.out.println("you must use category filter in order to use sorting based on special features");
                }
                parentMenu.execute();
            }
        };
    }
}
