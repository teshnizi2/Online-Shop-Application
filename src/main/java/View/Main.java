package View;

import Controller.LoginAndRegisterManager;
import Controller.ProductsManager;
import Serializer.*;
import View.RegistrationMenus.LoginAndRegisterMenu;
import java.util.Scanner;

public class Main{

    public static void main(String[] args) {

        deserializeXML();

        Menu.setLoginAndRegisterMenu(new LoginAndRegisterMenu(null));
        Menu.setLoginAndRegisterManager(new LoginAndRegisterManager());
        Menu.setProductsManager(new ProductsManager());
        Menu.setScanner(new Scanner(System.in));
        MainMenu mainMenu = new MainMenu();
        mainMenu.execute();

        serializeXML();
    }

    public static void deserializeXML(){
        Customers.deserializeXML();
        Admins.deserializeXML();
        Products.deserializeXML();
        Discounts.deserializeXML();
        Offs.deserializeXML();
        AddOffRequests.deserializeXML();
        AddProductRequests.deserializeXML();
        EditOffRequests.deserializeXML();
        RegisterSellerRequests.deserializeXML();
        RemoveProductRequests.deserializeXML();
        Categories.deserializeXML();
        Sellers.deserializeXML();
        Supporters.deserializeXML();
    }

    public static void serializeXML(){
        Customers.serializeXML();
        Admins.serializeXML();
        Sellers.serializeXML();
        Products.serializeXML();
        Categories.serializeXML();
        Discounts.serializeXML();
        Offs.serializeXML();
        AddOffRequests.serializeXML();
        AddProductRequests.serializeXML();
        EditOffRequests.serializeXML();
        RegisterSellerRequests.serializeXML();
        RemoveProductRequests.serializeXML();
        Supporters.serializeXML();
    }
}