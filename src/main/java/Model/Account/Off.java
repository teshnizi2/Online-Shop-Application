package Model.Account;

import Model.Product.Product;

import java.util.ArrayList;
import java.util.Date;

public class Off {
    private static ArrayList<Off> allOffs = new ArrayList<>();
    private String offID;
    private Date startTime;
    private Date endTime;
    private int offAmount;
    private OffStatus offStatus;
    private ArrayList<String> productIDs;
    private static ArrayList<String> offFields = new ArrayList<>();
    static {
        offFields.add("offID");
        offFields.add("startTime");
        offFields.add("endTime");
        offFields.add("offAmount");
        offFields.add("offStatus");
        offFields.add("products");
    }

    public Off(String offID, Date startTime, Date endTime, int offAmount, ArrayList<Product> products) {
        this.offID = offID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offAmount = offAmount;
        this.offStatus = OffStatus.CONFIRMED;
        this.productIDs = new ArrayList<>();
        for (Product product : products) {
            productIDs.add(product.getProductId());
        }
        allOffs.add(this);
    }

    public Off() {
        allOffs.add(this);
    }

    public static void setAllOffs(ArrayList<Off> allOffs) {
        Off.allOffs = allOffs;
    }

    public String getOffID() {
        return offID;
    }

    public void setOffID(String offID) {
        this.offID = offID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getOffAmount() {
        return offAmount;
    }

    public void setOffAmount(int offAmount) {
        this.offAmount = offAmount;
    }

    public OffStatus getOffStatus() {
        return offStatus;
    }

    public void setProductIDs(ArrayList<String> productIDs) {
        this.productIDs = productIDs;
    }

    public void setOffStatus(OffStatus offStatus) {
        this.offStatus = offStatus;
    }

    public ArrayList<String> getProductIDs() {
        return productIDs;
    }

    public void setProductsIDs(ArrayList<String> productIDs) {
        this.productIDs = productIDs;
    }

    public boolean isAvailable(){
        Date currentDate = new Date();
        return currentDate.compareTo(startTime) >= 0 && currentDate.compareTo(endTime) <= 0;
    }

    public static ArrayList<Off> getAllOffs() {
        return allOffs;
    }

    public static ArrayList<String> getOffFields() {
        return offFields;
    }

    public static Off getOffById(String offID) {
        for (Off off : allOffs) {
            if (off.getOffID().equals(offID)) {
                return off;
            }
        }
        return null;
    }

    public static ArrayList<String> getAllOffsStatus() {
        ArrayList<String> allOffsStatus = new ArrayList<>();
        for (Off off : allOffs) {
            allOffsStatus.add(off.toString());
        }
        return allOffsStatus;
    }

    public static ArrayList<String> getAllOffIds() {
        ArrayList<String> allOffIds = new ArrayList<>();
        for (Off off : allOffs) {
            allOffIds.add(off.getOffID());
        }
        return allOffIds;
    }

    public static void removeOff(Off off) {
        allOffs.remove(off);
    }

    @Override
    public String toString() {
        return "Off{" +
                "offID='" + offID + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", offAmount=" + offAmount +
                ", offStatus=" + offStatus +
                ", productIDs=" + productIDs +
                '}';
    }
}
