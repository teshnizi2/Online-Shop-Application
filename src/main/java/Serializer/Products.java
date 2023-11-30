package Serializer;

import Model.Product.Product;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Products {
    private final static String pathName = "src\\main\\resources\\products.xml";

    @XmlElement(name = "product")
    private ArrayList<Product> allProducts = new ArrayList<>();
    private Products(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Products products = JAXB.unmarshal(input, Products.class);
                Product.setAllProducts(products.allProducts);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Products products = new Products();
            products.allProducts = Product.getAllProducts();
            JAXB.marshal(products, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
