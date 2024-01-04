package org.dgp.processor;

import org.dgp.model.Message;
import org.dgp.processor.homework.ProcessorException;

public interface Processor {

    Message process(Message message) throws ProcessorException;
}
