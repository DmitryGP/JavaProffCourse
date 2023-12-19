package org.dgp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SlotImpl implements Slot {

    private final Queue<Banknote> noteContainer = new LinkedList<>();

    @Override
    public void put(List<Banknote> banknotes) {
        noteContainer.addAll(banknotes);
    }

    @Override
    public List<Banknote> poll(int count) {
        List<Banknote> resultNotes = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Banknote note = noteContainer.poll();
            if (note != null) {
                resultNotes.add(note);
            } else {
                break;
            }
        }
        return resultNotes;
    }

    @Override
    public int getNotesCount() {
        return noteContainer.size();
    }
}
