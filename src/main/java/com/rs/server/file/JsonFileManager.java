package com.rs.server.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Fuzzy 1/25/2018
 */
@Getter(AccessLevel.PRIVATE)
public class JsonFileManager {

    public <T> T load(String fileLocation, Class<T> type) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(fileLocation))) {
            return getGson().fromJson(reader, type);
        }
    }

    public <T> T load(String fileLocation, Type type) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(fileLocation))) {
            return getGson().fromJson(reader, type);
        }
    }

    public <T> void save(String fileLocation, T object) throws IOException {
        Path path = Paths.get(fileLocation);
        if (!Files.exists(path)) {
            if (path.getParent() != null && !Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.createFile(path);
        }
        try (Writer writer = Files.newBufferedWriter(path)) {
            getGson().toJson(object, writer);
        }
    }

    private Gson getGson() {
        return GSON;
    }

    private static final Gson GSON = new GsonBuilder().create();
    protected static final String JSON_SUFFIX = ".json";

}
