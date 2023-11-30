package graphics.SellerProfile.Offs;

import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Account.Seller;
import Model.Product.Product;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AddOff {
    public TextField offIDField;
    public TextField offProductsField;
    public TextField offAmountField;
    public DatePicker offStartTimeDatePicker;
    public DatePicker offEndTimePicker;
    public TableView productsTable;

    private static String parentMenu = "SellerProfileMenu";
    public ImageView backImage;
    public ImageView mainMenuImage;

    private ArrayList<String> offProductIDs;

    private SellerProfileManager sellerProfileManager;

    public void initialize() {
        this.sellerProfileManager = new SellerProfileManager((Seller) Account.getLoggedInAccount());
        offProductIDs = new ArrayList<>();
        productsTable = sellerProfileManager.getSellerProductsTable(productsTable);
        App.setBackButton(backImage, "SellerProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void confirm(MouseEvent mouseEvent) {
        String offID = offIDField.getText();
        Date offStartTime = Date.from(offStartTimeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date offEndTime = Date.from(offEndTimePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String offAmount = offAmountField.getText();
        boolean checkConfirmButtonInability = offID.isEmpty() || offStartTimeDatePicker.getValue() == null || offEndTimePicker.getValue() == null || offAmount.isEmpty() || offProductsField.getText().isEmpty();
        if (!(checkConfirmButtonInability)) {
            try {
                sellerProfileManager.makeNewAddOffRequest(offID, offStartTime, offEndTime, offAmount, offProductIDs);
                AlertBox.showMessage("Add Off", "Your Request Was Successfully Sent To Admins");
                App.setRoot(parentMenu);
            } catch (Exception e) {
                AlertBox.showMessage("Failed To Add", e.getMessage());
            }
        }
    }

    public void addProduct(MouseEvent mouseEvent) {
        if (productsTable.getSelectionModel().getSelectedItem() != null) {
            Product product = (Product) productsTable.getSelectionModel().getSelectedItem();
            offProductIDs.add(product.getProductId());
            offProductsField.setText(offProductsField.getText() + product.getProductId() + ", ");
            productsTable.getItems().remove(product);
        }
    }

    public void clearOffProducts(MouseEvent mouseEvent) {
        productsTable = new TableView();
        productsTable = sellerProfileManager.getSellerProductsTable(productsTable);
        offProductIDs.clear();
        offProductsField.setText("");
    }
}
