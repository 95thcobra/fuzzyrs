package com.rs.server.file.impl;

import com.google.gson.reflect.TypeToken;
import com.rs.server.Server;
import com.rs.core.utils.Logger;
import com.rs.player.Player;
import com.rs.player.content.FriendChatsManager;
import com.rs.server.file.JsonFileManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
@Getter(AccessLevel.PRIVATE)
public final class DisplayNamesFileManager extends JsonFileManager {

    private final String path;

    @Setter(AccessLevel.PRIVATE)
    private List<String> cachedNames;

    public DisplayNamesFileManager(String path) {
        this.path = path;
    }

    public void init() {
        final File file = new File(getPath());
        if (file.exists()) {
            try {
                setCachedNames(load(getPath(), new TypeToken<ArrayList<String>>() {
                }.getType()));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setCachedNames(new ArrayList<>());
        save();
    }

    public boolean setDisplayName(final Player player, final String displayName) {
        synchronized (getCachedNames()) {
            if ((Server.getInstance().getPlayerFileManager().exists(displayName) ||
                    getCachedNames().contains(displayName) ||
                    !getCachedNames().add(displayName)))
                return false;
            if (player.hasDisplayName()) {
                getCachedNames().remove(player.getDisplayName());
            }
        }
        player.setDisplayName(displayName);
        FriendChatsManager.refreshChat(player);
        player.getAppearance().generateAppearenceData();
        return true;
    }

    public boolean removeDisplayName(final Player player) {
        if (!player.hasDisplayName())
            return false;
        synchronized (getCachedNames()) {
            getCachedNames().remove(player.getDisplayName());
        }
        player.setDisplayName(null);
        player.getAppearance().generateAppearenceData();
        return true;
    }

    public void save() {
        try {
            save(getPath(), getCachedNames());
        } catch (final IOException e) {
            Logger.handle(e);
        }
    }
}
