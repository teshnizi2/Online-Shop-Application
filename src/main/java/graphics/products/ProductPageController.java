package graphics.products;

import Client.ChatClient;
import Client.Connection;
import Controller.ProductsManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.Customer;
import Model.Product.Comment;
import Model.Product.Product;
import Model.Product.Score;
import Server.ChatServer;
import com.google.gson.Gson;
import graphics.App;
import graphics.LoginAndRegister.LoginMenu;
import graphics.MainMenu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.util.*;

public class ProductPageController {

    public TextField searchField;
    public ImageView magnifier;
    public Label nameLabel;
    public Label categoryLabel;
    public Label propertiesLabel;
    public StackPane imageStackPane;
    public Label sellerNameLabel;
    public Label finalPriceLabel;
    public Text previousPriceLabel;
    public Label offPercentageLabel;
    public Pane addToCartButton;
    public Pane propertiesPane;
    public Pane commentsPane;
    public AnchorPane ratePane;
    public ProgressBar progressBar5;
    public Label scoreLabel;
    public ProgressBar progressBar4;
    public ProgressBar progressBar3;
    public ProgressBar progressBar2;
    public ProgressBar progressBar1;
    public Label numberOfScoresLabel;
    public Label noteForRateLabel;
    public Pane soldOutPane;
    public Label ratesLabel;
    public Label propertiesTitleLabel;
    public Pane firstPane;
    public Pane rateBox;
    public Label numberOfScores5;
    public Label numberOfScores4;
    public Label numberOfScores3;
    public Label numberOfScores2;
    public Label numberOfScores1;
    public Pane imagePane;
    public Label productAddedLabel;
    public Label timeLeftLabel;
    public Label timeLabel;
    public Label calculatedLeftTime;
    public Label numberOfCommentsLabel;
    public Button LeaveCommentButton;
    public Label explanationsLabel;
    public Label companyNameLabel;
    public Label visitNumberLabel;
    public Label helpUsLabel;
    public ImageView backImage;
    public ImageView cartImage;
    public ImageView loginImage;
    public Button deleteProductButton;
    public Label remainingLabel;
    public ImageView mainMenuButton;
    public ImageView profileImage;
    public ImageView chatImage;

    public TextField titleTextField;
    public TextArea commentTextArea;
    public Label XLabel;
    public Label commentNoteLabel;

    private Product product;
    private ArrayList<Pane> showingComments = new ArrayList<>();
    private boolean hasRated;
    private Stage commentPopUp;
    public static Stage cartPopUp;
    public static Stage loginPopUp;
    private ProductPageController parentForCommentPage;
    private static String parentAddress;
    public static ProductsManager productsManager;

    public void initialize(){
    }

