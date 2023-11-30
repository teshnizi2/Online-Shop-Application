package graphics.SellerProfile;

import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Account.Seller;
import Model.Product.Category;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AddProduct {
    public TextField productIDField;
    public TextField productNameField;
    public TextField productCompanyNameField;
    public TextField productPriceField;
    public TextField productExistingNumberField;

    public ImageView backImage;
    public ImageView mainMenuImage;

    public static String productImageAddress;
    public static Stage selectImagePopUp;

    public TextArea productExplanationsField;
    public MenuButton selectCategoryMenuButton;
    public Button confirmButton;

    public TableView specialFeaturesTable;
    public TableColumn specialFeatureColumn;
    public TableColumn valueColumn;

    private Category category;

    private static String parentMenu = "SellerProfileMenu";

    private ArrayList<FeatureData> allFeatureData;

    private SellerProfileManager sellerProfileManager;
    private boolean isSellingFile = false;
    private String addressOfFileForSell;

    public void initialize(){
        productImageAddress = "src\\main\\resources\\Images\\products\\unKnown.jpg";
        App.setBackButton(backImage, "SellerProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);

        allFeatureData = new ArrayList<>();

        this.sellerProfileManager = new SellerProfileManager((Seller) Account.getLoggedInAccount());

        specialFeatureColumn.setCellValueFactory(new PropertyValueFactory<String, FeatureData>("specialFeature"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<String, FeatureData>("value"));

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        for (Category category : sellerProfileManager.getAllCategories()) {
            MenuItem menuItem = new MenuItem(category.getName());
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setCategory(category.getName());
                }
            });
            selectCategoryMenuButton.getItems().add(menuItem);
        }
    }

    private void setCategory(String categoryName) {
        this.category = SellerProfileManager.getCategoryByName(categoryName);
        selectCategoryMenuButton.setText(categoryName);
        if (category.getSpecialFeatures().isEmpty()) {
            return;
        }
        specialFeaturesTable.getItems().clear();
        for (String specialFeature : category.getSpecialFeatures()) {
            FeatureData featureData = new FeatureData(specialFeature, "");
            specialFeaturesTable.getItems().add(featureData);
            allFeatureData.add(featureData);
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
        for (FeatureData featureData : allFeatureData) {
            values.add(featureData.getValue());
        }
        boolean informationIncomplete = productID.isEmpty() || productName.isEmpty() || productCompanyName.isEmpty() || productPrice.isEmpty() || productExistingNumber.isEmpty() ||
                productExplanations.isEmpty() || values.size() != specialFeaturesTable.getItems().size();
        if (informationIncomplete) {
            return;
        }
        try {
            byte[] image = null, file = null;
            String fileName = null;
            if (isSellingFile){
                File aFile = new File(addressOfFileForSell);
                fileName = aFile.getName();
                file = loadFile();
            }
            image = loadImage();
            sellerProfileManager.makeNewAddProductRequest(productID, productName, productCompanyName, productPrice,
                    productExistingNumber, productExplanations, category, values, image, file, fileName);
            AlertBox.showMessage("Add Product", "Your Request Was Successfully Sent To Admins");
            //App.setRoot(parentMenu);
        } catch (Exception e) {
            AlertBox.showMessage("Failed to Add Product", e.getMessage());
            e.printStackTrace();
        }
    }

    private byte[] loadImage() throws IOException {
        return Files.readAllBytes(Paths.get(productImageAddress));
    }

    private byte[] loadFile() throws IOException {
        return Files.readAllBytes(Paths.get(productImageAddress));
    }

    public void addImageButtonPressed(ActionEvent event) {
        Label label = new Label("Drag a image to here!");
        Stage stage = new Stage();
        label.setStyle("-fx-font-size: 30; -fx-font-weight: Bold; -fx-font-family: 'Times New Roman'");
        Label dropped = new Label("");
        VBox dragTarget = new VBox();
        dragTarget.setPadding(new Insets(50, 50, 50, 50));
        dragTarget.setSpacing(50);
        dragTarget.getChildren().addAll(label,dropped);
        dragTarget.setOnDragOver(event1 -> {
            if (event1.getGestureSource() != dragTarget
                    && event1.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event1.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event1.consume();
        });

        dragTarget.setOnDragDropped(event12 -> {
            Dragboard db = event12.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                dropped.setText(db.getFiles().toString());
                productImageAddress = db.getFiles().toString();
                productImageAddress = productImageAddress.substring(1, productImageAddress.length() - 1);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event12.setDropCompleted(success);
            event12.consume();
            if (success){
                stage.close();
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(dragTarget);
        Scene scene = new Scene(root, 500, 250);
        stage.setTitle("Drag image");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void editValueForSpecialFeature(TableColumn.CellEditEvent cellEditEvent) {
        FeatureData featureData = (FeatureData) specialFeaturesTable.getSelectionModel().getSelectedItem();
        featureData.setValue((String) cellEditEvent.getNewValue());
    }

    public void selectFileForSelling(ActionEvent event) {
        Stage stage = new Stage();
        Label label = new Label("Drag a file to here!");
        label.setStyle("-fx-font-size: 30; -fx-font-weight: Bold; -fx-font-family: 'Times New Roman'");
        Label dropped = new Label("");
        VBox dragTarget = new VBox();
        dragTarget.setPadding(new Insets(50, 50, 50, 50));
        dragTarget.setSpacing(50);
        dragTarget.getChildren().addAll(label,dropped);
        dragTarget.setOnDragOver(event1 -> {
            if (event1.getGestureSource() != dragTarget
                    && event1.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event1.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event1.consume();
        });

        dragTarget.setOnDragDropped(event12 -> {
            Dragboard db = event12.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                dropped.setText(db.getFiles().toString());
                isSellingFile = true;
                addressOfFileForSell = db.getFiles().toString();
                addressOfFileForSell = addressOfFileForSell.substring(1, addressOfFileForSell.length() - 1);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event12.setDropCompleted(success);
            event12.consume();
            if (success){
                stage.close();
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(dragTarget);
        Scene scene = new Scene(root, 500, 250);
        stage.setTitle("Drag file for selling");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
