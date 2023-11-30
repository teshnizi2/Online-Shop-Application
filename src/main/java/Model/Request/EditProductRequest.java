package Model.Request;

import Model.Account.Seller;
import Model.Product.Category;
import Model.Product.Product;
import Model.Product.ProductStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class EditProductRequest extends EditAddProductRequest {
    private static ArrayList<EditProductRequest> allEditProductRequests = new ArrayList<>();
    private Product product;

    public EditProductRequest(Product product) {
        super("edit_product_" + allRequests.size(), RequestType.Editing_Product_Request);
        this.setProduct(product);
        this.setProductId(product.getProductId());
        this.setProductName(product.getProductName());
        this.setProductStatus(ProductStatus.WAITING_TO_EDIT);
        this.setProductCompanyName(product.getCompanyName());
        this.setProductPrice(product.getPrice());
        this.setProductExistingNumber(product.getExistingNumber());
        this.setProductExplanations(product.getExplanations());
        this.setProductCategory(product.getProductCategory());
        this.setProductSpecialFeatures(product.getSpecialFeatures());
        this.setProductSeller(product.getSeller());
    }

    public EditProductRequest(Product product, String productID, String productName, String productCompanyName, double productPrice,
                             int productExistingNumber, String productExplanations, String productImageAddress,
                             Category productCategory, HashMap<String, Integer> productSpecialFeatures, Seller productSeller) {
        super("add_product_" + allRequests.size(), RequestType.Adding_Product_Request);
        this.product = product;
        this.productId = productID;
        this.productName = productName;
        this.productCompanyName = productCompanyName;
        this.productPrice = productPrice;
        this.productExistingNumber = productExistingNumber;
        this.productExplanations = productExplanations;
        this.productImageAddress = productImageAddress;
        this.productCategory = productCategory;
        this.productSpecialFeatures = productSpecialFeatures;
        this.productSeller = productSeller;
        allEditProductRequests.add(this);
    }

    public EditProductRequest(){
        this(null);
    }

    public static ArrayList<EditProductRequest> getAllEditProductRequests() {
        return allEditProductRequests;
    }

    public static void setAllEditProductRequests(ArrayList<EditProductRequest> allEditProductRequests) {
        EditProductRequest.allEditProductRequests = allEditProductRequests;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void acceptRequest() throws IllegalArgumentException{
        if (Product.getProductByID(productId) != null && (!(Product.getProductByID(productId)).equals(product))) {
            throw new IllegalArgumentException();
        }
        else {
            product.setProductId(productId);
            product.setProductName(productName);
            product.setProductStatus(ProductStatus.CONFIRMED);
            product.setCompanyName(productCompanyName);
            product.setPrice(productPrice);
            product.setExistingNumber(productExistingNumber);
            product.setExplanations(productExplanations);
            product.setProductCategory(productCategory);
            product.setSpecialFeatures(productSpecialFeatures);
            if (!(productCategory.getProducts().contains(product))) {
                productCategory.addProductToCategory(product);
            }
        }
    }

    @Override
    public String toString() {
        return "EditProductRequest{" +
                "product=" + product +
                ", productId='" + productId + '\'' +
                ", productStatus=" + productStatus +
                ", productName='" + productName + '\'' +
                ", companyName='" + productCompanyName + '\'' +
                ", price=" + productPrice +
                ", existingNumber=" + productExistingNumber +
                ", productSeller=" + productSeller +
                ", requestId='" + requestId + '\'' +
                ", requestType=" + requestType +
                '}';
    }

    @Override
    public void remove() {
        allEditProductRequests.remove(this);
    }
}
