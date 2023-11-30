package Controller;

public enum SortType {
    SORT_BY_TIME("Sort By Time"), SORT_BY_SCORE("Sort By Score"), SORT_BY_VISIT("Sort By Visit Number"),
    SORT_BY_SPECIAL_FEATURE("sort by special feature"), SORT_BY_HIGHEST_PRICE("sort by highest price"), SORT_BY_LOWEST_PRICE("sort by lowest price");
    private String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }
}
