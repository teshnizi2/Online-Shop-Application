package Controller;

import Client.Connection;
import Model.Account.Account;

public class ProfileManager {
    private Account account;

    public ProfileManager(Account account) {
        this.account = account;
    }

    public String viewPersonalInfo() {
        return account.toString();
    }

    public void editFieldOfProfile(String fieldName, String fieldChange) throws IllegalArgumentException, IllegalAccessException {
        if (fieldName.equals("username")) {
            throw new IllegalAccessException();
        } else {
            if (fieldName.equalsIgnoreCase("password")) {
                account.setPassword(fieldChange);
            } else if (fieldName.equalsIgnoreCase("name")) {
                account.setName(fieldChange);
            } else if (fieldName.equalsIgnoreCase("lastName")) {
                account.setLastName(fieldChange);
            } else if (fieldName.equalsIgnoreCase("email")) {
                account.setEmail(fieldChange);
            } else if (fieldName.equalsIgnoreCase("phone number")) {
                account.setPhoneNumber(fieldChange);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public String getUsername() {
        Connection.sendToServerWithToken("get username: ");
        String username = Connection.receiveFromServer();
        return username;
    }

    public String getPassword() {
        Connection.sendToServerWithToken("get password: ");
        String password = Connection.receiveFromServer();
        return password;
    }

    public String getFirstName() {
        Connection.sendToServerWithToken("get first name: ");
        String firstName = Connection.receiveFromServer();
        return firstName;
    }

    public String getLastName() {
        Connection.sendToServerWithToken("get last name: ");
        String lastName = Connection.receiveFromServer();
        return lastName;
    }

    public String getEmail() {
        Connection.sendToServerWithToken("get email: ");
        String email = Connection.receiveFromServer();
        return email;
    }

    public String getPhoneNumber() {
        Connection.sendToServerWithToken("get phone number: ");
        String phoneNumber = Connection.receiveFromServer();
        return phoneNumber;
    }

    public void editPassword(String password) throws IllegalArgumentException{
        if (password.equals("") || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Password.");
        }
        else {
            Connection.sendToServerWithToken("edit password: " + password);
        }
    }

    public void editFirstName(String firstName) throws IllegalArgumentException {
        if (firstName.matches("[a-zA-Z ]+")){
            Connection.sendToServerWithToken("edit first name: " + firstName);
        }
        else {
            throw new IllegalArgumentException("Invalid Name.");
        }
    }

    public void editLastName(String lastName) throws IllegalArgumentException {
        if (lastName.matches("[a-zA-Z ]+")){
            Connection.sendToServerWithToken("edit last name: " + lastName);
        }
        else {
            throw new IllegalArgumentException("Invalid Name.");
        }
    }

    public void editEmail(String email) throws IllegalArgumentException {
        if (email.matches(".+?@\\w+\\.\\w+")){
            Connection.sendToServerWithToken("edit email: " + email);
        }
        else {
            throw new IllegalArgumentException("Invalid Email.");
        }
    }

    public void editPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        if (phoneNumber.matches("\\d+")){
            Connection.sendToServerWithToken("edit phone number: " + phoneNumber);
        }
        else {
            throw new IllegalArgumentException("Invalid Phone Number.");
        }
    }
}
