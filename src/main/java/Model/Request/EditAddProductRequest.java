package Model.Request;

import Model.Account.Seller;
import Model.Product.Category;
import Model.Product.ProductStatus;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EditAddProductRequest extends Request {
    private static ArrayList<EditAddProductRequest> allEditAddProductRequests = new ArrayList<>();
    protected String productId;
    protected ProductStatus productStatus;
    protected String productName;
    protected String productCompanyName;
    protected double productPrice;
    protected int productExistingNumber;
    protected String productExplanations;
    protected String productImageAddress;
    protected Seller productSeller;
    protected Category productCategory;
    protected HashMap<String, Integer> productSpecialFeatures;

    protected byte[] imageBytes;

    public EditAddProductRequest(String requestId, RequestType requestType) {
        super(requestId, requestType);
        allEditAddProductRequests.add(this);
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public String getProductId() {
        return productId;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCompanyName() {
        return productCompanyName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductExistingNumber() {
        return productExistingNumber;
    }

    public Seller getProductSeller() {
        return productSeller;
    }

    public HashMap<String, Integer> getProductSpecialFeatures() {
        return productSpecialFeatures;
    }

    public String getProductExplanations() {
        return productExplanations;
    }

    public String getProductImageAddress() {
        return productImageAddress;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductCompanyName(String productCompanyName) {
        this.productCompanyName = productCompanyName;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductExistingNumber(int productExistingNumber) {
        this.productExistingNumber = productExistingNumber;
    }

    public void setProductSeller(Seller productSeller) {
        this.productSeller = productSeller;
    }

    public void setProductCategory(Category productCategory) {
        this.productCategory = productCategory;
    }

    public void setProductSpecialFeatures(HashMap<String, Integer> productSpecialFeatures) {
        this.productSpecialFeatures = productSpecialFeatures;
    }

    public void setProductSpecialFeatureValue(String key, int value) {
        this.productSpecialFeatures.replace(key, value);
    }

    public void setProductExplanations(String productExplanations) {
        this.productExplanations = productExplanations;
    }

    public void setProductImageAddress(String productImageAddress) {
        this.productImageAddress = productImageAddress;
    }

    @Override
    public abstract void acceptRequest();

    @Override
    public abstract String toString();

    public static EditAddProductRequest getRequestById(String requestId) {
        for (EditAddProductRequest editAddProductRequest : allEditAddProductRequests) {
            if (editAddProductRequest.getRequestId().equals(requestId)) {
                return editAddProductRequest;
            }
        }
        return null;
    }
}
