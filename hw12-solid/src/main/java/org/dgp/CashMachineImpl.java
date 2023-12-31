package org.dgp;

import java.util.List;

public class CashMachineImpl implements CashMachine {

    private final SlotManager slotManager;

    public CashMachineImpl(SlotManager slotManager) {
        this.slotManager = slotManager;
    }

    @Override
    public List<Banknote> get(int amountToGet) throws NotEnoughBanknotesException, IlligibleAmountException {

        if (amountToGet < 0) {
            throw new IlligibleAmountException();
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
