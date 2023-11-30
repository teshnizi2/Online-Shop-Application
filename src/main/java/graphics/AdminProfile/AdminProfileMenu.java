package graphics.AdminProfile;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Request.Request;
import graphics.AlertBox;

import graphics.App;
import graphics.LoginAndRegister.CreateAdminAccount;
import graphics.LoginAndRegister.CreateSupporterAccount;
import graphics.products.ProductPageController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminProfileMenu {
    public ImageView mainMenuImage;
    private AdminProfileManager adminProfileManager;
    public TextField usernameField;
    public TextField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField phoneNumberField;
    public Button confirmButton;

    private static String parentMenu;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        usernameField.setText(adminProfileManager.getUsername());
        passwordField.setText(adminProfileManager.getPassword());
        firstNameField.setText(adminProfileManager.getFirstName());
        lastNameField.setText(adminProfileManager.getLastName());
        emailField.setText(adminProfileManager.getEmail());
        phoneNumberField.setText(adminProfileManager.getPhoneNumber());
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void confirm() {
        changePassword();
        changeFirstName();
        changeLastName();
        changeEmail();
        changePhoneNumber();
    }

    private void changePassword() {
        String password = passwordField.getText();
        try {
            adminProfileManager.editPassword(password);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit password", e.getMessage());
        }
    }

    private void changeFirstName() {
        String firstName = firstNameField.getText();
        try {
            adminProfileManager.editFirstName(firstName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit first name.", e.getMessage());
        }
    }

    private void changeLastName() {
        String lastName = lastNameField.getText();
        try {
            adminProfileManager.editLastName(lastName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit last name", e.getMessage());
        }
    }

    private void changeEmail() {
        String email = emailField.getText();
        try {
            adminProfileManager.editEmail(email);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit email", e.getMessage());
        }
    }

    private void changePhoneNumber() {
        String phoneNumber = phoneNumberField.getText();
        try {
            adminProfileManager.editPhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit phone number", e.getMessage());
        }
    }

    public void manageUsers(MouseEvent mouseEvent) {
        Stage stage = new Stage();

        TableView allUsersTable = adminProfileManager.getAllUsersTable();
        allUsersTable.getItems().remove(Account.getLoggedInAccount());
        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setFont(Font.font("Times New Roman", 16));
        deleteUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object selectedUser = allUsersTable.getSelectionModel().getSelectedItem();
                if (selectedUser == null) {
                    return;
                }
                String selectedUsername = ((Account) selectedUser).getUsername();
                adminProfileManager.deleteUser(selectedUsername);
                AlertBox.showMessage("Delete User", "User with ID : <" + selectedUsername + "> deleted");
                allUsersTable.getItems().remove(selectedUser);
            }
        });

        Button doneButton = new Button("Done!");
        doneButton.setFont(Font.font("Times New Roman", 16));
        doneButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.close();
            }
        });

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(allUsersTable, deleteUserButton, doneButton);
        stage.setScene(new Scene(vBox));
        stage.show();
    }

    public void addAdmin(MouseEvent mouseEvent) {
        CreateAdminAccount.setParentMenu("AdminProfileMenu");
        try {
            App.setRoot("CreateAdminAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageRequests(MouseEvent mouseEvent) {
        try {
            App.setRoot("ManageRequests");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo: adding delete button in productsMenu
    public void manageProducts(MouseEvent mouseEvent) {
        try {
            App.setRoot("productsMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageDiscountCodes(MouseEvent mouseEvent) {
        try {
            App.setRoot("ManageDiscountsMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDiscountCode(MouseEvent mouseEvent) {
        try {
            App.setRoot("CreateDiscountCode");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageCategories(MouseEvent mouseEvent) {
        try {
            App.setRoot("ManageCategories");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(MouseEvent mouseEvent) {
        try {
            App.setRoot("AddCategory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setParentMenu(String parentMenu) {
        AdminProfileMenu.parentMenu = parentMenu;
    }

    public void ManageSalesHistory(MouseEvent mouseEvent) {
        try {
            App.setRoot("ManageSalesHistory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSupporter(MouseEvent mouseEvent) {
        CreateSupporterAccount.setParentMenu("AdminProfileMenu");
        try {
            App.setRoot("CreateSupporterAccount");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToBankingSettingMenu(MouseEvent mouseEvent) {
        BankingSettingMenu.setParentMenu("AdminProfileMenu");
        try {
            App.setRoot("BankingSettingMenu");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
