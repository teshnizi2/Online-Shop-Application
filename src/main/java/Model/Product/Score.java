package Model.Product;

import Model.Account.Customer;

public class Score {
    private Customer customer;
    private int score;

    public Score(Customer customer, Product product, int score) {
        this.customer = customer;
        this.score = score;
    }

    public Score() {
        this(null, null, 0);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "customer=" + customer +
                ", score=" + score +
                '}';
    }
}
