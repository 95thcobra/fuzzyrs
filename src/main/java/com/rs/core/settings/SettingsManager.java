package com.rs.core.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.core.file.DataFile;
import com.rs.core.utils.Logger;

import java.io.*;

/**
 * @author FuzzyAvacado
 */
public class SettingsManager {

    private static final String SETTINGS_LOC = GameConstants.DATA_PATH + "/settings/settings.json";
    private static Settings SETTINGS;

    public static void init() {
        try {
            SETTINGS = new DataFile<>(new File(SETTINGS_LOC), Settings.class).fromJsonClass();
            if (SETTINGS.HOSTED) {
                System.setErr(new PrintStream(new FileOutputStream(GameConstants.ERROR_OUTPUT)));
                System.setOut(new PrintStream(new FileOutputStream(GameConstants.CONSOLE_OUTPUT)));
            }
            Logger.info(SettingsManager.class, "Loaded settings from " + SETTINGS_LOC);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public static void saveSettings(Settings settings) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(SETTINGS_LOC), "UTF-8")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(settings, writer);
            Logger.info(SettingsManager.class, "Saved settings to " + SETTINGS_LOC);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public static Settings getSettings() {
        return SETTINGS;
    }

}
