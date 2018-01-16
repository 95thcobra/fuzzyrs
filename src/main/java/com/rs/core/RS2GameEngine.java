package com.rs.core;

import com.rs.core.cores.DefaultThreadFactory;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
public class RS2GameEngine implements Runnable {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("RS2GameEngine", Thread.MAX_PRIORITY));

    public static void init() {
        EXECUTOR.scheduleAtFixedRate(new RS2GameEngine(), 0, GameConstants.WORLD_CYCLE_TIME, TimeUnit.MILLISECONDS);
        Logger.info(RS2GameEngine.class, "WorldThread initialized @ " + GameConstants.WORLD_CYCLE_TIME);
    }

    public static void shutdown() {
        EXECUTOR.shutdownNow();
        Logger.info(RS2GameEngine.class, "WorldThread shutdown " + (EXECUTOR.isShutdown() ? "successfully" : "unsuccessfully"));
    }

    @Override
    public void run() {
        try {
            final long currentTime = System.currentTimeMillis();
            WorldTasksManager.processTasks();
            for (final Player player : World.getPlayers()) {
                if (player == null || !player.hasStarted() || player.hasFinished()) {
                    continue;
                }
                if (currentTime - player.getPacketsDecoderPing() > GameConstants.MAX_PACKETS_DECODER_PING_DELAY && player.getSession().getChannel().isOpen()) {
                    player.getSession().getChannel().close();
                }
                player.processEntity();
            }
            for (final NPC npc : World.getNPCs()) {
                if (npc == null || npc.hasFinished()) {
                    continue;
                }
                npc.processEntity();
            }
            for (final Player player : World.getPlayers()) {
                if (player == null || !player.hasStarted() || player.hasFinished()) {
                    continue;
                }
                player.getPackets().sendLocalPlayersUpdate();
                player.getPackets().sendLocalNPCsUpdate();
            }
            for (final Player player : World.getPlayers()) {
                if (player == null || !player.hasStarted()
                        || player.hasFinished()) {
                    continue;
                }
                player.resetMasks();
            }
            for (final NPC npc : World.getNPCs()) {
                if (npc == null || npc.hasFinished()) {
                    continue;
                }
                npc.resetMasks();
            }
        } catch (Throwable t) {
            Logger.handle(t);
        }
    }
}
