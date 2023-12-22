package org.dgp;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) {
        SlotManager slotManager = new SlotManagerImpl();
        CashMachine cashMachine = new CashMachineImpl(slotManager);

        List<Banknote> notes = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            notes.add(Banknote.NOTE500);
        }

        for (int i = 0; i < 70; i++) {
            notes.add(Banknote.NOTE100);
        }

        for (int i = 0; i < 100; i++) {
            notes.add(Banknote.NOTE50);
        }

        for (int i = 0; i < 150; i++) {
            notes.add(Banknote.NOTE10);
        }

        cashMachine.load(notes);

        log.atInfo()
                .setMessage("Balance of Cash Machine is {}")
                .addArgument(cashMachine.getBalance())
                .log();

        tryToGetSomeMoney(cashMachine, 1870);

        tryToGetSomeMoney(cashMachine, 123);
    }

    private static void tryToGetSomeMoney(CashMachine cashMachine, int amountToGet) {
        List<Banknote> notes;
        try {
            notes = cashMachine.get(amountToGet);

            log.atInfo()
                    .setMessage("Received from Cash Machine: {}")
                    .addArgument(calculateAmountOf(notes))
                    .log();

            log.atInfo()
                    .setMessage("Balance of Cash Machine is {}")
                    .addArgument(cashMachine.getBalance())
                    .log();

        } catch (NotEnoughBanknotesException exc) {
            log.atError()
                    .setMessage("Not enough banknotes to get {}")
                    .addArgument(amountToGet)
                    .log();
        } catch (IlligibleAmountException exc) {
            log.atError()
                    .setMessage("Illigible entered amount. {}")
                    .addArgument(amountToGet)
                    .log();
        }
    }

    private static int calculateAmountOf(List<Banknote> notes) {
        return notes.stream().map(Banknote::getDenomination).reduce(0, (x, y) -> x + y);
    }
}
