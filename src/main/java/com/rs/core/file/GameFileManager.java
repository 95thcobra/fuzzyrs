package com.rs.core.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.content.economy.exchange.GrandExchange;
import com.rs.core.file.managers.*;
import com.rs.player.Player;
import com.rs.world.World;
import com.sun.istack.internal.NotNull;

import java.io.*;

/**
 * Uses both json and serialization to save files. Simple methods using try with resources and the gson lib from google.
 *
 * @author John (FuzzyAvacado) on 12/22/2015.
 */
public class GameFileManager {

    /**
     * Uses the {@link Gson} object to load a json file from a location and build it off a layout of a Class.
     *
     * @param f    file location of the json file to load
     * @param type the class in which json will base the structure off of.
     * @return the object loaded from json representing the layout of the class.
     * In case the file is not found, a new object is returned rather than null to prevent code from continuing on a null object and instead throwing a
     * ClassCastException so the object wont be deemed valid when casting.
     * @throws IOException            if the file cannot be found or read properly.
     * @throws ClassNotFoundException if the class cannot be found for the layout.
     */
    public static Object loadJsonFile(@NotNull final File f, @NotNull Class type) throws IOException, ClassNotFoundException {
        if (!f.exists())
            return new Object();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(new FileReader(f), type);
    }


    /**
     * Writes an object to json file using a {@link FileWriter} and converting to a json object with {@link Gson}
     *
     * @param o              the object to be stored in the json file.
     * @param f              the location of the json file for the object to be stored.
     * @param prettyPrinting whether or not the json file output should look beautiful.
     * @throws IOException if the file cannot be written or found.
     */
    public static void storeJsonFile(@NotNull final Object o, @NotNull final File f, @NotNull boolean prettyPrinting) throws IOException {
        try (Writer writer = new FileWriter(f)) {
            Gson gson = prettyPrinting ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
            gson.toJson(o, writer);
        }
    }

    /**
     * Loads an object using an {@link ObjectInputStream} through a serialized file stored through {@link GameFileManager#storeSerializableClass}
     *
     * @param f the location of the serialized file to be loaded.
     * @return the serialized object.
     * In case the file is not found, a new object is returned rather than null to prevent code from continuing on a null object and instead throwing a
     * ClassCastException so the object wont be deemed valid when casting.
     * @throws IOException            if the file cannot be written or found.
     * @throws ClassNotFoundException if the class of the serialized object cannot be found.
     */
    public static Object loadSerializedFile(@NotNull final File f) throws IOException, ClassNotFoundException {
        if (!f.exists())
            return new Object();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(f))) {
            return inputStream.readObject();
        }
    }

    /**
     * Writes to a file a serializable object using an {@link ObjectInputStream}
     *
     * @param o the object to be stored by serialization.
     * @param f where to store the serialized file.
     * @throws IOException if the file cannot be found or written to.
     */
    public static void storeSerializableClass(@NotNull final Serializable o, @NotNull final File f) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
            out.writeObject(o);
        }
    }

    /**
     * Saves all the data for players and such.
     */
    public static void saveAll() {
        for (final Player player : World.getPlayers()) {
            if (player == null || !player.hasStarted() || player.hasFinished()) {
                continue;
            }
            PlayerFilesManager.savePlayer(player);
        }
        DisplayNamesFileManager.save();
        GrandExchange.save();
        IPBanFileManager.save();
        PkRankFileManager.save();
        DTRankFileManager.save();
    }
}
