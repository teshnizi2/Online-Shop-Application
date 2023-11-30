package Model.Request;

import Model.Product.Product;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class RemoveProductRequest extends Request {
    private static ArrayList<RemoveProductRequest> allRemoveProductRequests = new ArrayList<>();
    private Product product;

    public RemoveProductRequest(Product product) {
        super("remove_product_" + allRequests.size(), RequestType.Remove_Product_Request);
        this.product = product;
        allRemoveProductRequests.add(this);
    }

    public RemoveProductRequest() {
        super("", null);
        allRemoveProductRequests.add(this);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public static ArrayList<RemoveProductRequest> getAllRemoveProductRequests() {
        return allRemoveProductRequests;
    }

    public static void setAllRemoveProductRequests(ArrayList<RemoveProductRequest> allRemoveProductRequests) {
        RemoveProductRequest.allRemoveProductRequests = allRemoveProductRequests;
    }

    @Override
    public void acceptRequest() {
        Product.removeProduct(product);
    }

    @Override
    public String toString() {
        return "RemoveProductRequest{" +
                "product=" + product.toString() +
                ", requestId='" + requestId + '\'' +
                ", requestType=" + requestType +
                '}';
    }

    @Override
    public void remove() {
        allRemoveProductRequests.remove(this);
    }

    public static RemoveProductRequest getRequestById(String removeProductRequestID) {
        for (RemoveProductRequest removeProductRequest : allRemoveProductRequests) {
            if (removeProductRequest.getRequestId().equals(removeProductRequestID)) {
                return removeProductRequest;
            }
        }
        return null;
    }
}
