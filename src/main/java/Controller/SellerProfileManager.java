package Controller;

import Client.Connection;
import Model.Account.*;
import Model.Request.*;
import Model.Product.Category;
import Model.Product.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

public class SellerProfileManager extends ProfileManager {
    public Seller seller;

    public SellerProfileManager(Seller seller) {
        super(seller);
        this.seller = seller;
    }

    public static Category getCategoryByName(String categoryName) {
        Connection.sendToServer("get category: " + categoryName);
        Category category = new Gson().fromJson(Connection.receiveFromServer(), Category.class);
        return category;
    }

    public double getBalance() {
        Connection.sendToServerWithToken("get account balance");
        double sellerBalance = Double.parseDouble(Connection.receiveFromServer());
        return sellerBalance;
    }

    public double getBankAccountBalance() {
        Connection.sendToServerWithToken("get bank account balance");
        double sellerBankAccountBalance = Double.parseDouble(Connection.receiveFromServer());
        return sellerBankAccountBalance;
    }

    public void chargeWallet(int chargeAmountMoney) throws Exception{
        double sellerBankAccountBalance = getBankAccountBalance();
        if (sellerBankAccountBalance > chargeAmountMoney) {
            Connection.sendToServerWithToken("charge wallet: " + chargeAmountMoney);
        }
        else {
            throw new Exception("You don't have enough money in your bank account");
        }
    }

    public void withdrawFromWallet(int withdrawAmountMoney) throws Exception {
        int minWalletBalance = AdminProfileManager.getMinWalletBalance();
        double sellerBalance = getBalance();
        if ((sellerBalance - withdrawAmountMoney) < minWalletBalance) {
            throw new Exception("Wallet cannot contain less than " + minWalletBalance);
        }
        Connection.sendToServerWithToken("withdraw from wallet: " + withdrawAmountMoney);//todo: !!
    }

    public String getCompanyName() {
        Connection.sendToServerWithToken("get company name: ");
        String companyName = Connection.receiveFromServer();
        return companyName;
    }

    public void editCompanyName(String companyName) {
        Connection.sendToServerWithToken("edit company name: " + companyName);
        //this.seller.setNameOfCompany(companyName);
    }

    public String getCompanyPhoneNumber() {
        Connection.sendToServerWithToken("get company phone number: ");
        String companyPhoneNumber = Connection.receiveFromServer();
        return companyPhoneNumber;
    }

    public String getCompanyOpenYear() {
        Connection.sendToServerWithToken("get company open year: ");
        String companyOpenYear = Connection.receiveFromServer();
        return companyOpenYear;
    }

    public String getCompanyAddress() {
        Connection.sendToServerWithToken("get company address: ");
        String companyAddress = Connection.receiveFromServer();
        return companyAddress;
    }

    public void editPhoneNumberOfCompany(String companyPhoneNumber) {
        Connection.sendToServerWithToken("edit company phone number: " + companyPhoneNumber);
    }

    public void editCompanyAddress(String companyAddress) {
        Connection.sendToServerWithToken("edit company address: " + companyAddress);
    }

    public void editCompanyOpenYear(String companyOpenYear) {
        Connection.sendToServerWithToken("edit company open year: " + companyOpenYear);
    }

    public TableView getSellerProductsTable(TableView sellerProductsTable) {
        TableColumn<String, Product> column = new TableColumn<>("Product ID");
        column.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<String, Product> column1 = new TableColumn<>("Product Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("productName"));

        sellerProductsTable.getColumns().addAll(column, column1);

        Connection.sendToServerWithToken("get seller product IDs: ");
        ArrayList<String> sellerProducts = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<String>>(){}.getType());
        for (String productID : sellerProducts) {
            Product product = Connection.getProduct(productID);
            sellerProductsTable.getItems().add(product);
        }
        sellerProductsTable.setPlaceholder(new Label("No Data To Display"));
        return sellerProductsTable;
    }

