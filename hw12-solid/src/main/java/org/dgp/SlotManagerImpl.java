package org.dgp;

import java.util.*;
import java.util.stream.Collectors;

public class SlotManagerImpl implements SlotManager {

    private final Map<Integer, Slot> slots = new HashMap<>();

    private final List<Integer> denominations = Arrays.stream(Banknote.values())
            .map(Banknote::getDenomination)
            .sorted((a, b) -> b - a)
            .toList();

    public SlotManagerImpl() {
        initializeSlots();
    }

    private void initializeSlots() {
        for (Integer note : denominations) {
            slots.put(note, new SlotImpl());
        }
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
        for (int i = 0; i < denominations.size(); i++) {
            int count = amountToGet / denominations.get(i);

            if (count > 0) {
                var slot = slots.get(denominations.get(i));

                if (count > slot.getNotesCount()) {
                    count = slot.getNotesCount();
                }

                noteToCountMap.put(denominations.get(i), count);
                amountToGet -= count * denominations.get(i);
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
