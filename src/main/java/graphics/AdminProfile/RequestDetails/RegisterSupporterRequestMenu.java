package graphics.AdminProfile.RequestDetails;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Request.RegisterSellerRequest;
import Model.Request.RegisterSupporterRequest;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegisterSupporterRequestMenu {
    public TextField requestIDField;
    public TextField requestTypeField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField phoneNumberField;
    public ImageView backImage;
    public ImageView mainMenuImage;

    private static String parentMenu = "ManageRequests";
    private AdminProfileManager adminProfileManager;
    private static RegisterSupporterRequest registerSupporterRequest;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        requestIDField.setText(registerSupporterRequest.getRequestId());
        requestTypeField.setText(registerSupporterRequest.getRequestType());
        usernameField.setText(registerSupporterRequest.getUsername());
        passwordField.setText(registerSupporterRequest.getPassword());
        firstNameField.setText(registerSupporterRequest.getName());
        lastNameField.setText(registerSupporterRequest.getLastName());
        emailField.setText(registerSupporterRequest.getEmail());
        phoneNumberField.setText(registerSupporterRequest.getPhoneNumber());
        App.setBackButton(backImage, "AdminProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void declineRequest(MouseEvent mouseEvent) {
        adminProfileManager.declineRequest(registerSupporterRequest.getRequestId());
        AlertBox.showMessage("Decline Request", "Request with ID : <" + registerSupporterRequest.getRequestId() + "> declined.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(MouseEvent mouseEvent) {
        adminProfileManager.acceptRequest(registerSupporterRequest.getRequestId());
        AlertBox.showMessage("Accept Request", "Request with ID : <" + registerSupporterRequest.getRequestId() + "> accepted.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRegisterSupporterRequest(RegisterSupporterRequest registerSupporterRequest) {
        RegisterSupporterRequestMenu.registerSupporterRequest = registerSupporterRequest;
    }
}
