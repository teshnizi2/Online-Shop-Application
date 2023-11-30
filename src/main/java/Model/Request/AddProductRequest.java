package Model.Request;

import Model.Account.Seller;
import Model.Product.Category;
import Model.Product.Product;
import Model.Product.ProductStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProductRequest extends EditAddProductRequest {
    private static ArrayList<AddProductRequest> allAddProductRequest = new ArrayList<>();

    //private byte[] image;
    //private byte[] file;
    private String fileName;

    public AddProductRequest() {
        super("add_product_" + allRequests.size(), RequestType.Adding_Product_Request);
        allAddProductRequest.add(this);
        this.setProductStatus(ProductStatus.WAITING_TO_PRODUCE);
    }

    public AddProductRequest(String productID, String productName, String productCompanyName, double productPrice,
                             int productExistingNumber, String productExplanations, Category productCategory,
                             HashMap<String, Integer> productSpecialFeatures, Seller productSeller, String fileName) {
        super("add_product_" + allRequests.size(), RequestType.Adding_Product_Request);
        this.productId = productID;
        this.productName = productName;
        this.productCompanyName = productCompanyName;
        this.productPrice = productPrice;
        this.productExistingNumber = productExistingNumber;
        this.productExplanations = productExplanations;
        this.productCategory = productCategory;
        this.productSpecialFeatures = productSpecialFeatures;
        this.productSeller = productSeller;
        this.fileName = fileName;
        allAddProductRequest.add(this);
    }

    public static ArrayList<AddProductRequest> getAllAddProductRequest() {
        return allAddProductRequest;
    }

    public static void setAllAddProductRequest(ArrayList<AddProductRequest> allAddProductRequest) {
        AddProductRequest.allAddProductRequest = allAddProductRequest;
    }

    @Override
    public void acceptRequest() throws IllegalArgumentException{
        if (Product.getProductByID(productId) == null) {
            Product product = new Product(productId, ProductStatus.CONFIRMED, productName, productCompanyName,
                    productPrice, productExistingNumber, productExplanations,
                    productCategory, productSpecialFeatures, productSeller);
            productCategory.addProductToCategory(product);
            product.setImageAddressInServer("src\\main\\resources\\server\\" + productId + ".jpg");
            product.setImageBytes(imageBytes);
            if (fileName != null && !fileName.isBlank()){
                //product.setFile(file);
                product.setFileAddressInServer("src\\main\\resources\\server\\" + fileName);
                product.setFileName(fileName);
                product.setFile(true);
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "AddProductRequest{" +
                "productId='" + productId + '\'' +
                ", productStatus=" + productStatus +
                ", productName='" + productName + '\'' +
                ", companyName='" + productCompanyName + '\'' +
                ", price=" + productPrice +
                ", existingNumber=" + productExistingNumber +
                ", productSeller=" + productSeller +
                ", productCategory=" + productCategory +
                ", specialFeatures=" + productSpecialFeatures +
                ", requestId='" + requestId + '\'' +
                ", requestType=" + requestType +
                '}';
    }

    @Override
    public void remove() {
        allAddProductRequest.remove(this);
    }

    public void setProductImageBytes(byte[] imageBytes) {

    }
}
