package graphics.CustomerProfile;

import Controller.CustomerProfileManager;
import Model.Account.*;
import Model.Product.Product;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CustomerProfileMenu {
    public Button confirmButton;
    public TextField phoneNumberField;
    public TextField emailField;
    public TextField lastNameField;
    public TextField firstNameField;
    public TextField passwordField;
    public TextField usernameField;
    public Label balanceLabel;
    public ImageView backImage;
    public ImageView mainMenuImage;
    public ImageView cartImage;
    public Tab showOrdersTab;

    private static String parentMenu;
    public TextField orderIDForShowOrder;

    private CustomerProfileManager customerProfileManager;

    public void initialize() {
        this.customerProfileManager = new CustomerProfileManager((Customer) Account.getLoggedInAccount());
        usernameField.setText(customerProfileManager.getUsername());
        passwordField.setText(customerProfileManager.getPassword());
        firstNameField.setText(customerProfileManager.getFirstName());
        lastNameField.setText(customerProfileManager.getLastName());
        emailField.setText(customerProfileManager.getEmail());
        phoneNumberField.setText(customerProfileManager.getPhoneNumber());
        balanceLabel.setText(customerProfileManager.viewBalance() + "$");
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
            customerProfileManager.editPassword(password);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit password", e.getMessage());
        }
    }

    private void changeFirstName() {
        String firstName = firstNameField.getText();
        try {
            customerProfileManager.editFirstName(firstName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit first name.", e.getMessage());
        }
    }

    private void changeLastName() {
        String lastName = lastNameField.getText();
        try {
            customerProfileManager.editLastName(lastName);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit last name", e.getMessage());
        }
    }

    private void changeEmail() {
        String email = emailField.getText();
        try {
            customerProfileManager.editEmail(email);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit email", e.getMessage());
        }
    }

    private void changePhoneNumber() {
        String phoneNumber = phoneNumberField.getText();
        try {
            customerProfileManager.editPhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            AlertBox.showMessage("Failed to edit phone number", e.getMessage());
        }
    }

    public static void setParentMenu(String parentMenu) {
        CustomerProfileMenu.parentMenu = parentMenu;
    }

    public void showOrderByID(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();

            stage.setTitle("Order");
            stage.setWidth(550);
            stage.setHeight(500);

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));

            String orderID =  orderIDForShowOrder.getText();
            BuyLog buyLog = customerProfileManager.getBuyLogByID(orderID);

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
        productSellerCol.setCellValueFactory(new PropertyValueFactory<>("sendCondition"));



        table.getColumns().addAll(productNameCol, productNumberCol, productSellerCol);

        ArrayList<Product> products = new ArrayList<>(buyLog.getBoughtProducts().keySet());
        for (int i = 0; i < products.size(); i++) {
            table.getItems().add(new Data(products.get(i).getProductName(), buyLog.getBoughtProducts().get(products.get(i)).toString(),
                    products.get(i).getSeller().getName(), String.valueOf(products.get(i).isFile())));
        }
        return table;
    }

    public void showAllOrders(MouseEvent mouseEvent) throws Exception{
        try {
            TableView table = getAllOrders();
            final Label label = new Label("Orders ID And Date");
            label.setFont(new Font("Arial", 20));
            Stage stage = new Stage();

            stage.setTitle("Orders");
            stage.setWidth(600);
            stage.setHeight(500);

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(label, table);

            Scene scene1 = new Scene(vbox);

            stage.setScene(scene1);
            stage.show();
        } catch (NullPointerException e) {
            AlertBox.showMessage("null exception", "There is no BuyLog");
        }

    }

    private TableView getAllOrders() {
        TableView table = new TableView<>();
        TableColumn<String, OrderData> orderIdCol = new TableColumn("Order ID");
        orderIdCol.setMinWidth(300);
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<String, OrderData> orderDateCol = new TableColumn("Date");
        orderDateCol.setMinWidth(300);
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(orderIdCol, orderDateCol);

        ArrayList<BuyLog> buyLogs = customerProfileManager.getBuyLogs();
        for (int i = 0; i < buyLogs.size(); i++) {
            table.getItems().add(new OrderData(buyLogs.get(i).getID(),buyLogs.get(i).getDate().toString()));
        }
        table.setPlaceholder(new Label("No Data To Display"));
        return table;
    }

    public void ShowDiscountCodes(MouseEvent mouseEvent) {
        try{
            TableView table = getAllDiscountsForCustomer();
            final Label label = new Label("Discount Codes");
            label.setFont(new Font("Arial", 20));
            Stage stage = new Stage();

            stage.setTitle("Discount Codes");
            stage.setWidth(650);
            stage.setHeight(500);

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(label, table);

            Scene scene1 = new Scene(vbox);

            stage.setScene(scene1);
            stage.show();
        } catch (NullPointerException e) {
            AlertBox.showMessage("null exception", "There is no BuyLog");
        }
    }

    private TableView getAllDiscountsForCustomer() {
        TableView table = new TableView<>();
        TableColumn<String, DiscountData> discountIdCol = new TableColumn("ID");
        discountIdCol.setMinWidth(150);
        discountIdCol.setCellValueFactory(new PropertyValueFactory<>("discountId"));

        TableColumn<String, DiscountData> discountStartTimeCol = new TableColumn("Start");
        discountStartTimeCol.setMinWidth(100);
        discountStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("discountStartTime"));

        TableColumn<String, DiscountData> discountEndTimeCol = new TableColumn("End");
        discountEndTimeCol.setMinWidth(100);
        discountEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("discountEndTime"));

        TableColumn<String, DiscountData> percentCol = new TableColumn("Percent");
        percentCol.setMinWidth(100);
        percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

        TableColumn<String,
                DiscountData> maxPossibleUsageCol = new TableColumn("MaxPossibleUsage");
        maxPossibleUsageCol.setMinWidth(100);
        maxPossibleUsageCol.setCellValueFactory(new PropertyValueFactory<>("discountPerCustomer"));

        table.getColumns().addAll(discountIdCol, discountStartTimeCol, discountEndTimeCol, percentCol, maxPossibleUsageCol);

        ArrayList<Discount> discounts = customerProfileManager.getAllDiscountCodesForCustomer();
        for (int i = 0; i < discounts.size(); i++) {
            Discount discount = discounts.get(i);
            table.getItems().add(new DiscountData(discount.getDiscountCode(),discount.getStartTime().toString(),
                    discount.getEndTime().toString(),Double.toString(discount.getDiscountPercent()),
                    String.valueOf(discount.getDiscountPerCustomer())));
        }
        return table;
    }

    public void connectSupporters(MouseEvent mouseEvent) {
        try {
            App.setRoot("ConnectSupporters");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCustomerWalletMenu(MouseEvent mouseEvent) {
        try {
            App.setRoot("CustomerWalletMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}