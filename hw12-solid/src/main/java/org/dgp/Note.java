package org.dgp;

public abstract class Note implements Banknote {

    protected final int amount;

    protected Note(int amount) {
        this.amount = amount;
    }

    @Override
    public int getDenomination() {
        return amount;
    }
}
