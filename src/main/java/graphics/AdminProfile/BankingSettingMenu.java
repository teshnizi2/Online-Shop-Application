package graphics.AdminProfile;

import Client.Connection;
import Controller.AdminProfileManager;
import Model.Account.Admin;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class BankingSettingMenu {
    public TextField minWalletBalanceField;
    public TextField bankingFeePercentField;

    public ImageView backImage;
    public ImageView mainMenuImage;

    private static String parentMenu;

    private AdminProfileManager adminProfileManager;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Connection.getLoggedInAccount());
        minWalletBalanceField.setText(String.valueOf(AdminProfileManager.getMinWalletBalance()));
        bankingFeePercentField.setText(String.valueOf(AdminProfileManager.getBankingFeePercent()));
        ProductPageController.setMainMenuButton(mainMenuImage);
        App.setBackButton(backImage, "AdminProfileMenu");
    }

    public void confirm(MouseEvent mouseEvent) {
        String minWalletBalance = minWalletBalanceField.getText();
        String bankingFeePercent = bankingFeePercentField.getText();
        try {
            adminProfileManager.setMinWalletBalance(Integer.parseInt(minWalletBalance));
            adminProfileManager.setBankingFeePercent(Integer.parseInt(bankingFeePercent));
        } catch (Exception e) {
            AlertBox.showMessage("Failed To Edit", e.getMessage());
        }
    }

    public static void setParentMenu(String parentMenu) {
        BankingSettingMenu.parentMenu = parentMenu;
    }
}
