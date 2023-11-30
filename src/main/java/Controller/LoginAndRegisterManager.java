package Controller;


import Client.Connection;
import Model.Account.*;
import Model.Product.Product;
import Model.Request.RegisterSellerRequest;
import View.MainMenu;
import com.google.gson.Gson;

public class LoginAndRegisterManager {

    public boolean isThereAccountWithUserName(String username) {
        Account account = Account.getAccountByUsername(username);
        return account != null;
    }

    public void registerCustomer(String username, String password, String name, String lastName, String email,
                                 String phoneNumber) throws IllegalArgumentException {
        if (checkUsernameValidity(username) && checkPasswordValidity(password) && checkNameValidity(name) && checkNameValidity(lastName)
                && checkEmailValidity(email) && checkPhoneNumberValidity(phoneNumber)) {
            String message = "register customer: ," + username + "," + password + "," + name + "," + lastName + "," + email + "," + phoneNumber + ",";
            Connection.sendToServer(message);
            //new Customer(username, password, name, lastName, email, phoneNumber, 0);
        }
    }

    public void registerSupporter(String username, String password, String name, String lastName, String email,
                                  String phoneNumber) throws IllegalArgumentException {
        if (checkUsernameValidity(username) && checkPasswordValidity(password) && checkNameValidity(name) && checkNameValidity(lastName)
                && checkEmailValidity(email) && checkPhoneNumberValidity(phoneNumber)) {
            String message = "register supporter : ," + username + "," + password + "," + name + "," + lastName + "," + email + "," + phoneNumber + ",";
            Connection.sendToServer(message);
        }
    }

    public void registerSeller(String username, String password, String name, String lastName, String email,
                               String phoneNumber, String companyName) throws IllegalArgumentException {
        if (checkUsernameValidity(username) && checkPasswordValidity(password) && checkNameValidity(name) && checkNameValidity(lastName)
                && checkEmailValidity(email) && checkPhoneNumberValidity(phoneNumber) && checkNameValidity(companyName)) {
            String message = "register seller request: ," + username + "," + password + "," + name + "," + lastName + "," + email + "," + phoneNumber + "," + companyName + ",";
            Connection.sendToServer(message);
            //new RegisterSellerRequest(username, password, name, lastName, email, phoneNumber, companyName);
        }
    }

    public void registerAdmin(String username, String password, String name, String lastName, String email,
                              String phoneNumber) throws IllegalArgumentException {
        if (checkUsernameValidity(username) && checkPasswordValidity(password) && checkNameValidity(name) && checkNameValidity(lastName)
                && checkEmailValidity(email) && checkPhoneNumberValidity(phoneNumber)) {
            String message = "register admin: ," + username + "," + password + "," + name + "," + lastName + "," + email + "," + phoneNumber + ", ";
            Connection.sendToServer(message);
        }
    }

    public boolean canCreateAdminManually() {
        return Admin.getAllAdmins().size() == 0;
    }

    public void loginUser(String username, String password) throws IllegalArgumentException {
        Connection.sendToServer("get account: " + username);
        Account account = Connection.getAccountFromServer();
        if (account == null) {
            throw new IllegalArgumentException("There is no Account with this Username");
        }
        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Your Password is wrong");
        }
        if (account instanceof Customer) {
            Customer customer = (Customer) account;
            for (Product product : Customer.getTmpCart().keySet()) {
                customer.addToCart(product, Customer.getTmpCart().get(product));
            }
        }
        Account.setLoggedInAccount(account);
    }

    public void logoutUser() {
        Account.setLoggedInAccount(null);
        new MainMenu().execute();
    }

    private boolean checkUsernameValidity(String username) throws IllegalArgumentException {
        if (username.matches("[a-zA-Z0-9.]+") && !username.contains("..") && !(username.contains("\\s"))) {
            Account account = Account.getAccountByUsername(username);
            if (account == null) {
                return true;
            } else {
                throw new IllegalArgumentException("There is another account with this username.");
            }
        } else {
            throw new IllegalArgumentException("Invalid Username : " + "UserNames can only contain letters (a-z), numbers (0-9), and periods (.)" + "(UserNames cannot contain more than one dot in a row)\n");
        }
    }

    private boolean checkPasswordValidity(String password) throws IllegalArgumentException {
        if (password.equals("") || password.contains("\\s")) {
            throw new IllegalArgumentException("Invalid Password.");
        } else {
            return true;
        }
    }

    private boolean checkNameValidity(String name) throws IllegalArgumentException {
        if (name.matches("[a-zA-Z ]+")) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid Name.");
        }
    }

    private boolean checkEmailValidity(String email) {
        if (email.matches(".+?@\\w+\\.\\w+")) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid Email.");
        }
    }

    private boolean checkPhoneNumberValidity(String phoneNumber) {
        if (phoneNumber.matches("\\d+")) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid Phone Number.");
        }
    }

    public boolean isLogin() {
        return Account.getLoggedInAccount() != null;
    }
}
