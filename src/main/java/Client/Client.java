package Client;

import Controller.AdminProfileManager;
import Controller.ProductsManager;
import Model.Account.Supporter;
import Server.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import graphics.App;
import graphics.LoginAndRegister.CreateAdminAccount;
import graphics.products.ProductPageController;
import graphics.products.ProductsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client extends Application {
    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        if (AdminProfileManager.isThereAdmin()) {
            scene = new Scene(loadFXML("MainMenu").load());
        }
        else {
            scene = new Scene(loadFXML("CreateAdminAccount").load());
            CreateAdminAccount.setParentMenu("MainMenu");
        }
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            if (Connection.getLoggedInAccount() != null) {
                Connection.sendToServer("logout");
            }
            stage.close();
        });
        stage.show();
    }

    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static Runnable connectSupporters = new Runnable() {
        @Override
        public void run() {
            ArrayList<Supporter> removeFromQueue = new ArrayList<>();
            Connection.sendToServer("get customers in queue");
            HashMap<Supporter, String> customersInQueue = new Gson().fromJson(Connection.receiveFromServer(),
                    new TypeToken<HashMap<Supporter, String>>(){}.getType());
            System.out.println("chatclient 5 sec checked:"+customersInQueue);
            System.out.println("0");
            assert customersInQueue != null;
            for (Supporter supporter: customersInQueue.keySet()) {
                System.out.println("0.5");
                if (Connection.getLoggedInAccount().getUsername().equals(supporter.getUsername())) {
                    System.out.println("1");
                    try {
                        new Thread(() -> {
                            try {
                                System.out.println("2");
                                ChatClient.main(Integer.parseInt(customersInQueue.get(supporter)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        System.out.println("3");
                        removeFromQueue.add(supporter);
                        System.out.println("queue:" + customersInQueue);
                        System.out.println("removequeue" + removeFromQueue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(customersInQueue);
            System.out.println("4");
            for (Supporter supporter : removeFromQueue) {
                //Server.getCustomersInQueue().remove(supporter);
                System.out.println("supporter removed:"+customersInQueue);
                Connection.sendToServer("remove supporter from customers in queue: " + supporter.getUsername());
            }
            System.out.println("5");
        }
    };

    public static Object setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = loadFXML(fxml);
        scene.setRoot(fxmlLoader.load());
        return fxmlLoader.getController();
    }

    private static FXMLLoader loadFXML(String fxml){
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }

    public static void main(String[] args) {
        executor.scheduleAtFixedRate(connectSupporters, 5, 5, TimeUnit.SECONDS);
        ProductPageController.productsManager = new ProductsManager();
        ProductsController.productsManager = new ProductsManager();
        launch();
    }
}
