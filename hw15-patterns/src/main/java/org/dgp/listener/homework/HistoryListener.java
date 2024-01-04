package org.dgp.listener.homework;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.dgp.listener.Listener;
import org.dgp.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message.State> messageStates = new TreeMap<>();

    @Override
    public void onUpdated(Message msg) {
        messageStates.put(msg.getId(), msg.save());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        var state = messageStates.get(Long.valueOf(id));

        if (state != null) {
            return createMessage(state);
        }

        return Optional.empty();
    }

    private Optional<Message> createMessage(Message.State state) {
        var builder = new Message.Builder(state.getId());

        var message = builder.field1(state.getField1())
                .field2(state.getField2())
                .field3(state.getField3())
                .field4(state.getField4())
                .field5(state.getField5())
                .field6(state.getField6())
                .field8(state.getField8())
                .field7(state.getField7())
                .field9(state.getField9())
                .field10(state.getField10())
                .field11(state.getField11())
                .field12(state.getField12())
                .field13(state.getField13())
                .build();

        return Optional.of(message);
    }
}
