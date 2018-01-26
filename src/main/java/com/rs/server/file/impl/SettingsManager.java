package com.rs.server.file.impl;

import com.rs.core.settings.GameConstants;
import com.rs.core.settings.Settings;
import com.rs.server.file.JsonFileManager;
import com.rs.core.utils.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * @author FuzzyAvacado
 */
public final class SettingsManager extends JsonFileManager {

    @Getter(AccessLevel.PRIVATE)
    private final String path;

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private Settings settings;

    public SettingsManager(String path) {
        this.path = path;
    }

    public void init() {
        try {
            setSettings(load(getPath(), Settings.class));
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
        save(getPath(), getSettings());
    }

}
