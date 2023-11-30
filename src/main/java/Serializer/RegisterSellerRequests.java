package Serializer;


import Model.Request.RegisterSellerRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RegisterSellerRequests {
    private final static String pathName = "src\\main\\resources\\registerSellerRequests.xml";

    @XmlElement(name = "registerSellerRequest")
    private ArrayList<RegisterSellerRequest> allRegisterSellerRequests = new ArrayList<>();
    private RegisterSellerRequests(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                RegisterSellerRequests registerSellerRequests = JAXB.unmarshal(input, RegisterSellerRequests.class);
                RegisterSellerRequest.setAllRegisterSellerRequests(registerSellerRequests.allRegisterSellerRequests);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            RegisterSellerRequests registerSellerRequests = new RegisterSellerRequests();
            registerSellerRequests.allRegisterSellerRequests = RegisterSellerRequest.getAllRegisterSellerRequests();
            JAXB.marshal(registerSellerRequests, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
