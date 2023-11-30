package Model.Account;

import java.util.Date;

public abstract class Log {
    private String ID;
    private Date date;

    public Log(String ID, Date date) {
        this.ID = ID;
        this.date = date;
    }

    public Log() {
        this("", null);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public abstract String toString();
}
