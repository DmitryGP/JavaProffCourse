package ru.dgp.dataprocessor;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        Gson gson = new Gson();

        try (var fileWriter = new FileWriter(fileName)) {
            var json = gson.toJson(data);

            fileWriter.write(json);

        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
