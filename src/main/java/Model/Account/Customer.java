package Model.Account;

import Model.Product.Product;
import Server.BankConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends Account {
    private static ArrayList<Customer> allCustomers = new ArrayList<>();
    private static HashMap<Product, Integer> tmpCart = new HashMap<>();
    private double balance;
    private HashMap<Product, Integer> cart;
    private ArrayList<BuyLog> buyLogs;
    private HashMap<Discount, Integer> usedDiscounts;
    private ArrayList<Discount> allDiscountCodesForCustomer = new ArrayList<>(); //todo:admin add codes to this array too!
    private int numberOfDiscountGifts;
    private static final ArrayList<String> customerFieldsForPurchase = new ArrayList<>();
    static {
        customerFieldsForPurchase.add("name");
        customerFieldsForPurchase.add("lastName");
        customerFieldsForPurchase.add("phoneNumber");
        customerFieldsForPurchase.add("email");
        customerFieldsForPurchase.add("HomeAddress");
        customerFieldsForPurchase.add("PostCode");
    }

    private String bankAccountID;

    public Customer(String username, String password, String name, String lastName, String email, String phoneNumber, int balance) {
        super(username, password, name, lastName, email, phoneNumber);
        this.balance = balance;
        cart = new HashMap<>();
        buyLogs = new ArrayList<>();
        usedDiscounts = new HashMap<>();
        this.balance = 1500;
        createBankAccount();
        allCustomers.add(this);
    }

    public Customer(){
        allCustomers.add(this);
        cart = new HashMap<>();
        buyLogs = new ArrayList<>();
        usedDiscounts = new HashMap<>();
    }

    public String getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(String bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    private void createBankAccount() {
        bankAccountID = BankConnection.createAccount(name, lastName, username, password);
        try {
            String output = BankConnection.deposit(username, password, 1000, bankAccountID);
            System.out.println(output);//todo
            balance = balance - 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllDiscountCodesForCustomer(ArrayList<Discount> allDiscountCodesForCustomer) {
        this.allDiscountCodesForCustomer = allDiscountCodesForCustomer;
    }

    public int getNumberOfDiscountGifts() {
        return numberOfDiscountGifts;
    }

    public void setNumberOfDiscountGifts(int numberOfDiscountGifts) {
        this.numberOfDiscountGifts = numberOfDiscountGifts;
    }

    public static ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static void setAllCustomers(ArrayList<Customer> allCustomers) {
        Customer.allCustomers = allCustomers;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public void addToCart(Product product, int number) {
        if (cart.containsKey(product)) {
            int newNumber = cart.get(product) + number;
            if (product.isFile() && newNumber > 1) {
                if (number <= 0) {
                    cart.remove(product);
                } else {
                    cart.put(product, newNumber);
                }
            }
        }
        else if (number > 0) {
            cart.put(product, number);
        }
    }

    public double getTotalPrice(){
        double price = 0;
        for (Product product : cart.keySet()) {
            price += product.getPrice() * cart.get(product);
        }
        return price;
    }

    public void setUsedDiscounts(HashMap<Discount, Integer> usedDiscounts) {
        this.usedDiscounts = usedDiscounts;
    }

    public HashMap<Product, Integer> getCart() {
        return cart;
    }

    public void setCart(HashMap<Product, Integer> cart) {
        this.cart = cart;
    }

    public ArrayList<BuyLog> getBuyLogs() {
        return buyLogs;
    }

    public HashMap<Discount, Integer> getUsedDiscounts() {
        return usedDiscounts;
    }

    public BuyLog getBuyLogByID(String ID) {
        for (BuyLog buylog : buyLogs) {
            if (buylog.getID().equals(ID)) {
                return buylog;
            }
        }
        return null;
    }

    public ArrayList<Discount> getAllDiscountCodesForCustomer() {
        return allDiscountCodesForCustomer;
    }

    public static ArrayList<String> getCustomerFieldsForPurchase() {
        return customerFieldsForPurchase;
    }

    public static void addProductToTmpCart(Product product){
        if (tmpCart.containsKey(product)) {
            tmpCart.put(product, tmpCart.get(product) + 1);
        }
        else {
            tmpCart.put(product, 1);
        }
    }

    public static Customer getCustomerById(String username){
        for (Customer customer : allCustomers) {
            if (customer.getUsername().equals(username)){
                return customer;
            }
        }
        return null;
    }

    public static HashMap<Product, Integer> getTmpCart() {
        return tmpCart;
    }

    public static void setTmpCart(HashMap<Product, Integer> tmpCart) {
        Customer.tmpCart = tmpCart;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "balance=" + balance +
                ", cart=" + cart +
                ", buyLogs=" + buyLogs +
                ", usedDiscounts=" + usedDiscounts +
                ", allDiscountCodesForCustomer=" + allDiscountCodesForCustomer +
                ", numberOfDiscountGifts=" + numberOfDiscountGifts +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public void removeUser() {
        allCustomers.remove(this);
        for (Discount discount : allDiscountCodesForCustomer) {
            discount.removeCustomer(this);
        }
    }

    public void removeDiscount(Discount discount) {
        this.allDiscountCodesForCustomer.remove(discount);
    }
}
