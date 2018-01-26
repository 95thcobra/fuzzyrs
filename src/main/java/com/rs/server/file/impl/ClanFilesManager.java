package com.rs.server.file.impl;

import com.rs.content.clans.Clan;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.server.GameFileConstants;
import com.rs.server.file.JsonFileManager;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

/**
 * @author FuzzyAvacado
 */
@Getter(AccessLevel.PRIVATE)
public class ClanFilesManager extends JsonFileManager {

    private String buildClanPath(String name) {
        return GameFileConstants.CLANS_DIR + name + JSON_SUFFIX;
    }

    public synchronized boolean containsClan(String name) {
        return new File(buildClanPath(name)).exists();
    }

    public synchronized Clan loadClan(String name) {
        try {
            return load(buildClanPath(name), Clan.class);
        } catch (IOException e) {
            Logger.handle(e);
        }
        return null;
    }

    public synchronized void saveClan(Clan clan) {
        try {
            save(buildClanPath(clan.getClanName()), clan);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public synchronized boolean deleteClan(Clan clan) {
        return new File(buildClanPath(clan.getClanName())).delete();
    }
}
