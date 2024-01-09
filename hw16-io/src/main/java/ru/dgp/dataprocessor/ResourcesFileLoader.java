package ru.dgp.dataprocessor;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import ru.dgp.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        ClassLoader classLoader = getClass().getClassLoader();

        try (var inputStream = classLoader.getResourceAsStream(fileName)) {
            var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            Gson gson = new Gson();

            var measures = gson.fromJson(bufferedReader, Measurement[].class);

            return new ArrayList<>(List.of(measures));

        } catch (IOException exc) {
            throw new FileProcessException(exc);
        }
    }
}
