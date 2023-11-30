package graphics.LoginAndRegister;

import Client.Connection;
import Controller.LoginAndRegisterManager;
import Model.Account.Account;
import graphics.AlertBox;
import graphics.App;
import graphics.MainMenu;
import graphics.products.ProductPageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LoginMenu {
    public AnchorPane pane;
    public Button loginButton;
    public Button signUpButton;
    public TextField usernameField;
    public TextField passwordField;
    public Button logOutButton;
    public Label loggedInLabel;

    private LoginAndRegisterManager loginAndRegisterManager;
    private static String parentMenu;

    public void initialize() {
        this.loginAndRegisterManager = new LoginAndRegisterManager();
        Account account = Account.getLoggedInAccount();
        if (account != null){
            pane.getChildren().removeAll(pane.getChildren().stream().filter(node ->
                    !node.equals(logOutButton) && !node.equals(loggedInLabel)).collect(Collectors.toList()));
            logOutButton.setOnAction(e -> {
                Account.setLoggedInAccount(null);
                ProductPageController.loginPopUp = null;
                MainMenu.isLoginMenuOpen = false;
                ((Stage) logOutButton.getScene().getWindow()).close();
                try {
                    ((MainMenu) App.setRoot("MainMenu")).initialize();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
        else {
            logOutButton.setDisable(true);
            logOutButton.setOpacity(0);
            loggedInLabel.setOpacity(0);
        }
    }

    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            loginAndRegisterManager.loginUser(username, password);
            ((Stage) usernameField.getScene().getWindow()).close();
            ProductPageController.loginPopUp = null;
            Connection.sendToServer("login: " + username);
            Connection.setToken(Connection.receiveFromServer());
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Login Failed", e.getMessage());
        }
    }

    public void signUp() {
        try {
            Stage registerPopUp = (Stage) usernameField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("RegisterMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            registerPopUp.setScene(scene);
            registerPopUp.setTitle("register");
            registerPopUp.setResizable(false);
            /*loginPopUp.initStyle(StageStyle.UNDECORATED);*/
            RegisterMenu.setParentMenu(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setParentMenu(String parentMenu) {
        LoginMenu.parentMenu = parentMenu;
    }
}
