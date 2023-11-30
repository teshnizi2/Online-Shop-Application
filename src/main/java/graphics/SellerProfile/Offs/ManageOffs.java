package graphics.SellerProfile.Offs;

import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Account.Off;
import Model.Account.Seller;
import Model.Product.Product;
import com.sun.xml.fastinfoset.algorithm.BASE64EncodingAlgorithm;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class ManageOffs {
    public DatePicker offEndTimePicker;
    public DatePicker offStartTimeDatePicker;
    public TextField offAmountField;
    public TextField offProductsField;
    public TextField offIDField;

    public TableView offsTable;
    public TableView notIncludingProducts;
    public ImageView backImage;
    public ImageView mainMenuImage;

    private ArrayList<String> offProductIDs;

    private SellerProfileManager sellerProfileManager;

    public void initialize() {
        this.sellerProfileManager = new SellerProfileManager((Seller) Account.getLoggedInAccount());
        offProductIDs = new ArrayList<>();

        notIncludingProducts = new TableView();

        offsTable = sellerProfileManager.getSellerOffsTable(offsTable);
        notIncludingProducts = sellerProfileManager.getSellerProductsTable(notIncludingProducts);
        App.setBackButton(backImage, "SellerProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void showOffDetails(MouseEvent mouseEvent) {
        Object selectedOff = offsTable.getSelectionModel().getSelectedItem();
        if (selectedOff == null) {
            return;
        }

        Off off = (Off) selectedOff;

        offIDField.setText(off.getOffID());
        offStartTimeDatePicker.setValue(off.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        offEndTimePicker.setValue(off.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        offAmountField.setText(String.valueOf(off.getOffAmount()));

        offProductsField.setText("");
        offProductIDs = off.getProductIDs();
        if (offProductIDs == null) {
            return;
        }
        for (String offProductID : offProductIDs) {
            offProductsField.setText(offProductsField.getText() + offProductID + ", ");
        }

        for (String offProductID : offProductIDs) {
            notIncludingProducts.getItems().remove(Product.getProductByID(offProductID));
        }
    }

    public void showNotIncludingProducts() {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        Button addOffProductButton = new Button("Add Product");
        addOffProductButton.setFont(Font.font("Times New Roman", 16));
        addOffProductButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addOffProduct();
            }
        });

        vBox.getChildren().addAll(notIncludingProducts, addOffProductButton);
        Scene scene = new Scene(vBox);
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle("Add Product");
        window.show();
    }

    public void addOffProduct() {
        Object selectedProduct = notIncludingProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        Product product = (Product) selectedProduct;
        offProductIDs.add(product.getProductId());
        offProductsField.setText(offProductsField.getText() + product.getProductId() + ", ");
        notIncludingProducts.getItems().remove(product);
    }

    public void clearOffProducts(MouseEvent mouseEvent) {
        notIncludingProducts = new TableView();
        notIncludingProducts = sellerProfileManager.getSellerProductsTable(notIncludingProducts);
        offProductIDs.clear();
        offProductsField.setText("");
    }

    public void confirm(MouseEvent mouseEvent) {
        String offID = offIDField.getText();
        /*Date offStartTime = new Date(offStartTimeDatePicker.getValue().toEpochDay());
        Date offEndTime = new Date(offEndTimePicker.getValue().toEpochDay());*/
        Date offStartTime = Date.from(offStartTimeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date offEndTime = new Date(offEndTimePicker.getValue().toEpochDay());
        String offAmount = offAmountField.getText();
        boolean checkConfirmButtonInability = offID.isEmpty() || offStartTimeDatePicker.getValue() == null || offEndTimePicker.getValue() == null || offAmount.isEmpty() || offProductsField.getText().isEmpty();
        if (!(checkConfirmButtonInability)) {
            try {
                sellerProfileManager.makeNewEditOffRequest(offID, offStartTime, offEndTime, offAmount, offProductIDs);
                AlertBox.showMessage("Edit Off", "Your Request Was Successfully Sent To Admins");
            } catch (Exception e) {
                AlertBox.showMessage("Failed To Edit", e.getMessage());
            }
        }
    }
}
