package com.rs;

import com.rs.core.RS2GameEngine;
import com.rs.core.RS2Loader;
import com.rs.core.RS2NetworkEngine;
import com.rs.core.cores.CoresManager;
import com.rs.core.file.GameFileManager;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.impl.ServerOnlineUpdateTask;

public final class RS2Launcher {

    public static void main(final String[] args) {
        final long currentTime = System.currentTimeMillis();
        SettingsManager.init();

        RS2Loader.init();
        RS2GameEngine.init();
        RS2NetworkEngine.deploy();

        Runtime.getRuntime().addShutdownHook(new Thread(GameFileManager::saveAll));
        GameTaskManager.scheduleTask(new ServerOnlineUpdateTask());
        Logger.info(RS2Launcher.class, "Server started in " + (System.currentTimeMillis() - currentTime) + " milliseconds");
    }

    public static void shutdown() {
        closeServices();
        System.exit(0);
    }

    public static void closeServices() {
        GameFileManager.saveAll();
        RS2NetworkEngine.shutdown();
        RS2GameEngine.shutdown();
        CoresManager.shutdown();
        if (SettingsManager.getSettings().HOSTED) {
            //setWebsitePlayersOnline(0);
        }
    }

    public static void restart() {
        shutdown();
        /*closeServices();
        System.gc();
        try {
            Runtime.getRuntime().exec("TODO");
            System.exit(0);
        } catch (final Throwable e) {
            Logger.handle(e);
        }*/
    }

}
