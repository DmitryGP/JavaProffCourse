package org.dgp;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlotImplTest {

    private SlotImpl slot;

    @BeforeEach
    void setUp() {
        slot = new SlotImpl();
    }

    @Test
    void putSomeNotes() {
        List<Banknote> notes = List.of(new Note100(), new Note100(), new Note100());

        Assertions.assertEquals(0, slot.getNotesCount());

        slot.put(notes);

        Assertions.assertEquals(3, slot.getNotesCount());
    }

    @Test
    void pollNotes() {
        List<Banknote> notes = List.of(new Note100(), new Note100(), new Note100());

        slot.put(notes);

        slot.poll(2);

        Assertions.assertEquals(1, slot.getNotesCount());

        slot.poll(1);

        Assertions.assertEquals(0, slot.getNotesCount());
    }

    @Test
    void pollMoreThanHave() {
        List<Banknote> notes = List.of(new Note100(), new Note100(), new Note100());

        slot.put(notes);

        List<Banknote> pollNotes = slot.poll(5);

        Assertions.assertEquals(0, slot.getNotesCount());
        Assertions.assertEquals(3, pollNotes.size());
    }
}
