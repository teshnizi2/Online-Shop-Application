package Controller;

public enum FilteringType {
    CATEGORY_FILTER("Category Filter"), NAME_FILTER("Name Filter"),
    EXISTENCE_FILTER("Existence Filter"), MAXIMUM_PRICE_FILTER("Maximum price Filter"),
    MINIMUM_PRICE_FILTER("Minimum price Filter"), OFF_FILTER("Off Filter");

    private String filterType;

    FilteringType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterType() {
        return filterType;
    }
}
