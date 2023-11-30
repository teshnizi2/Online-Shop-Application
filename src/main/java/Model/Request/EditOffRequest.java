package Model.Request;

import Model.Account.Off;
import Model.Account.OffStatus;
import Model.Product.Product;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Date;

public class EditOffRequest extends EditAddOffRequest {
    private static ArrayList<EditOffRequest> allEditOffRequests = new ArrayList<>();
    private Off off;

    public EditOffRequest(Off off) {
        super("edit_off_" + allRequests.size(), RequestType.Editing_Off_Request);
        allEditOffRequests.add(this);
        this.setOff(off);
        this.setOffID(off.getOffID());
        this.setStartTime(off.getStartTime());
        this.setEndTime(off.getEndTime());
        this.setOffAmount(off.getOffAmount());
        this.setOffStatus(OffStatus.PENDING_FOR_EDITION);
        this.setOffProductIDs(off.getProductIDs());
    }

    public EditOffRequest(String offID, Date offStartTime, Date offEndTime, int offAmount, ArrayList<String> offProductIDs) {
        super("edit_off_" + allRequests.size(), RequestType.Editing_Off_Request);
        this.setOff(Off.getOffById(offID));
        this.setOffID(offID);
        this.setStartTime(offStartTime);
        this.setEndTime(offEndTime);
        this.setOffAmount(offAmount);
        this.setOffStatus(OffStatus.PENDING_FOR_EDITION);
        this.setOffProductIDs(offProductIDs);
        allEditOffRequests.add(this);
    }

    public EditOffRequest() {
        super("edit_off_" + allRequests.size(), RequestType.Editing_Off_Request);
        allEditOffRequests.add(this);
    }

    public static ArrayList<EditOffRequest> getAllEditOffRequests() {
        return allEditOffRequests;
    }

    public static void setAllEditOffRequests(ArrayList<EditOffRequest> allEditOffRequests) {
        EditOffRequest.allEditOffRequests = allEditOffRequests;
    }

    public void setOff(Off off) {
        this.off = off;
    }

    public void removeProduct(Product product) {
        offProductIDs.remove(product.getProductId());
    }

    @Override
    public void acceptRequest() throws IllegalArgumentException{
        for (String productID : off.getProductIDs()) {
            Product.getProductByID(productID).setOff(null);
        }
        off.setOffID(offID);
        off.setStartTime(startTime);
        off.setEndTime(endTime);
        off.setOffStatus(OffStatus.CONFIRMED);
        off.setOffAmount(offAmount);
        off.setProductsIDs(offProductIDs);
        for (String productID : offProductIDs) {
            Product.getProductByID(productID).setOff(off);
        }
    }

    @Override
    public String toString() {
        return "EditOffRequest{" +
                "offID='" + offID + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", offAmount=" + offAmount +
                ", offStatus=" + offStatus +
                ", offProductIDs=" + offProductIDs +
                ", requestId='" + requestId + '\'' +
                ", requestType=" + requestType +
                '}';
    }

    @Override
    public void remove() {
        allEditOffRequests.remove(this);
    }
}
