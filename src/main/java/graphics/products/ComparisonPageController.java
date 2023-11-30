package graphics.products;

import Model.Product.Product;
import graphics.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ComparisonPageController {

    public AnchorPane firstProductPane;
    public AnchorPane secondProductPane;

    private Product product1;
    private Product product2;
    private StackPane firstProductImageStackPane;
    private StackPane secondProductImageStackPane;
    private Parent parent;

    public void initialize(){

    }

    public void setProduct1(Product product1){
        this.product1 = product1;
    }

    public void setProduct2(Product product2) {
        this.product2 = product2;
    }

    public void setFirstProductImageStackPane(StackPane firstProductImageStackPane) {
        this.firstProductImageStackPane = firstProductImageStackPane;
    }

    public void setSecondProductImageStackPane(StackPane secondProductImageStackPane) {
        this.secondProductImageStackPane = secondProductImageStackPane;
    }

    public void setEveryThing(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("productPage.fxml"));
            firstProductPane.getChildren().add(fxmlLoader.load());
            ProductPageController productPageController = (ProductPageController) fxmlLoader.getController();
            ProductPageController.setParentAddress("productsMenu");
            productPageController.setProduct(product1);
            productPageController.getImageStackPane().getChildren().addAll(firstProductImageStackPane.getChildren());
            productPageController.setEveryThing();

            FXMLLoader fxmlLoader2 = new FXMLLoader(App.class.getResource("productPage.fxml"));
            secondProductPane.getChildren().add(fxmlLoader2.load());
            ProductPageController productPageController2 = (ProductPageController) fxmlLoader2.getController();
            ProductPageController.setParentAddress("productsMenu");
            productPageController2.setProduct(product2);
            productPageController2.getImageStackPane().getChildren().addAll(secondProductImageStackPane.getChildren());
            productPageController2.setEveryThing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
