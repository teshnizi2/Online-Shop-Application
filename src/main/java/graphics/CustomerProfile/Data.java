package graphics.CustomerProfile;

public class Data {
    private String productName;
    private String productNumber;
    private String productSeller;
    private String sendCondition;

    public Data(String pName, String pNumber, String pSeller, String sCondition) {
        this.productName = pName;
        this.productNumber = pNumber;
        this.productSeller = pSeller;
        this.sendCondition = sCondition;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public String getSendCondition() {
        return sendCondition;
    }

    /*public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductNumber() {
        return productNumber.get();
    }

    public SimpleStringProperty productNumberProperty() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber.set(productNumber);
    }

    /*public String getProductSeller() {
        return productSeller.get();
    }

    public SimpleStringProperty productSellerProperty() {
        return productSeller;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller.set(productSeller);
    }
    */
}
