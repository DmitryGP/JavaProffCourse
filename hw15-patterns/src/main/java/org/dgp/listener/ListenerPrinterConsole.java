package org.dgp.listener;

import org.dgp.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerPrinterConsole implements Listener {

    private static Logger logger = LoggerFactory.getLogger(ListenerPrinterConsole.class);

    @Override
    public void onUpdated(Message msg) {
        var logString = String.format("oldMsg:%s", msg);
        logger.atInfo().setMessage(logString).log();
    }
}
