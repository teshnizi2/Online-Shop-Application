package graphics.AdminProfile.RequestDetails;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Request.EditAddOffRequest;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.ZoneId;

public class EditAddOffRequestMenu {
    public Label requestTitle;
    public TextField requestIDField;
    public TextField requestTypeField;
    public TextField offIDField;
    public TextField offProductsField;
    public TextField offAmountField;
    public DatePicker offStartTimeDatePicker;
    public DatePicker offEndTimeDatePicker;

    private static EditAddOffRequest editAddOffRequest;
    private static String parentMenu = "ManageRequests";
    public ImageView backImage;
    public ImageView mainMenuImage;

    private AdminProfileManager adminProfileManager;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());

        requestIDField.setText(editAddOffRequest.getRequestId());
        requestTypeField.setText(editAddOffRequest.getRequestType());
        offIDField.setText(editAddOffRequest.getOffID());
        offStartTimeDatePicker.setValue(editAddOffRequest.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        offEndTimeDatePicker.setValue(editAddOffRequest.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        offAmountField.setText(String.valueOf(editAddOffRequest.getOffAmount()));
        for (String offProductID : editAddOffRequest.getOffProductIDs()) {
            offProductsField.setText(offProductsField.getText() + offProductID + ", ");
        }
        requestTitle.setText(editAddOffRequest.getRequestType());
        ProductPageController.setMainMenuButton(mainMenuImage);
        App.setBackButton(backImage, "ManageRequests");
    }

    public void declineRequest(MouseEvent mouseEvent) {
        adminProfileManager.declineRequest(editAddOffRequest.getRequestId());
        AlertBox.showMessage("Decline Request", "Request with ID : <" + editAddOffRequest.getRequestId() + "> declined.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(MouseEvent mouseEvent) {
        adminProfileManager.acceptRequest(editAddOffRequest.getRequestId());
        AlertBox.showMessage("Accept Request", "Request with ID : <" + editAddOffRequest.getRequestId() + "> accepted.");
        try {
            App.setRoot(parentMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setEditAddOffRequest(EditAddOffRequest editAddOffRequest) {
        EditAddOffRequestMenu.editAddOffRequest = editAddOffRequest;
    }
}
