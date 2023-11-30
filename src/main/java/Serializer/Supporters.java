package Serializer;

import Model.Account.Supporter;

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

public class Supporters {
    private final static String pathName = "src\\main\\resources\\supporters.xml";

    @XmlElement(name = "supporter")
    private ArrayList<Supporter> allSupporters = new ArrayList<>();
    private Supporters(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Supporters supporters = JAXB.unmarshal(input, Supporters.class);
                HashMap<String, Integer> duplicateSupporters = new HashMap<>();
                supporters.allSupporters.forEach(supporter -> duplicateSupporters.put(supporter.getUsername(),
                        duplicateSupporters.getOrDefault(supporter.getUsername(), 0) + 1));
                duplicateSupporters.forEach((supporter, number) -> {
                    for (int i = 0; i < number - 1; i++) {
                        supporters.allSupporters.remove(Supporter.getSupporterByUserName(supporter));
                    }
                });
                Supporter.setAllSupporters(supporters.allSupporters);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Supporters supporters = new Supporters();
            supporters.allSupporters = Supporter.getAllSupporters();
            JAXB.marshal(supporters, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
