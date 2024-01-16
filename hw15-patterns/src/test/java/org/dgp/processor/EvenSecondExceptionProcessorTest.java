package org.dgp.processor;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.time.Month;
import org.dgp.model.Message;
import org.dgp.processor.homework.EvenSecondException;
import org.dgp.processor.homework.EvenSecondExceptionProcessor;
import org.junit.jupiter.api.Test;

class EvenSecondExceptionProcessorTest {

    @Test
    void throwExceptionWhenEvenSecond() {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.JANUARY, 3, 10, 10, 12);

        var processor = new EvenSecondExceptionProcessor(() -> dateTime);

        var builder = new Message.Builder(100l);
        var msg = builder.field2("2").build();

        assertThatExceptionOfType(EvenSecondException.class).isThrownBy(() -> processor.process(msg));
    }

    @Test
    void doesntThrowExceptionWhenOddSecond() throws EvenSecondException {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.JANUARY, 3, 10, 10, 11);

        var processor = new EvenSecondExceptionProcessor(() -> dateTime);

        var builder = new Message.Builder(100l);
        var msg = builder.field2("2").build();

        assertThat(processor.process(msg)).isNotNull();
    }
}
