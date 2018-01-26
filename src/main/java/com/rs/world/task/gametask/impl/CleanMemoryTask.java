package com.rs.world.task.gametask.impl;

import com.alex.store.Index;
import com.rs.Server;
import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.settings.SettingsManager;
import com.rs.world.Region;
import com.rs.world.World;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class CleanMemoryTask extends GameTask {

    public CleanMemoryTask() {
        super(ExecutionType.FIXED_DELAY, 0, 10, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        boolean force = Runtime.getRuntime().freeMemory() < Server.getInstance().getSettingsManager().getSettings().getMinFreeMemoryAllowed();
        if (force) {
            ItemDefinitions.clearItemsDefinitions();
            NPCDefinitions.clearNPCDefinitions();
            ObjectDefinitions.clearObjectDefinitions();
            World.getRegions().values().forEach(Region::removeMapFromMemory);
        }
        for (final Index index : Cache.STORE.getIndexes()) {
            index.resetCachedFiles();
        }
        System.gc();
    }
}