    public TableView getSellerOffsTable(TableView offsTable) {
        TableColumn<String, Off> column = new TableColumn<>("Off ID");
        column.setCellValueFactory(new PropertyValueFactory<>("offID"));

        offsTable.getColumns().add(column);

        Connection.sendToServerWithToken("get seller offs: ");
        ArrayList<Off> sellerOffs = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Off>>(){}.getType());
        for (Off sellerOff : sellerOffs) {
            offsTable.getItems().add(sellerOff);
        }
        offsTable.setPlaceholder(new Label("No Data To Display"));
        return offsTable;
    }

    public TableView getProductBuyersTable(String productId) {
        TableView productBuyersTable = new TableView();

        TableColumn<String, Customer> column1 = new TableColumn<>("Username");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<String, Customer> column2 = new TableColumn<>("First Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, Customer> column3 = new TableColumn<>("Last Name");
        column3.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<String, Customer> column4 = new TableColumn<>("Email");
        column4.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<String, Customer> column5 = new TableColumn<>("Phone Number");
        column5.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        productBuyersTable.getColumns().addAll(column1, column2, column3, column4, column5);

        Connection.sendToServer("get product buyers: " + productId);
        ArrayList<String> productBuyers = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<String>>(){}.getType());
        for (String username : productBuyers) {
            Connection.sendToServer("get customer: " + username);
            Customer customer = new Gson().fromJson(Connection.receiveFromServer(), Customer.class);
            productBuyersTable.getItems().add(customer);
        }
        productBuyersTable.setPlaceholder(new Label("No Data to display"));
        return productBuyersTable;
    }

    public TableView getAllCategoriesTable() {
        TableView allCategoriesTable = new TableView();

        TableColumn<String, Category> column = new TableColumn<>("Category Name");
        column.setCellValueFactory(new PropertyValueFactory<>("name"));

        allCategoriesTable.getColumns().add(column);

        Connection.sendToServer("getCategories");
        ArrayList<Category> allCategories = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Category>>(){}.getType());
        for (Category category : allCategories) {
            allCategoriesTable.getItems().add(category);
        }
        allCategoriesTable.setPlaceholder(new Label("No Data To Display"));
        return allCategoriesTable;
    }

    public ArrayList<Category> getAllCategories() {
        Connection.sendToServer("getCategories");
        ArrayList<Category> allCategories = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Category>>(){}.getType());
        return allCategories;
    }

    public TableView getSellerSalesHistoryTable() {
        TableView sellerSalesHistoryTable = new TableView();

        //todo: check These columns
        TableColumn<String, SellLog> column = new TableColumn<>("Sell Log ID");
        column.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<Double, SellLog> column1 = new TableColumn<>("Received Amount");
        column1.setCellValueFactory(new PropertyValueFactory<>("received"));

        TableColumn<Double, SellLog> column2 = new TableColumn<>("Off Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("offAmount"));

        TableColumn<Product, SellLog> column3 = new TableColumn<>("Product");
        column3.setCellValueFactory(new PropertyValueFactory<>("product"));

        TableColumn<String, SellLog> column4 = new TableColumn<>("Buyer Name");
        column4.setCellValueFactory(new PropertyValueFactory<>("buyerName"));

        TableColumn<Boolean, SellLog> column5 = new TableColumn<>("Has Sent");
        column5.setCellValueFactory(new PropertyValueFactory<>("hasSent"));

        TableColumn<Integer, SellLog> column6 = new TableColumn<>("Number");
        column6.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Date, SellLog> column7 = new TableColumn<>("Date");
        column7.setCellValueFactory(new PropertyValueFactory<>("date"));

        sellerSalesHistoryTable.getColumns().addAll(column, column1, column2, column3, column4, column5, column6, column7);

        Connection.sendToServerWithToken("get seller sellLogs: ");
        ArrayList<SellLog> sellerSellLogs = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<SellLog>>(){}.getType());
        for (SellLog sellLog : sellerSellLogs) {
            sellerSalesHistoryTable.getItems().add(sellLog);
        }
        sellerSalesHistoryTable.setPlaceholder(new Label("No Data To Display"));
        sellerSalesHistoryTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        return sellerSalesHistoryTable;
    }

    public Product getProductByID(String id) {
        Product product = Connection.getProduct(id);
        return product;
    }

    public void makeNewAddProductRequest(String productID, String productName, String productCompanyName,
                                         String productPrice, String productExistingNumber, String productExplanations,
                                         Category productCategory, ArrayList<String> specialFeatureValues,
                                         byte[] image, byte[] file, String fileType) throws Exception{
        ArrayList<String> specialFeatures = productCategory.getSpecialFeatures();
        ArrayList<Integer> values = getSpecialFeatureValuesInInteger(specialFeatureValues);
        HashMap<String, Integer> productSpecialFeatures = new HashMap<>();
        int i = 0;
        for (String specialFeature : specialFeatures) {
            productSpecialFeatures.put(specialFeature, values.get(i));
            i++;
        }
        if (checkProductIDValidity(productID) && checkProductNameValidity(productName) &&
                checkProductCompanyName(productCompanyName) && checkProductPrice(productPrice) &&
                checkProductExistingNumber(productExistingNumber) && checkProductExplanations(productExplanations)) {
            String message = "add product request: &" + productID + "&" + productName + "&";
            message = message + productCompanyName + "&" + productPrice + "&" + productExistingNumber + "&";
            message = message + productExplanations + "&" + new Gson().toJson(productCategory) + "&";
            message = message + new Gson().toJson(productSpecialFeatures) + "&";
            message = message + seller.getUsername() + "&" + fileType + "&";
            /*message = message + Arrays.toString(image);
            if (file != null) {
                message = message + "&" + Arrays.toString(file);
            }*/
            Connection.sendToServer(message);
            //Connection.sendToServer(image);
            Socket clientSocket = new Socket("localhost", 8000);
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataOutputStream.writeUTF(String.valueOf(image.length));
            dataOutputStream.write(image);
            //dataOutputStream.flush();
            if (file != null){
                dataOutputStream.writeUTF(String.valueOf(file.length));
                dataOutputStream.write(file);
            }
            //dataOutputStream.flush();
        }
    }

    private boolean checkProductIDValidity(String productID) throws Exception {
        Connection.sendToServer("get product: " + productID);
        Product product = new Gson().fromJson(Connection.receiveFromServer(), Product.class);
        if (product == null && !(productID.trim().isEmpty())) {
            return true;
        }
        else {
            throw new Exception("There is another product with this ID");
        }
    }

    private boolean checkProductNameValidity(String productName) throws Exception {
        if (productName.trim().isEmpty()) {
            throw new Exception("Invalid Product Name");
        }
        else {
            return true;
        }
    }

    private boolean checkProductCompanyName(String productCompanyName) throws Exception {
        if (productCompanyName.trim().isEmpty()) {
            throw new Exception("Invalid Company Name");
        } else {
            return true;
        }
    }

    private boolean checkProductPrice(String productPrice) throws Exception {
        try {
            Double.parseDouble(productPrice);
            return true;
        } catch (Exception e) {
            throw new Exception("Invalid Product Price");
        }
    }

    private boolean checkProductExistingNumber(String productExistingNumber) throws Exception {
        try {
            Integer.parseInt(productExistingNumber);
            return true;
        } catch (Exception e) {
            throw new Exception("Invalid Product Existing Number");
        }
    }

    private boolean checkProductExplanations(String productExplanations) throws Exception {
        if (productExplanations.trim().isEmpty()) {
            throw new Exception("Invalid Explanations");
        } else {
            return true;
        }
    }

    private ArrayList<Integer> getSpecialFeatureValuesInInteger(ArrayList<String> specialFeatureValues) throws Exception{
        ArrayList<Integer> values = new ArrayList<>();
        for (String specialFeatureValue : specialFeatureValues) {
            try {
                values.add(Integer.parseInt(specialFeatureValue));
            } catch (Exception e) {
                throw new Exception("Invalid Value For Special Feature");
            }
        }
        return values;
    }

    public void makeNewEditProductRequest(String productID, String productName, String productCompanyName,
                                         String productPrice, String productExistingNumber, String productExplanations,
                                         Category productCategory, ArrayList<String> specialFeatureValues,
                                         String productImageAddress) throws Exception{
        try {
            ArrayList<String> specialFeatures = productCategory.getSpecialFeatures();
            ArrayList<Integer> values = getSpecialFeatureValuesInInteger(specialFeatureValues);
            HashMap<String, Integer> productSpecialFeatures = new HashMap<>();
            int i = 0;
            for (String specialFeature : specialFeatures) {
                productSpecialFeatures.put(specialFeature, values.get(i));
                i++;
            }
            if (checkProductNameValidity(productName) &&
                    checkProductCompanyName(productCompanyName) && checkProductPrice(productPrice) &&
                    checkProductExistingNumber(productExistingNumber) && checkProductExplanations(productExplanations)) {
                String message = "edit product request: &";
                message = message + productID + "&" + productName + "&" + productCompanyName + "&";
                message = message + productPrice + "&" + productExistingNumber + "&" + productExplanations + "&";
                message = message + productImageAddress + "&" + new Gson().toJson(productCategory) + "&";
                message = message + new Gson().toJson(productSpecialFeatures) + "&";
                message = message + seller.getUsername();
                Connection.sendToServer("edit product request: " + message);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void removeProductRequest(String productId) {
        Connection.sendToServer("remove product request: " + productId);
    }

    public void makeNewAddOffRequest(String offID, Date offStartTime, Date offEndTime, String offAmount, ArrayList<String> offProductIDs) throws Exception {
        try {
            if (checkOffAmountValidity(offAmount) && checkOffIDValidity(offID)) {
                String message = "add off request: &" + offID + "&";
                message = message + new Gson().toJson(offStartTime) + "&" + new Gson().toJson(offEndTime) + "&";
                message = message + offAmount + "&";
                message = message + new Gson().toJson(offProductIDs) + "&" + new Gson().toJson(this.seller.getUsername());
                Connection.sendToServerWithToken("add off request: " + message);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void makeNewEditOffRequest(String offID, Date offStartTime, Date offEndTime, String offAmount, ArrayList<String> offProductIDs) throws Exception {
        try {
            if (checkOffAmountValidity(offAmount)) {
                String message = "edit off request: " + offID + "&";
                message = message + new Gson().toJson(offStartTime) + "&" + new Gson().toJson(offEndTime) + "&";
                message = message + offAmount + "&" + new Gson().toJson(offProductIDs);
                Connection.sendToServer("edit off request: " + message);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean checkOffIDValidity(String offID) throws Exception{
        Connection.sendToServer("get off: " + offID);
        Off off = new Gson().fromJson(Connection.receiveFromServer(), Off.class);
        if (off != null) {
            throw new Exception("There is another off with this ID");
        }
        else {
            return true;
        }
    }

    private boolean checkOffAmountValidity(String offAmount) throws Exception {
        try {
            int offAmountValue = Integer.parseInt(offAmount);
            if (offAmountValue > 100 || offAmountValue < 0) {
                throw new Exception("Off Amount should be between 0 and 100");
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Invalid Off Amount");
        }
    }

    public static Off getOffByID(String offID) {
        Connection.sendToServer("get off: " + offID);
        Off off = new Gson().fromJson(Connection.receiveFromServer(), Off.class);
        return off;
    }







    //phase1
    public HashMap<String, String> getCompanyInformation() {
        HashMap <String, String> companyInformation = new HashMap<String, String>();
        companyInformation.put("Name", seller.getNameOfCompany());
        return companyInformation;
    }

    public ArrayList<String> getSalesHistory() {
        ArrayList<String> salesHistory = new ArrayList<>();
        for (SellLog sellLog : seller.getSellLogs()) {
            salesHistory.add(sellLog.toString());
        }
        return salesHistory;
    }

    public void addSellLog(double received, double offAmount, Product product, int number, String buyerName) {
        String sellLogID = seller.getUsername() + seller.getSellLogs().size();
        SellLog sellLog = new SellLog(sellLogID, new Date(), received, offAmount, product, number, buyerName);
        seller.getSellLogs().add(sellLog);
    }

    /*public ArrayList<String> getSellerProducts() {
        ArrayList<Product> sellerProducts = seller.getProducts();
        ArrayList<String> sellerProductsIds = new ArrayList<>();
        for (Product product : sellerProducts) {
            sellerProductsIds.add(product.getProductId());
        }
        return sellerProductsIds;
    }*/

    public HashMap<String, String> getProductBuyers(String productId) {
        HashMap<String, String> allBuyersUsernameAndPhoneNumber = new HashMap<>();
        for (String userName : Product.getProductByID(productId).getProductBuyers()) {
            Customer customer = Customer.getCustomerById(userName);
            allBuyersUsernameAndPhoneNumber.put(userName,customer.getPhoneNumber());
        }
        return allBuyersUsernameAndPhoneNumber;
    }

    public static ArrayList<String> getAllProductFields() {
        return Product.getProductFields();
    }

    public static boolean isProductIdFormatValid(String productId) {
        if (Product.getProductByID(productId) == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isInputInOffFields(String input) {
        for (int i = 0; i < Off.getOffFields().size(); i++) {
            if (Off.getOffFields().get(i).equals(input)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInputInProductFields(String input) {
        for (int i = 0; i < Product.getProductFields().size(); i++) {
            if (Product.getProductFields().get(i).equals(input)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidInputForOffID (String ID) {
        for (String offID : Off.getAllOffIds()) {
            if (offID.equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, String> getOffsAmountAndID() {
        HashMap<String, String> offsAmountAndID = new HashMap<>();
        for (int i = 0; i < seller.getOffs().size(); i ++) {
            offsAmountAndID.put(String.valueOf(seller.getOffs().get(i).getOffAmount()), seller.getOffs().get(i).getOffID());
        }
        return offsAmountAndID;
    }

    /*public HashMap<String, String> getSellerProductsNameAndID() {
        HashMap<String, String> SellerProductsNameAndID = new HashMap<>();
        for (int i = 0; i < seller.getProducts().size(); i ++) {
            SellerProductsNameAndID.put(String.valueOf(seller.getProducts().get(i).getProductName()), seller.getProducts().get(i).getProductId());
        }
        return SellerProductsNameAndID;
    }

    public AddOffRequest addOffRequest () {
        return new AddOffRequest();
    }*/

    public EditOffRequest makeNewEditOffRequest(String offID) throws NullPointerException, IllegalArgumentException {
        Off off = Off.getOffById(offID);
        if (off == null) {
            throw new NullPointerException();
        }
        else if (this.seller.getOffs().contains(off)) {
            return new EditOffRequest();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void setOffId(EditAddOffRequest editAddOffRequest, String offId) throws IllegalArgumentException{
        if (Off.getOffById(offId) == null) {
            editAddOffRequest.setOffID(offId);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void setOffStartTime(EditAddOffRequest editAddOffRequest, Date startTime) {
        editAddOffRequest.setStartTime(startTime);
    }

    public void setOffEndTime(EditAddOffRequest editAddOffRequest, Date endTime) {
        editAddOffRequest.setEndTime(endTime);
    }

    public void setOffAmount(EditAddOffRequest editAddOffRequest, String offAmount) throws InputMismatchException{
        if (offAmount.matches("\\d+")) {
            editAddOffRequest.setOffAmount(Integer.parseInt(offAmount));
        }
        else {
            throw new InputMismatchException();
        }
    }

    /*public void setOffProduct(EditAddOffRequest editAddOffRequest, String productID) throws NullPointerException, IllegalArgumentException {
        Product product = Product.getProductByID(productID);
        if (product == null) {
            throw new NullPointerException();
        }
        else if (!(seller.getProducts().contains(product))) {
            throw new IllegalArgumentException();
        }
        else {
            editAddOffRequest.addOffProduct(product);
        }
    }*/

    public void removeProductInOffRequest(EditOffRequest editOffRequest, String productID) throws NullPointerException, IllegalArgumentException {
        Product product = Product.getProductByID(productID);
        if (product == null) {
            throw new NullPointerException();
        }
        else if (editOffRequest.getOffProductIDs().contains(product)) {
            editOffRequest.removeProduct(product);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void setProductId(EditAddProductRequest editAddProductRequest, String productId) throws IllegalArgumentException {
        if (Product.getProductByID(productId) != null) {
            throw new IllegalArgumentException();
        }
        editAddProductRequest.setProductId(productId);
    }

    public void setProductName(EditAddProductRequest editAddProductRequest, String productName) {
        editAddProductRequest.setProductName(productName);
    }

    public void setProductCompanyName(EditAddProductRequest editAddProductRequest, String companyName) {
        editAddProductRequest.setProductCompanyName(companyName);
    }

    public void setProductPrice(EditAddProductRequest editAddProductRequest, String producePrice) throws InputMismatchException {
        editAddProductRequest.setProductPrice(Double.parseDouble(producePrice));
    }

    public void setExistingNumberOfProduct(EditAddProductRequest editAddProductRequest, String existingNumber) throws InputMismatchException {
        editAddProductRequest.setProductExistingNumber(Integer.parseInt(existingNumber));
    }

    public void setProductCategory(EditAddProductRequest editAddProductRequest, String categoryName) throws NullPointerException{
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) {
            throw new NullPointerException();
        }
        editAddProductRequest.setProductCategory(category);
        HashMap<String, Integer> productSpecialFeatures = new HashMap<>();
        for (String specialFeature : category.getSpecialFeatures()) {
            productSpecialFeatures.put(specialFeature, 0);
        }
        editAddProductRequest.setProductSpecialFeatures(productSpecialFeatures);
    }

    public Category getProductCategoryInRequest(EditAddProductRequest editAddProductRequest) {
        return editAddProductRequest.getProductCategory();
    }

    public ArrayList<String> getProductCategorySpecialFeatures(EditAddProductRequest editAddProductRequest) {
        return editAddProductRequest.getProductCategory().getSpecialFeatures();
    }

    public void setValueForProductSpecialFeature(EditAddProductRequest editAddProductRequest, int keyIndex, int value) {
        Category category = editAddProductRequest.getProductCategory();
        editAddProductRequest.setProductSpecialFeatureValue(category.getSpecialFeatures().get(keyIndex), value);
    }

    public int viewBalance(Account account) {
        return 0;
    }

    public AddProductRequest addProductRequest() {
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setProductSeller(this.seller);
        return addProductRequest;
    }

    /*public EditProductRequest makeNewEditProductRequest(String productId) throws NullPointerException, IllegalArgumentException{
        Product product = Product.getProductByID(productId);
        if (product == null){
            throw new NullPointerException();
        }
        else if (seller.getProducts().contains(product)) {
            EditProductRequest editProductRequest = new EditProductRequest(product);
            editProductRequest.setProductSeller(this.seller);
            return editProductRequest;
        }
        else {
            throw new IllegalArgumentException();
        }
    }*/
}
