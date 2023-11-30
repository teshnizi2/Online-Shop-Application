package graphics.SellerProfile.Company;

import Controller.SellerProfileManager;
import Model.Account.Account;
import Model.Account.Seller;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AddCompanyInformation {
    public TextField phoneNumber;
    public TextField address;
    public TextField openYear;


    public ImageView backImage;
    public ImageView mainMenuImage;

    private SellerProfileManager sellerProfileManager;

    public void initialize() {
        this.sellerProfileManager = new SellerProfileManager((Seller) Account.getLoggedInAccount());
        String parentMenu = "SellerProfileMenu";
        App.setBackButton(backImage, parentMenu);
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void submitInformationButton(MouseEvent mouseEvent) {
        sellerProfileManager.editPhoneNumberOfCompany(phoneNumber.getText());
        sellerProfileManager.editCompanyAddress(address.getText());
        sellerProfileManager.editCompanyOpenYear(openYear.getText());
        AlertBox.showMessage("", "Your Information Submitted");
    }
}
