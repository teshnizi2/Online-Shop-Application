package Serializer;

import Model.Product.Category;
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
import java.util.HashMap;

public class Categories {
    private final static String pathName = "src\\main\\resources\\categories.xml";

    @XmlElement(name = "category")
    private ArrayList<Category> allCategories = new ArrayList<>();
    private Categories(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Categories categories = JAXB.unmarshal(input, Categories.class);
                Category.setAllCategories(categories.allCategories);
                HashMap<String, Integer> numberOfCategories = new HashMap<>();
                categories.allCategories.forEach(category ->
                        numberOfCategories.put(category.getName(), numberOfCategories.getOrDefault(category.getName(), 0) + 1));
                numberOfCategories.forEach((name, number) -> {
                    for (int i = 0; i < number - 1; i++) {
                        categories.allCategories.remove(Category.getCategoryByName(name));
                    }
                });
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Categories categories = new Categories();
            categories.allCategories = Category.getAllCategories();
            JAXB.marshal(categories, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
