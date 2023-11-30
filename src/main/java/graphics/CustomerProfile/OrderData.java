package graphics.CustomerProfile;

public class OrderData {
    private String id;
    private String date;

    OrderData(String ID, String Date) {
        this.id = ID;
        this.date = Date;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
