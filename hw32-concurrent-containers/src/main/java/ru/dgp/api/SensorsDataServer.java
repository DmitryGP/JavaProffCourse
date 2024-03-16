package ru.dgp.api;

import ru.dgp.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
