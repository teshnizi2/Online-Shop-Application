package Model.Product;

import Model.Account.Account;
import Model.Account.Customer;

import java.util.Date;

public class Comment {
    private String accountUserName;
    private String id;
    private String title;
    private String comment;
    private CommentStatus status;
    private Date date;
    private boolean bought;

    public Comment(Account account, String id, String comment, String title) {
        this.accountUserName = account.getUsername();
        this.id = id;
        this.comment = comment;
        this.title = title;
        date = new Date();
        status = CommentStatus.WAITING_FOR_CONFIRM;
    }

    public Comment() {

    }

    public Account getAccount() {
        return Account.getAccountByUsername(accountUserName);
    }

    public void setAccountUserName(Account account) {
        this.accountUserName = account.getUsername();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isBought() {
        return bought || Product.getProductByID(id).getProductBuyers().contains(Customer.getCustomerById(accountUserName));
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAccountUserName(String accountUserName) {
        this.accountUserName = accountUserName;
    }

    public String getAccountUserName() {
        return accountUserName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "account=" + accountUserName +
                ", product=" + id +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                ", checkBuy=" + bought +
                '}';
    }
}
