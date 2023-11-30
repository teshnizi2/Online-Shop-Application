package Model.Product;

public enum ScoreEnumeration {
    VERY_BAD(1), BAD(2), NOT_BAD(3), GOOD(4), VERY_GOOD(5);

    private int score;

    ScoreEnumeration(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
