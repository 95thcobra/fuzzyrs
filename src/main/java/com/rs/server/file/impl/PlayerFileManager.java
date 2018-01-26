package com.rs.server.file.impl;

import com.rs.player.Player;
import com.rs.server.file.JsonFileManager;
import com.rs.server.util.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created by Fuzzy 1/25/2018
 */
@RequiredArgsConstructor
public final class PlayerFileManager extends JsonFileManager {

    @Getter(AccessLevel.PRIVATE)
    private final String directory;

    public Optional<Player> load(String userName) {
        String playerFilePath = userNameToPath(userName);
        try {
            return Optional.of(load(playerFilePath, Player.class));
        } catch (IOException e) {
            e.printStackTrace();
            playerFilePath = userNameToBackupPath(userName);
            try {
                return Optional.of(load(playerFilePath, Player.class));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public void save(Player player) {
        String userName = formatUserNameForFile(player.getUsername());
        String playerFilePath = getDirectory() + userName + JSON_SUFFIX;
        try {
            save(playerFilePath, player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String userName) {
        return Files.exists(Paths.get(userNameToPath(userName)));
    }

    public void backupPlayer(String userName) throws IOException {
        Files.copy(Paths.get(userNameToPath(userName)), Paths.get(userNameToBackupPath(userName)));
    }

    private String userNameToPath(String userName) {
        return getDirectory() + formatUserNameForFile(userName) + JSON_SUFFIX;
    }

    private String userNameToBackupPath(String userName) {
        return getDirectory() + BACKUPS_DIR + formatUserNameForFile(userName) + JSON_SUFFIX;
    }

    public static String formatUserNameForFile(String userName) {
        return userName == null ? StringUtils.EMPTY_STRING : userName.replaceAll(" ", "_").toLowerCase();
    }

    private static final String BACKUPS_DIR = "backups/";
}
