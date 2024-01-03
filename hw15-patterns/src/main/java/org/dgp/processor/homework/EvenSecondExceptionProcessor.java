package org.dgp.processor.homework;

import org.dgp.model.Message;
import org.dgp.processor.Processor;
import org.dgp.processor.homework.DateTimeProvider;
import org.dgp.processor.homework.EvenSecondException;

import java.time.LocalDateTime;

public class EvenSecondExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }
    @Override
    public Message process(Message message) throws EvenSecondException {
        LocalDateTime dateTime = dateTimeProvider.getDate();

        if(dateTime.getSecond() % 2 == 0) {
            throw new EvenSecondException();
        }

        return message;
    }
}
