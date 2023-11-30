package graphics.SellerProfile;

import Client.Connection;
import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Account.Seller;
import Model.Product.Category;
import Model.Product.Product;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class ManageProducts {
    public TextField productIDField;
    public TextField productNameField;
    public TextField productCompanyNameField;
    public TextField productPriceField;
    public TextField productExistingNumberField;
    public TextArea productExplanationsField;

    public TableView productsTable;
    public ImageView backImage;
    public ImageView mainMenuImage;
    public MenuButton categoriesMenuButton;
    public TableColumn specialFeaturesColumn;
    public TableColumn valueColumn;
    public TableView productSpecialFeaturesTable;

    public static String productImageAddress;
    public Button addToActionButton;

    private Category category;
    private Product product;

    private ArrayList<FeatureData> productFeatureData;

    private SellerProfileManager sellerProfileManager;

    public void initialize() {
        this.sellerProfileManager = new SellerProfileManager((Seller) Account.getLoggedInAccount());
        productsTable = sellerProfileManager.getSellerProductsTable(productsTable);

        productFeatureData = new ArrayList<>();

        specialFeaturesColumn.setCellValueFactory(new PropertyValueFactory<String, FeatureData>("specialFeature"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<String, FeatureData>("value"));

        ProductPageController.setMainMenuButton(mainMenuImage);
        App.setBackButton(backImage, "SellerProfileMenu");

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        for (Category category : sellerProfileManager.getAllCategories()) {
            MenuItem menuItem = new MenuItem(category.getName());
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setCategory(category.getName());
                }
            });
            categoriesMenuButton.getItems().add(menuItem);
        }
    }

    private void setCategory(String categoryName) {
        this.category = SellerProfileManager.getCategoryByName(categoryName);
        categoriesMenuButton.setText(categoryName);
        if (category.getSpecialFeatures().isEmpty()) {
            return;
        }
        productSpecialFeaturesTable.getItems().clear();
        productFeatureData.clear();
        for (String specialFeature : category.getSpecialFeatures()) {
            FeatureData featureData = new FeatureData(specialFeature, "");
            productSpecialFeaturesTable.getItems().add(featureData);
            productFeatureData.add(featureData);
        }
    }

    public void confirm(MouseEvent mouseEvent) {
        String productID = productIDField.getText();
        String productName = productNameField.getText();
        String productCompanyName = productCompanyNameField.getText();
        String productPrice = productPriceField.getText();
        String productExistingNumber = productExistingNumberField.getText();
        String productExplanations = productExplanationsField.getText();
        ArrayList<String> values = new ArrayList<>();
        for (FeatureData featureData : productFeatureData) {
            values.add(featureData.getValue());
        }
        boolean informationIncomplete = productID.isEmpty() || productName.isEmpty() || productCompanyName.isEmpty() || productPrice.isEmpty() || productExistingNumber.isEmpty() ||
                productExplanations.isEmpty() || values.size() != productSpecialFeaturesTable.getItems().size();
        if (informationIncomplete) {
            return;
        }
        try {
            sellerProfileManager.makeNewEditProductRequest(productID, productName, productCompanyName, productPrice,
                    productExistingNumber, productExplanations, category, values, productImageAddress);
            AlertBox.showMessage("Edit Product", "Your Request Was Successfully Sent To Admins");
            productsTable.getItems().clear();
            productsTable = sellerProfileManager.getSellerProductsTable(productsTable);
        } catch (Exception e) {
            AlertBox.showMessage("Failed to Edit Product", e.getMessage());
        }
    }

    public void showProductBuyers(MouseEvent mouseEvent) {
        Object selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        Product product = (Product) selectedProduct;
        TableView productBuyersTable = sellerProfileManager.getProductBuyersTable(product.getProductId());

        Stage window = new Stage();
        window.setTitle("Product Buyers");

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        Button OKButton = new Button("OK");
        OKButton.setFont(Font.font("Times New Roman", 16));
        OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                window.close();
            }
        });

        vBox.getChildren().addAll(productBuyersTable, OKButton);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    public void removeProduct(MouseEvent mouseEvent) {
        Object selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        Product product = (Product) selectedProduct;
        sellerProfileManager.removeProductRequest(product.getProductId());
        AlertBox.showMessage("Remove Product", "Your Request In Order to remove Product With ID <" +
                product.getProductId() + "> Was Sent To Admins Successfully");
        productsTable.getItems().remove(product);

        productIDField.setText("");
        productNameField.setText("");
        productCompanyNameField.setText("");
        productPriceField.setText("");
        productExistingNumberField.setText("");
        productExplanationsField.setText("");
        productSpecialFeaturesTable.getItems().clear();
        productFeatureData.clear();

    }

    public void showProductDetails(MouseEvent mouseEvent) {
        Object selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        product = (Product) selectedProduct;
        productIDField.setText(product.getProductId());
        productNameField.setText(product.getProductName());
        productCompanyNameField.setText(product.getCompanyName());
        productPriceField.setText(String.valueOf(product.getPrice()));
        productExistingNumberField.setText(String.valueOf(product.getExistingNumber()));
        productExplanationsField.setText(product.getExplanations());

        category = product.getProductCategory();

        for (String specialFeature : product.getSpecialFeatures().keySet()) {
            productFeatureData.add(new FeatureData(specialFeature, String.valueOf(product.getSpecialFeatures().get(specialFeature))));
        }
        productSpecialFeaturesTable.getItems().setAll(productFeatureData);
        categoriesMenuButton.setText(product.getProductCategory().getName());
    }

    public void editValueForSpecialFeature(TableColumn.CellEditEvent cellEditEvent) {
        FeatureData featureData = (FeatureData) productSpecialFeaturesTable.getSelectionModel().getSelectedItem();
        featureData.setValue((String) cellEditEvent.getNewValue());
    }

    public void addProductToAction(ActionEvent event) {
        Object selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        product = (Product) selectedProduct;
        Stage stage = new Stage();
        Label label = new Label("select end time of action");
        DatePicker datePicker = new DatePicker();
        Button button = new Button("done!");
        VBox vBox = new VBox(label, datePicker, button);
        vBox.setSpacing(50);
        vBox.setPadding(new Insets(100, 100, 100, 100));
        button.setOnAction(e -> {
            Date actionEndTime = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            product.setEndOfAction(actionEndTime);
            product.setInAction(true);
            addToActionButton.setDisable(false);
            Connection.sendToServerWithToken("add product to auction: " + actionEndTime.getTime() + " " + product.getProductId());
            stage.close();
        });
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
