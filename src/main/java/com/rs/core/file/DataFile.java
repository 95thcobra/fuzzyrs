package com.rs.core.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;

/**
 * @author John (FuzzyAvacado) on 3/5/2016.
 */
public class DataFile<T> {

    private final File file;
    private final Class<T> klass;
    private final Gson gson;

    @Deprecated
    public DataFile(File file) {
        this(file, null);
    }

    public DataFile(File file, Class<T> klass) {
        this(file, klass, true);
    }

    public DataFile(File file, Class<T> klass, boolean prettyPrinting) {
        this.file = file;
        this.klass = klass;
        this.gson = prettyPrinting ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
    }

    @Deprecated
    public T fromJson() throws FileNotFoundException {
        return gson.fromJson(new FileReader(file), (new TypeToken<T>() {
        }).getType());
    }

    public T fromJsonClass() throws FileNotFoundException {
        if (klass != null) {
            return gson.fromJson(new FileReader(file), klass);
        }
        return null;
    }

    public Object fromSerial() {
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                return inputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        return new Object();
    }

    @SuppressWarnings("unchecked")
    public T fromSerialUnchecked() {
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                return (T) inputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public boolean saveJson(T object) {
        try (Writer writer = new FileWriter(file, false)) {
            gson.toJson(object, writer);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
