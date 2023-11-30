package graphics.AdminProfile;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.BuyLog;
import Model.Product.Product;
import graphics.AlertBox;
import graphics.App;
import graphics.CustomerProfile.Data;
import graphics.products.ProductPageController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ManageSaleHistory {

    public ImageView backImage;
    public ImageView mainMenuImage;
    public TableView allBuyLogsTable;

    private AdminProfileManager adminProfileManager;

    private static String parentMenu = "AdminProfileMenu";

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        allBuyLogsTable = adminProfileManager.getSaleHistoryTable(allBuyLogsTable);

        App.setBackButton(backImage, parentMenu);
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void send(MouseEvent mouseEvent) {
        Object selectedBuyLog = allBuyLogsTable.getSelectionModel().getSelectedItem();
        if (selectedBuyLog == null) {
            return;
        }
        if (((BuyLog) selectedBuyLog).getSendingCondition().equals("Send")) {
            AlertBox.showMessage("Send Condition", "Your buylog is already send");
            return;
        }
        ((BuyLog) selectedBuyLog).setSendingCondition("Send");
        allBuyLogsTable = adminProfileManager.getSaleHistoryTable(allBuyLogsTable);
    }

    public void ShowProducts(MouseEvent mouseEvent) {
        Object selectedBuyLog = allBuyLogsTable.getSelectionModel().getSelectedItem();
        if (selectedBuyLog == null) {
            return;
        }
        try {
            Stage stage = new Stage();

            stage.setTitle("Order");
            stage.setWidth(550);
            stage.setHeight(500);

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));

            BuyLog buyLog = (BuyLog)selectedBuyLog;

            TableView table = getOrderTable(buyLog);

            final Label label = new Label("Date" + buyLog.getDate() + " " + "Price:" + buyLog.getPaidAmount()+"$" + "\n" + buyLog.getAddress());
            label.setFont(new Font("Arial", 20));

            vbox.getChildren().addAll(label, table);

            Scene scene1 = new Scene(vbox);

            stage.setScene(scene1);
            stage.show();
        } catch (NullPointerException e) {
            AlertBox.showMessage("null exception", "There is no BuyLog");
        }
    }

    private TableView getOrderTable(BuyLog buyLog) {
        TableView table = new TableView<>();
        TableColumn<String, Data> productNameCol = new TableColumn("Product Name");
        productNameCol.setMinWidth(200);
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<String, Data> productNumberCol = new TableColumn("Product Number");
        productNumberCol.setMinWidth(100);
        productNumberCol.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

        TableColumn<String, Data> productSellerCol = new TableColumn("Product Seller");
        productSellerCol.setMinWidth(200);
        productSellerCol.setCellValueFactory(new PropertyValueFactory<>("productSeller"));

        TableColumn<String, Data> productSendCol = new TableColumn("Send Condition");
        productSellerCol.setMinWidth(200);
        productSendCol.setCellValueFactory(new PropertyValueFactory<>("sendCondition"));

        table.getColumns().addAll(productNameCol, productNumberCol, productSellerCol);

        ArrayList<Product> products = new ArrayList<>(buyLog.getBoughtProducts().keySet());
        for (int i = 0; i < products.size(); i++) {
            table.getItems().add(new Data(products.get(i).getProductName(), buyLog.getBoughtProducts().get(products.get(i)).toString(),
                    products.get(i).getSeller().getName(), String.valueOf(products.get(i).isFile())));
        }
        return table;
    }
}
