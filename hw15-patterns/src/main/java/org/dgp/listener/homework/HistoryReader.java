package org.dgp.listener.homework;

import java.util.Optional;
import org.dgp.model.Message;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
