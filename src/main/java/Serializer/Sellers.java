package Serializer;

import Model.Account.Seller;

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

public class Sellers {
    private final static String pathName = "src\\main\\resources\\sellers.xml";

    @XmlElement(name = "seller")
    private ArrayList<Seller> allSellers = new ArrayList<>();
    private Sellers(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Sellers sellers = JAXB.unmarshal(input, Sellers.class);
                HashMap<String, Integer> duplicateSellers = new HashMap<>();
                sellers.allSellers.forEach(seller -> duplicateSellers.put(seller.getUsername(),
                        duplicateSellers.getOrDefault(seller.getUsername(), 0) + 1));
                duplicateSellers.forEach((seller, number) -> {
                    for (int i = 0; i < number - 1; i++) {
                        sellers.allSellers.remove(Seller.getSellerByUserName(seller));
                    }
                });
                Seller.setAllSellers(sellers.allSellers);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Sellers sellers = new Sellers();
            sellers.allSellers = Seller.getAllSellers();
            JAXB.marshal(sellers, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
