package ru.dgp.dataprocessors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;
import ru.dgp.dataprocessor.ResourcesFileLoader;

class ResourcesFileLoaderTest {

    @Test
    void loadsListOfMeasuresFromFile() {
        var fileLoader = new ResourcesFileLoader("inputData.json");

        var result = fileLoader.load();

        assertThat(result).hasSize(9);
        assertThat(result.stream()
                        .filter(m -> m.name().equals("val1") && Double.compare(m.value(), 0.0d) == 0)
                        .count())
                .isEqualTo(1l);
        assertThat(result.stream()
                        .filter(m -> m.name().equals("val3") && Double.compare(m.value(), 12.0d) == 0)
                        .count())
                .isEqualTo(1l);
    }
}
