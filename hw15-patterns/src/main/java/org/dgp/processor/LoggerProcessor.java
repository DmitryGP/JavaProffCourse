package org.dgp.processor;

import org.dgp.model.Message;
import org.dgp.processor.homework.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerProcessor implements Processor {

    private static Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);
    private final Processor processor;

    public LoggerProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) throws ProcessorException {

        logger.atInfo()
                .setMessage("log processing message: {}")
                .addArgument(message)
                .log();

        return processor.process(message);
    }
}
