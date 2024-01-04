package org.dgp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.dgp.handler.ComplexProcessor;
import org.dgp.listener.homework.HistoryListener;
import org.dgp.model.Message;
import org.dgp.model.ObjectForMessage;
import org.dgp.processor.homework.EvenSecondException;
import org.dgp.processor.homework.EvenSecondExceptionProcessor;
import org.dgp.processor.homework.ReplaceFields1112Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeWork {

    private static Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors =
                List.of(new ReplaceFields1112Processor(), new EvenSecondExceptionProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, exc -> {
            if (exc instanceof EvenSecondException) {
                logger.atError()
                        .setMessage("Message was processed in even second.")
                        .log();
            } else {
                logger.atError().setMessage("Something wrong happened.").log();
            }
        });

        var historyListener = new HistoryListener();

        complexProcessor.addListener(historyListener);

        var data = new ArrayList<String>();
        data.add("aaa");
        var field13 = new ObjectForMessage();
        field13.setData(data);

        var builder = new Message.Builder(1l);
        var msg =
                builder.field1("1").field11("11").field12("12").field13(field13).build();

        var processedMsg = complexProcessor.handle(msg);

        logger.atInfo().setMessage("Original message: {}").addArgument(msg).log();

        logger.atInfo()
                .setMessage("Processed message: {}")
                .addArgument(processedMsg)
                .log();

        var historicalMsg = historyListener.findMessageById(1l);

        logger.atInfo()
                .setMessage("Historical message: {}")
                .addArgument(historicalMsg)
                .log();

        complexProcessor.removeListener(historyListener);
    }
}
