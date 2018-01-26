package com.rs.core.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Fuzzy 1/25/2018
 */
@Getter(AccessLevel.PRIVATE)
public class JsonFileManager {

    private final Gson gson;

    private JsonFileManager(Gson gson) {
        this.gson = gson;
    }

    public <T> T load(String fileLocation, Class<T> type) throws IOException {
        try (FileReader fileReader = new FileReader(fileLocation)) {
            return getGson().fromJson(fileReader, type);
        }
    }

    public <T> T load(String fileLocation, Type type) throws IOException {
        try (FileReader fileReader = new FileReader(fileLocation)) {
            return getGson().fromJson(fileReader, type);
        }
    }

    public <T> void save(String fileLocation, T object) throws IOException {
        File file = new File(fileLocation);
        if (!file.exists()) {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.createNewFile()) {
                return;
            }
        }
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            getGson().toJson(object, fileWriter);
        }
    }

    public static JsonFileManager create() {
        return new JsonFileManager(new GsonBuilder().create());
    }
}
