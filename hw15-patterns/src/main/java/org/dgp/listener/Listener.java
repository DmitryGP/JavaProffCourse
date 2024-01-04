package org.dgp.listener;

import org.dgp.model.Message;

public interface Listener {

    void onUpdated(Message msg);
}
