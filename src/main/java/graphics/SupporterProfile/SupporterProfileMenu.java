package graphics.SupporterProfile;

import Controller.ProfileManager;
import Controller.SupporterProfileManager;
import Model.Account.Account;
import Model.Account.Supporter;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SupporterProfileMenu {
    public ImageView mainMenuImage;
    public ImageView cartImage;
    public ImageView backImage;
    public Button confirmButton;
    public TextField usernameField;
    public TextField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField phoneNumberField;

    private ProfileManager profileManager;

    private SupporterProfileManager supporterProfileManager;

    private static String parentMenu;


    public static void setParentMenu(String parentMenu) {
        SupporterProfileMenu.parentMenu = parentMenu;
    }

    public void initialize() {
        this.profileManager = new ProfileManager((Supporter) Account.getLoggedInAccount());
        usernameField.setText(profileManager.getUsername());
        passwordField.setText(profileManager.getPassword());
        firstNameField.setText(profileManager.getFirstName());
        lastNameField.setText(profileManager.getLastName());
        emailField.setText(profileManager.getEmail());
        phoneNumberField.setText(profileManager.getPhoneNumber());
        App.setBackButton(backImage, "MainMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
        ProductPageController.setCartButton(cartImage);
    }

    public void confirm(ActionEvent event) {
        changePassword();
        changeFirstName();
        changeLastName();
        changeEmail();
        changePhoneNumber();
    }

    private void changePassword() {
        String password = passwordField.getText();
        try {
            profileManager.editPassword(password);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit password", e.getMessage());
        }
    }

    private void changeFirstName() {
        String firstName = firstNameField.getText();
        try {
            profileManager.editFirstName(firstName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit first name.", e.getMessage());
        }
    }

    private void changeLastName() {
        String lastName = lastNameField.getText();
        try {
            profileManager.editLastName(lastName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit last name", e.getMessage());
        }
    }

    private void changeEmail() {
        String email = emailField.getText();
        try {
            profileManager.editEmail(email);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit email", e.getMessage());
        }
    }

    private void changePhoneNumber() {
        String phoneNumber = phoneNumberField.getText();

        try {
            profileManager.editPhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit phone number", e.getMessage());
        }
    }

    public void ReadyForSupporting(MouseEvent mouseEvent) throws Exception {
        supporterProfileManager.setLineCondition(true);
    }

    public void stopSupporting(MouseEvent mouseEvent) {
        supporterProfileManager.setLineCondition(false);
    }
}
