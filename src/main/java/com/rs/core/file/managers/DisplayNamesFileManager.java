package com.rs.core.file.managers;

import com.rs.core.file.GameFileManager;
import com.rs.core.file.DataFile;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.FriendChatsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
public class DisplayNamesFileManager {

    private static final String PATH = GameConstants.DATA_PATH + "/displayNames.json";
    private static List<String> cachedNames;

    public static void init() {
        final File file = new File(PATH);
        if (file.exists()) {
            try {
                cachedNames = new DataFile<ArrayList<String>>(file).fromJson();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        cachedNames = new ArrayList<>();
        save();
    }

    public static boolean setDisplayName(final Player player, final String displayName) {
        synchronized (cachedNames) {
            if ((PlayerFilesManager.containsPlayer(Utils.formatPlayerNameForProtocol(displayName)) ||
                    cachedNames.contains(displayName) ||
                    !cachedNames.add(displayName)))
                return false;
            if (player.hasDisplayName()) {
                cachedNames.remove(player.getDisplayName());
            }
        }
        player.setDisplayName(displayName);
        FriendChatsManager.refreshChat(player);
        player.getAppearance().generateAppearenceData();
        return true;
    }

    public static boolean removeDisplayName(final Player player) {
        if (!player.hasDisplayName())
            return false;
        synchronized (cachedNames) {
            cachedNames.remove(player.getDisplayName());
        }
        player.setDisplayName(null);
        player.getAppearance().generateAppearenceData();
        return true;
    }

    public static void save() {
        try {
            GameFileManager.storeJsonFile(cachedNames,
                    new File(PATH));
        } catch (final IOException e) {
            Logger.handle(e);
        }
    }
}
