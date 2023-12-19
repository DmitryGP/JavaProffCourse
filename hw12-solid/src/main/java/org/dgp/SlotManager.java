package org.dgp;

import java.util.List;

public interface SlotManager {
    List<Banknote> tryPoll(int amountToGet) throws NotEnoughBanknotesException;

    int getBalance();

    void load(List<Banknote> notes);
}
