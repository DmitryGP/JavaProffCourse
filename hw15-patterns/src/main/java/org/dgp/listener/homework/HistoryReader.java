package org.dgp.listener.homework;

import org.dgp.model.Message;

import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
