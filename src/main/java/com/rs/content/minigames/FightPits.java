package com.rs.content.minigames;

import com.rs.server.Server;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.controlers.fightpits.FightPitsArena;
import com.rs.player.controlers.fightpits.FightPitsLobby;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.fightpits.FightPitsNPC;
import com.rs.world.npc.fightpits.TzKekPits;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.rs.world.task.gametask.GameTask.ExecutionType;

public final class FightPits {

    public static final List<Player> arena = new ArrayList<Player>();
    public static final Object lock = new Object();
    private static final int THHAAR_MEJ_KAH = 2618;
    private static final List<Player> lobby = new ArrayList<Player>();
    public static String currentChampion;
    private static FightPitsTask fightPitsTask;
    private static boolean startedGame;
    private static WorldTile[] GAME_TELEPORTS = {new WorldTile(4577, 5086, 0),
            new WorldTile(4571, 5083, 0), new WorldTile(4564, 5086, 0),
            new WorldTile(4564, 5097, 0), new WorldTile(4571, 5101, 0),
            new WorldTile(4578, 5097, 0)};

    private FightPits() {

    }

    /*
     * because of the lvl 22s
     */
    public static void addNPC(final NPC n) {
        synchronized (lock) {
            if (fightPitsTask == null || fightPitsTask.spawns == null)
                return;
            fightPitsTask.spawns.add(n);
        }
    }

    public static boolean canFight() {
        synchronized (lock) {
            if (fightPitsTask == null)
                return false;
            return fightPitsTask.minutes > 0;
        }
    }

    public static void passPlayersToArena() {
        for (final Iterator<Player> it = lobby.iterator(); it.hasNext(); ) {
            final Player player = it.next();
            player.stopAll();
            player.getControllerManager().removeControlerWithoutCheck();
            enterArena(player);
            it.remove();
        }
        refreshFoes();
    }

    public static void refreshFoes() {
        final int foes = arena.size() - 1;
        for (final Player player : arena) {
            player.getPackets().sendConfig(560, foes);
        }

    }

    public static void enterArena(final Player player) {
        player.lock(5);
        player.getControllerManager().startController(FightPitsArena.class);
        player.setNextWorldTile(new WorldTile(GAME_TELEPORTS[Utils
                .random(GAME_TELEPORTS.length)], 3));
        player.getDialogueManager().startDialogue(SimpleNPCMessage.class,
                THHAAR_MEJ_KAH, "Please wait for the signal before fight.");
        player.setCanPvp(true);
        player.setCantTrade(true);
        arena.add(player);
    }

    /*
     * 0 - logout, 1 - walk, 2 - dead, 3 - teled
     */
    public static void leaveArena(final Player player, final int type) {
        synchronized (lock) {
            arena.remove(player);
            player.reset();
            player.getControllerManager().removeControlerWithoutCheck();
            if (type != 3) {
                player.getControllerManager().startController(FightPitsLobby.class);
            }
            if (type == 0) {
                player.setLocation(4592, 5073, 0);
            } else {
                if (type != 3) {
                    lobby.add(player);
                }
                player.setCanPvp(false);
                player.setCantTrade(false);
                player.getPackets().closeInterface(
                        player.getInterfaceManager().hasRezizableScreen() ? 34
                                : 0);
                if (player.hasSkull() && player.getSkullId() == 1) {// if has
                    // champion
                    // skull
                    player.removeSkull();
                    player.getDialogueManager()
                            .startDialogue(SimpleNPCMessage.class, THHAAR_MEJ_KAH,
                                    "Well done in the pit, here take TokKul as reward.");
                    int tokkul = (lobby.size() + arena.size()) * 100;
                    tokkul *= Server.getInstance().getSettingsManager().getSettings().getDropRate(); // 10x more
                    if (!player.getInventory().addItem(6529, tokkul)
                            && type == 1) {
                        World.addGroundItem(new Item(6529, tokkul),
                                new WorldTile(4585, 5076, 0), player, true,
                                180, true);
                    }
                }
                if (type == 1) {
                    player.lock(5);
                    player.addWalkSteps(4585, 5076, 5, false);
                } else if (type == 2) {
                    player.setNextWorldTile(new WorldTile(new WorldTile(4592,
                            5073, 0), 2));
                }
            }
            refreshFoes();
            checkPlayersAmmount();
            if (startedGame && arena.size() <= 1) {
                endGame();
            }
        }
    }

    public static void enterLobby(final Player player, final boolean login) {
        synchronized (lock) {
            if (!login) {
                player.lock(5);
                player.addWalkSteps(4595, 5066, 5, false);
                player.getControllerManager().startController(FightPitsLobby.class);
            }
            lobby.add(player);
            checkPlayersAmmount();
        }
    }

