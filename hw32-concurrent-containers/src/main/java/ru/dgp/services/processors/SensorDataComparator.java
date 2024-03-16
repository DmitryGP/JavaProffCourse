package ru.dgp.services.processors;

import java.util.Comparator;
import ru.dgp.api.model.SensorData;

public class SensorDataComparator implements Comparator<SensorData> {
    @Override
    public int compare(SensorData o1, SensorData o2) {
        return o1.getMeasurementTime().compareTo(o2.getMeasurementTime());
    }
}
