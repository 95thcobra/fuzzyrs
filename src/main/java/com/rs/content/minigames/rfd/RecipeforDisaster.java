package com.rs.content.minigames.rfd;

import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.core.cores.CoresManager;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.player.Player;
import com.rs.player.content.FadingScreen;
import com.rs.player.controlers.Controller;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.zombies.ZombieCaves;
import com.rs.world.npc.zombies.ZombiesNPC;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam
 * @since Aug, 1st.
 */

public class RecipeforDisaster extends Controller {
    public static boolean canpray = false;
    /**
     * Holds the Zombies
     */

    private final int[][] MONSTERS = {{3493}, {3494}, {3495}, {3496},
            {3491}};
    public boolean spawned;
    /**
     * Data
     */

    private int[] regionChucks;
    private RecipeStages stage;
    private boolean logoutAtEnd;
    private boolean login;

	/*
     * 14281//135 14339//85
	 */

    /**
     * @param player
     */

    public static void enterRfd(final Player player) {
        player.getControllerManager().startController(RecipeforDisaster.class, 1);
    }

    public static NPC findNPC(final int id) {
        for (final NPC npc : World.getNPCs()) {
            if (npc == null || npc.getId() != id) {
                continue;
            }
            return npc;
        }
        return null;
    }

    /**
     * Starts gametask
     */
    @Override
    public void start() {
        startGame(false);

    }

    /**
     * Starts the gametask & loads the map.
     *
     * @param player
     */

