package org.dgp.processor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.dgp.model.Message;
import org.dgp.processor.homework.ReplaceFields1112Processor;
import org.junit.jupiter.api.Test;

class ReplaceFields1112ProcessorTest {

    @Test
    void processorReplacesField11AndField12() {
        var processor = new ReplaceFields1112Processor();

        var builder = new Message.Builder(101l);

        var message = builder.field2("asdf")
                .field5("tyui")
                .field11("11")
                .field12("12")
                .build();

        var newMessage = processor.process(message);

        assertThat(newMessage.getField11()).isEqualTo("12");
        assertThat(newMessage.getField12()).isEqualTo("11");
    }

    @Test
    void processorReplacesField11AndField12WhenOneFieldIsNull() {
        var processor = new ReplaceFields1112Processor();

        var builder = new Message.Builder(101l);

        var message = builder.field2("asdf").field5("tyui").field12("12").build();

        var newMessage = processor.process(message);

        assertThat(newMessage.getField11()).isEqualTo("12");
        assertThat(newMessage.getField12()).isNull();

        newMessage = processor.process(newMessage);

        assertThat(newMessage.getField12()).isEqualTo("12");
        assertThat(newMessage.getField11()).isNull();
    }
}
