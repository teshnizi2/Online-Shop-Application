package graphics;

import Model.Account.*;
import View.Main;
import graphics.AdminProfile.AdminProfileMenu;
import graphics.CustomerProfile.CustomerProfileMenu;
import graphics.LoginAndRegister.LoginMenu;
import graphics.SellerProfile.SellerProfileMenu;
import graphics.SupporterProfile.SupporterProfileMenu;
import graphics.products.ProductPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
    public Button productsMenuButton;
    public Button profileButton;
    public ImageView accountImage;

    public static boolean isLoginMenuOpen;

    public void initialize(){
        ProductPageController.setLoginButton(accountImage, "MainMenu");
    }

    public void goToProductsMenu(ActionEvent event) {
        try {
            App.setRoot("productsMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToProfileMenu(ActionEvent event) {
        openProfile();
    }

    public static void openProfile(){
        System.out.println(Account.getLoggedInAccount());
        Account account = Account.getLoggedInAccount();
        if (account == null) {
            try {
                if (ProductPageController.loginPopUp == null && !isLoginMenuOpen) {
                    Stage registerPopUp = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoginMenu.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    registerPopUp.setScene(scene);
                    registerPopUp.setTitle("login");
                    registerPopUp.setResizable(false);
                    ProductPageController.loginPopUp = registerPopUp;
                    isLoginMenuOpen = true;
                    registerPopUp.setOnCloseRequest(e -> {
                        registerPopUp.close();
                        ProductPageController.loginPopUp = null;
                        isLoginMenuOpen = false;
                    });
                    registerPopUp.show();
                    LoginMenu.setParentMenu("MainMenu");
                    AlertBox.showMessage("Login Error", "You must login first!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (account instanceof Admin) {
            try {
                App.setRoot("AdminProfileMenu");
                AdminProfileMenu.setParentMenu("MainMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (account instanceof Customer) {
            try {
                App.setRoot("CustomerProfileMenu");
                CustomerProfileMenu.setParentMenu("MainMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (account instanceof Supporter) {
            try {
                App.setRoot("SupporterProfileMenu");
                SupporterProfileMenu.setParentMenu("MainMenu");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (account instanceof Seller) {
            try {
                App.setRoot("SellerProfileMenu");
                SellerProfileMenu.setParentMenu("MainMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exit(ActionEvent event) {
        Main.serializeXML();
        System.exit(0);
    }
}
