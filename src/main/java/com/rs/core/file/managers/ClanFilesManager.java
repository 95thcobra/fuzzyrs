package com.rs.core.file.managers;

import com.rs.content.clans.Clan;
import com.rs.core.file.GameFileManager;
import com.rs.core.file.DataFile;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author FuzzyAvacado
 */
public class ClanFilesManager {

    private static final String CLAN_PATH = GameConstants.DATA_PATH + "/clans/";

    public synchronized static boolean containsClan(String name) {
        return new File(CLAN_PATH + name + ".json").exists();
    }

    public synchronized static Clan loadClan(String name) {
        try {
            return new DataFile<>(new File(CLAN_PATH + name + ".json"), Clan.class).fromJsonClass();
        } catch (IOException e) {
            Logger.handle(e);
        }
        return null;
    }

    public synchronized static void saveClan(Clan clan) {
        try {
            GameFileManager.storeJsonFile(clan, new File(CLAN_PATH + clan.getClanName() + ".json"));
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public synchronized static boolean deleteClan(Clan clan) {
        return new File(CLAN_PATH + clan.getClanName() + ".json").delete();
    }
}
