package org.dgp;

import java.util.List;

public interface CashMachine {
    List<Banknote> get(int amountToGet) throws NotEnoughBanknotesException, IlligibleAmountException;

    int getBalance();

    void load(List<Banknote> notesToLoad);
}
