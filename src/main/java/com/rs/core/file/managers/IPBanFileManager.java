package com.rs.core.file.managers;

import com.rs.core.file.GameFileManager;
import com.rs.core.file.DataFile;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author FuzzyAvacado
 */
public class IPBanFileManager {

    private static final String PATH = GameConstants.DATA_PATH + "/bannedIPS.json";
    private static CopyOnWriteArrayList<String> ipList;
    private static boolean edited;

    public static void init() {
        final File file = new File(PATH);
        if (file.exists()) {
            try {
                ipList = new DataFile<CopyOnWriteArrayList<String>>(file).fromJson();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ipList = new CopyOnWriteArrayList<>();
        save();
    }

    public static void save() {
        if (!edited)
            return;
        try {
            GameFileManager.storeJsonFile(ipList, new File(PATH));
        } catch (IOException e) {
            edited = false;
            Logger.handle(e);
        }
    }

    public static boolean isBanned(final String ip) {
        return ipList.contains(ip);
    }

    public static void ban(final Player player, final boolean loggedIn) {
        player.setPermBanned(true);
        if (loggedIn) {
            ipList.add(player.getSession().getIP());
            player.getSession().getChannel().disconnect();
        } else {
            ipList.add(player.getLastIP());
            PlayerFilesManager.savePlayer(player);
        }
        edited = true;
    }

    public static void unban(final Player player) {
        player.setPermBanned(false);
        player.setBanned(0);
        ipList.remove(player.getLastIP());
        edited = true;
        save();
    }

    public static void checkCurrent() {
        ipList.forEach(System.out::println);
    }

    public static CopyOnWriteArrayList<String> getList() {
        return ipList;
    }
}
