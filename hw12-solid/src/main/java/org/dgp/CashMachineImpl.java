package org.dgp;

import java.util.ArrayList;
import java.util.List;

public class CashMachineImpl implements CashMachine {

    private final SlotManager slotManager;

    public CashMachineImpl(SlotManager slotManager) {
        this.slotManager = slotManager;
    }

    @Override
    public List<Banknote> get(int amountToGet) throws NotEnoughBanknotesException {

        if (amountToGet <= 0) {
            return new ArrayList<>();
        }

        return slotManager.tryPoll(amountToGet);
    }

    @Override
    public int getBalance() {
        return slotManager.getBalance();
    }

    @Override
    public void load(List<Banknote> notesToLoad) {
        slotManager.load(notesToLoad);
    }
}
