package graphics.AdminProfile.RequestDetails;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Request.EditAddProductRequest;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class EditAddProductRequestMenu {
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
    public ImageView imageView;

    private AdminProfileManager adminProfileManager;

    private static EditAddProductRequest editAddProductRequest;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        requestIDField.setText(editAddProductRequest.getRequestId());
        requestTypeField.setText(editAddProductRequest.getRequestType());
        productIDField.setText(editAddProductRequest.getProductId());
        productNameField.setText(editAddProductRequest.getProductName());
        productPriceField.setText(String.valueOf(editAddProductRequest.getProductPrice()));
        productCompanyNameField.setText(editAddProductRequest.getProductCompanyName());
        productExistingNumberField.setText(String.valueOf(editAddProductRequest.getProductExistingNumber()));
        productCategoryField.setText(editAddProductRequest.getProductCategory().getName());
        for (String s : editAddProductRequest.getProductSpecialFeatures().keySet()) {
            productSpecialFeaturesField.setText(productSpecialFeaturesField.getText() + s + ": " + editAddProductRequest.getProductSpecialFeatures().get(s) + ", ");
        }
        requestTitle.setText(editAddProductRequest.getRequestType());
        //imageView.setImage(new Image(editAddProductRequest.getProductImageAddress()));

        App.setBackButton(backImage, parentMenu);
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void declineRequest(MouseEvent mouseEvent) {
        adminProfileManager.declineRequest(editAddProductRequest.getRequestId());
        AlertBox.showMessage("Decline Request", "Request with ID : <" + editAddProductRequest.getRequestId() + "> declined.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(MouseEvent mouseEvent) {
        adminProfileManager.acceptRequest(editAddProductRequest.getRequestId());
        AlertBox.showMessage("Accept Request", "Request with ID : <" + editAddProductRequest.getRequestId() + "> accepted.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setEditAddProductRequest(EditAddProductRequest editAddProductRequest) {
        EditAddProductRequestMenu.editAddProductRequest = editAddProductRequest;
    }
}
