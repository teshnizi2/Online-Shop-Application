package Model.Product;

import Model.Account.*;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Product {
    private static ArrayList<Product> allProducts = new ArrayList<>();
    private String productId;
    private String imageAddressInServer;
    private byte[] imageBytes;
    private ProductStatus productStatus; 
    private String productName;
    private String companyName;
    private double price;
    private int existingNumber;
    private Category productCategory;
    private HashMap<String, Integer> specialFeatures;
    private String explanations;
    private Discount discount;
    private ArrayList<Score> allScores;
    private ArrayList<Comment> productComments;
    private String productSeller;
    private int visitNumber;
    private Date timeOfCreation;
    private ArrayList<String> productBuyers;
    private Off off;
    private boolean isFile;
    private String fileAddressInServer;
    private byte[] file;
    private String fileName;
    private boolean isInAction;
    private HashMap<String, Double> customersAmountForAction;
    private Date endOfAction;

    private static ArrayList<String> productFields = new ArrayList<>();
    static {
        productFields.add("productId");
        productFields.add("productStatus");
        productFields.add("productName");
        productFields.add("companyName");
        productFields.add("price");
        productFields.add("ProductSeller");
        productFields.add("existingNumber");
    }

    {
        allScores = new ArrayList<>();
        productComments = new ArrayList<>();
        productBuyers = new ArrayList<>();
    }

    public Product(String productId, ProductStatus productStatus, String productName, String companyName, double price,
                   int existingNumber, String explanations, Category productCategory,
                   HashMap<String, Integer> specialFeatures, Seller productSeller) {
        this.productId = productId;
        this.productStatus = productStatus;
        this.productName = productName;
        this.companyName = companyName;
        this.price = price;
        this.existingNumber = existingNumber;
        this.explanations = explanations;
        this.productCategory = productCategory;
        this.specialFeatures = specialFeatures;
        this.productSeller = productSeller.getUsername();
        productSeller.getProductIDs().add(productId);

        if (specialFeatures != null) {
            this.specialFeatures = specialFeatures;
        }
        else {
            this.specialFeatures = new HashMap<>();
        }
        timeOfCreation = new Date();
        customersAmountForAction = new HashMap<>();
        allProducts.add(this);
    }

    public Product() {
        allProducts.add(this);
        customersAmountForAction = new HashMap<>();
        this.specialFeatures = new HashMap<>();
    }

    public void setCustomersAmountForAction(HashMap<String, Double> customersAmountForAction) {
        this.customersAmountForAction = customersAmountForAction;
    }

    public String getImageAddressInServer() {
        return imageAddressInServer;
    }

    public void setImageAddressInServer(String imageAddressInServer) {
        this.imageAddressInServer = imageAddressInServer;
    }

    public String getFileAddressInServer() {
        return fileAddressInServer;
    }

    public void setFileAddressInServer(String fileAddressInServer) {
        this.fileAddressInServer = fileAddressInServer;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public void addASpecialFeature(String feature, int amount){
        specialFeatures.putIfAbsent(feature, amount);
    }

    public HashMap<String, Integer> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(HashMap<String, Integer> specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Image getImage() {
        //return new Image(imageBytes);
        String address = "src\\main\\resources\\Images\\Client Images" + productName + getProductSeller() + ".jpg";
        //File file = new File(address);
        try (FileOutputStream fileOutputStream = new FileOutputStream(address)){
            fileOutputStream.write(imageBytes);
            return new Image("file:" + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Image("file:src\\main\\resources\\Images\\Client Images\\unKnown.jpg");
    }

    public void addRate(Customer customer, int score){
        Score newScore = new Score(customer, this, score);
        this.allScores.add(newScore);
    }

    public void setASpecialFeature(String feature, int value) throws Exception {
        if (specialFeatures.containsKey(feature)){
            specialFeatures.put(feature, value);
        }
        else {
            throw new Exception("This product doesn't have such a feature");
        }
    }

    public void addAComment(Comment comment){
        productComments.add(comment);
    }

    public int getValueOfAFeature(String feature) throws Exception {
        if (specialFeatures.containsKey(feature)){
            return specialFeatures.get(feature);
        }
        else {
            throw new Exception("This product doesn't have such a feature");
        }
    }

    public void removeSpecialFeature(String specialFeature) {
        specialFeatures.remove(specialFeature);
    }

    public Off getOff() {
        return off;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getExistingNumber() {
        return existingNumber;
    }

    public void setExistingNumber(int existingNumber) {
        this.existingNumber = existingNumber;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Category productCategory) {
        this.productCategory = productCategory;
    }

    public ArrayList<Score> getAllScores() {
        return allScores;
    }

    public void setAllScores(ArrayList<Score> allScores) {
        this.allScores = allScores;
    }

    public ArrayList<Comment> getProductComments() {
        return productComments;
    }

    public void setProductComments(ArrayList<Comment> productComments) {
        this.productComments = productComments;
    }

    public Seller getSeller() {
        return Seller.getSellerByUserName(productSeller);
    }

    public void setProductSeller(Seller productSeller) {
        this.productSeller = productSeller.getUsername();
    }

    public int getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(int visitNumber) {
        this.visitNumber = visitNumber;
    }

    public Date getTimeOfCreation() {
        return timeOfCreation;
    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public String getExplanations() {
        return explanations;
    }

    public void setExplanations(String explanations) {
        this.explanations = explanations;
    }

    public static void setAllProducts(ArrayList<Product> allProducts) {
        Product.allProducts = allProducts;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public void setTimeOfCreation(Date timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public ArrayList<String> getProductBuyers() {
        return productBuyers;
    }

    public static ArrayList<String> getProductFields() {
        return productFields;
    }

    public double getTotalScore() {
        if (allScores.size() == 0)
            return 0;
        double productScore = 0;
        for (Score score : allScores) {
            productScore += score.getScore();
        }
        return productScore / allScores.size();
    }

    public double getPriceWithOff(){
        if (off != null && off.isAvailable()){
            return price * (100 - off.getOffAmount()) / 100;
        }
        return price;

    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOff(Off off) {
        this.off = off;
    }

    public void setProductBuyers(ArrayList<String> productBuyers) {
        this.productBuyers = productBuyers;
    }

    public boolean isInAction() {
        return isInAction;
    }

    public void setInAction(boolean inAction) {
        isInAction = inAction;
    }

    public HashMap<String, Double> getCustomersAmountForAction() {
        return customersAmountForAction;
    }

    public Date getEndOfAction() {
        return endOfAction;
    }

    public void setEndOfAction(Date endOfAction) {
        this.endOfAction = endOfAction;
    }

    public boolean isActionAvailable(){
        return isInAction && (new Date().getTime() < getEndOfAction().getTime());
    }

    //todo: checking this
    public void removeCategory(Category category) {
        productCategory = null;
        specialFeatures.clear();
    }

    public static void setProductFields(ArrayList<String> productFields) {
        Product.productFields = productFields;
    }

    public static Product getProductByID(String ID){
        for (Product product : allProducts) {
            if (product.productId.equals(ID)){
                return product;
            }
        }
        return null;
    }

    //todo: checking this, removing from offs, discounts and categories?
    public static void removeProduct(Product product) {
        Seller seller = Seller.getSellerByUserName(product.getProductSeller());
        seller.getProductIDs().remove(product.getProductId());
        product.getProductCategory().removeProduct(product);
        allProducts.remove(product);
    }

    public void digest(){
        System.out.printf("%s: %s%n%s: %f%n%s: %d%n%s: %f %s: %f%n%s: %s%n%s: %s%n%s: %f%n%n",
                "explanations", explanations,
                "price", price,
                "Off Percentage", (off == null || !off.isAvailable() ? 0 : off.getOffAmount()),
                "discount percentage", discount.getDiscountPercent(), "max discount amount", discount.getMaxPossibleDiscount(),
                "category", productCategory.getName(),
                "seller", Seller.getSellerByUserName(productSeller).getName(),
                "average score", getTotalScore());
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productId='" + productId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", Original Price=" + price +
                ", price after off=" + getPriceWithOff() +
                ", score=" + getTotalScore() +
                ", 'does" + (existingNumber != 0 ? "" :  "not") + "exist'" +
                '}';
    }

    public void endAuction() {
        setInAction(false);
        HashMap<String, Double> auction = getCustomersAmountForAction();
        double maxPrice = 0;
        String winner = "";
        for (String username : auction.keySet()) {
            if (auction.get(username) > maxPrice){
                maxPrice = auction.get(username);
                winner = username;
            }
        }
        Customer customer = Customer.getCustomerById(winner);
        customer.setBalance(customer.getBalance() - maxPrice);
        Seller seller = getSeller();
        seller.setBalance((int)(seller.getBalance() + maxPrice));
        auction.clear();
    }
}
