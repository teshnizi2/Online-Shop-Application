package Model.Account;

import Client.Connection;

import java.util.ArrayList;

public abstract class Account {
    private static ArrayList<Account> allAccounts = new ArrayList<>();
    private static Account loggedInAccount;
    protected String username;
    protected String password;
    protected String name;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    private String token;
    private boolean online;

    public Account(String username, String password, String name, String lastName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        allAccounts.add(this);
    }

    public Account() {
        this("", "", "", "", "", "");
    }

    public static void setAllAccounts(ArrayList<Account> allAccounts) {
        Account.allAccounts = allAccounts;
    }

    public boolean isOnline() {
        return online;
    }

    public String getOnline() {
        if (online) {
            return "Online";
        }
        else {
            return "Offline";
        }
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static ArrayList<Account> getAllAccounts() {
        return allAccounts;
    }

    public static Account getLoggedInAccount() {
        return Connection.getLoggedInAccount();
    }

    public static void setLoggedInAccount(Account loggedInAccount) {
        if (loggedInAccount == null){
            logout();
        }
        Account.loggedInAccount = loggedInAccount;
    }

    public static void logout(){
        Connection.sendToServerWithToken("logout");
    }

    public static Account getAccountByUsername (String username) {
        for (Account account : allAccounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    public static Account getAccountByToken (String token) {
        for (Account account : allAccounts) {
            if (account.getToken() == null){
                continue;
            }
            if (account.getToken().equals(token)) {
                return account;
            }
        }
        return null;
    }

    public static void deleteAccount(Account account) {
        allAccounts.remove(account);
        account.removeUser();
    }

    public static ArrayList<Account> getOnlineUsers(){
        ArrayList<Account> onlineAccount = new ArrayList<>();
        for (Account account : allAccounts) {
            if (account.isOnline()){
                onlineAccount.add(account);
            }
        }
        return onlineAccount;
    }

    public static ArrayList<String> getOnlineUsersUsername(){
        ArrayList<String> onlineAccount = new ArrayList<>();
        for (Account account : allAccounts) {
            if (account.isOnline()){
                onlineAccount.add(account.getUsername());
            }
        }
        return onlineAccount;
    }

    public abstract void removeUser();

    @Override
    public abstract String toString();
}

