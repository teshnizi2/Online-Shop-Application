package graphics.SellerProfile;

import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Account.Seller;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class SellerProfileMenu {
    public TextField usernameField;
    public TextField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField phoneNumberField;
    public TextField companyField;
    public Button confirmButton;

    private static String parentMenu;
    public ImageView mainMenuImage;
    public Label balance;

    private SellerProfileManager sellerProfileManager;

    public void initialize() {
        this.sellerProfileManager = new SellerProfileManager((Seller) Account.getLoggedInAccount());
        usernameField.setText(sellerProfileManager.getUsername());
        passwordField.setText(sellerProfileManager.getPassword());
        firstNameField.setText(sellerProfileManager.getFirstName());
        lastNameField.setText(sellerProfileManager.getLastName());
        emailField.setText(sellerProfileManager.getEmail());
        phoneNumberField.setText(sellerProfileManager.getPhoneNumber());
        companyField.setText(sellerProfileManager.getCompanyName());
        balance.setText(sellerProfileManager.getBalance() +"$");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void confirm(ActionEvent event) {
        changePassword();
        changeFirstName();
        changeLastName();
        changeEmail();
        changePhoneNumber();
        changeCompanyName();
    }

    private void changePassword() {
        String password = passwordField.getText();
        try {
            sellerProfileManager.editPassword(password);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit password", e.getMessage());
        }
    }

    private void changeFirstName() {
        String firstName = firstNameField.getText();
        try {
            sellerProfileManager.editFirstName(firstName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit first name.", e.getMessage());
        }
    }

    private void changeLastName() {
        String lastName = lastNameField.getText();
        try {
            sellerProfileManager.editLastName(lastName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit last name", e.getMessage());
        }
    }

    private void changeEmail() {
        String email = emailField.getText();
        try {
            sellerProfileManager.editEmail(email);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit email", e.getMessage());
        }
    }

    private void changePhoneNumber() {
        String phoneNumber = phoneNumberField.getText();
        try {
            sellerProfileManager.editPhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit phone number", e.getMessage());
        }
    }

    private void changeCompanyName() {
        String companyName = companyField.getText();
        sellerProfileManager.editCompanyName(companyName);
    }

    public void manageOffs(MouseEvent mouseEvent) {
        try {
            App.setRoot("ManageOffs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addOff(MouseEvent mouseEvent) {
        try {
            App.setRoot("AddOff");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageProducts(MouseEvent mouseEvent) {
        try {
            App.setRoot("ManageProducts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(MouseEvent mouseEvent) {
        try {
            App.setRoot("AddProduct");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setParentMenu(String parentMenu) {
        SellerProfileMenu.parentMenu = parentMenu;
    }

    public void showSalesHistory(MouseEvent mouseEvent) {
        TableView salesHistory = sellerProfileManager.getSellerSalesHistoryTable();
        showTable(salesHistory, "Sales History");
    }

    private void showTable(TableView tableView, String stageTitle) {
        Stage window = new Stage();
        window.setTitle(stageTitle);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        Button OKButton = new Button("OK!");
        OKButton.setFont(Font.font("Times New Roman", 16));
        OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                window.close();
            }
        });

        vBox.getChildren().addAll(tableView, OKButton);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    public void showCategories(MouseEvent mouseEvent) {
        TableView allCategoriesTable = sellerProfileManager.getAllCategoriesTable();
        showTable(allCategoriesTable, "All Categories");
    }

    public void addCompanyInformation(MouseEvent mouseEvent) {
        try {
            App.setRoot("AddCompanyInformation");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ShowCompanyInformation(MouseEvent mouseEvent) {
        try {
            App.setRoot("ShowCompanyInformation");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToSellerWalletMenu(MouseEvent mouseEvent) {
        try {
            App.setRoot("SellerWalletMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
