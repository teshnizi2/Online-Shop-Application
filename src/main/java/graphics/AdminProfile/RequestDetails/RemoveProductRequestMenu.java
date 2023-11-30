package graphics.AdminProfile.RequestDetails;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Product.Product;
import Model.Request.RemoveProductRequest;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RemoveProductRequestMenu {
    public Label requestTitle;
    public TextField productSpecialFeaturesField;
    public TextField productCategoryField;
    public TextField productExistingNumberField;
    public TextField productPriceField;
    public TextField productCompanyNameField;
    public TextField productNameField;
    public TextField productIDField;
    public TextField requestTypeField;
    public TextField requestIDField;

    private static String parentMenu = "ManageRequests";
    public ImageView backImage;
    public ImageView mainMenuImage;

    private AdminProfileManager adminProfileManager;

    private static RemoveProductRequest removeProductRequest;

    private static Product product;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        requestIDField.setText(removeProductRequest.getRequestId());
        requestTypeField.setText(removeProductRequest.getRequestType());
        productIDField.setText(product.getProductId());
        productNameField.setText(product.getProductName());
        productPriceField.setText(String.valueOf(product.getPrice()));
        productCompanyNameField.setText(product.getCompanyName());
        productExistingNumberField.setText(String.valueOf(product.getExistingNumber()));
        productCategoryField.setText(product.getProductCategory().getName());
        for (String s : product.getSpecialFeatures().keySet()) {
            productSpecialFeaturesField.setText(productSpecialFeaturesField.getText() + s + ": " + product.getSpecialFeatures().get(s) + ", ");
        }
        requestTitle.setText(removeProductRequest.getRequestType());

        App.setBackButton(backImage, parentMenu);
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void declineRequest(MouseEvent mouseEvent) {
        adminProfileManager.declineRequest(removeProductRequest.getRequestId());
        AlertBox.showMessage("Decline Request", "Request with ID : <" + removeProductRequest.getRequestId() + "> declined.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(MouseEvent mouseEvent) {
        adminProfileManager.acceptRequest(removeProductRequest.getRequestId());
        AlertBox.showMessage("Accept Request", "Request with ID : <" + removeProductRequest.getRequestId() + "> accepted.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRemoveProductRequest(RemoveProductRequest removeProductRequest) {
        RemoveProductRequestMenu.removeProductRequest = removeProductRequest;
        RemoveProductRequestMenu.product = removeProductRequest.getProduct();
    }
}
