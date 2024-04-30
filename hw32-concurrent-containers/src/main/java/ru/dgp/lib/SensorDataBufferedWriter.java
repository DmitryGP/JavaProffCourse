package ru.dgp.lib;

import java.util.List;
import ru.dgp.api.model.SensorData;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
