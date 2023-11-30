package Model.Request;

import Model.Account.Off;
import Model.Account.OffStatus;
import Model.Account.Seller;
import Model.Product.Product;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Date;

public class AddOffRequest extends EditAddOffRequest {
    private static ArrayList<AddOffRequest> allAddOffRequest = new ArrayList<>();

    private Seller offSeller;

    public AddOffRequest() {
        super("add_off_" + allRequests.size(), RequestType.Adding_Off_Request);
        allAddOffRequest.add(this);
        this.setOffStatus(OffStatus.PENDING_FOR_CREATION);
        this.offProductIDs = new ArrayList<>();
    }

    public AddOffRequest(String offID, Date offStartTime, Date offEndTime, int offAmount, ArrayList<String> offProductIDs, Seller offSeller) {
        super("add_off_" + allRequests.size(), RequestType.Adding_Off_Request);
        this.setOffStatus(OffStatus.PENDING_FOR_CREATION);
        this.offID = offID;
        this.startTime = offStartTime;
        this.endTime = offEndTime;
        this.offAmount = offAmount;
        this.offProductIDs = offProductIDs;
        this.offSeller = offSeller;
        allAddOffRequest.add(this);
    }

    public static ArrayList<AddOffRequest> getAllAddOffRequest() {
        return allAddOffRequest;
    }

    public static void setAllAddOffRequest(ArrayList<AddOffRequest> allAddOffRequest) {
        AddOffRequest.allAddOffRequest = allAddOffRequest;
    }

    @Override
    public void acceptRequest() throws IllegalArgumentException{
        if (Off.getOffById(offID) == null) {
            ArrayList<Product> offProducts = new ArrayList<>();
            for (String offProductID : offProductIDs) {
                offProducts.add(Product.getProductByID(offProductID));
            }
            Off off = new Off(offID, startTime, endTime, offAmount, offProducts);
            offSeller.getOffs().add(off);
            for (Product product : offProducts) {
                product.setOff(off);
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "AddOffRequest{" +
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
        allAddOffRequest.remove(this);
    }
}
