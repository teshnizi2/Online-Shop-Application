package Serializer;

import Model.Request.EditOffRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class EditOffRequests {
    private final static String pathName = "src\\main\\resources\\editOffRequest.xml";

    @XmlElement(name = "editOffRequest")
    private ArrayList<EditOffRequest> allEditOffRequests = new ArrayList<>();

    private EditOffRequests(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                EditOffRequests editOffRequest = JAXB.unmarshal(input, EditOffRequests.class);
                EditOffRequest.setAllEditOffRequests(editOffRequest.allEditOffRequests);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            EditOffRequests editOffRequest = new EditOffRequests();
            editOffRequest.allEditOffRequests = EditOffRequest.getAllEditOffRequests();
            JAXB.marshal(editOffRequest, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