    public void fade(final Player player) {
        final long time = FadingScreen.fade(player);
        CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FadingScreen.unfade(player, time, new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (final Throwable e) {
                    Logger.handle(e);
                }
            }

        });
    }

    public void startGame(final boolean login) {

        fade(player);
        this.login = login;
        stage = RecipeStages.LOADING;
        player.lock(); // locks player
        canpray = true;
        CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // regionChucks = RegionBuilder.findEmptyChunkBound(9, 9);
                // RegionBuilder.copyAllPlanesMap(456, 439, regionChucks[0],//
                // mhmk ima eat icecream have fun
                // regionChucks[1], 9);
                regionChucks = RegionBuilder.findEmptyChunkBound(8, 8);
                RegionBuilder.copyAllPlanesMap(235, 667, regionChucks[0],
                        regionChucks[1], 8);// is this rightno urs is abovethes

                player.setNextWorldTile(getWorldTile(10, 19));
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        canpray = true;
                        player.unlock();
                        stage = RecipeStages.RUNNING;
                    }

                }, 1);
                if (!login) {
                    GameTaskManager.scheduleTask(new StartWaveTask(GameTask.ExecutionType.SCHEDULE, 6, 0, TimeUnit.SECONDS));
                }
            }
        });
    }

    /**
     * @return
     */

    public WorldTile getSpawnTile() {
        return getWorldTile(15, 19);
    }

    /**
     * Handles the buttons.
     */

    @Override
    public boolean processButtonClick(final int interfaceId,
                                      final int componentId, final int slotId, final int packetId) {
        if (stage != RecipeStages.RUNNING)
            return false;
        if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
            if (!logoutAtEnd) {
                logoutAtEnd = true;
                player.getPackets()
                        .sendGameMessage(
                                "<col=ff0000>You will be logged out automatically at the end of this wave.");
                player.getPackets()
                        .sendGameMessage(
                                "<col=ff0000>If you log out sooner, you will have to repeat this wave.");
            } else {
                player.forceLogout();
            }
            return false;
        }
        return true;
    }

    /**
     * return process normaly
     */

    @Override
    public boolean processObjectClick1(final WorldObject object) {
        if (object.getId() == 12356) {
            if (stage != RecipeStages.RUNNING)
                return false;
            exitCave(1);
            return false;
        }
        return true;
    }

    @Override
    public void moved() {
        if (stage != RecipeStages.RUNNING || !login)
            return;
        login = false;
        setWaveEvent();
    }

    public void win() {
        if (stage != RecipeStages.RUNNING)
            return;
        exitCave(4);
    }

    public void startWave() {
        final int currentWave = getCurrentWave();
        if (currentWave > MONSTERS.length) {
            win();
            return;
        }
        if (stage != RecipeStages.RUNNING)
            return;
        for (final int id : MONSTERS[currentWave - 1]) {
            new ZombiesNPC(id, getSpawnTile());
            final NPC Monster = findNPC(id);
            player.getHintIconsManager().addHintIcon(Monster, 0, -1, false);
        }
        spawned = true;

        if (getCurrentWave() == 2) {
            player.rfd1 = true;
            player.getBank().addItem(7453, 1, true);
            player.getBank().addItem(7454, 1, true);
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>Two pair of gloves have been added to your bank.");
        } else if (getCurrentWave() == 3) {
            player.rfd2 = true;
            player.getBank().addItem(7455, 1, true);
            player.getBank().addItem(7456, 1, true);
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>Two pair of gloves have been added to your bank.");
        } else if (getCurrentWave() == 4) {
            player.rfd3 = true;
            player.getBank().addItem(7457, 1, true);
            player.getBank().addItem(7458, 1, true);
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>Two pair of gloves have been added to your bank.");
        } else if (getCurrentWave() == 5) {
            player.rfd4 = true;
            player.getBank().addItem(7459, 1, true);
            player.getBank().addItem(7460, 1, true);
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>Two pair of gloves have been added to your bank.");
        }
    }

    /**
     *
     */
    public void setWaveEvent() {

        if (getCurrentWave() == 5) {
            player.getDialogueManager().startDialogue(SimpleNPCMessage.class, 3491,
                    "You DARE come here !?!?");
        }
        GameTaskManager.scheduleTask(new StartWaveTask(GameTask.ExecutionType.SCHEDULE, 600, 0, TimeUnit.MILLISECONDS));
    }

    /**
     * Processing.
     */

    @Override
    public void process() {
        if (spawned) {
            final List<Integer> npcs = World.getRegion(player.getRegionId())
                    .getNPCsIndexes();
            if (npcs == null || npcs.isEmpty()) {
                spawned = false;
                nextWave();
            }
        }
    }

    /**
     * Sets the next wave.
     */

    public void nextWave() {
        setCurrentWave(getCurrentWave() + 1);
        if (logoutAtEnd) {
            player.forceLogout();
            return;
        }
        setWaveEvent();
    }

    /**
     * Death method.
     */
    @Override
    public boolean sendDeath() {
        player.lock(7);
        player.stopAll();
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    player.setNextAnimation(new Animation(836));
                } else if (loop == 1) {
                    player.getPackets().sendGameMessage(
                            "Oh, dear you have died.");
                } else if (loop == 3) {
                    player.reset();
                    exitCave(1);
                    player.setNextAnimation(new Animation(-1));
                } else if (loop == 4) {
                    player.getPackets().sendMusicEffect(90);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
        return false;
    }

    /**
     *
     */

    @Override
    public void magicTeleported(final int type) {
        exitCave(2);
    }

    /*
     * logout or not. if didnt logout means lost, 0 logout, 1, normal, 2 tele
     */
    public void exitCave(final int type) {
        stage = RecipeStages.DESTROYING;
        final WorldTile outside = new WorldTile(SettingsManager.getSettings().START_PLAYER_LOCATION);
        if (type == 0 || type == 2) {
            player.setLocation(outside);
        } else {
            player.setForceMultiArea(false);
            if (type == 1 || type == 4) {
                player.setNextWorldTile(outside);
                if (type == 4) {
                    fade(player);
                    player.reset();
                    player.rfd5 = true;
                    player.getBank().addItem(7461, 1, true);
                    player.getBank().addItem(7462, 1, true);
                    player.getPackets().sendGameMessage(
                            "2 Pairs of gloves have been added to your bank.");
                    canpray = false;
                }
            }
            canpray = false;
            removeControler();
        }

        CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
            @Override
            public void run() {
                RegionBuilder
                        .destroyMap(regionChucks[0], regionChucks[1], 8, 8);
            }
        }, 1200, TimeUnit.MILLISECONDS);

    }

    /*
     * gets worldtile inside the map
     */
    public WorldTile getWorldTile(final int mapX, final int mapY) {
        return new WorldTile(regionChucks[0] * 8 + mapX, regionChucks[1] * 8
                + mapY, 2);
    }

    /*
     * return false so wont remove script
     */
    @Override
    public boolean logout() {
		/*
		 * only can happen if dungeon is loading and system update happens
		 */
        if (stage != RecipeStages.RUNNING)
            return false;
        exitCave(0);
        return false;

    }

    /**
     * @return
     */

    public int getCurrentWave() {
        if (getArguments() == null || getArguments().length == 0)
            return 0;
        return (Integer) getArguments()[0];
    }

    /**
     * @param wave
     */

    public void setCurrentWave(final int wave) {
        if (getArguments() == null || getArguments().length == 0) {
            this.setArguments(new Object[1]);
        }
        getArguments()[0] = wave;
    }

    @Override
    public void forceClose() {
		/*
		 * shouldnt happen
		 */
        if (stage != RecipeStages.RUNNING)
            return;
        exitCave(2);
    }

    public void spawnZombieMembers() {
        if (stage != RecipeStages.RUNNING)
            return;
        for (int i = 0; i < 4; i++) {
            new ZombieCaves(2746, getSpawnTile());
        }
    }

    /**
     * @author Adam
     */
    private enum RecipeStages {
        LOADING, RUNNING, DESTROYING
    }

    @GameTaskType(GameTaskManager.GameTaskType.FAST)
    private class StartWaveTask extends GameTask {

        public StartWaveTask(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
            super(executionType, initialDelay, tick, timeUnit);
        }

        @Override
        public void run() {
            if (stage != RecipeStages.RUNNING)
                return;
            startWave();
        }
    }

}
