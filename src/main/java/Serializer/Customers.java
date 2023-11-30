package Serializer;

import Model.Account.Customer;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Customers {
    private final static String pathName = "src\\main\\resources\\customers.xml";

    @XmlElement(name = "customer")
    private ArrayList<Customer> allCustomer = new ArrayList<>();
    private Customers(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Customers customers = JAXB.unmarshal(input, Customers.class);
                HashMap<String, Integer> duplicateCustomers = new HashMap<>();
                customers.allCustomer.forEach(customer -> duplicateCustomers.put(customer.getUsername(),
                        duplicateCustomers.getOrDefault(customer.getUsername(), 0) + 1));
                duplicateCustomers.forEach((customer, number) -> {
                    for (int i = 0; i < number - 1; i++) {
                        customers.allCustomer.remove(Customer.getCustomerById(customer));
                    }
                });
                Customer.setAllCustomers(customers.allCustomer);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Customers customers = new Customers();
            customers.allCustomer = Customer.getAllCustomers();
            JAXB.marshal(customers, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
