package graphics.CustomerProfile;

import Controller.CustomerProfileManager;
import Model.Account.Account;
import Model.Account.Customer;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CustomerWalletMenu {
    public TextField walletBalanceField;
    public TextField chargeWalletAmount;

    public ImageView backImage;
    public ImageView mainMenuImage;

    private CustomerProfileManager customerProfileManager;

    public void initialize() {
        this.customerProfileManager = new CustomerProfileManager((Customer) Account.getLoggedInAccount());
        walletBalanceField.setText(String.valueOf(customerProfileManager.getBalance()));
        ProductPageController.setMainMenuButton(mainMenuImage);
        App.setBackButton(backImage, "CustomerProfileMenu");
    }

    public void chargeWallet(MouseEvent mouseEvent) {
        String chargeAmount = chargeWalletAmount.getText();
        try {
            customerProfileManager.chargeWallet(Integer.parseInt(chargeAmount));
            walletBalanceField.setText(String.valueOf(customerProfileManager.getBalance()));
        } catch (Exception e) {
            AlertBox.showMessage("Failed to charge", e.getMessage());
        }
    }
}
