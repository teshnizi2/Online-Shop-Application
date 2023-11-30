package Model.Account;

import Model.Product.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SellLog extends Log {
    private static ArrayList<SellLog> allSellLogs = new ArrayList<>();
    private double received;
    private double offAmount;
    private Product product;
    private String buyerName;
    private boolean hasSent;
    private int number;

    public SellLog(String ID, Date date, double received, double offAmount, Product product,int  number, String buyerName) {
        super(ID, date);
        allSellLogs.add(this);
        this.received = received;
        this.offAmount = offAmount;
        this.product = product;
        this.number = number;
        this.buyerName = buyerName;
    }

    public SellLog() {
        allSellLogs.add(this);
    }

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public double getOffAmount() {
        return offAmount;
    }

    public void setOffAmount(double offAmount) {
        this.offAmount = offAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public boolean isHasSent() {
        return hasSent;
    }

    public void setHasSent(boolean hasSent) {
        this.hasSent = hasSent;
    }

    public static ArrayList<SellLog> getAllSellLogs() {
        return allSellLogs;
    }

    @Override
    public String toString() {
        return "SellLog{" +
                "received=" + received +
                ", offAmount=" + offAmount +
                ", product=" + product +
                ", buyerName='" + buyerName + '\'' +
                ", hasSent=" + hasSent +
                '}';
    }
}
