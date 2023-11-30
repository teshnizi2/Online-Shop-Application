package graphics.products;

import Client.Connection;
import Model.Account.Account;
import Model.Account.Customer;
import Model.Product.Product;
import graphics.AlertBox;
import graphics.App;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CartController {

    public Pane mainPane;
    public Button purchaseButton;
    public Button clearButton;
    public Label messageLabel;
    public Label totalLabel;
    public Label totalAmountLabel;

    private HashMap<Product, Integer> cart;

    public void initialize(){
        Account account = Account.getLoggedInAccount();
        if (account == null){
            cart = Customer.getTmpCart();
        }
        else if (account instanceof Customer){
            Customer customer = (Customer) account; //todo: do we need to send request?
            cart = customer.getCart();
        }
        if (cart == null || cart.isEmpty()){
            setEmptyLabel();
            totalLabel.setOpacity(0);
            totalAmountLabel.setOpacity(0);
        }
        else {
            fillCart();
            clearButton.setOnAction(e -> {
                cart.clear();
                mainPane.getChildren().clear();
                initialize();
            });
            purchaseButton.setOnAction(e -> {
                Stage thisStage = (Stage) mainPane.getScene().getWindow();
                if (Account.getLoggedInAccount() != null && Account.getLoggedInAccount() instanceof Customer) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("purchaseMenu.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        ((PurchaseMenuController) fxmlLoader.getController()).setCart(cart);
                        thisStage.setScene(scene);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loginMenu.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        thisStage.setScene(scene);
                        thisStage.setTitle("login");
                        thisStage.setResizable(false);
                        AlertBox.showMessage("login error", "you must login as a customer in order to purchase");
                        ProductPageController.cartPopUp = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                thisStage.setOnCloseRequest(windowEvent -> {
                    thisStage.close();
                    ProductPageController.cartPopUp = null;
                });
            });
        }
    }

    private void setEmptyLabel(){
        Label label = new Label("Your Cart Is Empty!");
        label.setWrapText(true);
        label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 30");
        mainPane.getChildren().add(label);
        label.setLayoutX(mainPane.getPrefWidth() / 3);
        label.setLayoutY(50);
        clearButton.setDisable(true);
        clearButton.setOpacity(0.5);
        purchaseButton.setDisable(true);
        purchaseButton.setOpacity(0.5);
    }

    private void fillCart(){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label label1, label2, label3, label4, label5, label6;
        gridPane.add((label1 = new Label("serial")), 0, 0);
        gridPane.add(label2 = new Label("name"), 1, 0);
        gridPane.add(label3 = new Label("Price"),2, 0);
        gridPane.add(label4 = new Label("quantity"), 3, 0);
        gridPane.add(label5 = new Label("amount"),4, 0);
        gridPane.add(label6 = new Label("options"), 5, 0);
        Stream.of(label1, label2, label3, label4, label5, label6).forEach(label ->
                label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 25; -fx-font-weight: bold"));
        AtomicInteger row = new AtomicInteger(1);
        cart.forEach(((product, number) -> setProductRow(product, number, gridPane, row.getAndIncrement())));
        mainPane.getChildren().add(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        totalLabel.setOpacity(1);
        totalAmountLabel.setOpacity(1);
        totalAmountLabel.setText(cart.entrySet().stream().mapToDouble(entry -> entry.getKey().getPriceWithOff() * entry.getValue()).sum() + "$");
    }

    private void setProductRow(Product product, int number, GridPane gridPane, int row){
        Label serial, name, price, quantity, amount;
        serial = new Label(String.valueOf(row));
        name = new Label(product.getProductName());
        name.setOnMouseClicked(event -> {
            ProductsController.openProduct(product, new StackPane(new ImageView(product.getImage())));
            ProductPageController.cartPopUp.close();
            ProductPageController.cartPopUp = null;
        });
        price = new Label(String.valueOf(product.getPriceWithOff()));
        quantity = new Label(String.valueOf(number));
        amount = new Label(String.valueOf(product.getPriceWithOff() * number));
        Stream.of(serial, name, price, quantity, amount).forEach(label -> {
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 20");
        });
        gridPane.add(serial, 0, row);
        gridPane.add(name, 1, row);
        gridPane.add(price, 2, row);
        gridPane.add(quantity, 3, row);
        gridPane.add(amount, 4, row);
        ImageView increaseImage = new ImageView(new Image("file:src\\main\\resources\\Images\\plus.png"));
        ImageView decreaseImage = new ImageView(new Image("file:src\\main\\resources\\Images\\minus.png"));
        ImageView deleteImage = new ImageView(new Image("file:src\\main\\resources\\Images\\delete.png"));
        Stream.of(increaseImage, decreaseImage, deleteImage).forEach(imageView -> {
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(30);
        });
        ProductPageController.shadowOnMouseHover(increaseImage);
        ProductPageController.shadowOnMouseHover(decreaseImage);
        ProductPageController.shadowOnMouseHover(deleteImage);
        increaseImage.setOnMouseClicked(e -> {
            if (product .getExistingNumber() > number) {
                Connection.sendToServerWithToken("add to cart: " + product.getProductId() + " 1");
                if (!product.isFile()) {
                    cart.put(product, number + 1);
                }
                gridPane.getChildren().clear();
                mainPane.getChildren().clear();
                fillCart();
            }
            else {
                messageLabel.setText("There is no more of this product currently");
                messageLabel.setOpacity(1);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), actionEvent -> messageLabel.setOpacity(0)));
                timeline.setCycleCount(1);
                timeline.play();
            }
        });
        decreaseImage.setOnMouseClicked(e -> {
            Connection.sendToServerWithToken("add to cart: " + product.getProductId() + " -1");
            if (number > 1) {
                cart.put(product, number - 1);
            }
            else {
                cart.remove(product);
            }
            gridPane.getChildren().clear();
            mainPane.getChildren().clear();
            fillCart();
        });
        deleteImage.setOnMouseClicked(e -> {
            Connection.sendToServerWithToken("add to cart: " + product.getProductId() + -cart.get(product));
            cart.remove(product);
            gridPane.getChildren().clear();
            mainPane.getChildren().clear();
            fillCart();
        });
        Pane optionsPane = new HBox(increaseImage, decreaseImage, deleteImage);
        gridPane.add(optionsPane, 5, row);
    }
}
