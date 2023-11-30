package graphics;

import Client.Client;
import Controller.AdminProfileManager;
import Controller.ProductsManager;
import View.Main;
import graphics.LoginAndRegister.CreateAdminAccount;
import graphics.products.ProductPageController;
import graphics.products.ProductsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        if (AdminProfileManager.isThereAdmin()) {
            scene = new Scene(loadFXML("MainMenu").load());
        }
        else {
            AlertBox.showMessage("Register Admin", "You must first register admin");
            scene = new Scene(loadFXML("CreateAdminAccount").load());
            CreateAdminAccount.setParentMenu("MainMenu");
        }
        stage.setScene(scene);
        /*stage.setFullScreen(true);*/
        stage.show();
        stage.setOnCloseRequest(e -> {
            Main.serializeXML();
            System.exit(0);
        });
    }

    public static Object setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = loadFXML(fxml);
        Client.scene.setRoot(fxmlLoader.load());
        return fxmlLoader.getController();
    }

    private static FXMLLoader loadFXML(String fxml){
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }

    public static void main(String[] args) {
        Main.deserializeXML();
        ProductPageController.productsManager = new ProductsManager();
        ProductsController.productsManager = new ProductsManager();

        launch();
    }

    public static void setBackButton(ImageView back, String parentMenuAddress){
        ProductPageController.shadowOnMouseHover(back);
        back.setOnMouseClicked(e -> {
            try {
                App.setRoot(parentMenuAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}