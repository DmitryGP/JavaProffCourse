package org.dgp;

import java.util.List;

public interface Slot {
    void put(List<Banknote> banknotes);

    List<Banknote> poll(int count);

    int getNotesCount();
}
