package Model.Request;

public enum RequestType {
    Adding_Off_Request("Add Off Request"), Adding_Product_Request("Add Product Request"),
    Editing_Off_Request("Edit Off Request"), Editing_Product_Request("Edit Product Request"),
    Register_Seller_Request("Register Seller Request"), Remove_Product_Request("Remove Product Request"),
    Register_Supporter_Request("Register Supporter Request");

    private String requestType;

    RequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }
}
