package graphics.AdminProfile.Categories;

import Controller.AdminProfileManager;
import Model.Account.Account;
import Model.Account.Admin;
import Model.Product.Category;
import graphics.AlertBox;
import graphics.App;
import graphics.products.ProductPageController;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ManageCategories {
    public TableView allCategoriesTable;

    public TextField categoryNameField;

    public TextField specialFeaturesField;
    public ListView specialFeaturesList;

    public Button confirmButton;

    public Button removeSpecialFeatureButton;
    public Button addSpecialFeatureButton;

    public ImageView backImage;
    public ImageView mainMenuImage;

    private Category selectedCategory;

    private AdminProfileManager adminProfileManager;

    public void initialize() {
        this.adminProfileManager = new AdminProfileManager((Admin) Account.getLoggedInAccount());
        allCategoriesTable = adminProfileManager.getAllCategoriesTable(allCategoriesTable);

        App.setBackButton(backImage, "AdminProfileMenu");
        ProductPageController.setMainMenuButton(mainMenuImage);
    }

    public void showCategoryDetails(MouseEvent mouseEvent) {
        Object selectedItem = allCategoriesTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        Category category = (Category) selectedItem;
        categoryNameField.setText(category.getName());
        specialFeaturesField.setEditable(true);
        specialFeaturesList.getItems().setAll(category.getSpecialFeatures());

        this.selectedCategory = category;

        setEditionMode();
    }

    public void removeCategory(MouseEvent mouseEvent) {
        Object selectedCategory = allCategoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            return;
        }
        Category category = (Category) selectedCategory;
        adminProfileManager.removeCategory(category.getName());
        AlertBox.showMessage("Remove Category", category.getName() + " Category Successfully Removed");
        allCategoriesTable.getItems().remove(category);
    }

    public void addFeature(MouseEvent mouseEvent) {
        String specialFeature = specialFeaturesField.getText();
        if (specialFeature.isEmpty() || specialFeature.trim().isEmpty()) {
            return;
        }
        adminProfileManager.addSpecialFeature(selectedCategory, specialFeature);
        specialFeaturesField.setText("");
        specialFeaturesList.getItems().add(specialFeature);
    }

    public void removeSpecialFeature(MouseEvent mouseEvent) {
        Object selectedSpecialFeature = specialFeaturesList.getSelectionModel().getSelectedItem();
        if (selectedSpecialFeature == null) {
            return;
        }
        String specialFeature = (String) selectedSpecialFeature;
        adminProfileManager.removeCategorySpecialFeature(selectedCategory, specialFeature);
        specialFeaturesList.getItems().remove(specialFeature);
    }

    public void confirmEdition(MouseEvent mouseEvent) {
        categoryNameField.setText("");
        selectedCategory = null;
        specialFeaturesList.getItems().clear();

        setViewMode();
    }

    private void setEditionMode() {
        confirmButton.setDisable(false);
        removeSpecialFeatureButton.setDisable(false);
        addSpecialFeatureButton.setDisable(false);
    }

    private void setViewMode() {
        confirmButton.setDisable(true);
        removeSpecialFeatureButton.setDisable(true);
        addSpecialFeatureButton.setDisable(true);
        allCategoriesTable.getItems().clear();
        for (Category category : adminProfileManager.getAllCategories()) {
            allCategoriesTable.getItems().add(category);
        }
    }
}
