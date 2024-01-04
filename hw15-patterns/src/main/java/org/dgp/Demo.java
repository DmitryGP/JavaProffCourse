package org.dgp;

import java.util.List;
import org.dgp.handler.ComplexProcessor;
import org.dgp.listener.ListenerPrinterConsole;
import org.dgp.model.Message;
import org.dgp.processor.LoggerProcessor;
import org.dgp.processor.ProcessorConcatFields;
import org.dgp.processor.ProcessorUpperField10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {

    private static Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(), new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);

        logger.atInfo().setMessage("result: {}").addArgument(result).log();

        complexProcessor.removeListener(listenerPrinter);
    }
}
