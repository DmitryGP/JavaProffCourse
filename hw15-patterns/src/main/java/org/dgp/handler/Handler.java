package org.dgp.handler;

import org.dgp.listener.Listener;
import org.dgp.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
