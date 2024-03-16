package ru.dgp.services.processors;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dgp.api.SensorDataProcessor;
import ru.dgp.api.model.SensorData;
import ru.dgp.lib.SensorDataBufferedWriter;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    private final PriorityBlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(bufferSize, new SensorDataComparator());
    }

    @Override
    public void process(SensorData data) {

        dataBuffer.put(data);

        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        try {

            var bufferedData = new ArrayList<SensorData>();

            dataBuffer.drainTo(bufferedData);

            if (!bufferedData.isEmpty()) {
                writer.writeBufferedData(bufferedData);
            }

        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
