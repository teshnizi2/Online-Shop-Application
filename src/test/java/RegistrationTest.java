import Model.Account.Account;
import Model.Account.Admin;
import Model.Account.Customer;
import Model.Account.Seller;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegistrationTest {

    @Test
    public void numberOfAccountsTest(){
        int numberOfAccounts = Account.getAllAccounts().size();
        int numberOfCustomers = Customer.getAllAccounts().size();
        int numberOfAdmins = Admin.getAllAdmins().size();
        int numberOfSellers = Seller.getAllSellers().size();

        new Customer("ali", "80", "ali", "abbasi", "a80.abbasi@yahoo.com", "7887978", 100);
        assertEquals(numberOfAccounts + 1, Account.getAllAccounts().size());
        assertEquals(numberOfCustomers + 1, Customer.getAllCustomers().size());
        numberOfCustomers += 1;
        numberOfAccounts += 1;
        new Admin("amir", "85", "amir", "abbasi", "a85@a.a", "899898");
        assertEquals(numberOfAccounts + 1, Account.getAllAccounts().size());
        assertEquals(numberOfCustomers, Customer.getAllCustomers().size());
        numberOfAccounts += 1;
        numberOfAdmins++;
        new Seller("he", "hehe", "he", "she", "he@she.me", "7272", "apple", 10000);
        assertEquals(numberOfAccounts + 1, Account.getAllAccounts().size());
        assertEquals(numberOfSellers + 1, Seller.getAllSellers().size());
        assertEquals(numberOfAdmins, Admin.getAllAdmins().size());
    }
}
