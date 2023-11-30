package Model.Account;

import java.util.ArrayList;
import java.util.Date;

public class Discount {
    private static ArrayList<Discount> allDiscounts = new ArrayList<>();
    private String discountCode;
    private Date startTime;
    private Date endTime;
    private double discountPercent;
    private double maxPossibleDiscount;
    private int discountPerCustomer;
    private ArrayList<String> includingCustomerUsername;

    public Discount (String discountCode, Date startTime, Date endTime, double discountPercent, double maxPossibleDiscount,
                     int discountPerCustomer, ArrayList<String> includingCustomerUsername) {
        this.discountCode = discountCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountPercent = discountPercent;
        this.maxPossibleDiscount = maxPossibleDiscount;
        this.discountPerCustomer = discountPerCustomer;
        this.setIncludingCustomers(includingCustomerUsername);
        allDiscounts.add(this);
    }

    public Discount() {
        allDiscounts.add(this);
    }

    public static void setAllDiscounts(ArrayList<Discount> allDiscounts) {
        Discount.allDiscounts = allDiscounts;
    }

    public double calculateTotalDiscount(double money){
        double discountAmount = money * discountPercent / 100;
        return Math.min(discountAmount, maxPossibleDiscount);
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
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

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getMaxPossibleDiscount() {
        return maxPossibleDiscount;
    }

    public ArrayList<String> getIncludingCustomerUsername() {
        return includingCustomerUsername;
    }

    public void setDiscountPerCustomer(int discountPerCustomer) {
        this.discountPerCustomer = discountPerCustomer;
    }

    public void setMaxPossibleDiscount(double maxPossibleDiscount) {
        this.maxPossibleDiscount = maxPossibleDiscount;
    }

    public void setIncludingCustomers(ArrayList<String> includingCustomerUsername) {
        this.includingCustomerUsername = includingCustomerUsername;
        for (String s : includingCustomerUsername) {
            Customer customer = Customer.getCustomerById(s);
            if (customer.getAllDiscountCodesForCustomer().contains(this)) {
                return;
            }
            customer.getAllDiscountCodesForCustomer().add(this);
        }
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public int getDiscountPerCustomer() {
        return discountPerCustomer;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setIncludingCustomerUsername(ArrayList<String> includingCustomerUsername) {
        this.includingCustomerUsername = includingCustomerUsername;
    }

    public boolean isAvailable(){
        Date currentDate = new Date();
        return currentDate.compareTo(startTime) >= 0 && currentDate.compareTo(endTime) <= 0;
    }

    public void removeCustomer(Customer customer) {
        includingCustomerUsername.remove(customer.getUsername());
    }

    public static ArrayList<Discount> getAllDiscounts() {
        return allDiscounts;
    }

    public static Discount getDiscountByDiscountCode(String discountCode) {
        for (Discount discount : allDiscounts) {
            if (discount.getDiscountCode().equals(discountCode)) {
                return discount;
            }
        }
        return null;
    }

    public static void removeDiscount(Discount discount) {
        allDiscounts.remove(discount);
        for (String customerUsername : discount.getIncludingCustomerUsername()) {
            Customer customer = Customer.getCustomerById(customerUsername);
            customer.removeDiscount(discount);
        }
    }

    @Override
    public String toString() {
        return "Discount{" +
                "discountCode='" + discountCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", discountPercent=" + discountPercent +
                ", maxPossibleDiscount=" + maxPossibleDiscount +
                ", discountPerCustomer=" + discountPerCustomer +
                ", includingCustomers=" + includingCustomerUsername +
                '}';
    }
}
