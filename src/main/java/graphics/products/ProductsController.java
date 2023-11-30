package graphics.products;

import Client.Connection;
import Controller.ProductsManager;
import Controller.SortType;
import Model.Product.Category;
import Model.Product.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import graphics.App;
import graphics.ToggleSwitch;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class ProductsController {
    public Label latestLabel;
    public Label visitNumberLabel;
    public Label lowestPriceLabel;
    public Label scoreLabel;
    public Label highestPriceLabel;
    public ImageView magnifier;
    public TextField searchField;
    public Pagination pagination;
    public Pane existingFilterPane;
    public Slider minPriceSlider;
    public Slider maxPriceSlider;
    public Label minPriceLabel;
    public Label maxPriceLabel;
    public Button setPriceRangeButton;
    public Pane productsWIthOffPane;
    public Pane productsPane;
    public Label disableSearchFilterLabel;
    public ImageView backImage;
    public ImageView cartImage;
    public MenuButton categories;
    public Pane mainPane;
    public ImageView loginImage;
    public MenuButton sortByFeatureMenuButton;
    public ImageView magnifier2;
    public ImageView magnifier3;
    public TextField filterBySellerTextField;
    public TextField filterByCompanyTextField;
    public Label messageLabel;
    public ImageView mainMenuButton;
    public ImageView profileImage1;
    public Pane onlyActionPane;

    private ArrayList<Product> showingProducts;
    public static ProductsManager productsManager;
    private int maxPrice;
    private int minPrice;
    private int pageIndex;

    private final static String selectedColor = " #7ec7f6";
    private static final int numberOfProductsPerPage = 10;
    private boolean disableSearchIsHidden = true;
    private static String parentAddress = "mainMenu";
    private boolean isComparisonPage;
    private Product firstProductForComparison;
    private StackPane firstProductStackPane;

    public void initialize() {
        addToggleButtonForExistingFilter();
        addToggleButtonForActionFilter();
        addToggleButtonForOffFilter();
        addPageFactoryForPagination();
        showProducts();
        setSliders();
        setFilterBySellerAndCompany();
        MenuItem allCategoriesItem = new MenuItem("All Categories");
        allCategoriesItem.setOnAction(e -> {
            productsManager.disableCategoryFilter();
            categories.setText("All Categories");
            mainPane.getChildren().removeAll(mainPane.getChildren().stream().filter(node -> node instanceof MenuButton).
                    filter(menuButton -> menuButton != categories && menuButton != sortByFeatureMenuButton).collect(Collectors.toList()));
            sortByFeatureMenuButton.setOpacity(0);
            sortByFeatureMenuButton.setDisable(true);
            sortByFeatureMenuButton.setDisable(true);
            sortByFeatureMenuButton.setOpacity(0);
            productsManager.disableSpecialFeatureSort();
            showProducts();
        });
        categories.getItems().add(allCategoriesItem);
        Connection.sendToServer("getCategories");
        setCategories(new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Category>>(){}.getType()), categories); //todo: request

        ProductPageController.setCartButton(cartImage);
        App.setBackButton(backImage, parentAddress);
        ProductPageController.setLoginButton(loginImage, "productsMenu");
        ProductPageController.setMainMenuButton(mainMenuButton);
        ProductPageController.setProfileButton(profileImage1);
    }

    public void prepareForComparison(Product firstProductForComparison, StackPane firstProductStackPane){
        this.firstProductForComparison = firstProductForComparison;
        this.firstProductStackPane = firstProductStackPane;
        isComparisonPage = true;
        messageLabel.setText("Select Second Product for Comparison");
        messageLabel.setOpacity(1);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> messageLabel.setOpacity(0)));
        timeline.setCycleCount(1);
        timeline.play();
        showProducts();
    }

    private void setFilterBySellerAndCompany() {
        shadowOnMouseHover(magnifier2);
        shadowOnMouseHover(magnifier3);
        magnifier2.setOnMouseClicked(e -> {
            if (filterBySellerTextField.getText().isBlank()){
                productsManager.disableFilterBySeller();
            }
            else {
                productsManager.addFilterBySeller(filterBySellerTextField.getText());
            }
            showProducts();
        });
        magnifier3.setOnMouseClicked(e -> {
            if (filterByCompanyTextField.getText().isBlank()){
                productsManager.disableFilterByCompany();
            }
            else {
                productsManager.addFilterByCompany(filterByCompanyTextField.getText());
            }
            showProducts();
        });
    }

    private void setSortBySpecialFeatureMenuButton(Category category){
        sortByFeatureMenuButton.setOpacity(1);
        sortByFeatureMenuButton.setDisable(false);
        sortByFeatureMenuButton.getItems().clear();
        MenuItem doNot = new MenuItem("don't sort by special feature");
        sortByFeatureMenuButton.getItems().add(doNot);
        doNot.setOnAction(e -> {
            productsManager.disableSpecialFeatureSort();
            sortByFeatureMenuButton.setText("don't sort by special feature");
            showProducts();
        });
        for (String feature : category.getSpecialFeatures()) {
            MenuItem featureItem = new MenuItem(feature);
            sortByFeatureMenuButton.getItems().add(featureItem);
            featureItem.setOnAction(e -> {
                sortByFeatureMenuButton.setText("sort by: " + feature);
                productsManager.useSpecialFeatureSort(feature);
                showProducts();
            });
        }
    }

    private void setCategories(ArrayList<Category> allCategories, MenuButton menuButton) {
        for (Category category : allCategories) {
            MenuItem categoriesItem = new MenuItem(category.getName());
            categoriesItem.setOnAction(e -> {
                menuButton.setText(category.getName());
                productsManager.addCategoryFilter(category);
                setSortBySpecialFeatureMenuButton(category);
                if (!category.getSubCategories().isEmpty()){
                    MenuButton subMenuButton = new MenuButton();
                    subMenuButton.setText("Select sub category");
                    MenuItem allCategoriesItem = new MenuItem("All sub categories");
                    allCategoriesItem.setOnAction(event -> {
                        mainPane.getChildren().removeAll(mainPane.getChildren().stream().filter(node -> node instanceof MenuButton).
                                filter(node -> node != categories && node != menuButton && node != subMenuButton && node != sortByFeatureMenuButton).collect(Collectors.toList()));
                        productsManager.addCategoryFilter(category);
                        subMenuButton.setText("All sub categories");
                        showProducts();
                    });
                    subMenuButton.getItems().add(allCategoriesItem);
                    mainPane.getChildren().add(subMenuButton);
                    subMenuButton.setLayoutX(menuButton.getLayoutX() + 50);
                    subMenuButton.setPrefWidth(menuButton.getPrefWidth() - 50);
                    subMenuButton.setLayoutY(menuButton.getLayoutY() + 50);
                    setCategories(category.getSubCategories(), subMenuButton);
                }
                else {
                    mainPane.getChildren().removeAll(mainPane.getChildren().stream().filter(node -> node instanceof MenuButton).
                            filter(node -> node != categories && node != menuButton && node != sortByFeatureMenuButton).collect(Collectors.toList()));
                }
                showProducts();
            });
            menuButton.getItems().add(categoriesItem);
        }
    }

    private void addPageFactoryForPagination(){
        pagination.setPageFactory(page -> {
            pageIndex = page;
            showProducts();
            return productsPane;
        });
    }

    private void addToggleButtonForActionFilter(){
        ToggleSwitch toggleSwitch = new ToggleSwitch(50, new EventHandler<Event>() {
            boolean isOn = false;
            @Override
            public void handle(Event event) {
                isOn = !isOn;
                if (isOn){
                    productsManager.addActionFilter();
                }
                else {
                    productsManager.disableActionFilter();
                }
                showProducts();
            }
        });
        toggleSwitch.setLayoutX(200);
        toggleSwitch.setLayoutY(20);
        onlyActionPane.getChildren().add(toggleSwitch);
    }

    private void addToggleButtonForOffFilter(){
        ToggleSwitch toggleSwitch = new ToggleSwitch(50, new EventHandler<Event>() {
            boolean isOn = false;
            @Override
            public void handle(Event event) {
                isOn = !isOn;
                if (isOn){
                    productsManager.addOffFilter();
                }
                else {
                    productsManager.disableOffFilter();
                }
                showProducts();
            }
        });
        toggleSwitch.setLayoutX(200);
        toggleSwitch.setLayoutY(20);
        productsWIthOffPane.getChildren().add(toggleSwitch);
    }

    private void addToggleButtonForExistingFilter(){
        ToggleSwitch toggleSwitch = new ToggleSwitch(50, new EventHandler<Event>() {
            boolean isOn = false;
            @Override
            public void handle(Event event) {
                isOn = !isOn;
                if (isOn){
                    productsManager.addExistenceFilter(true);
                }
                else {
                    productsManager.addExistenceFilter(false);
                }
                showProducts();
            }
        });
        toggleSwitch.setLayoutX(200);
        toggleSwitch.setLayoutY(20);
        existingFilterPane.getChildren().add(toggleSwitch);
    }

    private void showProducts() {
        productsManager.setAllProducts();
        showingProducts = productsManager.showProducts();
        pagination.setPageCount(showingProducts.size() % numberOfProductsPerPage == 0 ?
                showingProducts.size() / numberOfProductsPerPage : showingProducts.size() / numberOfProductsPerPage + 1);

        productsPane.getChildren().clear();
        productsPane.setPadding(new Insets(100, 100, 100, 100));
        int count = pageIndex * numberOfProductsPerPage;
        Outer: for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (count >= showingProducts.size()){
                    break Outer;
                }
                Pane pane = getProductPane(showingProducts.get(count++));
                productsPane.getChildren().add(pane);
                pane.setLayoutX(j * (200 + 30) + 30);
                pane.setLayoutY(i * (280 + 30) + 30);
            }
        }
    }

    private Pane getProductPane(Product product){
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        ImageView productImage = new ImageView();
        productImage.setImage(product.getImage());
        productImage.setPreserveRatio(true);
        productImage.setFitWidth(125);
        stackPane.getChildren().add(productImage);
        if (product.getExistingNumber() <= 0) {
            ImageView soldOutImage = new ImageView(new Image("file:src\\main\\resources\\Images\\soldOut.png"));
            soldOutImage.setPreserveRatio(true);
            soldOutImage.setFitWidth(125);
            stackPane.getChildren().add(soldOutImage);
        }

        Label name = new Label(product.getProductName());
        name.setStyle("-fx-font-family: 'Times New Roman' ; -fx-font-size: 20");
        Text price = new Text(product.getPrice() + "$");
        name.setWrapText(true);
        price.setStyle("-fx-font-family: 'Times New Roman' ; -fx-font-size: 20");
        Rating rating = new Rating(5);
        rating.setUpdateOnHover(false);
        rating.setPartialRating(true);
        rating.setDisable(true);
        rating.setRating(product.getTotalScore());
        rating.setMaxSize(10, 10);

        VBox vBox;
        if (product.getOff() != null && product.getOff().isAvailable()){
            Label priceWithOff = new Label(product.getOff().getOffAmount() + "%:  " + product.getPriceWithOff() + "$");
            priceWithOff.setTextFill(Color.RED);
            priceWithOff.setStyle("-fx-font-family: 'Times New Roman' ; -fx-font-size: 20");
            price.setStrikethrough(true);
            Label endTime = new Label();
            endTime.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15");
            endTime.setTextFill(Color.RED);
            endTime.setWrapText(true);
            ProductPageController.setOffEndTime(product, endTime);
            vBox = new VBox(stackPane, name, price, priceWithOff, endTime, rating);
        }
        else {
            vBox = new VBox(stackPane, name, price, rating);
        }
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-border-color :  #c5c5c5");
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setPrefWidth(150);
        vBox.setPrefHeight(280);
        vBox.setSpacing(8);
        shadowOnMouseHover(vBox);
        vBox.setOnMouseClicked(event -> {
            if (isComparisonPage){
                goToComparisonPage(product, stackPane);
            }
            else {
                loginImage.getScene().setCursor(Cursor.DEFAULT);
                openProduct(product, stackPane);
            }
        });
        return vBox;
    }

    private void goToComparisonPage(Product secondProduct, StackPane secondProductStackPane){
        try {
            ComparisonPageController comparisonPageController = ((ComparisonPageController)App.setRoot("comparisonPage"));
            comparisonPageController.setProduct1(firstProductForComparison);
            comparisonPageController.setProduct2(secondProduct);
            comparisonPageController.setFirstProductImageStackPane(firstProductStackPane);
            comparisonPageController.setSecondProductImageStackPane(secondProductStackPane);

            comparisonPageController.setEveryThing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openProduct(Product product, StackPane imageStackPane){
        try {
            ProductPageController productPageController = (ProductPageController) App.setRoot("productPage");
            ProductPageController.setParentAddress("productsMenu");
            productPageController.setProduct(product);
            productPageController.getImageStackPane().getChildren().addAll(imageStackPane.getChildren());
            productPageController.setEveryThing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shadowOnMouseHover(Node node){
        shadowOnMouseEntered(node);
        shadowOnMouseExited(node);
    }

    private void shadowOnMouseEntered(Node node){
        node.setOnMouseEntered(e -> {
            node.setOpacity(0.75);
            node.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius: 10");
            node.getScene().setCursor(Cursor.HAND);
        });
    }

    private void shadowOnMouseExited(Node node){
        node.setOnMouseExited(e -> {
            node.setOpacity(1);
            node.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius: 0");
            try {
                node.getScene().setCursor(Cursor.DEFAULT);
            } catch (Exception ignored) {
            }
        });
    }

    private void setSliders(){
        OptionalDouble min = showingProducts.stream().mapToDouble(Product::getPrice).min();
        OptionalDouble max = showingProducts.stream().mapToDouble(Product::getPrice).max();
        if (max.isPresent()){
            maxPrice = (int)max.getAsDouble();
        }
        if (min.isPresent()){
            minPrice = (int)min.getAsDouble();
        }
        minPrice = 0;
        if (maxPriceSlider.getValue() > maxPrice){
            maxPriceSlider.setValue(maxPrice);
        }
        if (minPriceSlider.getValue() < minPrice){
            minPriceSlider.setValue(minPrice);
        }
        minPriceSlider.setMin(minPrice);
        minPriceSlider.setMax(maxPrice);
        maxPriceSlider.setMin(minPrice);
        maxPriceSlider.setMax(maxPrice);

        minPriceSlider.setValue(minPrice);
        maxPriceSlider.setValue(maxPrice);
        minPriceLabel.setText(String.valueOf(minPrice));
        maxPriceLabel.setText(String.valueOf(maxPrice));

        maxPriceSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->{
            maxPriceLabel.setText(String.format("%.1f$", (double)newValue));
            if ((Double)newValue < minPriceSlider.getValue()){
                minPriceSlider.setValue((Double) newValue);
            }
        });
        minPriceSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->{
            minPriceLabel.setText(String.format("%.1f$", (double)newValue));
            if ((Double)newValue > maxPriceSlider.getValue()){
                maxPriceSlider.setValue((Double) newValue);
            }
        });
        setPriceRangeButton.setOnAction(e -> {
            productsManager.addMinimumPriceFilter((int) minPriceSlider.getValue());
            productsManager.addMaximumPriceFilter((int) maxPriceSlider.getValue() + 1);
            showProducts();
        });
    }

    private void mouseEntered(Label label){
        label.setOpacity(0.5);
    }

    private void mouseExited(Label label){
        label.setOpacity(1);
    }

    public void latestLabelClicked(MouseEvent mouseEvent) {
        if (!productsManager.getCurrentSortType().equals(SortType.SORT_BY_TIME)) {
            productsManager.useSortByTime();
            clearBackgroundColorOfSortLabels();
            latestLabel.setStyle("-fx-background-color : #7ec7f6");
            showProducts();
        }
    }

    public void latestLabelMouseEntered(MouseEvent mouseEvent) {
        mouseEntered(latestLabel);
    }

    public void latestLabelMouseExited(MouseEvent mouseEvent) {
        mouseExited(latestLabel);
    }

    public void visitNumberLabelClicked(MouseEvent mouseEvent) {
        if (!productsManager.getCurrentSortType().equals(SortType.SORT_BY_VISIT)) {
            productsManager.useSortByVisit();
            clearBackgroundColorOfSortLabels();
            visitNumberLabel.setStyle("-fx-background-color : #7ec7f6");
            showProducts();
        }
    }

    public void visitNumberLabelEntered(MouseEvent mouseEvent) {
        mouseEntered(visitNumberLabel);
    }

    public void visitNumberLabelExited(MouseEvent mouseEvent) {
        mouseExited(visitNumberLabel);
    }

    public void lowestPriceLabelClicked(MouseEvent mouseEvent) {
        if (!productsManager.getCurrentSortType().equals(SortType.SORT_BY_LOWEST_PRICE)) {
            productsManager.useSortByLowestPrice();
            clearBackgroundColorOfSortLabels();
            lowestPriceLabel.setStyle("-fx-background-color : #7ec7f6");
            showProducts();
        }
    }

    public void lowestPriceLabelMouseEntered(MouseEvent mouseEvent) {
        mouseEntered(lowestPriceLabel);
    }

    public void lowestPriceLabelMouseExited(MouseEvent mouseEvent) {
        mouseExited(lowestPriceLabel);
    }

    public void scoreLabelClicked(MouseEvent mouseEvent) {
        if (!productsManager.getCurrentSortType().equals(SortType.SORT_BY_SCORE)) {
            productsManager.useSortByScore();
            clearBackgroundColorOfSortLabels();
            scoreLabel.setStyle("-fx-background-color : #7ec7f6");
            showProducts();
        }
    }

    public void scoreLabelMouseEntered(MouseEvent mouseEvent) {
        mouseEntered(scoreLabel);
    }

    public void scoreLabelMouseExited(MouseEvent mouseEvent) {
        mouseExited(scoreLabel);
    }

    public void highestPriceLabelClicked(MouseEvent mouseEvent) {
        if (!productsManager.getCurrentSortType().equals(SortType.SORT_BY_HIGHEST_PRICE)) {
            productsManager.useSortByHighestPrice();
            clearBackgroundColorOfSortLabels();
            highestPriceLabel.setStyle("-fx-background-color : #7ec7f6");
            showProducts();
        }
    }

    public void highestPriceLabelMouseEntered(MouseEvent mouseEvent) {
        mouseEntered(highestPriceLabel);
    }

    public void highestPriceLabelMouseExited(MouseEvent mouseEvent) {
        mouseExited(highestPriceLabel);
    }

    private void clearBackgroundColorOfSortLabels(){
        latestLabel.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius :  5");
        scoreLabel.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius :  5");
        lowestPriceLabel.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius :  5");
        visitNumberLabel.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius :  5");
        highestPriceLabel.setStyle("-fx-border-color :  #c5c5c5; -fx-border-radius :  5");
    }

    public void magnifierClicked(MouseEvent mouseEvent) {
        if (!searchField.getText().isBlank()) {
            productsManager.addNameFiltering(searchField.getText());
            disableSearchFilterLabel.setOpacity(1);
            disableSearchFilterLabel.setText("X " + searchField.getText());
            disableSearchIsHidden = false;
        }
        else {
            productsManager.disableNameFiltering();
            disableSearchFilterLabel.setOpacity(0);
        }
        showProducts();
    }

    public void magnifierMouseEntered(MouseEvent mouseEvent) {
        magnifier.setOpacity(0.25);
    }

    public void magnifierMouseExited(MouseEvent mouseEvent) {
        magnifier.setOpacity(1);
    }

    public void disableSearchFilterLabelClicked(MouseEvent event) {
        searchField.setText("");
        productsManager.disableNameFiltering();
        showProducts();
        disableSearchFilterLabel.setOpacity(0);
        disableSearchIsHidden = true;
    }

    public void disableSearchFilterLabelMouseEntered(MouseEvent event) {
        if (!disableSearchIsHidden) {
            mouseEntered(disableSearchFilterLabel);
        }
    }

    public void disableSearchFilterLabelMouseExited(MouseEvent event) {
        if (!disableSearchIsHidden) {
            mouseExited(disableSearchFilterLabel);
        }
    }
}
