package graphics.CustomerProfile;

public class DiscountData {
    private String discountId;
    private String discountStartTime;
    private String discountEndTime;
    private String percent;
    private String discountPerCustomer;

    public DiscountData(String discountId, String discountStartTime, String discountEndTime, String percent, String discountPerCustomer) {
        this.discountId = discountId;
        this.discountStartTime = discountStartTime;
        this.discountEndTime = discountEndTime;
        this.percent = percent;
        this.discountPerCustomer = discountPerCustomer;
    }

    public String getDiscountId() {
        return discountId;
    }

    public String getDiscountStartTime() {
        return discountStartTime;
    }

    public String getDiscountEndTime() {
        return discountEndTime;
    }

    public String getPercent() {
        return percent;
    }

    public String getDiscountPerCustomer() {
        return discountPerCustomer;
    }
}
