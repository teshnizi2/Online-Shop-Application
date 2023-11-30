package Serializer;

import Model.Account.Admin;
import Model.Account.Customer;

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

public class Admins {
    private final static String pathName = "src\\main\\resources\\admin.xml";

    @XmlElement(name = "admin")
    private ArrayList<Admin> allAdmins = new ArrayList<>();
    private Admins(){
    }

    public static void deserializeXML(){
        File file = new File(pathName);
        if(file.exists() && !file.isDirectory() && file.length() > 0) {
            try(BufferedReader input = Files.newBufferedReader(Paths.get(pathName))) {
                Admins admins = JAXB.unmarshal(input, Admins.class);
                HashMap<String, Integer> duplicateAdmins = new HashMap<>();
                admins.allAdmins.forEach(admin -> duplicateAdmins.put(admin.getUsername(),
                        duplicateAdmins.getOrDefault(admin.getUsername(), 0) + 1));
                duplicateAdmins.forEach((admin, number) -> {
                    for (int i = 0; i < number - 1; i++) {
                        admins.allAdmins.remove(Admin.getAdminByUserName(admin));
                    }
                });
                Admin.setAllAdmins(admins.allAdmins);
            } catch (IOException e) {
                System.out.println("Error opening file");
            }
        }
    }

    public static void serializeXML(){
        try(BufferedWriter output = Files.newBufferedWriter(Paths.get(pathName))) {
            Admins admins = new Admins();
            admins.allAdmins = Admin.getAllAdmins();
            JAXB.marshal(admins, output);
        }
        catch (IOException e) {
            System.out.println("Error opening file");
        }
    }
}
