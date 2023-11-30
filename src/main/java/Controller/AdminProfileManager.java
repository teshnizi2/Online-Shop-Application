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

import java.util.*;

public class AdminProfileManager extends ProfileManager {

    public AdminProfileManager(Admin admin) {
        super(admin);
    }

    public static int getMinWalletBalance() {
        Connection.sendToServer("get min wallet balance");
        int minWalletBalance = Integer.parseInt(Connection.receiveFromServer());
        return minWalletBalance;
    }

    public static int getBankingFeePercent() {
        Connection.sendToServer("get banking fee percent");
        int bankingFeePercent = Integer.parseInt(Connection.receiveFromServer());
        return bankingFeePercent;
    }

    public void setMinWalletBalance(int minWalletBalance) {
        Connection.sendToServer("edit min wallet balance: " + minWalletBalance);
    }

    public void setBankingFeePercent(int bankingFeePercent) {
        Connection.sendToServer("edit banking fee percent: " + bankingFeePercent);
    }

    public static boolean isThereAdmin() {
        Connection.sendToServer("isThereAdmin");
        String response = Connection.receiveFromServer();
        if (response.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
    }

    public TableView getAllUsersTable() {
        TableView allUsers = new TableView();

        TableColumn<String, Account> column1 = new TableColumn<>("Username");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<String, Account> column2 = new TableColumn<>("First Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, Account> column3 = new TableColumn<>("Last Name");
        column3.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<String, Account> column4 = new TableColumn<>("Email");
        column4.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<String, Account> column5 = new TableColumn<>("Phone Number");
        column5.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<String, Account> column6 = new TableColumn<>("Status");
        column6.setCellValueFactory(new PropertyValueFactory<>("online"));

        allUsers.getColumns().addAll(column1, column2, column3, column4, column5, column6);

        Connection.sendToServer("getCustomers");
        ArrayList<Customer> allCustomers = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Customer>>(){}.getType());

        Connection.sendToServer("getSupporters");
        ArrayList<Supporter> allSupporters = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Supporter>>(){}.getType());

        Connection.sendToServer("getSellers");
        ArrayList<Seller> allSellers = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Seller>>(){}.getType());

