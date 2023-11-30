package Model.Product;

import java.util.Comparator;

public class ProductComparatorForVisitNumber implements Comparator<Product> {

    @Override
    public int compare(Product product1, Product product2) {
        int visitNumberDifference = product2.getVisitNumber() - product1.getVisitNumber();
        if (visitNumberDifference != 0){
            return visitNumberDifference;
        }
        return product1.getProductName().compareTo(product2.getProductName());
    }
}
