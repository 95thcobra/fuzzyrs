package com.rs.core.file.managers;

import com.rs.core.file.DataFile;
import com.rs.core.file.GameFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * @author FuzzyAvacado
 */
public class PlayerFilesManager {

    private static final String PATH = GameConstants.DATA_PATH + "/playersaves/characters/";
    private static final String BACKUP_PATH = GameConstants.DATA_PATH + "/playersaves/charactersBackup/";

    public static Player loadPlayer(final String username) {
        try {
            return new DataFile<Player>(new File(PATH + username + ".p")).fromSerialUnchecked();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
        try {
            return new DataFile<Player>(new File(BACKUP_PATH + username + ".p")).fromSerialUnchecked();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
        return null;
    }

    public static void savePlayer(final Player player) {
        try {
            GameFileManager.storeSerializableClass(player, new File(PATH + player.getUsername()
                    + ".p"));
        } catch (IOException | ConcurrentModificationException e) {
            Logger.handle(e);
        }
    }

    public static boolean containsPlayer(final String username) {
        return new File(PATH + username + ".p").exists();
    }

    public static boolean createBackup(final String username) {
        try {
            Utils.copyFile(new File(PATH + username + ".p"), new File(
                    BACKUP_PATH + username + ".p"));
            return true;
        } catch (IOException e) {
            Logger.handle(e);
        }
        return false;
    }

}
