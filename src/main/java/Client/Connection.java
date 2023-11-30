package Client;

import Model.Account.*;
import Model.Product.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    private static final int serverPort = 8000;

    private static String token;
    private static Socket clientSocket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private Connection(){}

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Connection.token = token;
    }

    public static void sendToServer(String message){
        try {
            clientSocket = new Socket("localhost", serverPort);
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToServerWithToken(String message){
        sendToServer(createJsonObject(token, message));
    }

    private static String createJsonObject(String token, String content){
        return "{\"token\":\"" + token + "\",\"content\":\"" + content + "\"}";
    }

    public static String receiveFromServer(){
        if (clientSocket == null || !clientSocket.isConnected() || clientSocket.isClosed()){
            throw new RuntimeException("clientSocket is not connected");
        }
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            return dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account getLoggedInAccount(){
        Connection.sendToServerWithToken("get logged in account");
        return getAccountFromServer();
    }

    public static Account getAccountFromServer(){
        Gson gson = new Gson();
        String loggedInInfo = Connection.receiveFromServer();
        if (loggedInInfo.startsWith("Admin: ")){
            return gson.fromJson(loggedInInfo.substring("Admin: ".length()), Admin.class);
        }
        else if (loggedInInfo.startsWith("Customer: ")){
            String customerInfo = loggedInInfo.substring("Customer: ".length());
            //System.out.println(customerInfo);
            //return gson.fromJson(customerInfo, Customer.class);
            return new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(customerInfo, Customer.class);
        }
        else if (loggedInInfo.startsWith("Supporter: ")) {
            return gson.fromJson(loggedInInfo.substring(("Supporter: ").length()), Supporter.class);
        }
        else if (loggedInInfo.startsWith("Seller: ")){
            return gson.fromJson(loggedInInfo.substring("Seller: ".length()), Seller.class);
        }
        else {
            return null;
        }
    }

    public static Product getProduct(String productId){
        try {
            Connection.sendToServer("get product with id: " + productId);
            Product product = new Gson().fromJson(Connection.receiveFromServer(), Product.class);
            byte[] image = dataInputStream.readAllBytes();
            product.setImageBytes(image);
            if (product.isFile()) {
                byte[] file = dataInputStream.readAllBytes();
                product.setFile(file);
            }
            return product;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Product> getAllProducts(){
        Connection.sendToServer("get all products IDs");
        List<String> productIDs = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<String>>(){}.getType());
        ArrayList<Product> products = new ArrayList<>();
        for (String id : productIDs) {
            products.add(getProduct(id));
        }
        return products;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
