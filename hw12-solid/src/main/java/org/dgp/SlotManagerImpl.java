package org.dgp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SlotManagerImpl implements SlotManager {

    private final Map<Integer, Slot> slots = new HashMap<>();

    private final int[] denominations = new int[] {500, 100, 50, 10};

    public SlotManagerImpl() {
        initializeSlots();
    }

    private void initializeSlots() {
        slots.put(new Note10().getDenomination(), new SlotImpl());
        slots.put(new Note50().getDenomination(), new SlotImpl());
        slots.put(new Note100().getDenomination(), new SlotImpl());
        slots.put(new Note500().getDenomination(), new SlotImpl());
    }

    @Override
    public List<Banknote> tryPoll(int amountToGet) throws NotEnoughBanknotesException {
        List<Banknote> resultNotes = new ArrayList<>();
        Map<Integer, Integer> noteToCountMap = new HashMap<>();

        int remains = tryToCalculateNotesCount(amountToGet, noteToCountMap);

        if (remains != 0) {
            throw new NotEnoughBanknotesException();
        }

        for (var entity : noteToCountMap.entrySet()) {
            var slot = slots.get(entity.getKey());
            resultNotes.addAll(slot.poll(entity.getValue()));
        }

        return resultNotes;
    }

    private int tryToCalculateNotesCount(int amountToGet, Map<Integer, Integer> noteToCountMap) {
        for (int i = 0; i < denominations.length; i++) {
            int count = amountToGet / denominations[i];

            if (count > 0) {
                var slot = slots.get(denominations[i]);

                if (count > slot.getNotesCount()) {
                    count = slot.getNotesCount();
                }

                noteToCountMap.put(denominations[i], count);
                amountToGet -= count * denominations[i];
            }
        }
        return amountToGet;
    }

    @Override
    public int getBalance() {
        int sum = 0;

        for (var entity : slots.entrySet()) {
            int denomination = entity.getKey();
            int notesCount = entity.getValue().getNotesCount();
            sum += denomination * notesCount;
        }

        return sum;
    }

    @Override
    public void load(List<Banknote> notes) {

        var denominationToNotesMap = notes.stream().collect(Collectors.groupingBy(Banknote::getDenomination));

        for (var entity : denominationToNotesMap.entrySet()) {
            var slot = slots.get(entity.getKey());
            slot.put(entity.getValue());
        }
    }
}
