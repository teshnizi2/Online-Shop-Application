package View.SellerProfileMenus;

import Controller.SellerProfileManager;
import Model.Request.EditProductRequest;
import View.Menu;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class EditProductMenu extends Menu {
    private SellerProfileManager sellerProfileManager;
    private EditProductRequest editProductRequest;

    public EditProductMenu(Menu parentMenu, SellerProfileManager sellerProfileManager) {
        super("Edit Product Menu", parentMenu);
        this.sellerProfileManager = sellerProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getEditProductId());
        submenus.add(getEditProductName());
        submenus.add(getEditCompanyName());
        submenus.add(getEditPrice());
        submenus.add(getEditExistingNumberOfProduct());
        submenus.add(getEditProductCategory());
        submenus.add(getEditSpecialFeatureValues());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        System.out.println("Enter ID of Product you want to edit, (back) to return or (logout) to log out::");
        String productId = scanner.nextLine();
        if (productId.equalsIgnoreCase("back")) {
            parentMenu.execute();
        } else if (productId.equalsIgnoreCase("logout")) {
            loginAndRegisterManager.logoutUser();
        }
        try {
            //this.editProductRequest = sellerProfileManager.makeNewEditProductRequest(productId);
            super.show();
        }
        catch (NullPointerException e) {
            System.out.println("There is no Product with this ID.");
            this.show();
        }
        catch (IllegalArgumentException e) {
            System.out.println("You don't have this product.");
            this.show();
        }
    }

    private Menu getEditProductId() {
        return new Menu("Edit ProductId", this) {
            @Override
            public void execute() {
                System.out.println("Enter the Product ID, (back) to return or (logout) to log out::");
                String productId = scanner.nextLine();
                if (productId.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (productId.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setProductId(editProductRequest, productId);
                    System.out.println("Product ID " + productId + " successfully added to your request.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("There is a Product with this ID.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditProductName() {
        return new Menu("Edit Product Name", this) {
            @Override
            public void execute() {
                System.out.println("Enter Product Name, (back) to return or (logout) to log out::");
                String productName = scanner.nextLine();
                if (productName.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (productName.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                sellerProfileManager.setProductName(editProductRequest, productName);
                System.out.println("Product Name " + productName + " successfully added to your request.");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditCompanyName() {
        return new Menu("Edit graphics.SellerProfile.Company Name", this) {
            @Override
            public void execute() {
                System.out.println("Enter graphics.SellerProfile.Company Name, (back) to return or (logout) to log out::");
                String companyName = scanner.nextLine();
                sellerProfileManager.setProductCompanyName(editProductRequest, companyName);
                if (companyName.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (companyName.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                sellerProfileManager.setProductCompanyName(editProductRequest, companyName);
                System.out.println("graphics.SellerProfile.Company Name " + companyName + " successfully added to your request");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditPrice() {
        return new Menu("Edit Price", this) {
            @Override
            public void execute() {
                System.out.println("Enter Product Price, (back) to return or (logout) to log out::");
                String productPrice = scanner.nextLine();
                if (productPrice.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (productPrice.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setProductPrice(editProductRequest, productPrice);
                    System.out.println("Product Price " + productPrice + " successfully added to your request");
                }
                catch (InputMismatchException e) {
                    System.out.println("You must enter a number.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditExistingNumberOfProduct() {
        return new Menu("Edit Existing Number of Product", this) {
            @Override
            public void execute() {
                System.out.println("Enter the Number Of Existing Products, (back) to return or (logout) to log out::");
                String existingNumber = scanner.nextLine();
                if (existingNumber.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (existingNumber.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setExistingNumberOfProduct(editProductRequest, existingNumber);
                    System.out.println("Existing Number Of Product " + existingNumber + " successfully added to your request.");
                }
                catch (InputMismatchException e) {
                    System.out.println("You must enter an integer number.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getEditProductCategory() {
        return new Menu("Edit Product Category", this) {
            @Override
            public void execute() {
                System.out.println("Enter Category Name or (back) to return:");
                String categoryName = scanner.nextLine();
                if (categoryName.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        sellerProfileManager.setProductCategory(editProductRequest, categoryName);
                        System.out.println("Category " + categoryName + " successfully added to your request.");
                    } catch (NullPointerException e) {
                        System.out.println("There is no category with this name.");
                    }
                    this.execute();
                }
            }
        };
    }

    private Menu getEditSpecialFeatureValues() {
        return new Menu("Edit Special Feature Values Menu", this) {
            @Override
            public void execute() {
                if (sellerProfileManager.getProductCategoryInRequest(editProductRequest) == null) {
                    System.out.println("You must first Edit category for this product.");
                    getEditProductCategory().execute();
                }
                else {
                    ArrayList<String> categorySpecialFeatures = sellerProfileManager.getProductCategorySpecialFeatures(editProductRequest);
                    for (int i = 0 ; i < categorySpecialFeatures.size() ; i++) {
                        System.out.println((i + 1) + ". " + categorySpecialFeatures.get(i));
                    }
                    System.out.println("Enter the number of feature or (back) to return:");
                    String index = scanner.nextLine();
                    if (index.equalsIgnoreCase("back")) {
                        this.parentMenu.execute();
                    }
                    else if (index.matches("\\d+") && Integer.parseInt(index) <= categorySpecialFeatures.size()) {
                        System.out.println("Now enter value for this specialFeature:");
                        String value = scanner.nextLine();
                        if (value.matches("\\d+")) {
                            sellerProfileManager.setValueForProductSpecialFeature(editProductRequest, Integer.parseInt(index), Integer.parseInt(value));
                            System.out.println("Value " + Integer.parseInt(value) + " added to " + categorySpecialFeatures.get(Integer.parseInt(index)));
                        }
                        else {
                            System.out.println("You must enter an integer number");
                        }
                    }
                    else {
                        System.out.println("You must enter the index of the feature.");
                    }
                }
                this.execute();
            }
        };
    }
}
