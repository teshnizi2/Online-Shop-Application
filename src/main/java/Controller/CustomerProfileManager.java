package Controller;

import Client.ChatClient;
import Client.Connection;
import Model.Account.*;
import Model.Product.Product;
import Model.Product.Score;
import Server.ChatServer;
import Server.Server;
import View.Menu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.Socket;
import java.util.*;

public class CustomerProfileManager extends ProfileManager{
    public Customer customer;
    public SupporterProfileManager supporterProfileManager;

    public CustomerProfileManager(Customer customer) {
        super(customer);
        this.customer = customer;
    }

    public double getBalance() {
        Connection.sendToServerWithToken("get account balance");
        double customerBalance = Double.parseDouble(Connection.receiveFromServer());
        return customerBalance;
    }

    private double getBankAccountBalance() {
        Connection.sendToServerWithToken("get bank account balance");
        double customerBankAccountBalance = Double.parseDouble(Connection.receiveFromServer());
        return customerBankAccountBalance;
    }

    public void chargeWallet(int chargeWalletAmount) throws Exception{
        double bankAccountBalance = getBankAccountBalance();
        double previousBalance = getBalance();
        int minWalletBalance = AdminProfileManager.getMinWalletBalance();
        if (bankAccountBalance < chargeWalletAmount) {
            throw new Exception("You don't have enough money");
        }
        else if ((previousBalance + chargeWalletAmount) < minWalletBalance) {
            throw new Exception("You must have at least " + minWalletBalance + " in your wallet.");
        }
        else {
            Connection.sendToServerWithToken("charge wallet: " + chargeWalletAmount);
        }
    }

    public String checkForDiscountGift(){
        final int minimumAmountOfMoney = 1000;
        final int discountPercentage = 10;
        final int discountPerCustomer = 1;
        final int maxPossibleDiscount = 100;
        final long duration = 1000L * 60 * 60 * 24 * 30;

        int totalMoneyPaid = 0;
        for (BuyLog buyLog : customer.getBuyLogs()) {
            totalMoneyPaid += buyLog.getPaidAmount();
        }

        String output;
        if (totalMoneyPaid > minimumAmountOfMoney * (customer.getNumberOfDiscountGifts() + 1)){
            customer.setBalance(customer.getNumberOfDiscountGifts() + 1);
            output = ("Congratulations! you have won" + discountPercentage + "% discount up to" + maxPossibleDiscount +
                    "$ for buying more than " + (customer.getNumberOfDiscountGifts() + 1) * minimumAmountOfMoney + " dollars from Us!") +
                    ("\nNote! you can use it once till next month.") +
                    ("\nYour discount code is:" );
            String code = AdminProfileManager.generateRandomDiscountCode();
            output += code;
            Date now = new Date();
            ArrayList<String> includingCustomers = new ArrayList<>();
            includingCustomers.add(customer.getUsername());
            try {
                new AdminProfileManager(null).createDiscountCode(code, now, new Date(now.getTime() + duration), String.valueOf(discountPercentage),
                        String.valueOf(maxPossibleDiscount), String.valueOf(discountPerCustomer), includingCustomers);
            } catch (Exception e) {
                System.out.println("couldn't give bonus discount code");
            }
        }
        else {
            output = "";
        }
        return output;
    }

    public BuyLog getBuyLogByID(String orderID) throws NullPointerException{
        Connection.sendToServer("get buyLog: " + orderID);
        BuyLog buyLog = new Gson().fromJson(Connection.receiveFromServer(), BuyLog.class);
        if (buyLog == null) {
            throw new NullPointerException();
        }
        return buyLog;
    }

