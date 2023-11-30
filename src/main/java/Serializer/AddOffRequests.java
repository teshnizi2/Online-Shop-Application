package Serializer;

import Model.Request.AddOffRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AddOffRequests {
    private final static String pathName = "src\\main\\resources\\AddOffRequests.xml";

    @XmlElement(name = "addOffRequest")
    private ArrayList<AddOffRequest> allAddOffRequests = new ArrayList<>();
    private AddOffRequests(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                AddOffRequests addOffRequests = JAXB.unmarshal(input, AddOffRequests.class);
                AddOffRequest.setAllAddOffRequest(addOffRequests.allAddOffRequests);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            AddOffRequests addOffRequests = new AddOffRequests();
            addOffRequests.allAddOffRequests = AddOffRequest.getAllAddOffRequest();
            JAXB.marshal(addOffRequests, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
