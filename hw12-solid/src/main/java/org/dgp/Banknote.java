package org.dgp;

public enum Banknote {
    NOTE500(500),
    NOTE100(100),
    NOTE50(50),
    NOTE10(10);
    private final int amount;

    Banknote(int amount) {
        this.amount = amount;
    }

    public int getDenomination() {
        return amount;
    }
}
