package ru.dgp.dataprocessors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.dgp.dataprocessor.ProcessorAggregator;
import ru.dgp.model.Measurement;

class ProcessorAggregatorTest {
    private final ProcessorAggregator processor = new ProcessorAggregator();

    @Test
    void aggregatesData() {
        List<Measurement> data = List.of(
                new Measurement("a", 1d),
                new Measurement("b", 2d),
                new Measurement("a", 3d),
                new Measurement("b", 4d),
                new Measurement("c", 10d));

        var result = processor.process(data);

        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("a", 4d);
        assertThat(result).containsEntry("b", 6d);
        assertThat(result).containsEntry("c", 10d);
    }
}
