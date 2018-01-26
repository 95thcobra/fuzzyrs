package com.rs.core.settings;

import com.rs.core.file.JsonFileManager;
import com.rs.core.utils.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * @author FuzzyAvacado
 */
public final class SettingsManager {

    @Getter(AccessLevel.PRIVATE)
    private final String path;

    @Getter(AccessLevel.PRIVATE)
    private final JsonFileManager jsonFileManager;

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private Settings settings;

    public SettingsManager(String path, JsonFileManager jsonFileManager) {
        this.path = path;
        this.jsonFileManager = jsonFileManager;
    }

    public void init() {
        try {
            setSettings(getJsonFileManager().load(getPath(), Settings.class));
            if (getSettings().isHosted()) {
                System.setErr(new PrintStream(new FileOutputStream(GameConstants.ERROR_OUTPUT)));
                System.setOut(new PrintStream(new FileOutputStream(GameConstants.CONSOLE_OUTPUT)));
            }
            Logger.info(SettingsManager.class, "Loaded settings from " + getPath());
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public void save() throws IOException {
        getJsonFileManager().save(getPath(), getSettings());
    }

}
