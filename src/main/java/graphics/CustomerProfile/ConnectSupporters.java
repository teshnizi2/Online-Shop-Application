package graphics.CustomerProfile;

import Client.Connection;
import Controller.AdminProfileManager;
import Controller.CustomerProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.Customer;
import Model.Account.Supporter;
import Model.Request.*;
import com.google.gson.Gson;
import graphics.AdminProfile.RequestDetails.*;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ConnectSupporters {

    public TableView allSupportersTable;
    public ImageView backImage;
    public ImageView mainMenuImage;

    private CustomerProfileManager customerProfileManager;

    private static String parentMenu = "CustomerProfileMenu";

    public void initialize() {
        this.customerProfileManager = new CustomerProfileManager((Customer) Account.getLoggedInAccount());
        allSupportersTable = customerProfileManager.getAllSupportersTable(allSupportersTable);

        App.setBackButton(backImage, parentMenu);
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void connect(MouseEvent mouseEvent) throws Exception {
        Object selectedSupporter = allSupportersTable.getSelectionModel().getSelectedItem();
        if (selectedSupporter == null) {
            return;
        }
        if (!((Supporter) selectedSupporter).isOnline()) {
            AlertBox.showMessage("Supporter Condition", "Supporter is offline now!");
            return;
        }
        int supporterID = ((Supporter) selectedSupporter).getSupporterID();
        customerProfileManager.connectSupporter(supporterID);
    }

}
