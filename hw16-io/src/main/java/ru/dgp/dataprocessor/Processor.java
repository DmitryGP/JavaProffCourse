package ru.dgp.dataprocessor;

import java.util.List;
import java.util.Map;
import ru.dgp.model.Measurement;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
