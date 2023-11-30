package View.CustomerProfileMenus;

import Controller.CustomerProfileManager;
import Controller.ProductsManager;
import Model.Account.Customer;
import View.Menu;

import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseMenu extends Menu {
    private Customer customer;
    private CustomerProfileManager customerProfileManager;
    private CustomerProfileMenu customerProfileMenu;
    public PurchaseMenu(Customer customer, Menu parentMenu) {
        super("Purchase Menu", parentMenu);
        this.customer = customer;
        this.customerProfileManager = new CustomerProfileManager(customer);
    }
    @Override
    public void execute() {
        System.out.println("PurchaseMenu:");
        System.out.println("In each Menu you can use (Back) to return, (Next) to go next page or (Logout) to leave your account!");
        HashMap<String, String> customerInformationForPurchase = ReceiverInformation();
        String DiscountCodeForPurchase = DiscountCode();
        payment(customerInformationForPurchase, DiscountCodeForPurchase);
    }

    public HashMap<String, String> ReceiverInformation() {
        ArrayList<String> receiveInformationFields = CustomerProfileManager.getReceiveFieldsForPurchase();
        ArrayList<String> receiveInformationFieldsValue = new ArrayList<>(receiveInformationFields.size());
        HashMap<String, String> fieldsAndNewValues = new HashMap<>();
        int pageNumber = 1;
        while (true) {
            if (pageNumber == 1)
                System.out.println("Receiver Information:");
            if (pageNumber <= receiveInformationFields.size()) {
                System.out.println("Enter Your " + receiveInformationFields.get(pageNumber - 1) + ":");
            } else if (pageNumber == receiveInformationFields.size() + 1) {
                System.out.println("are this information true?(Write (next) if they are true)");
                for (int i = 0; i < receiveInformationFieldsValue.size(); i++) {
                    fieldsAndNewValues.put(receiveInformationFields.get(i), receiveInformationFieldsValue.get(i));
                }
            }
            MyResult input = scanAdvance(pageNumber,1,parentMenu);
            if (input.getValid())
                pageNumber += Integer.parseInt(input.getMessage());
            else {
                pageNumber += 1;
                if (pageNumber == receiveInformationFields.size() + 2) {
                    if (!customerProfileManager.areNewReceivedFieldsValueValid(fieldsAndNewValues).getValid()) {
                        System.out.println(input.getMessage());
                        pageNumber = Integer.parseInt(input.getSecondMessage());
                    } else {
                        return fieldsAndNewValues;
                    }
                } else {
                    receiveInformationFieldsValue.set(pageNumber - 2, input.getMessage());
                }
            }
        }
    }

    public String DiscountCode() {
        System.out.println("Discount Code:");
        int pageNumber = 1;
        MyResult input =  scanAdvance(1,1,this);
        if (input.getValid())
            return "";
        if (!CustomerProfileManager.isDiscountCodeValid(input.getMessage()).getValid())
            System.out.println(input.getMessage());
        System.out.println(input.getMessage());
        return input.getSecondMessage();
    }

    public void payment(HashMap<String, String> customerInformationForPurchase, String discountCodeForPurchase) {
        System.out.println("Payment:");
        double price = customerProfileManager.costCalculatorWithOffAndDiscount(discountCodeForPurchase);
        double totalPrice = customerProfileManager.costCalculator();
        System.out.printf("The total cost of your products is %s$ and with discount and off is %s$ do you want to pay?(write (next) if your want to pay)\n", (ProductsManager.getTotalPrice()), price);
        while(scanAdvance(1,1,parentMenu).getValid())
            System.out.println("please enter a valid command!");
        if (customer.getBalance() < price) {
            System.out.println("You don`t have enough money. Go Out Of My Shop And Work Harder!");
            parentMenu.execute();
        }
        System.out.println("You payed successfully.have nice day!");
//        customerProfileManager.doingsAfterBuyProducts(price, totalPrice ,discountCodeForPurchase);

    }

}
