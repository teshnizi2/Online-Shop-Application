package Model.Product;

import java.util.Comparator;

public class ProductComparatorForSpecialFeature implements Comparator<Product> {
    String feature;

    public ProductComparatorForSpecialFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public int compare(Product product1, Product product2) {
        try {
            int difference = product2.getValueOfAFeature(feature) - product1.getValueOfAFeature(feature);
            if (difference != 0){
                return difference;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return product1.getProductName().compareTo(product2.getProductName());
    }
}
