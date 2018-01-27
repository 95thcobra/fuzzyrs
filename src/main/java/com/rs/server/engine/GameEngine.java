package com.rs.server.engine;

import com.rs.core.cores.DefaultThreadFactory;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.task.worldtask.WorldTasksManager;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
public class GameEngine implements Runnable {

    @Getter(AccessLevel.PRIVATE)
    private final ScheduledExecutorService executorService;

    public GameEngine() {
        this.executorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("GameEngine", Thread.MAX_PRIORITY));
    }

    public void init() {
        getExecutorService().scheduleAtFixedRate(this, 0, GameConstants.WORLD_CYCLE_TIME, TimeUnit.MILLISECONDS);
        Logger.info(GameEngine.class, "WorldThread initialized @ " + GameConstants.WORLD_CYCLE_TIME);
    }

    public void shutdown() {
        getExecutorService().shutdownNow();
        Logger.info(GameEngine.class, "WorldThread shutdown " + (getExecutorService().isShutdown() ? "successfully" : "unsuccessfully"));
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
