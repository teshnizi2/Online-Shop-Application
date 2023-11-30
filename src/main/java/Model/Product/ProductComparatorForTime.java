package Model.Product;

import java.util.Comparator;

public class ProductComparatorForTime implements Comparator<Product> {
    @Override
    public int compare(Product product1, Product product2) {
        int timeDifference = product1.getTimeOfCreation().compareTo(product2.getTimeOfCreation());
        if (timeDifference != 0){
            return timeDifference;
        }
        return product1.getProductName().compareTo(product2.getProductName());
    }
}