    /*
     * 0 - logout, 1 normal, 2 death/tele
     */
    public static void leaveLobby(final Player player, final int type) {
        synchronized (lock) {
            if (type != 0) {
                if (type == 1) {
                    player.lock(5);
                    player.addWalkSteps(4597, 5064, 5, false);
                }
                player.getControllerManager().removeControlerWithoutCheck();
            }
            lobby.remove(player);
            checkPlayersAmmount();
        }
    }

    public static void checkPlayersAmmount() {
        if (fightPitsTask == null) {
            if (lobby.size() + arena.size() >= 2) {
                startGame(false);
            }
        } else {
            if (lobby.size() + arena.size() < 2) {
                cancelGame();
            }
        }
    }

    public static void startGame(final boolean end) {
        if (end) {
            fightPitsTask.cancel(true);
            fightPitsTask.removeNPCs();
            setChampion();
            startedGame = false;
        }
        fightPitsTask = new FightPitsTask(ExecutionType.FIXED_RATE, end ? 60 : 10, 60, TimeUnit.SECONDS);
        GameTaskManager.scheduleTask(fightPitsTask);

    }

    public static void cancelGame() {
        fightPitsTask.cancel(true);
        fightPitsTask.removeNPCs();
        fightPitsTask = null;
        if (startedGame) {
            setChampion();
        }
        startedGame = false;
    }

    public static void setChampion() {
        if (arena.isEmpty())
            return;
        final Player champion = arena.get(0);
        currentChampion = champion.getDisplayName();
        champion.getPackets().sendIComponentText(373, 10,
                "Current Champion: JaLYt-Ket-" + currentChampion);
        champion.setFightPitsSkull();
        champion.setWonFightPits();
        champion.getDialogueManager()
                .startDialogue(
                        SimpleNPCMessage.class,
                        THHAAR_MEJ_KAH,
                        "Well done, you were the last person in the pit and won that fight! The next round will start soon, wait for my signal before fighting.");
    }

    public static void endGame() {
        startGame(true);
    }

    @GameTaskType(GameTaskManager.GameTaskType.FAST)
    private static class FightPitsTask extends GameTask {

        private int minutes;
        private List<NPC> spawns;

        public FightPitsTask(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
            super(executionType, initialDelay, tick, timeUnit);
        }

        @Override
        public void run() {
            try {
                synchronized (lock) {
                    if (!startedGame) {
                        startedGame = true;
                        passPlayersToArena();
                    } else {
                        if (minutes == 0) {
                            for (final Player player : arena) {
                                player.getDialogueManager().startDialogue(
                                        SimpleNPCMessage.class, THHAAR_MEJ_KAH,
                                        "FIGHT!");
                            }
                        } else if (minutes == 5) { // spawn tz-kih
                            // spawns
                            spawns = new ArrayList<NPC>();
                            for (int i = 0; i < 10; i++) {
                                spawns.add(new FightPitsNPC(
                                        2734,
                                        new WorldTile(
                                                GAME_TELEPORTS[Utils
                                                        .random(GAME_TELEPORTS.length)],
                                                3)));
                            }
                        } else if (minutes == 6) { // spawn tz-kek
                            for (int i = 0; i < 10; i++) {
                                spawns.add(new TzKekPits(
                                        2736,
                                        new WorldTile(
                                                GAME_TELEPORTS[Utils
                                                        .random(GAME_TELEPORTS.length)],
                                                3)));
                            }
                        } else if (minutes == 7) { // spawn tok-xil
                            for (int i = 0; i < 10; i++) {
                                spawns.add(new FightPitsNPC(
                                        2739,
                                        new WorldTile(
                                                GAME_TELEPORTS[Utils
                                                        .random(GAME_TELEPORTS.length)],
                                                3)));
                            }
                        } else if (minutes == 10) { // spawn tz-kek
                            // alot hits appears on players
                            WorldTasksManager.schedule(new WorldTask() {

                                @Override
                                public void run() {
                                    if (!startedGame) {
                                        stop();
                                        return;
                                    }
                                    for (final Player player : arena) {
                                        player.applyHit(new Hit(player, 150,
                                                HitLook.REGULAR_DAMAGE));
                                    }
                                }

                            }, 0, 0);
                        }
                        minutes++;
                    }
                }
            } catch (final Throwable e) {
                Logger.handle(e);
            }
        }

        public void removeNPCs() {
            if (spawns == null)
                return;
            for (final NPC n : spawns) {
                n.finish();
            }
        }

    }
}
