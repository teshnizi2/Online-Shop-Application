package Serializer;

import Model.Account.Admin;
import Model.Account.Off;

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

public class Offs {
    private final static String pathName = "src\\main\\resources\\offs.xml";

    @XmlElement(name = "off")
    private ArrayList<Off> allOffs = new ArrayList<>();
    private Offs(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Offs offs = JAXB.unmarshal(input, Offs.class);
                HashMap<String, Integer> duplicateOffs = new HashMap<>();
                offs.allOffs.forEach(off -> duplicateOffs.put(off.getOffID(),
                        duplicateOffs.getOrDefault(off.getOffID(), 0) + 1));
                duplicateOffs.forEach((off, number) -> {
                    for (int i = 0; i < number - 1; i++) {
                        offs.allOffs.remove(Off.getOffById(off));
                    }
                });
                Off.setAllOffs(offs.allOffs);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Offs offs = new Offs();
            offs.allOffs = Off.getAllOffs();
            JAXB.marshal(offs, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
