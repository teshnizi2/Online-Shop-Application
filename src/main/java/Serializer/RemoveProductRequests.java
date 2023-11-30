package Serializer;

import Model.Request.RemoveProductRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RemoveProductRequests {
    private final static String pathName = "src\\main\\resources\\removeProductRequests.xml";

    @XmlElement(name = "removeProductRequest")
    private ArrayList<RemoveProductRequest> allRemoveProductRequests = new ArrayList<>();
    private RemoveProductRequests(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                RemoveProductRequests removeProductRequests = JAXB.unmarshal(input, RemoveProductRequests.class);
                RemoveProductRequest.setAllRemoveProductRequests(removeProductRequests.allRemoveProductRequests);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            RemoveProductRequests removeProductRequests = new RemoveProductRequests();
            removeProductRequests.allRemoveProductRequests = RemoveProductRequest.getAllRemoveProductRequests();
            JAXB.marshal(removeProductRequests, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
