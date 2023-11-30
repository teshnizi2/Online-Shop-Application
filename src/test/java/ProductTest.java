import Controller.ProductsManager;
import Model.Account.Seller;
import Model.Product.Product;
import Model.Product.ProductStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProductTest {

    @Test
    public void productSortingTest(){
        Seller seller = new Seller("he", "hehe", "he", "she", "he@she.me", "7272", "apple", 10000);
        ProductsManager productsManager = new ProductsManager();
        /*Product product1 = new Product("0", ProductStatus.CONFIRMED, "phone", "samsung", 1000, seller, 10, null, null);*/
        Product product1 = new Product("",  ProductStatus.CONFIRMED, "mobile", "samsung", 900, 10, "good product", null, null, null);
        product1.setVisitNumber(10);
        Product product2 = new Product("",  ProductStatus.CONFIRMED, "mobile", "samsung", 900, 10, "good product", null, null, null);
        product2.setVisitNumber(5);
        product2.addRate(null, 5);
        productsManager.useSortByHighestPrice();
        assertEquals(product1, productsManager.showProducts().get(0));
        productsManager.useSortByLowestPrice();
        assertEquals(product2, productsManager.showProducts().get(0));
        productsManager.useSortByVisit();
        assertEquals(product1, productsManager.showProducts().get(0));
        productsManager.useSortByScore();
        assertEquals(product2, productsManager.showProducts().get(0));
    }
}
