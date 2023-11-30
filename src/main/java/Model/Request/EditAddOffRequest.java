package Model.Request;

import Model.Account.OffStatus;
import Model.Product.Product;

import java.util.ArrayList;
import java.util.Date;

public abstract class EditAddOffRequest extends Request {
    private static ArrayList<EditAddOffRequest> allEditAddOffRequests = new ArrayList<>();
    protected String offID;
    protected Date startTime;
    protected Date endTime;
    protected int offAmount;
    protected OffStatus offStatus;
    protected ArrayList<String> offProductIDs;

    public EditAddOffRequest(String requestID, RequestType requestType) {
        super(requestID, requestType);
        allEditAddOffRequests.add(this);
    }

    public ArrayList<String> getOffProductIDs() {
        return offProductIDs;
    }

    public String getOffID() {
        return offID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getOffAmount() {
        return offAmount;
    }

    public OffStatus getOffStatus() {
        return offStatus;
    }

    public void setOffID(String offID) {
        this.offID = offID;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setOffAmount(int offAmount) {
        this.offAmount = offAmount;
    }

    public void setOffStatus(OffStatus offStatus) {
        this.offStatus = offStatus;
    }

    public void setOffProductIDs(ArrayList<String> offProductIDs) {
        this.offProductIDs = offProductIDs;
    }

    public void addOffProduct(Product product) {
        offProductIDs.add(product.getProductId());
    }

    @Override
    public abstract void acceptRequest();

    @Override
    public abstract String toString();

    public static EditAddOffRequest getRequestById(String requestId) {
        for (EditAddOffRequest editAddOffRequest : allEditAddOffRequests) {
            if (editAddOffRequest.getRequestId().equals(requestId)) {
                return editAddOffRequest;
            }
        }
        return null;
    }
}