    public ArrayList<BuyLog> getBuyLogs() {
        Connection.sendToServerWithToken("get customer buyLogs: ");
        ArrayList<BuyLog> customerBuyLogs = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<BuyLog>>(){}.getType());
        return customerBuyLogs;
    }

    public ArrayList<Discount> getAllDiscountCodesForCustomer() {
        Connection.sendToServerWithToken("get customer discounts: ");
        ArrayList<Discount> customerDiscounts = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Discount>>(){}.getType());
        return customerDiscounts;
    }

    public void connectSupporter(int supporterID) throws Exception {
        new Thread(() -> {
            try {
                ChatServer.main(ChatServer.getI() + supporterID + 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            try {
                ChatClient.main(ChatServer.getI() + supporterID + 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        String message = "put supporter in customers in queue: &" + supporterID + "&" + (ChatServer.getI() + supporterID + 1000);
        Connection.sendToServer(message);
//        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Thread.sleep(500);
        new Thread(() -> {
            try {
                Connection.sendToServer("get customers in queue");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            try {
                HashMap<Supporter, String> customersInQueue = new Gson().fromJson(Connection.receiveFromServer(),
                        new TypeToken<HashMap<Supporter, String>>(){}.getType());
                System.out.println("SUPPORTER PUT"+ customersInQueue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
//        System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
//        Server.getCustomersInQueue().put(supporterProfileManager.getSupporterByID(supporterID), String.valueOf(ChatServer.getI() + supporterID + 1000));
    }

    public TableView getAllSupportersTable(TableView allSupportersTable) {
        TableColumn<String, Supporter> column1 = new TableColumn<>("Supporter Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<String, Supporter> column2 = new TableColumn<>("Supporter Line");
        column2.setCellValueFactory(new PropertyValueFactory<>("online"));

        allSupportersTable.getColumns().addAll(column1, column2);

        Connection.sendToServer("getSupporters");
        ArrayList<Supporter> allSupporters = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Supporter>>(){}.getType());

        for (Supporter supporter : allSupporters) {
            allSupportersTable.getItems().add(supporter);
        }
        allSupportersTable.setPlaceholder(new Label("No Data to display"));
        return allSupportersTable;
    }








    //phase1
    public int getNumberOfProductInCart(Product product, Customer customer) {
        for (Product product1 : customer.getCart().keySet()) {
            if (product1.getProductId().equals(product.getProductId())) {
                return customer.getCart().get(product1);
            }
        }
        return 0;
    }

    public BuyLog showOrder(String id) {
        return customer.getBuyLogByID(id);
    }

    public void rateProduct(String id, String stringScore) {
        int intScore = Integer.parseInt(stringScore);
        Score score = new Score(customer, Product.getProductByID(id), intScore);
        Product.getProductByID(id).getAllScores().add(score);
    }

    public double viewBalance() {
        return customer.getBalance();
    }

    public static int getExistingNumberOfProductInStore(Product product, int number) {
        return product.getExistingNumber();
    }

    public static ArrayList<String> getReceiveFieldsForPurchase() {
        return Customer.getCustomerFieldsForPurchase();
    }

    public Menu.MyResult areNewReceivedFieldsValueValid(HashMap<String, String> receivedFields) {
        boolean valid = true;
        String message = "Your information submitted";
        String secondMessage = "";
        if (!receivedFields.get("name").matches("\\A\\w+\\z")) {
            valid = false;
            message = "please enter a valid name";
            secondMessage = "1";
        } else if (!receivedFields.get("name").matches("\\A\\w+\\z")) {
            valid = false;
            message = "please enter a valid lastName";
            secondMessage = "2";
        } else if (!receivedFields.get("lastName").matches("\\A\\w+\\z")) {
            valid = false;
            message = "please enter a valid name";
            secondMessage = "3";
        } else if (!receivedFields.get("phoneNumber").matches("\\A\\d+\\z")) {
            valid = false;
            message = "please enter a valid phoneNumber";
            secondMessage = "4";
        } else if (!receivedFields.get("email").matches("@gmail.com\\z")) {
            valid = false;
            message = "please enter a valid email";
            secondMessage = "5";
        } else if (!receivedFields.get("PostCode").matches("\\A\\d+\\z")) {
            valid = false;
            message = "please enter a valid PostCode";
            secondMessage = "6";
        }
        return new Menu.MyResult(valid, message, "");
    }

    public ArrayList<Discount> getDiscountCodes() {
        return customer.getAllDiscountCodesForCustomer();
    }

    public static Menu.MyResult isDiscountCodeValid(String discountCode) {
        Discount discount = Discount.getDiscountByDiscountCode(discountCode);
        if (discount == null){
            return new Menu.MyResult(false, "There is no discount with this ID", "") ;
        }
        if (!discount.getIncludingCustomerUsername().contains((Customer) Account.getLoggedInAccount())) {
            return new Menu.MyResult(false, "You cant use this discount!", "");
        }
        Date date = new Date();
        if(!(date.compareTo(discount.getStartTime()) >= 0 && date.compareTo(discount.getEndTime()) <= 0)) {
            return new Menu.MyResult(false, "This discountID is not up now!", "");
        }
        return new Menu.MyResult(true, "Your discount code submitted", discountCode);
    }

    public double costCalculatorWithOff() {
        double priceWithOff = 0.0;
        for (Product product: customer.getCart().keySet()) {
            priceWithOff += product.getPriceWithOff();
        }
        return priceWithOff;
    }
    public double costCalculatorWithOffAndDiscount(String discountCode) {
        double priceWithOffAndDiscount = 0.0;
        Discount discount;
        for (Product product: customer.getCart().keySet()) {
            priceWithOffAndDiscount += product.getPriceWithOff();
        }
        if(!discountCode.equals("")) {
            discount = Discount.getDiscountByDiscountCode(discountCode);
            priceWithOffAndDiscount -= priceWithOffAndDiscount * discount.getDiscountPercent()/100;
        }
        return priceWithOffAndDiscount;
    }

    public double costCalculator () {
        double totalCost = 0;
        for (Product product: customer.getCart().keySet()) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }

//    public void doingsAfterBuyProducts(double price, double totalPrice, String usedDiscountCode) {
//        customer.setBalance(customer.getBalance() - price);
//        setUsedDiscountCodes(usedDiscountCode);
//        addBuyLog(price,totalPrice, customer.getCart());
//        //System.out.println(checkForDiscountGift());
//        for (Product product : customer.getCart().keySet()) {
//            Seller seller = product.getSeller();
//            int productNewExistingNumber = product.getExistingNumber() - customer.getCart().get(product);
//            seller.setBalance(seller.getBalance() + product.getPriceWithOff()); //seller setBalance
//            product.setExistingNumber(productNewExistingNumber); //Product setExistingNumber
//            if (!product.getProductBuyers().contains(customer))
//                product.getProductBuyers().add(customer.getUsername()); //Product setProductBuyers
//        }
//        for (Product product :customer.getCart().keySet()) {
//            double received = product.getPriceWithOff();
//            addSellLog(received, product.getPrice() - received, product, customer.getCart().get(product),customer.getName(), product.getSeller());
//        }
    //discount.getDiscountPerCustomer();
    //todo: check discount code use less than

//    }

    public boolean canCustomerPay(double cost) {
        if (customer.getBalance() >= cost) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCustomerBuyLogsNull() {
        if (customer.getBuyLogs() == null) {
            return true;
        }
        return false;
    }

    public boolean isInputInteger(String input) {
        return input.matches("\\A\\d+\\z");
    }

    public void setUsedDiscountCodes(String usedDiscountCode) {
        if (!usedDiscountCode.equals("")) {
            Discount discount =  Discount.getDiscountByDiscountCode(usedDiscountCode);
            if (customer.getUsedDiscounts().containsKey(discount)) {
                customer.getUsedDiscounts().put(discount, customer.getUsedDiscounts().get(discount) + 1);
            } else {
                customer.getUsedDiscounts().put(discount, 1);
            }
        }
    }

//    public void addBuyLog(double price, double totalPrice, HashMap<Product, Integer> Cart) { //todo
//        String buyLogID = customer.getUsername() + customer.getBuyLogs().size();
//        BuyLog buyLog = new BuyLog(buyLogID, new Date(), price, totalPrice - price, customer.getCart());
//        customer.getBuyLogs().add(buyLog);
//    }

    public void addSellLog(double received, double offAmount, Product product, int number, String buyerName, Seller seller) {
        String sellLogID = seller.getUsername() + seller.getSellLogs().size();
        SellLog sellLog = new SellLog(sellLogID, new Date(), received, offAmount, product, number, buyerName);
        seller.getSellLogs().add(sellLog);
    }

    public HashMap<String, Date> showOrderIDAndDate () {
        HashMap<String, Date> iDAndDate = new HashMap<>();
        for (BuyLog buyLog : customer.getBuyLogs()) {
            iDAndDate.put(buyLog.getID(),buyLog.getDate());
        }
        return iDAndDate;
    }

    public boolean isInputValidForBuyLogID(String ID) {
        for (BuyLog buyLog : customer.getBuyLogs()) {//todo:if buy log be null we will give wrong input;
            if (buyLog.getID().equals(ID)){
                return true;
            }
        }
        return false;
    }
}
