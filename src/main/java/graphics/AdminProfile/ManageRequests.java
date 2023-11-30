package graphics.AdminProfile;

import Client.Connection;
import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
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

public class ManageRequests {

    public TableView allRequestsTable;
    public ImageView backImage;
    public ImageView mainMenuImage;

    private AdminProfileManager adminProfileManager;

    private static String parentMenu = "AdminProfileMenu";

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        allRequestsTable = adminProfileManager.getAllRequestsTable(allRequestsTable);

        App.setBackButton(backImage, parentMenu);
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void acceptRequest(MouseEvent mouseEvent) {
        Object selectedRequest = allRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            return;
        }

        String selectedRequestID = ((Request) selectedRequest).getRequestId();
        adminProfileManager.acceptRequest(selectedRequestID);
        AlertBox.showMessage("Accept Request", "Request with ID : <" + selectedRequestID + "> accepted.");
        allRequestsTable.getItems().remove(selectedRequest);
    }

    public void showRequestDetails(MouseEvent mouseEvent) {
        Object selectedRequest = allRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            return;
        }
        Request request = (Request) selectedRequest;
        if (request instanceof EditAddOffRequest) {
            try {
                EditAddOffRequest editAddOffRequest;
                if (request instanceof EditOffRequest) {
                    Connection.sendToServer("get editOffRequest: " + request.getRequestId());
                    editAddOffRequest = new Gson().fromJson(Connection.receiveFromServer(), EditOffRequest.class);
                }
                else {
                    Connection.sendToServer("get addOffRequest: " + request.getRequestId());
                    editAddOffRequest = new Gson().fromJson(Connection.receiveFromServer(), AddOffRequest.class);
                }
                EditAddOffRequestMenu.setEditAddOffRequest(editAddOffRequest);
                App.setRoot("EditAddOffRequestMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request instanceof EditAddProductRequest) {
            try {
                EditAddProductRequest editAddProductRequest;
                if (request instanceof AddProductRequest) {
                    Connection.sendToServer("get addProductRequest: " + request.getRequestId());
                    editAddProductRequest = new Gson().fromJson(Connection.receiveFromServer(), AddProductRequest.class);
                }
                else {
                    Connection.sendToServer("get editProductRequest: " + request.getRequestId());
                    editAddProductRequest = new Gson().fromJson(Connection.receiveFromServer(), EditProductRequest.class);
                }
                EditAddProductRequestMenu.setEditAddProductRequest(editAddProductRequest);
                App.setRoot("EditAddProductRequestMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request instanceof RegisterSellerRequest) {
            try {
                Connection.sendToServer("get registerSellerRequest: " + request.getRequestId());
                RegisterSellerRequest registerSellerRequest = new Gson().fromJson(Connection.receiveFromServer(), RegisterSellerRequest.class);
                RegisterSellerRequestMenu.setRegisterSellerRequest(registerSellerRequest);
                App.setRoot("RegisterSellerRequestMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request instanceof RegisterSupporterRequest) {
            try {
                Connection.sendToServer("get registerSupporterRequest: " + request.getRequestId());
                RegisterSupporterRequest registerSupporterRequest = new Gson().fromJson(Connection.receiveFromServer(), RegisterSupporterRequest.class);
                RegisterSupporterRequestMenu.setRegisterSupporterRequest(registerSupporterRequest);
                App.setRoot("RegisterSupporterRequestMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request instanceof RemoveProductRequest) {
            try {
                Connection.sendToServer("get removeProductRequest: " + request.getRequestId());
                RemoveProductRequest removeProductRequest = new Gson().fromJson(Connection.receiveFromServer(), RemoveProductRequest.class);
                RemoveProductRequestMenu.setRemoveProductRequest(removeProductRequest);
                App.setRoot("RemoveProductRequestMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void declineRequest(MouseEvent mouseEvent) {
        Object selectedRequest = allRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            return;
        }
        String selectedRequestID = ((Request) selectedRequest).getRequestId();
        adminProfileManager.declineRequest(selectedRequestID);
        AlertBox.showMessage("Decline Request", "Request with ID : <" + selectedRequestID + "> declined.");
        allRequestsTable.getItems().remove(selectedRequest);
    }
}
