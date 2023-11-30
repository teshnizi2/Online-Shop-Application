package graphics.AdminProfile.Discounts;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.Customer;
import Model.Account.Discount;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class ManageDiscountsMenu {
    public TextField includingCustomersField;
    public DatePicker startTimeDate;
    public TextField discountPerCustomerField;
    public TextField maxPossibleDiscountField;
    public TextField discountPercentField;
    public TextField discountCodeField;
    public DatePicker endTimeDate;

    public TableView allDiscountsTable;
    public TableView notIncludingCustomers;
    public ImageView backImage;
    public ImageView mainMenuImage;

    private ArrayList<String> includingCustomers;

    private static String parentMenu = "AdminProfileMenu";

    private AdminProfileManager adminProfileManager;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        {
            notIncludingCustomers = new TableView();
        }
        allDiscountsTable = adminProfileManager.getAllDiscountsTable(allDiscountsTable);
        notIncludingCustomers = adminProfileManager.getAllCustomersTable(notIncludingCustomers);

        includingCustomers = new ArrayList<>();
        App.setBackButton(backImage, "AdminProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void showDetails() {
        if (allDiscountsTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Discount selectedDiscount = (Discount) allDiscountsTable.getSelectionModel().getSelectedItem();

        discountCodeField.setText(selectedDiscount.getDiscountCode());
        startTimeDate.setValue(selectedDiscount.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        endTimeDate.setValue(selectedDiscount.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        maxPossibleDiscountField.setText(String.valueOf(selectedDiscount.getMaxPossibleDiscount()));
        discountPercentField.setText(String.valueOf(selectedDiscount.getDiscountPercent()));
        discountPerCustomerField.setText(String.valueOf(selectedDiscount.getDiscountPerCustomer()));

        includingCustomers = selectedDiscount.getIncludingCustomerUsername();
        includingCustomersField.setText("");
        for (String s : selectedDiscount.getIncludingCustomerUsername()) {
            includingCustomersField.setText(includingCustomersField.getText() + s + ", ");
        }

        for (String includingCustomer : includingCustomers) {
            notIncludingCustomers.getItems().remove(Account.getAccountByUsername(includingCustomer));
        }
    }

    public void confirm(MouseEvent mouseEvent) {
        String discountCode = discountCodeField.getText();
        //todo: checking this
        //Date startTime = new Date(startTimeDate.getValue().toEpochDay());
        //Date endTime = new Date(endTimeDate.getValue().toEpochDay());
        Date startTime = Date.from(startTimeDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(endTimeDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String discountPercent = discountPercentField.getText();
        String maxPossibleDiscount = maxPossibleDiscountField.getText();
        String discountPerCustomer = discountPerCustomerField.getText();
        String includingCustomersFieldText = includingCustomersField.getText();
        boolean checkConfirmButtonInability;
        checkConfirmButtonInability = discountCode.isEmpty() || startTimeDate.getValue() == null || endTimeDate.getValue() == null || discountPercent.isEmpty() || maxPossibleDiscount.isEmpty() || discountPerCustomer.isEmpty() || includingCustomersFieldText.isEmpty();
        if (!(checkConfirmButtonInability)) {
            changeStartTime(discountCode, startTime);
            changeEndTime(discountCode, endTime);
            changeDiscountPercent(discountCode, discountPercent);
            changeMaxPossibleDiscount(discountCode, maxPossibleDiscount);
            changeIncludingCustomers(discountCode, includingCustomers);
        }
    }

    private void changeStartTime(String discountCode, Date newStartTime) {
        adminProfileManager.editDiscountStartTime(discountCode, newStartTime);
        AlertBox.showMessage("Edit", "Start Time Successfully Updated");
    }

    private void changeEndTime(String discountCode, Date newEndTime) {
        adminProfileManager.editDiscountEndTime(discountCode, newEndTime);
        AlertBox.showMessage("Edit", "End Time Successfully Updated");
    }

    private void changeDiscountPercent(String discountCode, String newDiscountPercent) {
        try {
            adminProfileManager.editDiscountPercent(discountCode, newDiscountPercent);
            AlertBox.showMessage("Edit", "Discount Percent Successfully Updated");
        } catch (Exception e) {
            AlertBox.showMessage("Failed Editing", e.getMessage());
        }
    }

    private void changeMaxPossibleDiscount(String discountCode, String maxPossibleDiscount) {
        try {
            adminProfileManager.editDiscountMaxPossibleDiscount(discountCode, maxPossibleDiscount);
            AlertBox.showMessage("Edit", "Maximum Possible Discount Successfully Updated");
        } catch (Exception e) {
            AlertBox.showMessage("Failed Editing", e.getMessage());
        }
    }

    private void changeIncludingCustomers(String discountCode, ArrayList<String> includingCustomers) {
        try {
            adminProfileManager.editDiscountIncludingCustomers(discountCode, includingCustomers);
            AlertBox.showMessage("Edit", "Including Customers Successfully Updated");
        } catch (Exception e) {
            AlertBox.showMessage("Failed Editing", e.getMessage());
        }
    }

    public void removeDiscount(MouseEvent mouseEvent) {
        Object selectedItem = allDiscountsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        Discount selectedDiscount = (Discount) selectedItem;
        adminProfileManager.removeDiscount(selectedDiscount.getDiscountCode());
        allDiscountsTable.getItems().remove(selectedDiscount);
        discountCodeField.setText("");
        startTimeDate.setValue(null);
        endTimeDate.setValue(null);
        discountPercentField.setText("");
        maxPossibleDiscountField.setText("");
        discountPerCustomerField.setText("");
        includingCustomersField.setText("");
        includingCustomers.clear();
    }

    public void clearIncludingCustomers(MouseEvent mouseEvent) {
        notIncludingCustomers.getItems().clear();
        for (String customerID : includingCustomers) {
            notIncludingCustomers.getItems().add((Customer) Customer.getCustomerById(customerID));
        }
        includingCustomers.clear();
        includingCustomersField.setText("");
    }

    public void showNotIncludingCustomers(MouseEvent mouseEvent) {
        VBox vBox = new VBox();
        vBox.setSpacing(20);

        Button addCustomerButton = new Button("Add Customer");
        addCustomerButton.setFont(Font.font("Times New Roman", 16));
        addCustomerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Object selectedItem = notIncludingCustomers.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    return;
                }
                Customer customer = (Customer) selectedItem;
                includingCustomers.add(customer.getUsername());
                includingCustomersField.setText(includingCustomersField.getText() + customer.getUsername() + ", ");
                notIncludingCustomers.getItems().remove(customer);
            }
        });
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(notIncludingCustomers, addCustomerButton);
        Stage window = new Stage();
        window.setTitle("Not Including Customers");
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }
}
