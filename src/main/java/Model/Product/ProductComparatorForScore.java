package Model.Product;

import java.util.Comparator;

public class ProductComparatorForScore implements Comparator<Product> {
    @Override
    public int compare(Product product1, Product product2) {
        double scoreDifference = product2.getTotalScore() - product1.getTotalScore();
        if (scoreDifference != 0){
            if (scoreDifference > 0){
                return 1;
            }
            if (scoreDifference < 0){
                return -1;
            }
        }
        return product1.getProductName().compareTo(product2.getProductName());
    }
}
