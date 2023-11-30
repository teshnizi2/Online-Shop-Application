package Model.Request;

import Model.Account.Account;
import Model.Account.Supporter;

import java.util.ArrayList;

public class RegisterSupporterRequest extends Request {
    private static ArrayList<RegisterSupporterRequest> allRegisterSupporterRequests = new ArrayList<>();
    private String username;
    private String password;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;

    public RegisterSupporterRequest(String username, String password, String name, String lastName, String email,
                                 String phoneNumber) {
        super("register_supporter_" + allRequests.size(), RequestType.Register_Supporter_Request);
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        allRegisterSupporterRequests.add(this);
    }

    public RegisterSupporterRequest(){
        this("", "", "", "", "", "");
    }

    public static ArrayList<RegisterSupporterRequest> getAllRegisterSupporterRequests() {
        return allRegisterSupporterRequests;
    }

    public static void setAllRegisterSupporterRequests(ArrayList<RegisterSupporterRequest> allRegisterSupporterRequests) {
        RegisterSupporterRequest.allRegisterSupporterRequests = allRegisterSupporterRequests;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void acceptRequest() throws IllegalArgumentException{
        if (Account.getAccountByUsername(username) == null) {
            new Supporter(username, password, name, lastName, email, phoneNumber);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "RegisterSupporterRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", requestId='" + requestId + '\'' +
                ", requestType=" + requestType +
                '}';
    }

    @Override
    public void remove() {
        allRegisterSupporterRequests.remove(this);
    }

    public static RegisterSupporterRequest getRequestById(String requestId) {
        for (RegisterSupporterRequest registerSupporterRequest : allRegisterSupporterRequests) {
            if (registerSupporterRequest.getRequestId().equals(requestId)) {
                return registerSupporterRequest;
            }
        }
        return null;
    }
}
