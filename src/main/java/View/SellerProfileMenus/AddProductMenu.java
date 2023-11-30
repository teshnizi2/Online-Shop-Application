package View.SellerProfileMenus;

import Controller.SellerProfileManager;
import Model.Request.AddProductRequest;
import View.Menu;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class AddProductMenu extends Menu {
    private SellerProfileManager sellerProfileManager;
    private AddProductRequest addProductRequest;

    public AddProductMenu(Menu parentMenu, SellerProfileManager sellerProfileManager) {
        super("Add Product Menu", parentMenu);
        this.sellerProfileManager = sellerProfileManager;
        ArrayList<Menu> submenus = new ArrayList<>();
        submenus.add(getAddProductId());
        submenus.add(getAddProductName());
        submenus.add(getAddCompanyName());
        submenus.add(getAddPrice());
        submenus.add(getAddExistingNumberOfProduct());
        submenus.add(getAddProductCategory());
        submenus.add(getAddSpecialFeatureValues());
        this.setSubMenus(submenus);
    }

    @Override
    public void show() {
        if (addProductRequest == null) {
            this.addProductRequest = sellerProfileManager.addProductRequest();
        }
        super.show();
    }

    private Menu getAddProductId() {
        return new Menu("Add ProductId", this) {
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
                    sellerProfileManager.setProductId(addProductRequest, productId);
                    System.out.println("Product ID " + productId + " successfully added to your request.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("There is a Product with this ID.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddProductName() {
        return new Menu("Add Product Name", this) {
            @Override
            public void execute() {
                System.out.println("Enter Product Name:");
                String productName = scanner.nextLine();
                sellerProfileManager.setProductName(addProductRequest, productName);
                if (productName.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (productName.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                sellerProfileManager.setProductName(addProductRequest, productName);
                System.out.println("Product Name " + productName + " successfully added to your request.");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddCompanyName() {
        return new Menu("Add graphics.SellerProfile.Company Name", this) {
            @Override
            public void execute() {
                System.out.println("Enter graphics.SellerProfile.Company Name:");
                String companyName = scanner.nextLine();
                sellerProfileManager.setProductCompanyName(addProductRequest, companyName);
                if (companyName.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (companyName.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                sellerProfileManager.setProductCompanyName(addProductRequest, companyName);
                System.out.println("graphics.SellerProfile.Company Name " + companyName + " successfully added to your request");
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddPrice() {
        return new Menu("Add Price", this) {
            @Override
            public void execute() {
                System.out.println("Enter Product Price:");
                String productPrice = scanner.nextLine();
                if (productPrice.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (productPrice.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setProductPrice(addProductRequest, productPrice);
                    System.out.println("Product Price " + productPrice + " successfully added to your request");
                }
                catch (InputMismatchException e) {
                    System.out.println("You must enter a number.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddExistingNumberOfProduct() {
        return new Menu("Add Existing Number of Product", this) {
            @Override
            public void execute() {
                System.out.println("Enter the Number Of Existing Products:");
                String existingNumber = scanner.nextLine();
                if (existingNumber.equalsIgnoreCase("back")) {
                    parentMenu.execute();
                } else if (existingNumber.equalsIgnoreCase("logout")) {
                    loginAndRegisterManager.logoutUser();
                }
                try {
                    sellerProfileManager.setExistingNumberOfProduct(addProductRequest, existingNumber);
                    System.out.println("Existing Number Of Product " + existingNumber + " successfully added to your request.");
                }
                catch (InputMismatchException e) {
                    System.out.println("You must enter an integer number.");
                }
                this.parentMenu.execute();
            }
        };
    }

    private Menu getAddProductCategory() {
        return new Menu("Add Product Category", this) {
            @Override
            public void execute() {
                System.out.println("Enter Category Name or (back) to return:");
                String categoryName = scanner.nextLine();
                if (categoryName.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                }
                else {
                    try {
                        sellerProfileManager.setProductCategory(addProductRequest, categoryName);
                        System.out.println("Category " + categoryName + " successfully added to your request.");
                    } catch (NullPointerException e) {
                        System.out.println("There is no category with this name.");
                    }
                    this.execute();
                }
            }
        };
    }

    private Menu getAddSpecialFeatureValues() {
        return new Menu("Add Special Feature Values Menu", this) {
            @Override
            public void execute() {
                if (sellerProfileManager.getProductCategoryInRequest(addProductRequest) == null) {
                    System.out.println("You must first add category for this product.");
                    getAddProductCategory().execute();
                }
                else {
                    ArrayList<String> categorySpecialFeatures = sellerProfileManager.getProductCategorySpecialFeatures(addProductRequest);
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
                            sellerProfileManager.setValueForProductSpecialFeature(addProductRequest, Integer.parseInt(index), Integer.parseInt(value));
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
