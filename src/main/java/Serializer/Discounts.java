package Serializer;

import Model.Account.Discount;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Discounts {
    private final static String pathName = "src\\main\\resources\\discounts.xml";

    @XmlElement(name = "discount")
    private ArrayList<Discount> allDiscounts = new ArrayList<>();
    private Discounts(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Discounts discounts = JAXB.unmarshal(input, Discounts.class);
                Discount.setAllDiscounts(discounts.allDiscounts);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Discounts discounts = new Discounts();
            discounts.allDiscounts = Discount.getAllDiscounts();
            JAXB.marshal(discounts, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
