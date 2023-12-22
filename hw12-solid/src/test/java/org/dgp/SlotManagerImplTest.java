package org.dgp;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlotManagerImplTest {

    private SlotManagerImpl slotManager;

    @BeforeEach
    void setUp() {
        slotManager = new SlotManagerImpl();
    }

    @Test
    void loadNotesToManager() {

        List<Banknote> notes = List.of(
                Banknote.NOTE500,
                Banknote.NOTE500,
                Banknote.NOTE500,
                Banknote.NOTE100,
                Banknote.NOTE100,
                Banknote.NOTE50,
                Banknote.NOTE50);

        slotManager.load(notes);

        Assertions.assertEquals(1800, slotManager.getBalance());
    }

    @Test
    void pollNotes() throws NotEnoughBanknotesException {
        List<Banknote> notes = List.of(
                Banknote.NOTE500,
                Banknote.NOTE500,
                Banknote.NOTE500,
                Banknote.NOTE100,
                Banknote.NOTE100,
                Banknote.NOTE50,
                Banknote.NOTE50);

        slotManager.load(notes);

        List<Banknote> pollNotes = slotManager.tryPoll(700);

        Assertions.assertEquals(1100, slotManager.getBalance());

        Assertions.assertEquals(3, pollNotes.size());

        Assertions.assertEquals(
                1, pollNotes.stream().filter(n -> n.getDenomination() == 500).count());
        Assertions.assertEquals(
                2, pollNotes.stream().filter(n -> n.getDenomination() == 100).count());
    }

    @Test
    void throwExceptionWhenCantPollTheAmount() throws NotEnoughBanknotesException {
        List<Banknote> notes = List.of(
                Banknote.NOTE500,
                Banknote.NOTE500,
                Banknote.NOTE500,
                Banknote.NOTE100,
                Banknote.NOTE100,
                Banknote.NOTE50,
                Banknote.NOTE50);

        slotManager.load(notes);

        Assertions.assertThrows(NotEnoughBanknotesException.class, () -> slotManager.tryPoll(701));
        Assertions.assertThrows(NotEnoughBanknotesException.class, () -> slotManager.tryPoll(2700));

        Assertions.assertEquals(1800, slotManager.getBalance());
    }
}
