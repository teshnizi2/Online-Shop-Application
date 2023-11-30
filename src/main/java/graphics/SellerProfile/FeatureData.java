package graphics.SellerProfile;

public class FeatureData {
    private String specialFeature;
    private String value;

    public FeatureData(String specialFeature, String value) {
        this.specialFeature = specialFeature;
        this.value = value;
    }

    public String getSpecialFeature() {
        return specialFeature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
