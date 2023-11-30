package Model.Account;

import Model.Product.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BuyLog extends Log {
    private static ArrayList<BuyLog> allBuyLogs = new ArrayList<>();
    private double paidAmount;
    private double discountAmount;
    private boolean hasDelivered;
    private HashMap<Product, Integer> boughtProducts;
    private String sendingCondition;
    private String address;

    public BuyLog(String ID, Date date, double paidAmount, double discountAmount, HashMap<Product, Integer> cart, String address) {
        super(ID, date);
        this.paidAmount = paidAmount;
        allBuyLogs.add(this);
        this.discountAmount = discountAmount;
        this.boughtProducts = new HashMap<>(cart);
        for (Product product: cart.keySet()) {
            if(product.isFile()){
                setSendingCondition("Send");
            }else{
                setSendingCondition("Did not send");
            }
        }
        this.address = address;
    }

    public BuyLog() {
        allBuyLogs.add(this);
        this.boughtProducts = new HashMap<>();
    }

    public static BuyLog getBuyLogByID(String buyLogID) {
        for (BuyLog buyLog : allBuyLogs) {
            if (buyLog.getID().equals(buyLogID)) {
                return buyLog;
            }
        }
        return null;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public boolean isHasDelivered() {
        return hasDelivered;
    }

    public void setHasDelivered(boolean hasDelivered) {
        this.hasDelivered = hasDelivered;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public HashMap<Product, Integer> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(HashMap<Product, Integer> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public static ArrayList<BuyLog> getAllBuyLogs() {
        return allBuyLogs;
    }

    public void setSendingCondition(String sendingCondition) {
        this.sendingCondition = sendingCondition;
    }

    public String getSendingCondition() {
        return sendingCondition;
    }

    @Override
    public String toString() {
        return "BuyLog{" +
                "paidAmount=" + paidAmount +
                ", discountAmount=" + discountAmount +
                ", hasDelivered=" + hasDelivered +
                ", boughtProducts=" + boughtProducts +
                '}';
    }

    public String getAddress() {
        return address;
    }
}
