package com.rs.server.file.impl;

import com.google.gson.reflect.TypeToken;
import com.rs.core.utils.Logger;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.server.file.JsonFileManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author FuzzyAvacado
 */
@Getter(AccessLevel.PRIVATE)
public final class IPBanFileManager extends JsonFileManager {

    private final String path;

    @Setter(AccessLevel.PRIVATE)
    private CopyOnWriteArrayList<String> ipList;

    @Setter(AccessLevel.PRIVATE)
    private boolean edited;

    public IPBanFileManager(String path) {
        this.path = path;
    }

    public void init() {
        final File file = new File(getPath());
        if (file.exists()) {
            try {
                setIpList(load(getPath(), new TypeToken<CopyOnWriteArrayList<String>>() {}.getType()));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setIpList(new CopyOnWriteArrayList<>());
        save();
    }

    public void save() {
        if (!isEdited())
            return;
        try {
            save(getPath(), getIpList());
        } catch (IOException e) {
            setEdited(false);
            Logger.handle(e);
        }
    }

    public boolean isBanned(final String ip) {
        return getIpList().contains(ip);
    }

    public void ban(final Player player, final boolean loggedIn) {
        player.setPermBanned(true);
        if (loggedIn) {
            getIpList().add(player.getSession().getIP());
            player.getSession().getChannel().disconnect();
        } else {
            getIpList().add(player.getLastIP());
            Server.getInstance().getPlayerFileManager().save(player);
        }
        setEdited(true);
    }

    public void unBan(final Player player) {
        player.setPermBanned(false);
        player.setBanned(0);
        getIpList().remove(player.getLastIP());
        setEdited(true);
        save();
    }

    public boolean contains(String lastIp) {
        return getIpList().contains(lastIp);
    }
}
