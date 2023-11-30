package Server;

import Controller.ProductsManager;
import Controller.SupporterProfileManager;
import Model.Account.*;
import Model.Product.Category;
import Model.Product.Comment;
import Model.Product.Product;
import Model.Request.*;
import View.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import graphics.products.PurchaseMenuController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Server extends Application {
    private static final int serverPort = 8000;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ProductsManager productsManager = new ProductsManager();
    private static final Gson gson = new Gson();
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static HashMap<Supporter, String> CustomersInQueue = new HashMap<>();

    @Override
    public void start(Stage stage) {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        Button button = new Button("Shut down server");
        VBox vBox = new VBox(button);
        vBox.setSpacing(500);
        vBox.setPadding(new Insets(100, 100, 100, 100));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Server");
        stage.show();
        stage.setOnCloseRequest(e -> {
            stage.close();
            Main.serializeXML();
            System.exit(0);
        });
        button.setOnAction(e -> {
            stage.close();
            Main.serializeXML();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                ChatServer.main(5043);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Main.deserializeXML();
        Thread thread = new Thread(Server::run);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        launch();
    }

    private static void run(){
        System.out.println("Server is running...\n");
        try {
            serverSocket = new ServerSocket(serverPort);
            while (true){
                clientSocket = serverSocket.accept();
                checkEndOfAuctions();
                dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

                String message = dataInputStream.readUTF();
                System.out.println(message);
                System.out.println();
                String[] splitMessage = message.split("\\s+");
                if (message.startsWith("{\"token\":\"")) { //for requests with token
                    String token = getJasonObjectItem(message, "token");
                    Account account = Account.getAccountByToken(token);
                    String content = getJasonObjectItem(message, "content");
                    String[] splitContent = content.split("\\s+");

                    if (account != null){
                        if (content.startsWith("get username: ")) {
                            sendAccountUsername(dataOutputStream, account);
                        }
                        else if (content.startsWith("get password: ")) {
                            sendAccountPassword(dataOutputStream, account);
                        }
                        else if (content.startsWith("edit password: ")) {
                            editAccountPassword(account, content.substring(("edit password: ").length()));
                        }
                        else if (content.startsWith("get first name: ")) {
                            sendAccountFirstName(dataOutputStream, account);
                        }
                        else if (content.startsWith("edit first name: ")) {
                            editAccountFirstName(account, content.substring(("edit first name: ").length()));
                        }
                        else if (content.startsWith("get last name: ")) {
                            sendAccountLastName(dataOutputStream, account);
                        }
                        else if (content.startsWith("edit last name: ")) {
                            editAccountLastName(account, content.substring(("edit last name: ").length()));
                        }
                        else if (content.startsWith("get email: ")) {
                            sendAccountEmail(dataOutputStream, account);
                        }
                        else if (content.startsWith("edit email: ")) {
                            editAccountEmail(account, content.substring(("edit email: ").length()));
                        }
                        else if (content.startsWith("get phone number: ")) {
                            sendAccountPhoneNumber(dataOutputStream, account);
                        }
                        else if (content.startsWith("edit phone number: ")) {
                            editAccountPhoneNumber(account, content.substring(("edit phone number: ").length()));
                        }
                        else if (content.startsWith("get seller product IDs: ")) {
                            sendSellerProductIDs(dataOutputStream, account);
                        }
                        else if (content.startsWith("get seller sellLogs: ")) {
                            sendSellerSellLogs(dataOutputStream, account);
                        }
                        else if (content.startsWith("get company name: ")) {
                            sendSellerCompanyName(dataOutputStream, account);
                        }
                        else if (content.startsWith("get company phone number: ")) {
                            sendSellerCompanyPhoneNumber(dataOutputStream, account);
                        }
                        else if (content.startsWith("get company address: ")) {
                            sendSellerCompanyAddress(dataOutputStream, account);
                        }
                        else if (content.startsWith("get company open year: ")) {
                            sendSellerCompanyOpenYear(dataOutputStream, account);
                        }
                        else if (content.startsWith("edit company name: ")) {
                            editSellerCompanyName(account, content.substring(("edit company name: ").length()));
                        }
                        else if (content.startsWith("edit company phone number: ")) {
                            editSellerCompanyPhoneNumber(account, content.substring(("edit company phone number: ").length()));
                        }
                        else if (content.startsWith("edit company address: ")) {
                            editSellerCompanyAddress(account, content.substring(("edit company address: ").length()));
                        }
                        else if (content.startsWith("edit company open year: ")) {
                            editSellerCompanyOpenYear(account, content.substring(("edit company open year: ").length()));
                        }
                        else if (content.startsWith("add to cart: ")){
                            Customer customer = (Customer) account;
                            Product product = Product.getProductByID(splitContent[3]);
                            customer.addToCart(product, Integer.parseInt(splitContent[4]));
                        }
                        else if (content.startsWith("rate product: ")){
                            Customer customer = (Customer) account;
                            Product product = Product.getProductByID(splitContent[2]);
                            int rate = Integer.parseInt(splitContent[3]);
                            product.addRate(customer, rate);
                        }
                        else if (content.startsWith("new comment: ")){
                            Comment comment = gson.fromJson(content.substring("new comment: ".length()), Comment.class);
                            Product.getProductByID(comment.getId()).addAComment(comment);
                        }
                        else if (content.startsWith("use discount: ")){
                            Discount discount = Discount.getDiscountByDiscountCode(content.substring("use discount: ".length()));
                            Customer customer = (Customer) account;
                            HashMap<Discount, Integer> usedDiscounts = customer.getUsedDiscounts();
                            usedDiscounts.put(discount, usedDiscounts.getOrDefault(discount, 0) + 1);
                        }
                        else if (content.startsWith("finish buying: ")){
                            Customer customer = (Customer) account;
                            double finalAmount = Double.parseDouble(splitContent[2]);
                            double totalAmount = Double.parseDouble(splitContent[3]);
                            PurchaseMenuController.finishBuying(finalAmount, customer.getCart(), customer, totalAmount);
                        }
                        else if (content.equals("logout")){
                            account.setToken(null);
                            account.setOnline(false);
                        }
                        else if (content.startsWith("add customer to auction: ")){
                            double amount = Double.parseDouble(splitContent[4]);
                            String productId = splitContent[5];
                            Product product = Product.getProductByID(productId);
                            product.getCustomersAmountForAction().put(account.getUsername(), amount);
                        }
                        else if (content.startsWith("add product to auction: ")){
                            long time = Long.parseLong(splitContent[4]);
                            Date date = new Date(time);
                            Product product = Product.getProductByID(content.substring(content.indexOf(splitContent[4]) + splitContent[4].length() + 1));
                            product.setInAction(true);
                            product.setEndOfAction(date);
                        }
                        else if (content.startsWith("get customer buyLogs: ")) {
                            sendCustomerBuyLogs(dataOutputStream, account);
                        }
                        else if (content.startsWith("get customer discounts: ")) {
                            sendCustomerDiscounts(dataOutputStream, account);
                        }
                        else if (content.startsWith("charge wallet: ")){
                            chargeWallet(dataOutputStream, account, content);
                        }
                        else if (content.startsWith("withdraw from wallet: ")) {//todo:
                            withdrawFromWallet(dataOutputStream, account, content);
                        }
                        else if (content.startsWith("get account balance")) {
                            sendAccountBalance(dataOutputStream, account);
                        }
                        else if (content.startsWith("get bank account balance")) {
                            sendBankAccountBalance(dataOutputStream, account);
                        }
                        else if (content.startsWith("set line condition: ")) {
                            setSupporterLineCondition(account, content.substring(("set line condition: ").length()));
                        }
                        else if (content.startsWith("add off request: ")) {
                            addOffRequest(content, (Seller) account);
                        }
                    }
                    if (content.equals("get logged in account")){
                        sendAccountInfo(dataOutputStream, account);
                    }
                    else if (content.startsWith("can go to auction chat: ")){
                        Product product = Product.getProductByID(content.substring("can go to auction chat: ".length()));
                        if ((account instanceof Customer) && product.getCustomersAmountForAction().containsKey(account.getUsername())){
                            dataOutputStream.writeUTF("yes");
                        }
                        else {
                            dataOutputStream.writeUTF("no");
                        }
                        dataOutputStream.flush();
                    }
                }
                else if (message.equals("isThereAdmin")) {
                    if (Admin.getAllAdmins().isEmpty()) {
                        dataOutputStream.writeUTF("no");
                    } else {
                        dataOutputStream.writeUTF("yes");
                    }
                    dataOutputStream.flush();
                }
                else if (message.startsWith("register admin: ")) {
                    registerAdmin(message);
                }
                else if (message.startsWith("create admin account: ")){
                    createAdminAccount(message);
                }
                else if (message.startsWith("register customer: ")) {
                    registerCustomer(message);
                }
                else if (message.startsWith("register supporter : ")) {
                    registerSupporter(message);
                }
                else if (message.startsWith("register seller request: ")) {
                    registerSellerRequest(message);
                }
                else if (message.startsWith("delete user: ")) {
                    deleteUser(message.substring(("delete user: ").length()));
                }
                else if (message.startsWith("get customer: ")) {
                    sendCustomer(dataOutputStream, message.substring(("get customer: ").length()));
                }
                else if (message.equals("getAdmins")) {
                    sendAllAdmins(dataOutputStream);
                }
                else if (message.equals("getSellers")) {
                    sendAllSellers(dataOutputStream);
                }
                else if (message.equals("getCustomers")) {
                    sendAllCustomers(dataOutputStream);
                }
                else if (message.equals("getSupporters")) {
                    sendAllSupporters(dataOutputStream);
                }
                else if (message.equals("getAddOffRequests")) {
                    sendAllAddOffRequests(dataOutputStream);
                }
                else if (message.equals("getAddProductRequests")) {
                    sendAllAddProductRequests(dataOutputStream);
                }
                else if (message.equals("getEditOffRequests")) {
                    sendAllEditOffRequests(dataOutputStream);
                }
                else if (message.equals("getEditProductRequests")) {
                    sendAllEditProductRequests(dataOutputStream);
                }
                else if (message.equals("getRegisterSellerRequests")) {
                    sendAllRegisterSellerRequests(dataOutputStream);
                }
                else if (message.equals("getRegisterSupporterRequests")) {
                    sendAllRegisterSupporterRequests(dataOutputStream);
                }
                else if (message.equals("getRemoveProductRequests")) {
                    sendAllRemoveProductRequests(dataOutputStream);
                }
                else if (message.startsWith("get addOffRequest: ")) {
                    sendAddOffRequest(dataOutputStream, message.substring(("get addOffRequest: ").length()));
                }
                else if (message.startsWith("get editOffRequest: ")) {
                    sendEditOffRequest(dataOutputStream, message.substring(("get editOffRequest: ").length()));
                }
                else if (message.startsWith("get addProductRequest: ")) {
                    sendAddProductRequest(dataOutputStream, message.substring(("get addProductRequest: ").length()));
                }
                else if (message.startsWith("get editProductRequest: ")) {
                    sendEditProductRequest(dataOutputStream, message.substring(("get editProductRequest: ").length()));
                }
                else if (message.startsWith("get registerSellerRequest: ")) {
                    sendRegisterSellerRequest(dataOutputStream, message.substring(("get registerSellerRequest: ").length()));
                }
                else if (message.startsWith("get removeProductRequest: ")) {
                    sendRemoveProductRequest(dataOutputStream, message.substring(("get removeProductRequest: ").length()));
                }
                else if (message.startsWith("add product request: ")) {
                    addProductRequest(message);
                }
                else if (message.startsWith("edit product request: ")) {
                    editProductRequest(message);
                }
                else if (message.startsWith("remove product request: ")) {
                    removeProductRequest(message.substring(("remove product request: ").length()));
                }
                else if (message.startsWith("edit off request: ")) {
                    editOffRequest(message);
                }
                else if (message.startsWith("accept request: ")) {
                    acceptRequest(message.substring(("accept request: ").length()));
                }
                else if (message.startsWith("decline request: ")) {
                    declineRequest(message.substring(("decline request: ").length()));
                }
                else if (message.equals("getCategories")){
                    dataOutputStream.writeUTF(gson.toJson(Category.getAllCategories()));
                    dataOutputStream.flush();
                }
                else if (message.equals("getProducts")){
                    dataOutputStream.writeUTF(gson.toJson(Product.getAllProducts()));
                    dataOutputStream.flush();
                }
                else if (message.startsWith("get product: ")){
                    Product product = Product.getProductByID(message.substring("get product: ".length()));
                    //dataOutputStream.write(gson.toJson(product).getBytes());
                    dataOutputStream.writeUTF(gson.toJson(product));
                    dataOutputStream.flush();
                }
                else if (message.startsWith("get product buyers: ")) {
                    sendProductBuyers(dataOutputStream, message.substring(("get product buyers: ").length()));
                }
                else if (message.startsWith("visit product: ")){
                    Product product = Product.getProductByID(message.substring("visit product: ".length()));
                    product.setVisitNumber(product.getVisitNumber() + 1);
                }
                else if (message.startsWith("remove product: ")) {
                    removeProduct(message.substring(("remove product: ").length()));
                }
                else if (message.startsWith("get off: ")) {
                    sendOff(dataOutputStream, message.substring(("get off: ").length()));
                }
                else if (message.startsWith("add category: ")) {
                    addCategory(message);
                }
                else if (message.startsWith("get category: ")) {
                    sendCategory(dataOutputStream, message.substring(("get category: ").length()));
                }
                else if (message.startsWith("remove category special feature: ")) {
                    removeCategorySpecialFeature(message);
                }
                else if (message.startsWith("add category special feature: ")) {
                    addCategorySpecialFeature(message);
                }
                else if (message.startsWith("remove category: ")) {
                    removeCategory(message.substring(("remove category: ").length()));
                }
                else if (message.equals("getDiscounts")) {
                    sendAllDiscounts(dataOutputStream);
                }
                else if (message.startsWith("get discount: ")){
                    Discount discount = Discount.getDiscountByDiscountCode(message.substring("get discount: ".length()));
                    dataOutputStream.writeUTF(gson.toJson(discount));
                    dataOutputStream.flush();
                }
                else if (message.startsWith("Create discountCode: ")) {
                    createDiscountCode(message);
                }
                else if (message.startsWith("edit discount startTime: ")) {
                    editDiscountStartTime(message.split("&"));
                }
                else if (message.startsWith("edit discount endTime: ")) {
                    editDiscountEndTime(message.split("&"));
                }
                else if (message.startsWith("edit discount percent: ")) {
                    editDiscountPercent(message.split("&"));
                }
                else if (message.startsWith("edit discount maxPossibleDiscount: ")) {
                    editDiscountMaxPossibleDiscount(message.split("&"));
                }
                else if (message.startsWith("edit discount discountPerCustomer: ")) {
                    editDiscountPerCustomer(message.split("&"));
                }
                else if (message.startsWith("edit discount includingCustomers: ")) {
                    editDiscountIncludingCustomers(message.split("&"));
                }
                else if (message.startsWith("remove discount: ")) {
                    removeDiscount(message.substring(("remove discount: ").length()));
                }
                else if (message.startsWith("get buyLog: ")) {
                    sendBuyLog(dataOutputStream, message.substring(("get buyLog: ").length()));
                }
                else if (message.startsWith("login: ")){
                    Account account = Account.getAccountByUsername(message.substring("login: ".length()));
                    String token = generateToken();
                    account.setToken(token);
                    account.setOnline(true);
                    dataOutputStream.writeUTF(token);
                    dataOutputStream.flush();
                }
                else if (message.startsWith("get account: ")){
                    Account account = Account.getAccountByUsername(message.substring("get account: ".length()));
                    sendAccountInfo(dataOutputStream, account);
                }
                else if (message.equals("get min wallet balance")){
                    dataOutputStream.writeUTF(String.valueOf(Admin.getMinWalletBalance()));
                    dataOutputStream.flush();
                }
                else if (message.equals("get banking fee percent")) {
                    dataOutputStream.writeUTF(String.valueOf(Admin.getBankingFeePercent()));
                    dataOutputStream.flush();
                }
                else if (message.startsWith("edit min wallet balance: ")) {
                    int minWalletBalance = Integer.parseInt(message.substring(("edit min wallet balance: ").length()));
                    Admin.setMinWalletBalance(minWalletBalance);
                }
                else if (message.startsWith("edit banking fee percent: ")) {
                    int bankingFeePercent = Integer.parseInt(message.substring(("edit banking fee percent: ").length()));
                    Admin.setBankingFeePercent(bankingFeePercent);
                }
                else if (message.startsWith("get product with id: ")){
                    String id = message.substring("get product with id: ".length());
                    Product product = Product.getProductByID(id);
                    dataOutputStream.writeUTF(gson.toJson(product));
                    dataOutputStream.flush();
                    byte[] imageBytes = Files.readAllBytes(Paths.get(product.getImageAddressInServer()));
                    dataOutputStream.write(imageBytes);
                    dataOutputStream.flush();
                    if (product.isFile()){
                        byte[] file = Files.readAllBytes(Paths.get(product.getFileAddressInServer()));
                        dataOutputStream.write(file);
                        dataOutputStream.flush();
                    }
                }
                else if (message.equals("get all products IDs")){
                    dataOutputStream.writeUTF(gson.toJson(Product.getAllProducts().stream().map(Product::getProductId).collect(Collectors.toList())));
                    dataOutputStream.flush();
                }
                else if (message.equals("get customers in queue")) {
                    dataOutputStream.writeUTF(gson.toJson(getCustomersInQueue()));
                    dataOutputStream.flush();
                }
                else if (message.startsWith("remove supporter from customers in queue: ")) {
                    removeSupporterFromCustomersInQueue(message.substring(("remove supporter from customers in queue: ").length()));
                }
                else if (message.startsWith("put supporter in customers in queue:")) {
                    putSupporterInCustomersInQueue(message);
                }
                else if (message.startsWith("delete product: ")){
                    Product product = Product.getProductByID(message.substring("delete product: ".length()));
                    Product.getAllProducts().remove(product);
                    Category.getAllCategories().forEach(category -> category.getProductIds().remove(product.getProductId()));
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void putSupporterInCustomersInQueue(String message) {
        String[] splitMessage = message.split("&");
        String supporterID = splitMessage[1];
        Supporter supporter = SupporterProfileManager.getSupporterByID(Integer.parseInt(supporterID));
        String value = splitMessage[2];
        Server.CustomersInQueue.put(supporter, value);
        System.out.println(supporter.getSupporterID());
    }

    private static void removeSupporterFromCustomersInQueue(String supporterUsername) {
        Supporter supporter = Supporter.getSupporterByUserName(supporterUsername);
        getCustomersInQueue().remove(supporter);
    }

    private static void setSupporterLineCondition(Account account, String lineCondition) {
        Supporter supporter = Supporter.getSupporterByUserName(account.getUsername());
        if (lineCondition.equals("true")) {
            supporter.setLineCondition(true);
        } else if (lineCondition.equals("false")) {
            supporter.setLineCondition(false);
        }
    }

    private static void addCategory(String message) {
        String[] splitMessage = message.split("&");
        String categoryName = splitMessage[1];
        ArrayList<String> specialFeatures = gson.fromJson(splitMessage[2], new TypeToken<ArrayList<String>>(){}.getType());
        Category category = new Category(categoryName);
        category.setSpecialFeatures(specialFeatures);
    }

    private static void sendBankAccountBalance(DataOutputStream dataOutputStream, Account account) {
        String output = "";
        try {
            output = BankConnection.getBalance(account.getUsername(), account.getPassword());
        } catch (Exception e) {
            output = e.getMessage();
        }
        try {
            dataOutputStream.writeUTF(output);
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAccountBalance(DataOutputStream dataOutputStream, Account account) {
        String output = "";
        try {
            if (account instanceof Customer) {
                Customer customer = Customer.getCustomerById(account.getUsername());
                output = String.valueOf(customer.getBalance());
            }
            else if (account instanceof Seller) {
                Seller seller = Seller.getSellerByUserName(account.getUsername());
                output = String.valueOf(seller.getBalance());
            }
        } catch (Exception e) {
            output = e.getMessage();
        }
        try {
            dataOutputStream.writeUTF(output);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void chargeWallet(DataOutputStream dataOutputStream, Account account, String content) throws IOException {
        int money = Integer.parseInt(content.substring("charge wallet: ".length()));
        String outPut = "";
        if (account instanceof Customer) {
            Customer customer = Customer.getCustomerById(account.getUsername());
            try {
                outPut = BankConnection.move(customer.getUsername(), customer.getPassword(), money, customer.getBankAccountID(), Admin.getBankIDOfStore());
                customer.setBalance(customer.getBalance() + money);
            } catch (Exception e) {
                outPut = e.getMessage();
            }
            System.out.println(outPut);
            System.out.println(Admin.getBankIDOfStore());
            System.out.println(Admin.getAllAdmins().get(0).toString());
        }
        else if (account instanceof Seller) {
            Seller seller = Seller.getSellerByUserName(account.getUsername());
            try {
                outPut = BankConnection.move(seller.getUsername(), seller.getPassword(), money,
                        seller.getBankAccountID(), Admin.getBankIDOfStore());
                seller.setBalance(seller.getBalance() + money);
            } catch (Exception e) {
                outPut = e.getMessage();
            }
        }
        dataOutputStream.writeUTF(outPut);
        dataOutputStream.flush();
    }

    private static void withdrawFromWallet(DataOutputStream dataOutputStream, Account account, String content) throws IOException {
        int withdrawalMoney = Integer.parseInt(content.substring(("withdraw from wallet: ").length()));
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        String output = "";
        try {
            Admin mainAdmin = Admin.getAllAdmins().get(0);//todo:!!!
            output = BankConnection.move(mainAdmin.getUsername(), mainAdmin.getPassword(), withdrawalMoney,
                    Admin.getBankIDOfStore(), seller.getBankAccountID());
            seller.setBalance(seller.getBalance() - withdrawalMoney);
        } catch (Exception e) {
            output = e.getMessage();
        }
        dataOutputStream.writeUTF(output);
        dataOutputStream.flush();
    }

    private static void removeCategory(String categoryName) {
        Category category = Category.getCategoryByName(categoryName);
        Category.removeCategory(category);
    }

    private static void addCategorySpecialFeature(String message) {
        String[] splitMessage = message.split("&");
        String categoryName = splitMessage[1];
        Category category = Category.getCategoryByName(categoryName);
        String specialFeature = splitMessage[2];
        category.addSpecialFeature(specialFeature);
    }

    private static void removeCategorySpecialFeature(String message) {
        String[] splitMessage = message.split("&");
        String categoryName = splitMessage[1];
        Category category = Category.getCategoryByName(categoryName);
        String specialFeature = splitMessage[2];
        category.removeSpecialFeature(specialFeature);
    }

    private static void sendCategory(DataOutputStream dataOutputStream, String categoryName) {
        Category category = Category.getCategoryByName(categoryName);
        try {
            dataOutputStream.writeUTF(gson.toJson(category));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendBuyLog(DataOutputStream dataOutputStream, String buyLogID) {
        BuyLog buyLog = BuyLog.getBuyLogByID(buyLogID);
        try {
            dataOutputStream.writeUTF(gson.toJson(buyLog));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendCustomerBuyLogs(DataOutputStream dataOutputStream, Account account) {
        Customer customer = Customer.getCustomerById(account.getUsername());
        try {
            dataOutputStream.writeUTF(gson.toJson(customer.getBuyLogs()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendCustomerDiscounts(DataOutputStream dataOutputStream, Account account) {
        Customer customer = Customer.getCustomerById(account.getUsername());
        try {
            dataOutputStream.writeUTF(gson.toJson(customer.getAllDiscountCodesForCustomer()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAccountUsername(DataOutputStream dataOutputStream, Account account) {
        try {
            dataOutputStream.writeUTF(account.getUsername());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editAccountPhoneNumber(Account account, String newPhoneNumber) {
        account.setPhoneNumber(newPhoneNumber);
    }

    private static void sendAccountPhoneNumber(DataOutputStream dataOutputStream, Account account) {
        try {
            dataOutputStream.writeUTF(account.getPhoneNumber());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editAccountEmail(Account account, String newEmail) {
        account.setEmail(newEmail);
    }

    private static void sendAccountEmail(DataOutputStream dataOutputStream, Account account) {
        try {
            dataOutputStream.writeUTF(account.getEmail());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editAccountLastName(Account account, String newLastName) {
        account.setLastName(newLastName);
    }

    private static void sendAccountLastName(DataOutputStream dataOutputStream, Account account) {
        try {
            dataOutputStream.writeUTF(account.getLastName());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editAccountFirstName(Account account, String newFirstName) {
        account.setName(newFirstName);
    }

    private static void sendAccountFirstName(DataOutputStream dataOutputStream, Account account) {
        try {
            dataOutputStream.writeUTF(account.getName());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editAccountPassword(Account account, String newPassword) {
        account.setPassword(newPassword);
    }

    private static void sendAccountPassword(DataOutputStream dataOutputStream, Account account) {
        try {
            dataOutputStream.writeUTF(account.getUsername());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editSellerCompanyOpenYear(Account account, String newCompanyOpenYear) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        seller.setCompanyOpenYear(newCompanyOpenYear);
    }

    private static void editSellerCompanyAddress(Account account, String newCompanyAddress) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        seller.setCompanyAddress(newCompanyAddress);
    }

    private static void editSellerCompanyPhoneNumber(Account account, String newCompanyPhoneNumber) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        seller.setPhoneNumberOfCompany(newCompanyPhoneNumber);
    }

    private static void sendSellerCompanyOpenYear(DataOutputStream dataOutputStream, Account account) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        try {
            dataOutputStream.writeUTF(seller.getCompanyOpenYear());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendSellerCompanyAddress(DataOutputStream dataOutputStream, Account account) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        try {
            dataOutputStream.writeUTF(seller.getCompanyAddress());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendSellerCompanyPhoneNumber(DataOutputStream dataOutputStream, Account account) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        try {
            dataOutputStream.writeUTF(seller.getPhoneNumberOfCompany());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editSellerCompanyName(Account account, String newCompanyName) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        seller.setNameOfCompany(newCompanyName);
    }

    private static void sendSellerCompanyName(DataOutputStream dataOutputStream, Account account) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        try {
            dataOutputStream.writeUTF(seller.getNameOfCompany());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editOffRequest(String message) {
        String[] splitMessage = message.split("&");
        String offID = splitMessage[1];
        Date offStartTime = gson.fromJson(splitMessage[2], Date.class);
        Date offEndTime = gson.fromJson(splitMessage[3], Date.class);
        String offAmount = splitMessage[4];
        ArrayList<String> offProductIDs = gson.fromJson(splitMessage[5], new TypeToken<ArrayList<String>>(){}.getType());
        new EditOffRequest(offID, offStartTime, offEndTime, Integer.parseInt(offAmount), offProductIDs);
    }

    private static void addOffRequest(String message, Seller seller) {
        String[] splitMessage = message.split("&");
        String offID = splitMessage[1];
        Date offStartTime = gson.fromJson(splitMessage[2], Date.class);
        Date offEndTime = gson.fromJson(splitMessage[3], Date.class);
        String offAmount = splitMessage[4];
        ArrayList<String> offProductIDs = gson.fromJson(splitMessage[5], new TypeToken<ArrayList<String>>(){}.getType());
        String sellerUsername = splitMessage[6];
        //Seller seller = Seller.getSellerByUserName(sellerUsername);
        new AddOffRequest(offID, offStartTime, offEndTime, Integer.parseInt(offAmount), offProductIDs, seller);
    }

    private static void removeProductRequest(String productID) {
        Product product = Product.getProductByID(productID);
        new RemoveProductRequest(product);
    }

    private static void editProductRequest(String message) {
        String[] splitMessage = message.split("&");
        String productID = splitMessage[1];
        Product product = Product.getProductByID(productID);
        String productName = splitMessage[2];
        String productCompanyName = splitMessage[3];
        String productPrice = splitMessage[4];
        String productExistingNumber = splitMessage[5];
        String productExplanations = splitMessage[6];
        String productImageAddress = splitMessage[7];
        Category productCategory = gson.fromJson(splitMessage[8], Category.class);
        HashMap<String, Integer> productSpecialFeatures = gson.fromJson(splitMessage[9],
                new TypeToken<HashMap<String, Integer>>(){}.getType());
        String sellerUsername = splitMessage[10];
        Seller seller = Seller.getSellerByUserName(sellerUsername);

        new EditProductRequest(product, productID, productName,
                productCompanyName, Double.parseDouble(productPrice), Integer.parseInt(productExistingNumber),
                productExplanations, productImageAddress, productCategory, productSpecialFeatures, seller);
    }

    private static void addProductRequest(String message) throws IOException {
        String[] splitMessage = message.split("&");
        String productID = splitMessage[1];
        String productName = splitMessage[2];
        String productCompanyName = splitMessage[3];
        String productPrice = splitMessage[4];
        String productExistingNumber = splitMessage[5];
        String productExplanations = splitMessage[6];
        Category productCategory = gson.fromJson(splitMessage[7], Category.class);
        HashMap<String, Integer> productSpecialFeatures = gson.fromJson(splitMessage[8],
                new TypeToken<HashMap<String, Integer>>(){}.getType());
        String sellerUsername = splitMessage[9];
        Seller seller = Seller.getSellerByUserName(sellerUsername);
        String fileType = splitMessage[10];
        if (fileType.equals("null")){
            fileType = null;
        }
        AddProductRequest addProductRequest = new AddProductRequest(productID, productName, productCompanyName,
                Double.parseDouble(productPrice), Integer.parseInt(productExistingNumber), productExplanations,
                productCategory, productSpecialFeatures, seller, fileType);

        clientSocket.close();
        clientSocket = serverSocket.accept();
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        //todo: !!!
        int length = Integer.parseInt(dataInputStream.readUTF());
        if (length > 0){
            byte[] image = new byte[length];
            //byte[] image = splitMessage[11].getBytes();
            dataInputStream.readFully(image, 0, length);
            String imageAddress = "src\\main\\resources\\server\\" + addProductRequest.getProductId() + ".jpg";
            saveFile(image, imageAddress);
            addProductRequest.setProductImageAddress(imageAddress);
        }
        /*byte[] image = dataInputStream.readAllBytes();
        //byte[] image = splitMessage[11].getBytes();
        String imageAddress = "src\\main\\resources\\server\\" + addProductRequest.getProductId() + ".jpg";
        saveFile(image, imageAddress);
        addProductRequest.setProductImageAddress(imageAddress);
        //addProductRequest.setProductImageBytes(image);*/
        if (addProductRequest.getFileName() != null){
            int fileLength = Integer.parseInt(dataInputStream.readUTF());
            if (fileLength > 0){
                byte[] file = new byte[fileLength];
                //byte[] file = splitMessage[11].getBytes();
                dataInputStream.readFully(file, 0, fileLength);
                String fileAddress = "src\\main\\resources\\server\\" + addProductRequest.getFileName();
                saveFile(file, fileAddress);
                addProductRequest.setProductImageAddress(fileAddress);
            }
        }
    }

    private static void saveFile(byte[] file, String address){
        try (FileOutputStream fileOutputStream = new FileOutputStream(address)){
            fileOutputStream.write(file);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendOff(DataOutputStream dataOutputStream, String offID) {
        Off off = Off.getOffById(offID);
        try {
            dataOutputStream.writeUTF(gson.toJson(off));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendSellerSellLogs(DataOutputStream dataOutputStream, Account account) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        try {
            dataOutputStream.writeUTF(gson.toJson(seller.getSellLogs()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendCustomer(DataOutputStream dataOutputStream, String customerID) {
        Customer customer = Customer.getCustomerById(customerID);
        try {
            dataOutputStream.writeUTF(gson.toJson(customer));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendProductBuyers(DataOutputStream dataOutputStream, String productID) {
        Product product = Product.getProductByID(productID);
        try {
            dataOutputStream.writeUTF(gson.toJson(product.getProductBuyers()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendSellerProductIDs(DataOutputStream dataOutputStream, Account account) {
        Seller seller = Seller.getSellerByUserName(account.getUsername());
        try {
            ArrayList<String> getSellerProductIDs = seller.getProductIDs();
            dataOutputStream.writeUTF(gson.toJson(getSellerProductIDs));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*private static void sendRegisterSupporterRequest(DataOutputStream dataOutputStream, String registerSupporterRequestID) {
        RegisterSupporterRequest registerSupporterRequest = RegisterSupporterRequest.getRequestById(registerSupporterRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(registerSupporterRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }*/

    private static void sendRemoveProductRequest(DataOutputStream dataOutputStream, String removeProductRequestID) {
        RemoveProductRequest removeProductRequest = RemoveProductRequest.getRequestById(removeProductRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(removeProductRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendRegisterSellerRequest(DataOutputStream dataOutputStream, String registerSellerRequestID) {
        RegisterSellerRequest registerSellerRequest = RegisterSellerRequest.getRequestById(registerSellerRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(registerSellerRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendEditProductRequest(DataOutputStream dataOutputStream, String editProductRequestID) {
        EditProductRequest editProductRequest = (EditProductRequest) EditAddProductRequest.getRequestById(editProductRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(editProductRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAddProductRequest(DataOutputStream dataOutputStream, String addProductRequestID) {
        AddProductRequest addProductRequest = (AddProductRequest) EditAddProductRequest.getRequestById(addProductRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(addProductRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendEditOffRequest(DataOutputStream dataOutputStream, String editOffRequestID) {
        EditOffRequest editOffRequest = (EditOffRequest) EditAddOffRequest.getRequestById(editOffRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(editOffRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAddOffRequest(DataOutputStream dataOutputStream, String addOffRequestID) {
        AddOffRequest addOffRequest = (AddOffRequest) EditAddOffRequest.getRequestById(addOffRequestID);
        try {
            dataOutputStream.writeUTF(gson.toJson(addOffRequest));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void declineRequest(String requestID) {
        Request request = Request.getRequestById(requestID);
        Request.removeRequest(request);
    }

    private static void acceptRequest(String requestID) {
        Request request = Request.getRequestById(requestID);
        request.acceptRequest();
        Request.removeRequest(request);
    }

    private static void sendAllRemoveProductRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(RemoveProductRequest.getAllRemoveProductRequests()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllRegisterSellerRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(RegisterSellerRequest.getAllRegisterSellerRequests()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllRegisterSupporterRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(RegisterSupporterRequest.getAllRegisterSupporterRequests()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllEditProductRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(EditProductRequest.getAllEditProductRequests()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllEditOffRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(EditOffRequest.getAllEditOffRequests()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllAddProductRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(AddProductRequest.getAllAddProductRequest()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllAddOffRequests(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(AddOffRequest.getAllAddOffRequest()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void editDiscountIncludingCustomers(String[] split) {
        Discount discount = Discount.getDiscountByDiscountCode(split[1]);
        ArrayList<String> includingCustomers = new ArrayList<>();
        for (int i = 2; i < split.length - 1; i++) {
            includingCustomers.add(split[i]);
        }
        discount.setIncludingCustomers(includingCustomers);
        //todo:
    }

    private static void editDiscountPerCustomer(String[] split) {
        Discount discount = Discount.getDiscountByDiscountCode(split[1]);
        int discountPerCustomer = Integer.parseInt(split[2]);
        discount.setDiscountPerCustomer(discountPerCustomer);
    }

    private static void editDiscountMaxPossibleDiscount(String[] split) {
        Discount discount = Discount.getDiscountByDiscountCode(split[1]);
        Double maxPossibleDiscount = Double.parseDouble(split[2]);
        discount.setMaxPossibleDiscount(maxPossibleDiscount);
    }

    private static void editDiscountPercent(String[] split) {
        Discount discount = Discount.getDiscountByDiscountCode(split[1]);
        double discountPercent = Double.parseDouble(split[2]);
        discount.setDiscountPercent(discountPercent);
    }

    private static void editDiscountEndTime(String[] split) {
        Discount discount = Discount.getDiscountByDiscountCode(split[1]);
        Date endTime = gson.fromJson(split[2], Date.class);
        discount.setEndTime(endTime);
    }

    private static void editDiscountStartTime(String[] splitMessage) {
        Discount discount = Discount.getDiscountByDiscountCode(splitMessage[1]);
        Date startTime = gson.fromJson(splitMessage[2], Date.class);
        discount.setStartTime(startTime);
    }

    private static void createDiscountCode(String message) {
        String[] splitMessage = message.split("&");
        String discountCode = splitMessage[1];
        Date startTime = gson.fromJson(splitMessage[2], Date.class);
        Date endTime = gson.fromJson(splitMessage[3], Date.class);
        String discountPercent = splitMessage[4];
        String maxPossibleDiscount = splitMessage[5];
        String discountPerCustomer = splitMessage[6];
        ArrayList<String> includingCustomers = gson.fromJson(splitMessage[7], new TypeToken<ArrayList<String>>(){}.getType());
        new Discount(discountCode, startTime, endTime, Double.parseDouble(discountPercent),
                Double.parseDouble(maxPossibleDiscount), Integer.parseInt(discountPerCustomer), includingCustomers);
    }

    private static void removeDiscount(String discountCode) {
        Discount discount = Discount.getDiscountByDiscountCode(discountCode);
        Discount.removeDiscount(discount);
    }

    private static void sendAllDiscounts(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(Discount.getAllDiscounts()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllAdmins(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(Admin.getAllAdmins()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllSellers(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(Seller.getAllSellers()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllCustomers(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(Customer.getAllCustomers()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendAllSupporters(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(gson.toJson(Supporter.getAllSupporters()));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteUser(String username) {
        Account account = Account.getAccountByUsername(username);
        Account.deleteAccount(account);
    }

    private static void removeProduct(String productID) {
        Product product = Product.getProductByID(productID);
        Product.removeProduct(product);
    }

    public static HashMap<Supporter, String> getCustomersInQueue() {
        return CustomersInQueue;
    }

    public static void setCustomersInQueue(HashMap<Supporter, String> customersInQueue) {
        CustomersInQueue = customersInQueue;
    }

    private static void registerAdmin(String message) {
        String[] userInfo = message.split(",");
        String username = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];
        String email = userInfo[5];
        String phoneNumber = userInfo[6];
        Admin admin = new Admin(username, password, firstName, lastName, email, phoneNumber);
        String bankId = BankConnection.createAccount(admin.getName(), admin.getLastName(), admin.getUsername(), admin.getPassword());
        Admin.setAllStoreBankIDs(bankId);
    }

    private static void createAdminAccount(String message) {
        String[] userInfo = message.split(",");
        String username = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];
        String email = userInfo[5];
        String phoneNumber = userInfo[6];
        new Admin(username, password, firstName, lastName, email, phoneNumber);
    }

    private static void registerCustomer(String message) {
        String[] userInfo = message.split(",");
        String username = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];
        String email = userInfo[5];
        String phoneNumber = userInfo[6];
        new Customer(username, password, firstName, lastName, email, phoneNumber, 0);
    }

    private static void registerSupporter(String message) {
        String[] userInfo = message.split(",");
        String username = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];
        String email = userInfo[5];
        String phoneNumber = userInfo[6];
        new Supporter(username, password, firstName, lastName, email, phoneNumber);
    }

    private static void registerSellerRequest(String message) {
        String[] userInfo = message.split(",");
        String username = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];
        String email = userInfo[5];
        String phoneNumber = userInfo[6];
        String companyName = userInfo[7];
        new RegisterSellerRequest(username, password, firstName, lastName, email, phoneNumber, companyName);
    }

    private static void checkEndOfAuctions() {
        Product.getAllProducts().stream().filter(Product::isInAction).forEach(product -> {
            if (!product.isActionAvailable()){
                product.endAuction();
            }
        });
    }

    private static String getJasonObjectItem(String jasonObject, String item){
        if (item.equals("token")){
            return jasonObject.substring(10, jasonObject.indexOf(',') - 1);
        }
        else {
            return jasonObject.substring(jasonObject.indexOf(',') + 12, jasonObject.length() - 2);
        }
    }

    private static String generateToken(){
        char[] code = new char[25];
        Random random = new Random();
        for (int i = 0; i < 25; ++i) {
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

    private static void sendAccountInfo(DataOutputStream dataOutputStream, Account account){
        try {
            if (account instanceof Admin) {
                dataOutputStream.writeUTF("Admin: " + gson.toJson((Admin) account));
            }
            else if (account instanceof Customer) {
                String json = new GsonBuilder().enableComplexMapKeySerialization().create().toJson((Customer) account);
                //dataOutputStream.writeUTF("Customer: " + gson.toJson((Customer) account));
                dataOutputStream.writeUTF("Customer: " + json);
            }
            else if (account instanceof Supporter) {
                dataOutputStream.writeUTF("Supporter: " + gson.toJson((Supporter) account));
            }
            else {
                dataOutputStream.writeUTF("Seller: " + gson.toJson((Seller) account));
            }
            dataOutputStream.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
