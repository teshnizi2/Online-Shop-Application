package Controller;

import Client.Connection;
import Model.Account.Admin;
import Model.Account.Customer;
import Model.Account.Supporter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Objects;

public class SupporterProfileManager extends ProfileManager {

    public Supporter supporter;

    public SupporterProfileManager(Supporter supporter) {
        super(supporter);
        this.supporter = supporter;
    }

    public Supporter getSupporter() {
        return supporter;
    }

    public void setLineCondition(boolean lineCondition) {
        String lineConditionStr;
        if (lineCondition) {
            lineConditionStr = "true";
        } else {
            lineConditionStr = "false";
        }
        Connection.sendToServer("set line condition: " + lineConditionStr);
    }

    public static Supporter getSupporterByID(int supporterID) {
        Connection.sendToServer("getSupporters");
        ArrayList<Supporter> allSupporters = new Gson().fromJson(Connection.receiveFromServer(), new TypeToken<ArrayList<Supporter>>(){}.getType());
        for (Supporter supporter : Objects.requireNonNull(allSupporters)) {
            if (supporter.getSupporterID() == supporterID){
                return supporter;
            }
        }
        return null;
    }
}
