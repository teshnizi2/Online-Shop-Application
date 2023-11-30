package graphics.AdminProfile.Discounts;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.Customer;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class CreateDiscountCode {
    private static String parentMenu = "AdminProfileMenu";
    public ImageView backImage;
    public ImageView mainMenuImage;
    private AdminProfileManager adminProfileManager;

    public TableView customerTable;

    public TextField discountCodeField;
    public TextField discountPercentField;
    public TextField maxPossibleDiscountField;
    public TextField discountPerCustomerField;
    public DatePicker startTimeDate;
    public DatePicker endTimeDate;
    public TextField includingCustomersField;

    private ArrayList<String> includingCustomers;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        customerTable = adminProfileManager.getAllCustomersTable(customerTable);
        includingCustomers = new ArrayList<>();
        App.setBackButton(backImage, "AdminProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void confirm() {
        String discountCode = discountCodeField.getText();
        //todo: checking this
        //Date startTime = new Date(startTimeDate.getValue().toEpochDay());
        Date startTime = Date.from(startTimeDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        //Date endTime = new Date(endTimeDate.getValue().toEpochDay());
        Date endTime = Date.from(endTimeDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String discountPercent = discountPercentField.getText();
        String maxPossibleDiscount = maxPossibleDiscountField.getText();
        String discountPerCustomer = discountPerCustomerField.getText();
        String includingCustomersFieldText = includingCustomersField.getText();
        boolean checkConfirmButtonInability;
        checkConfirmButtonInability = discountCode.isEmpty() || startTimeDate.getValue() == null || endTimeDate.getValue() == null || discountPercent.isEmpty() || maxPossibleDiscount.isEmpty() || discountPerCustomer.isEmpty() || includingCustomersFieldText.isEmpty();
        if (!(checkConfirmButtonInability)) {
            try {
                adminProfileManager.createDiscountCode(discountCode, startTime, endTime, discountPercent, maxPossibleDiscount, discountPerCustomer, includingCustomers);
                AlertBox.showMessage("Create Discount Code", "Discount Code : " + discountCode + " successfully created.");
                try {
                    App.setRoot(parentMenu);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                AlertBox.showMessage("Failed to Create", e.getMessage());
            }
        }
    }

    public void addCustomerToDiscount(MouseEvent mouseEvent) {
        Object selectedUser = customerTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            return;
        }
        String customerUsername = ((Customer) selectedUser).getUsername();
        includingCustomers.add(customerUsername);

        includingCustomersField.setText(includingCustomersField.getText() + (customerUsername + ", "));
        customerTable.getItems().remove(selectedUser);
    }

    public void clearIncludingCustomers(MouseEvent mouseEvent) {
        customerTable.getItems().clear();
        for (String customerID : includingCustomers) {
            customerTable.getItems().add((Customer) Customer.getCustomerById(customerID));
        }
        includingCustomers.clear();
        includingCustomersField.setText("");
    }
}
