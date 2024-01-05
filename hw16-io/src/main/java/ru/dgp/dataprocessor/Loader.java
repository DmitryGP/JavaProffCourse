package ru.dgp.dataprocessor;

import java.util.List;
import ru.dgp.model.Measurement;

public interface Loader {

    List<Measurement> load();
}