    public void setEveryThing(){
        if (Account.getLoggedInAccount() instanceof Admin){
            deleteProductButton.setOpacity(1);
            deleteProductButton.setOnAction(e -> {
                productsManager.deleteAProduct(product); //todo: send delete request
                //Connection.sendToServer("delete product: " + product.getProductId());
                try {
                    App.setRoot(parentAddress);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
        else {
            deleteProductButton.setOpacity(0);
            deleteProductButton.setDisable(false);
        }
        //product.setVisitNumber(product.getVisitNumber() + 1); //todo: send increase request
        Connection.sendToServer("visit product: " + product.getProductId());
        setLabels();
        setRates();
        resizeImage();
        setPrice();

        setAddToCartButton();
        setComments();
        setExplanations();
        setPropertiesPane();

        setOffLeftTimeLabel();
        App.setBackButton(backImage, parentAddress);
        setCartButton(cartImage);
        setLoginButton(loginImage, "productPage");
        setMainMenuButton(mainMenuButton);
        setProfileButton(profileImage);
        setChatButton(chatImage);
    }

    private void setChatButton(ImageView chatImage) {
        Connection.sendToServerWithToken("can go to auction chat: " + product.getProductId());
        if (Objects.equals(Connection.receiveFromServer(), "yes")) {
            chatImage.setOpacity(1);
            shadowOnMouseHover(chatImage);
            chatImage.setOnMouseClicked(event -> {
                try {
                    ChatClient.main(5043);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            chatImage.setOpacity(0);
        }
    }

    public static void setProfileButton(ImageView profileImage){
        shadowOnMouseHover(profileImage);
        profileImage.setOnMouseClicked(e -> MainMenu.openProfile());
    }

    public static void setMainMenuButton(ImageView mainMenuImage){
        shadowOnMouseHover(mainMenuImage);
        mainMenuImage.setOnMouseClicked(e -> {
            try {
                App.setRoot("MainMenu");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void setLoginButton(ImageView loginImage, String parentAddress){
        shadowOnMouseHover(loginImage);
        loginImage.setOnMouseClicked(e -> {
            if (loginPopUp == null){
                loginPopUp = new Stage();
                Scene scene;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoginMenu.fxml"));
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                loginPopUp.setOnCloseRequest(windowEvent -> {
                    loginPopUp.close();
                    loginPopUp = null;
                });
                LoginMenu.setParentMenu(parentAddress);
                loginPopUp.setScene(scene);
                loginPopUp.setTitle("login");
                loginPopUp.setResizable(false);
                loginPopUp.showAndWait();
            }
        });
    }

    public static void setCartButton(ImageView cartImage){
        Account account = Account.getLoggedInAccount();
        if (account == null || account instanceof Customer) {
            shadowOnMouseHover(cartImage);
            cartImage.setOnMouseClicked(e -> {
                if (cartPopUp == null) {
                    cartPopUp = new Stage();
                    Scene scene;
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("cart.fxml"));
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    cartPopUp.setScene(scene);
                    cartPopUp.setTitle("Cart");
                    cartPopUp.setResizable(false);
                    cartPopUp.setOnCloseRequest(event -> {
                        cartPopUp.close();
                        cartPopUp = null;
                    });
                    cartPopUp.showAndWait();
                }
            });
        }
        else {
            cartImage.setOpacity(0);
        }
    }

    private void setOffLeftTimeLabel() {
        if (product.getOff() != null && product.getOff().isAvailable()){
            timeLeftLabel.setOpacity(1);
            timeLabel.setOpacity(1);
            setOffEndTime(product, timeLabel);
        }
        else {
            timeLeftLabel.setOpacity(0);
            timeLabel.setOpacity(0);
        }
    }

    public static void setOffEndTime(Product product, Label label){
        String endTime = product.getOff().getEndTime().toString();
        String firstAndTimeZone = endTime.substring(0, endTime.lastIndexOf(" "));
        label.setText(firstAndTimeZone.substring(0, firstAndTimeZone.lastIndexOf(" ")) +
                endTime.substring(endTime.lastIndexOf(" ")));
    }

    private void setPropertiesPane() {
        int x = 20;
        int y = 50;
        HashMap<String, Integer> properties = product.getSpecialFeatures();
        if (properties != null) {
            for (Map.Entry<String, Integer> entry : properties.entrySet()) {
                Pane pane = getPaneOfProperty(entry);
                propertiesPane.getChildren().add(pane);
                pane.setLayoutX(x);
                pane.setLayoutY(y);
                y += (int) (pane.getBoundsInParent().getHeight()) + 60;
                if (y > propertiesPane.getBoundsInParent().getHeight()) {
                    propertiesPane.setPrefHeight(y);
                }
            }
        }
    }

    private Pane getPaneOfProperty(Map.Entry<String, Integer> property){
        Label featureLabel = new Label(property.getKey());
        Pane featurePane = new Pane(featureLabel);

        Label valueLabel = new Label(String.valueOf(property.getValue()));
        Pane valuePane = new Pane(valueLabel);

        Pane[] panes = {featurePane, valuePane};
        Label[] labels = {featureLabel, valueLabel};

        Arrays.stream(labels).forEach(e -> {
            e.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 20");
            e.setWrapText(true);
            e.setLayoutY(13);
            e.setLayoutX(30);
        });
        Arrays.stream(panes).forEach(e -> {
            e.setStyle("-fx-background-color:  #d5d5d5");
            e.setMinHeight(50);
            e.setPadding(new Insets(10, 10, 10, 10));
        });
        featureLabel.setPrefWidth(propertiesPane.getPrefWidth() / 3 - 40);
        valueLabel.setPrefWidth(2 * propertiesPane.getPrefWidth() / 3 - 40 - 90);

        Pane mainPane = new Pane(featurePane, valuePane);
        valuePane.setLayoutX(propertiesPane.getPrefWidth() / 3 + 40);
        return mainPane;
    }

    private void setPrice(){
        if (product.getExistingNumber() <= 0){
            soldOutPane.setOpacity(1);
        }
        finalPriceLabel.setText(String.format("%.1f$", product.getPriceWithOff()));
        if (product.getPriceWithOff() != product.getPrice()){
            previousPriceLabel.setText(String.format("%.1f$", product.getPrice()));
            offPercentageLabel.setText(String.format("%d%%", product.getOff().getOffAmount()));
        }
        else {
            previousPriceLabel.setOpacity(0);
            offPercentageLabel.setOpacity(0);
        }
    }

    private void setLabels(){
        nameLabel.setText(product.getProductName());
        categoryLabel.setText(product.getProductCategory() == null ? "Others" : product.getProductCategory().getName());
        companyNameLabel.setText(product.getCompanyName());
        sellerNameLabel.setText(product.getProductSeller());
        visitNumberLabel.setText(String.valueOf(product.getVisitNumber()));
        remainingLabel.setText(String.valueOf(product.getExistingNumber()));
        setPropertiesLabel();
    }

    private void setExplanations(){
        explanationsLabel.setText(product.getExplanations() == null ? "" : product.getExplanations());
    }

    private void setComments(){
        ArrayList<Comment> comments = product.getProductComments();
        showingComments.clear();
        numberOfCommentsLabel.setText(comments.size() + " comments");
        for (Comment comment : comments) {
            showingComments.add(getCommentsPane(comment));
        }
        showComments();
    }

    private void showComments(){
        int x = 20;
        int y = 150;
        for (Pane pane : showingComments) {
            commentsPane.getChildren().add(pane);
            pane.setLayoutX(x);
            pane.setLayoutY(y);
            y += (int) (pane.getBoundsInParent().getHeight()) + 60;
            if (y > commentsPane.getBoundsInParent().getHeight()){
                commentsPane.setPrefHeight(y);
            }
        }
    }

    private Pane getCommentsPane(Comment comment){
        Pane pane = new Pane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setStyle("-fx-background-radius: 20 20 20 0; -fx-background-color: #83d3ff");
        Label title = new Label("title: " + comment.getTitle());
        Label cm = new Label(comment.getComment());
        Account account = comment.getAccount();
        Label name = new Label("name: " + account.getName() + " " + account.getLastName());
        Label[] labels = {title, cm, name};
        Arrays.stream(labels).forEach(e -> {
            e.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 20");
            e.setLayoutX(10);
            e.setWrapText(true);
        });
        pane.getChildren().addAll(labels);
        int x = 0;
        if (comment.isBought()){
            ImageView imageView = new ImageView(new Image("file:src\\main\\resources\\Images\\blueTick.png"));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(40);
            pane.getChildren().add(imageView);
            x = 10;
            name.setLayoutX(50);
        }
        name.setLayoutY(10);
        title.setLayoutY(30 + x);
        cm.setLayoutY(55 + x);
        return pane;
    }

    private void setAddToCartButton(){
        if (product.getExistingNumber() > 0){
            addToCartButton.setOnMouseClicked(e -> {
                Account account = Account.getLoggedInAccount();
                if (account == null || account instanceof Customer){
                    HashMap<Product, Integer> cart;
                    if (account == null) {
                        cart = Customer.getTmpCart();
                    }
                    else {
                        cart = ((Customer)account).getCart();
                    }
                    if (cart.containsKey(product) && cart.get(product) == product.getExistingNumber()){
                        productAddedLabel.setText("There is no more of this product");
                    }
                    else if (product.isFile() && cart.containsKey(product)){
                        productAddedLabel.setText("This file is already in your cart.");
                    }
                    else {
                        if (product.isInAction() && product.isActionAvailable()){
                            if (account == null){
                                productAddedLabel.setText("You must login as a customer to make a suggestion in action");
                            }
                            else{
                                Customer customer = (Customer) account;
                                addASuggestionForAction(customer);
                            }
                        }
                        else {
                            productAddedLabel.setText("Product Added to Your Cart.");
                            cart.put(product, cart.getOrDefault(product, 0) + 1); //todo: add Product to cart request
                            if (account != null) {
                                Connection.sendToServerWithToken("add to cart: " + product.getProductId() + " 1");
                            }
                        }
                    }
                }
                else {
                    productAddedLabel.setText("Admins And Sellers Cant buy Products!");
                }
                productAddedLabel.setOpacity(1);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), actionEvent -> productAddedLabel.setOpacity(0)));
                timeline.setCycleCount(1);
                timeline.play();
            });
        }
    }

    private void addASuggestionForAction(Customer customer){
        Stage stage = new Stage();
        Label label = new Label("enter your Amount:");
        label.setWrapText(true);
        TextField textField = new TextField();
        Button button = new Button("done!");
        button.setOnAction(event -> {
            double amount;
            try {
                amount = Double.parseDouble(textField.getText().trim());
            } catch (NumberFormatException e) {
                label.setText("Please enter a number!");
                return;
            }
            if (amount >= customer.getBalance() + Admin.getMinWalletBalance()){
                label.setText("You must have more that entering amount in your wallet!");
                return;
            }
            double previousAmount = product.getCustomersAmountForAction().getOrDefault(customer.getUsername(), 0.0);
            if (amount <= previousAmount){
                label.setText("Your new amount must be higher than previous one!");
                return;
            }
            product.getCustomersAmountForAction().put(customer.getUsername(), amount); //todo: send request to server to add this customer to action
            Connection.sendToServerWithToken("add customer to auction: " + amount + " " + product.getProductId());
            setChatButton(chatImage);
            stage.close();
        });
        VBox vBox = new VBox(label, textField, button);
        vBox.setSpacing(50);
        vBox.setPadding(new Insets(100, 100, 100, 100));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void setRates() {
        Rating rating = new Rating(5);
        rating.setUpdateOnHover(false);
        rating.setPartialRating(true);
        rating.setDisable(true);
        rating.setRating(product.getTotalScore());
        rating.setLayoutX(ratesLabel.getLayoutX() + 100);
        rating.setLayoutY(ratesLabel.getLayoutY());
        firstPane.getChildren().add(rating);
        Rating rate = new Rating(5);
        rate.setUpdateOnHover(true);
        rate.setPartialRating(false);
        rate.setLayoutX(noteForRateLabel.getLayoutX() + 25);
        rate.setLayoutY(noteForRateLabel.getLayoutY() - 50);
        rateBox.getChildren().add(rate);
        rate.setOnMouseClicked(e -> {
            if (hasRated) {
                e.consume();
                noteForRateLabel.setText("You cant rate anymore");
            } else {
                rateProduct(rate);
            }
        });
        setRatesAndProgresses();
    }

    private void setRatesAndProgresses(){
        ArrayList<Score> scores = product.getAllScores();
        scoreLabel.setText(String.format("%.1f", product.getTotalScore()));
        long numberOfScores = scores.size();
        numberOfScoresLabel.setText("from " + numberOfScores + " rates");
        long numberOf1Scores = scores.stream().filter(e -> e.getScore() == 1).count();
        progressBar1.setProgress((double) numberOf1Scores / numberOfScores);
        numberOfScores1.setText(numberOf1Scores + " rates");
        long numberOf2Scores = scores.stream().filter(e -> e.getScore() == 2).count();
        progressBar2.setProgress((double) numberOf2Scores / numberOfScores);
        numberOfScores2.setText(numberOf2Scores + " rates");
        long numberOf3Scores = scores.stream().filter(e -> e.getScore() == 3).count();
        progressBar3.setProgress((double) numberOf3Scores / numberOfScores);
        numberOfScores3.setText(numberOf3Scores + " rates");
        long numberOf4Scores = scores.stream().filter(e -> e.getScore() == 4).count();
        progressBar4.setProgress((double) numberOf4Scores / numberOfScores);
        numberOfScores4.setText(numberOf4Scores + " rates");
        long numberOf5Scores = scores.stream().filter(e -> e.getScore() == 5).count();
        progressBar5.setProgress((double) numberOf5Scores / numberOfScores);
        numberOfScores5.setText(numberOf5Scores + " rates");
    }

    private void rateProduct(Rating rating){
        Account account = Account.getLoggedInAccount();
        boolean flag = false;
        if (account == null){
            noteForRateLabel.setText("You must log in to rate product");
            flag = true;
        }
        else if (account instanceof Customer){
            Customer customer = (Customer) account;
            if (customer.getBuyLogs().stream().anyMatch(log -> log.getBoughtProducts().containsKey(product))){
                int rate = (int) (rating.getRating() + 0.5);
                //product.addRate(customer, rate); //todo: send rate request
                Connection.sendToServerWithToken("rate product: " + product.getProductId() + " " + rate);
                refreshPage();
                hasRated = true;
                rating.setDisable(true);
                rating.setUpdateOnHover(false);
                setRates();
            }
            else {
                noteForRateLabel.setText("You must buy product to rate it");
                flag = true;
            }
        }
        else {
            noteForRateLabel.setText("You must be logged in as a customer");
            flag = true;
        }
        if (flag){
            noteForRateLabel.setTextFill(Color.RED);
        }
    }

    private void refreshPage() {
        Connection.sendToServer("get product: " + product.getProductId());
        product = new Gson().fromJson(Connection.receiveFromServer(), Product.class);
        initialize();
        setEveryThing();
    }

    private void resizeImage() {
        for (Node image : imageStackPane.getChildren()) {
            ((ImageView)image).setPreserveRatio(true);
            ((ImageView)image).setFitWidth(450);
        }
    }

    private void setPropertiesLabel(){
        HashMap<String, Integer> properties = product.getSpecialFeatures();
        if (properties != null) {
            StringBuilder features = new StringBuilder();
            for (Map.Entry<String, Integer> entry : properties.entrySet()) {
                features.append(entry.getKey()).append(": ").append(entry.getValue());
            }
            propertiesLabel.setText(features.toString());
        }
        else {
            propertiesLabel.setOpacity(0);
            propertiesTitleLabel.setOpacity(0);
        }
    }

    public void magnifierClicked(MouseEvent event) {
        if (!searchField.getText().isBlank()){
            try {
                ProductsController productsController = (ProductsController) App.setRoot("productsMenu");
                productsController.magnifierClicked(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void magnifierMouseEntered(MouseEvent mouseEvent) {
        magnifier.setOpacity(0.25);
    }

    public void magnifierMouseExited(MouseEvent mouseEvent) {
        magnifier.setOpacity(1);
    }

    private void mouseEntered(Label label){
        label.setOpacity(0.5);
    }

    private void mouseExited(Label label){
        label.setOpacity(1);
    }

    public static void shadowOnMouseHover(Node node){
        shadowOnMouseEntered(node);
        shadowOnMouseExited(node);
    }

    private static void shadowOnMouseEntered(Node node){
        node.setOnMouseEntered(e -> {
            node.setOpacity(0.5);
            node.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius: 10");
            node.getScene().setCursor(Cursor.HAND);
        });
    }

    private static void shadowOnMouseExited(Node node){
        node.setOnMouseExited(e -> {
            try {
                node.setOpacity(1);
                node.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius: 0");
                node.getScene().setCursor(Cursor.DEFAULT);
            } catch (Exception ignore) {
            }
        });
    }

    public StackPane getImageStackPane() {
        return imageStackPane;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void leaveCommentButtonPressed(ActionEvent actionEvent) {
        Account account = Account.getLoggedInAccount();
        boolean flag = false;
        if (account == null){
            helpUsLabel.setText("You must log in to rate product");
            flag = true;
        }
        else if (account instanceof Customer){
            Customer customer = (Customer) account;
            openCommentPage();
        }
        else {
            helpUsLabel.setText("You must be logged in as a customer");
            flag = true;
        }
        if (flag){
            helpUsLabel.setTextFill(Color.RED);
        }
    }

    private void openCommentPage(){
        if (commentPopUp == null) {
            commentPopUp = new Stage();
            Scene scene;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("commentPage.fxml"));
                scene = new Scene(fxmlLoader.load());
                ProductPageController newPage = ((ProductPageController) fxmlLoader.getController());
                newPage.setProduct(product);
                newPage.setParentForCommentPage(this);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                return;
            }
            commentPopUp.setScene(scene);
            commentPopUp.setTitle("Leave a comment");
            commentPopUp.setResizable(false);
            commentPopUp.initStyle(StageStyle.UNDECORATED);
            commentPopUp.showAndWait();
        }
    }

    public void sendCommentButtonPressed(ActionEvent actionEvent) {
        if (!commentTextArea.getText().isBlank()){
            String comment = commentTextArea.getText();
            String title = titleTextField.getText();
            Comment sendingComment = new Comment(Account.getLoggedInAccount(), product.getProductId(), comment, title); //todo: send comment request
            Connection.sendToServerWithToken("new comment: " + new Gson().toJson(sendingComment));
            parentForCommentPage.setComments();
            parentForCommentPage.commentPopUp.close();
            parentForCommentPage.commentPopUp = null;
            refreshPage();
        }
        else {
            commentNoteLabel.setText("Comment Text Can't Be Blank");
            commentNoteLabel.setTextFill(Color.RED);
        }
    }

    public void XMouseEntered(MouseEvent mouseEvent) {
        mouseEntered(XLabel);
    }

    public void XMouseExited(MouseEvent mouseEvent) {
        mouseExited(XLabel);
    }

    public void XMouseClicked(MouseEvent mouseEvent) {
        parentForCommentPage.commentPopUp.close();
        parentForCommentPage.commentPopUp = null;
    }

    public void setParentForCommentPage(ProductPageController parentForCommentPage) {
        this.parentForCommentPage = parentForCommentPage;
    }

    public static String getParentAddress() {
        return parentAddress;
    }

    public static void setParentAddress(String parentAddress) {
        ProductPageController.parentAddress = parentAddress;
    }

    public static void setCartPopUp(Stage cartPopUp) {
        ProductPageController.cartPopUp = cartPopUp;
    }

    public static Stage getCartPopUp() {
        return cartPopUp;
    }

    public void compareButtonPressed(ActionEvent event) {
        try {
            ProductsController productsController = (ProductsController) App.setRoot("productsMenu");
            productsController.prepareForComparison(product, imageStackPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
