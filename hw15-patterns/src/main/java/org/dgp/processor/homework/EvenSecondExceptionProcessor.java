package org.dgp.processor.homework;

import java.time.LocalDateTime;
import org.dgp.model.Message;
import org.dgp.processor.Processor;

public class EvenSecondExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) throws EvenSecondException {
        LocalDateTime dateTime = dateTimeProvider.getDate();

        if (dateTime.getSecond() % 2 == 0) {
            throw new EvenSecondException();
        }

        return message;
    }
}