        Connection.sendToServer("getAdmins");
        ArrayList<Admin> allAdmins = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Admin>>(){}.getType());

        ArrayList<Account> allAccounts = new ArrayList<>();
        for (Admin admin : allAdmins) {
            allAccounts.add(admin);
        }
        for (Seller seller : allSellers) {
            allAccounts.add(seller);
        }
        for (Customer customer : allCustomers) {
            allAccounts.add(customer);
        }
        for (Supporter supporter : allSupporters) {
            allAccounts.add(supporter);
        }
        for (Account account : allAccounts) {
            if (account.equals(Account.getLoggedInAccount())) {
                continue;
            }
            allUsers.getItems().add(account);
        }
        allUsers.setPlaceholder(new Label("No Data to display"));
        allUsers.setPrefSize(600, 600);
        return allUsers;
    }

    public TableView getAllCustomersTable(TableView allCustomersTable) {
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

        allCustomersTable.getColumns().addAll(column1, column2, column3, column4, column5);

        Connection.sendToServer("getCustomers");
        ArrayList<Customer> allCustomers = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Customer>>(){}.getType());
        for (Customer customer : allCustomers) {
            allCustomersTable.getItems().add(customer);
        }
        allCustomersTable.setPlaceholder(new Label("No Data to display"));
        return allCustomersTable;
    }

    public void deleteUser(String username) throws NullPointerException, IllegalArgumentException {
        if (username.equals("")) {
            throw new IllegalArgumentException("You must enter username.");
        } else {
            Account account = Account.getAccountByUsername(username);
            if (account == null) {
                throw new NullPointerException("There is no account with this username.");
            }
            Connection.sendToServer("delete user: " + username);
        }
    }

    public static String generateRandomDiscountCode() {
        char[] code = new char[8];
        Random random = new Random();
        for (int i = 0; i < 8; ++i) {
            int a = random.nextInt(62);

            if (a < 10)
                code[i] = (char) (a + 48);

            else if (a < 36)
                code[i] = (char) (a + 55);
            else
                code[i] = (char) (a + 61);
        }
        return String.valueOf(code);
    }

    public static void registerAdmin(String username, String password, String name, String lastName, String email, String phoneNumber) {
        String message = "create admin account: ," + username + "," + password + "," + name + "," + lastName + "," + email + "," + phoneNumber + ", ";
        Connection.sendToServer(message);
    }

    //todo: checking this
    public void removeProduct(String productId) throws NullPointerException {
        Product product = Product.getProductByID(productId);
        if (product == null) {
            throw new NullPointerException();
        } else {
            Connection.sendToServer("remove product: " + productId);
        }
    }

    public void createDiscountCode(String discountCode, Date startTime, Date endTime, String discountPercent,
                                   String maxPossibleDiscount, String discountPerCustomer,
                                   ArrayList<String> includingCustomers) throws Exception {

        if (checkDiscountCodeValidity(discountCode) && checkDiscountPercentValidity(discountPercent) &&
                checkMaxPossibleDiscountValidity(maxPossibleDiscount) &&
                checkDiscountPerCustomerValidity(discountPerCustomer) && checkCustomersValidity(includingCustomers)) {
            String message = "Create discountCode: &" + discountCode + "&" + new Gson().toJson(startTime) + "&";
            message = message + new Gson().toJson(endTime) + "&" + discountPercent + "&" + maxPossibleDiscount;
            message = message + "&" + discountPerCustomer + "&" + new Gson().toJson(includingCustomers);
            Connection.sendToServer("Create discountCode: " + message);
        }
    }

    public TableView getAllDiscountsTable(TableView allDiscountsTable) {
        TableColumn<String, Discount> column = new TableColumn<>("Discount Code");
        column.setCellValueFactory(new PropertyValueFactory<>("discountCode"));

        allDiscountsTable.getColumns().add(column);

        Connection.sendToServer("getDiscounts");
        ArrayList<Discount> allDiscounts = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Discount>>(){}.getType());
        for (Discount discount : allDiscounts) {
            allDiscountsTable.getItems().add(discount);
        }

        allDiscountsTable.setPlaceholder(new Label("No Data To Display"));
        return allDiscountsTable;
    }

    public ArrayList<Discount> getAllDiscountCodes() {
        return Discount.getAllDiscounts();
    }

    public void editDiscountStartTime(String discountCode, Date startTime) throws NullPointerException {
        Connection.sendToServer("get discount: " + discountCode);
        Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
        if (discount == null) {
            throw new NullPointerException();
        }
        //discount.setStartTime(startTime);
        Connection.sendToServer("edit discount startTime: &" + discountCode + "&" + new Gson().toJson(startTime));
    }

    public void editDiscountEndTime(String discountCode, Date endTime) throws NullPointerException {
        Connection.sendToServer("get discount: " + discountCode);
        Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
        if (discount == null) {
            throw new NullPointerException();
        }
        //discount.setEndTime(endTime);
        Connection.sendToServer("edit discount endTime: &" + discountCode + "&" + new Gson().toJson(endTime));
    }

    public void editDiscountPercent(String discountCode, String discountPercent) throws NullPointerException, Exception {
        Connection.sendToServer("get discount: " + discountCode);
        Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
        if (discount == null) {
            throw new NullPointerException();
        } else if (checkDiscountPercentValidity(discountPercent)) {
            Connection.sendToServer("edit discount percent: &" + discountCode + "&" + discountPercent);
        }
    }

    public void editDiscountMaxPossibleDiscount(String discountCode, String maxPossibleDiscount) throws NullPointerException, Exception {
        Connection.sendToServer("get discount: " + discountCode);
        Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
        if (discount == null) {
            throw new NullPointerException();
        } else if (checkMaxPossibleDiscountValidity(maxPossibleDiscount)) {
            //discount.setMaxPossibleDiscount(Double.parseDouble(maxPossibleDiscount));
            Connection.sendToServer("edit discount maxPossibleDiscount: &" + discountCode + "&" + maxPossibleDiscount);
        }
    }

    public void editDiscountPerCustomer(String discountCode, String discountPerCustomer) throws NullPointerException, Exception {
        Connection.sendToServer("get discount: " + discountCode);
        Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
        if (discount == null) {
            throw new NullPointerException();
        } else if (checkDiscountPercentValidity(discountPerCustomer)) {
            //discount.setDiscountPerCustomer(Integer.parseInt(discountPerCustomer));
            Connection.sendToServer("edit discount discountPerCustomer: &" + discountCode + "&" + discountPerCustomer);
        }
    }

    public void editDiscountIncludingCustomers(String discountCode, ArrayList<String> customersUsername) throws Exception {
        if (checkCustomersValidity(customersUsername) && checkDiscountCodeValidity(discountCode)) {
            Connection.sendToServer("get discount: " + discountCode);
            Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
            //discount.setIncludingCustomers(customersUsername);
            String message = "edit discount includingCustomers: &" + discountCode + "&";
            for (String customer : customersUsername) {
                message = message + customer + "&";
            }
            Connection.sendToServer(message + " ");
            //todo:
        }
    }

    private boolean checkDiscountCodeValidity(String discountCode) throws IllegalArgumentException {
        if (discountCode.trim().equals("")) {
            throw new IllegalArgumentException("Invalid Discount Code");
        } else {
            return true;
        }
    }

    private boolean checkDiscountPercentValidity(String discountPercent) throws Exception {
        try {
            double discountPercentValue = Double.parseDouble(discountPercent);
            if (discountPercentValue < 100 && discountPercentValue > 0) {
                return true;
            }
            else {
                throw new Exception("Discount Percent should be between 0 and 100");
            }
        } catch (Exception e) {
            throw new Exception("Invalid Discount Percent");
        }
    }

    private boolean checkMaxPossibleDiscountValidity(String maxPossibleDiscount) throws Exception {
        try {
            Double.parseDouble(maxPossibleDiscount);
            return true;
        } catch (Exception e) {
            throw new Exception("Invalid Maximum Possible Discount");
        }
    }

    private boolean checkDiscountPerCustomerValidity(String discountPerCustomer) throws IllegalArgumentException {
        if (discountPerCustomer.trim().equals("") || !(discountPerCustomer.matches("\\d+"))) {
            throw new IllegalArgumentException("Invalid number of discount user per customer.");
        } else {
            return true;
        }
    }

    private boolean checkCustomersValidity(ArrayList<String> customersUsername) throws IllegalArgumentException {
        for (String s : customersUsername) {
            Connection.sendToServer("get customer: " + s);
            Account account = new Gson().fromJson(Connection.receiveFromServer(), Customer.class);
            if (account == null || !(account instanceof Customer)) {
                throw new IllegalArgumentException("Invalid Customer Username.");
            }
        }
        return true;
    }

    public void removeDiscount(String discountCode) throws NullPointerException {
        Connection.sendToServer("get discount: " + discountCode);
        Discount discount = new Gson().fromJson(Connection.receiveFromServer(), Discount.class);
        if (discount == null) {
            throw new NullPointerException();
        }
        Connection.sendToServer("remove discount: " + discountCode);
    }

    public TableView getAllRequestsTable(TableView allRequestsTable) {
        TableColumn<String, Request> column1 = new TableColumn<>("Request ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("requestId"));

        TableColumn<RequestType, Request> column2 = new TableColumn<>("Request Type");
        column2.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        allRequestsTable.getColumns().addAll(column1, column2);

        ArrayList<Request> allRequests = new ArrayList<>();

        Connection.sendToServer("getAddOffRequests");
        ArrayList<AddOffRequest> allAddOffRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<AddOffRequest>>(){}.getType());
        allRequests.addAll(allAddOffRequests);

        Connection.sendToServer("getAddProductRequests");
        ArrayList<AddProductRequest> allAddProductRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<AddProductRequest>>(){}.getType());
        allRequests.addAll(allAddProductRequests);

        Connection.sendToServer("getEditOffRequests");
        ArrayList<EditOffRequest> allEditOffRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<EditOffRequest>>(){}.getType());
        allRequests.addAll(allEditOffRequests);

        Connection.sendToServer("getEditProductRequests");
        ArrayList<EditProductRequest> allEditProductRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<EditOffRequest>>(){}.getType());
        allRequests.addAll(allEditProductRequests);

        Connection.sendToServer("getRegisterSellerRequests");
        ArrayList<RegisterSellerRequest> allRegisterSellerRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<RegisterSellerRequest>>(){}.getType());
        allRequests.addAll(allRegisterSellerRequests);

        /*Connection.sendToServer("getRegisterSupporterRequests");
        ArrayList<RegisterSupporterRequest> allRegisterSupporterRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<RegisterSupporterRequest>>(){}.getType());
        allRequests.addAll(allRegisterSupporterRequests);*/

        Connection.sendToServer("getRemoveProductRequests");
        ArrayList<RemoveProductRequest> allRemoveProductRequests = new Gson().fromJson(Connection.receiveFromServer(),
                new TypeToken<ArrayList<RemoveProductRequest>>(){}.getType());
        allRequests.addAll(allRemoveProductRequests);

        for (Request request : allRequests) {
            allRequestsTable.getItems().add(request);
        }
        allRequestsTable.setPlaceholder(new Label("No Data to display"));
        return allRequestsTable;
    }

    public void acceptRequest(String requestId) throws NullPointerException, IllegalArgumentException {
        Request request = Request.getRequestById(requestId);
        if (request == null) {
            throw new NullPointerException();
        } else {
            //request.acceptRequest();
            //Request.removeRequest(request);
            Connection.sendToServer("accept request: " + requestId);
        }
    }

    public void declineRequest(String requestId) throws NullPointerException {
        Request request = Request.getRequestById(requestId);
        if (request == null) {
            throw new NullPointerException();
        }
        //Request.removeRequest(request);
        Connection.sendToServer("decline request: " + requestId);
    }

    private Category getCategoryByName(String categoryName) {
        Connection.sendToServer("get category: " + categoryName);
        Category category = new Gson().fromJson(Connection.receiveFromServer(), Category.class);
        return category;
    }

    public void addCategory(String categoryName, ArrayList<String> specialFeatures) throws Exception {
        Category category = getCategoryByName(categoryName);
        if (category == null) {
            if (specialFeatures.isEmpty()) {
                throw new Exception("Special Features Field is Empty");
            } else {
                Connection.sendToServer("add category: &" + categoryName + "&" + (new Gson().toJson(specialFeatures)) + "&");
            }
        } else {
            throw new Exception("There is another Category with this name");
        }
    }

    //todo: checking this
    public void removeCategorySpecialFeature(Category category, String specialFeature) throws NullPointerException {
        if (category.getSpecialFeatures().contains(specialFeature)) {
            Connection.sendToServer("remove category special feature: &" + category.getName() + "&" + specialFeature);
        } else {
            throw new NullPointerException();
        }
    }

    //todo: checking this
    public void addSpecialFeature(Category category, String specialFeature) throws IllegalArgumentException {
        if (category.getSpecialFeatures().contains(specialFeature)) {
            throw new IllegalArgumentException();
        } else {
            Connection.sendToServer("add category special feature: &" + category.getName() + "&" + specialFeature);
        }
    }

    //todo: checking this
    public void removeCategory(String categoryName) throws NullPointerException {
        Category category = getCategoryByName(categoryName);
        if (category == null) {
            throw new NullPointerException();
        }
        Connection.sendToServer("remove category: " + categoryName);
    }

    public TableView getAllCategoriesTable(TableView allCategoriesTable) {
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

    public TableView getSaleHistoryTable(TableView allBuyLogs) {
        TableColumn<String, BuyLog> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<String, BuyLog> column2 = new TableColumn<>("Date");
        column2.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<String, BuyLog> column3 = new TableColumn<>("Paid Amount");
        column3.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));

        TableColumn<String, BuyLog> column4 = new TableColumn<>("Discount Amount");
        column4.setCellValueFactory(new PropertyValueFactory<>("discountAmount"));

        TableColumn<String, BuyLog> column5 = new TableColumn<>("Address");
        column5.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<String, BuyLog> column6 = new TableColumn<>("Sending Condition");
        column6.setCellValueFactory(new PropertyValueFactory<>("sendingCondition"));

        allBuyLogs.getColumns().addAll(column1, column2, column3,column4,column5,column6);

        Connection.sendToServer("getCustomers");
        ArrayList<Customer> allCustomers = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Customer>>(){}.getType());
        for (Customer customer : allCustomers) {
            for (BuyLog buyLog : customer.getBuyLogs()) {
                allBuyLogs.getItems().add(buyLog);
            }
        }
        allBuyLogs.setPlaceholder(new Label("No Data to display"));
        return allBuyLogs;
    }




    public ArrayList<Category> getAllCategories() {
        Connection.sendToServer("getCategories");
        ArrayList<Category> allCategories = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Category>>(){}.getType());
        return allCategories;
    }

    //todo: checking this
    public void removeSubCategory(Category parentCategory, String subCategory) {
        parentCategory.removeSubCategory(Category.getCategoryByName(subCategory));
    }

    public Category addAndGetSubCategory(String subCategoryName, Category parentCategory, ArrayList<String> specialFeatures) throws Exception {
        if (parentCategory == null) {
            throw new Exception("You haven't add category yet");
        } else {
            if (subCategoryName.isEmpty() || subCategoryName.trim().isEmpty()) {
                throw new Exception("You haven't entered subCategory name");
            } else if (specialFeatures.isEmpty()) {
                throw new Exception("Special Features Field is Empty");
            }
        }
        Category subCategory = parentCategory.addSubCategoryWithName(subCategoryName);
        subCategory.getSpecialFeatures().addAll(specialFeatures);
        return subCategory;
    }

    public void editCategoryName(Category category, String newCategoryName) throws IllegalArgumentException {
        if (Category.getCategoryByName(newCategoryName) != null) {
            throw new IllegalArgumentException();
        }
        category.setName(newCategoryName);
    }

    //phase1
    public String viewUser(String username) throws NullPointerException {
        Account account = Account.getAccountByUsername(username);
        if (account == null) {
            throw new NullPointerException();
        }
        return account.toString();
    }

    public static boolean isProductWithThisID(String productID) {
        for (Product product : Product.getAllProducts()) {
            if (product.getProductId().equals(productID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCategoryWithThisName(String categoryName) {
        for (Category category : Category.getAllCategories()) {
            if (category.getName().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDiscountWithThisID(String ID) {
        for (Discount discount : Discount.getAllDiscounts()) {
            if (discount.getDiscountCode().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInputValidForDiscountCode(String input) {
        if (!input.trim().matches("\\s")) {
            return true;
        }
        return false;
    }

    public static boolean isInputValidForDiscountPercent(String input) {
        if (input.matches("\\A\\d\\d\\z")) {
            return true;
        }
        return false;
    }

    public String viewDiscount(String discountCode) throws NullPointerException {
        Discount discount = Discount.getDiscountByDiscountCode(discountCode);
        if (discount == null) {
            throw new NullPointerException();
        }
        return discount.toString();
    }

    public void editDiscountCode(String discountCodeBefore, String discountCodeAfter) throws NullPointerException, IllegalArgumentException {
        Discount discount = Discount.getDiscountByDiscountCode(discountCodeBefore);
        if (discount == null) {
            throw new NullPointerException();
        } else if (Discount.getDiscountByDiscountCode(discountCodeAfter) != null) {
            throw new IllegalArgumentException();
        } else {
            discount.setDiscountCode(discountCodeAfter);
        }
    }

    public String getDetailsOfRequest(String requestId) throws NullPointerException {
        Request request = Request.getRequestById(requestId);
        if (request == null) {
            throw new NullPointerException();
        }
        return request.toString();
    }

    public ArrayList<String> getAllRequests() {
        ArrayList<Request> allRequests = Request.getAllRequests();
        ArrayList<String> allRequestIds = new ArrayList<>();
        for (Request request : allRequests) {
            allRequestIds.add(request.getRequestId());
        }
        return allRequestIds;
    }

    public ArrayList<Account> getAllUsers() {
        return Account.getAllAccounts();
    }
}
