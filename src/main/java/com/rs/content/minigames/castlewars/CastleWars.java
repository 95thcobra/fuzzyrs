package com.rs.content.minigames.castlewars;

import com.rs.player.Equipment;
import com.rs.player.Player;
import com.rs.player.content.Foods;
import com.rs.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class CastleWars {

    @SuppressWarnings("unchecked")
    private static final List<Player>[] waiting = new List[2];

    @SuppressWarnings("unchecked")
    private static final List<Player>[] playing = new List[2];

    private static int[] seasonWins = new int[2];

    private static PlayingGame playingGame;

    static {
        init();
    }

    public static void init() {
        for (int i = 0; i < waiting.length; i++) {
            waiting[i] = Collections.synchronizedList(new LinkedList<>());
        }
        for (int i = 0; i < getPlaying().length; i++) {
            getPlaying()[i] = Collections.synchronizedList(new LinkedList<>());
        }
    }

    public static void viewScoreBoard(final Player player) {
        player.getInterfaceManager().sendChatBoxInterface(55);
        player.getPackets().sendIComponentText(55, 1,
                "Saradomin: " + seasonWins[CastleWarsConstants.SARADOMIN]);
        player.getPackets().sendIComponentText(55, 2,
                "Zamorak: " + seasonWins[CastleWarsConstants.ZAMORAK]);
    }

    public static int getPowerfullestTeam() {
        final int zamorak = waiting[CastleWarsConstants.ZAMORAK].size() + getPlaying()[CastleWarsConstants.ZAMORAK].size();
        final int saradomin = waiting[CastleWarsConstants.SARADOMIN].size()
                + getPlaying()[CastleWarsConstants.SARADOMIN].size();
        if (saradomin == zamorak)
            return CastleWarsConstants.GUTHIX;
        if (zamorak > saradomin)
            return CastleWarsConstants.ZAMORAK;
        return CastleWarsConstants.SARADOMIN;
    }

    public static void joinPortal(final Player player, int team) {
        if (player.getEquipment().getHatId() != -1
                || player.getEquipment().getCapeId() != -1) {
            player.getPackets().sendGameMessage(
                    "You cannot wear hats, capes or helms in the arena.");
            return;
        }
        for (final Item item : player.getInventory().getItems().getItems()) {
            if (item == null) {
                continue;
            }
            if (Foods.Food.forId(item.getId()) != null) {
                player.getPackets().sendGameMessage(
                        "You cannot bring food into the arena.");
                return;
            }
        }
        final int powerfullestTeam = getPowerfullestTeam();
        if (team == CastleWarsConstants.GUTHIX) {
            team = powerfullestTeam == CastleWarsConstants.ZAMORAK ? CastleWarsConstants.SARADOMIN : CastleWarsConstants.ZAMORAK;
        } else if (team == powerfullestTeam) {
            if (team == CastleWarsConstants.ZAMORAK) {
                player.getPackets()
                        .sendGameMessage(
                                "The Zamorak team is powerful enough already! Guthix demands balance - join the Saradomin team instead!");
            } else if (team == CastleWarsConstants.SARADOMIN) {
                player.getPackets()
                        .sendGameMessage(
                                "The Saradomin team is powerful enough already! Guthix demands balance - join the Zamorak team instead!");
            }
            return;
        }
        player.lock(2);
        waiting[team].add(player);
        setCape(player, new Item(team == CastleWarsConstants.ZAMORAK ? 4042 : 4041));
        setHood(player, new Item(team == CastleWarsConstants.ZAMORAK ? 4515 : 4513));
        player.getControllerManager().startController(CastleWarsWaiting.class, team);
        player.setNextWorldTile(new WorldTile(team == CastleWarsConstants.ZAMORAK ? CastleWarsConstants.ZAMO_WAITING
                : CastleWarsConstants.SARA_WAITING, 1));
        player.getMusicsManager().playMusic(318); // temp testing else 5
        if (playingGame == null && waiting[team].size() >= CastleWarsConstants.PLAYERS_TO_START) {
            // 9players to
            // start
            createPlayingGame();
        } else {
            refreshTimeLeft(player);
            // You cannot take non-combat items into the arena
        }
    }

    public static void setHood(final Player player, final Item hood) {
        player.getEquipment().getItems().set(Equipment.SLOT_HAT, hood);
        player.getEquipment().refresh(Equipment.SLOT_HAT);
        player.getAppearance().generateAppearenceData();
    }

    public static void setCape(final Player player, final Item cape) {
        player.getEquipment().getItems().set(Equipment.SLOT_CAPE, cape);
        player.getEquipment().refresh(Equipment.SLOT_CAPE);
        player.getAppearance().generateAppearenceData();
    }

    public static void setWeapon(final Player player, final Item weapon) {
        player.getEquipment().getItems().set(Equipment.SLOT_WEAPON, weapon);
        player.getEquipment().refresh(Equipment.SLOT_WEAPON);
        player.getAppearance().generateAppearenceData();
    }

    public static void createPlayingGame() {
        playingGame = new PlayingGame(GameTask.ExecutionType.FIXED_RATE, 60, 60, TimeUnit.SECONDS);
        GameTaskManager.scheduleTask(playingGame);
        refreshAllPlayersTime();
    }

    public static void destroyPlayingGame() {
        playingGame.cancel(true);
        playingGame = null;
        refreshAllPlayersTime();
        leavePlayersSafely();
    }

    public static void leavePlayersSafely() {
        leavePlayersSafely(-1);
    }

    public static void leavePlayersSafely(final int winner) {
        for (final List<Player> element : getPlaying()) {
            for (final Player player : element) {
                player.lock(7);
                player.stopAll();
            }
        }
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                for (int i = 0; i < getPlaying().length; i++) {
                    for (final Player player : getPlaying()[i]
                            .toArray(new Player[getPlaying()[i].size()])) {
                        forceRemovePlayingPlayer(player);
                        if (winner != -1) {
                            if (winner == -2) {
                                player.getPackets().sendGameMessage("You draw.");
                                player.getInventory().addItem(CastleWarsConstants.CW_TICKET, 10);
                            } else if (winner == i) {
                                player.getPackets().sendGameMessage("You won.");
                                player.getInventory().addItem(CastleWarsConstants.CW_TICKET, 20);
                            } else {
                                player.getPackets().sendGameMessage("You lost.");
                            }
                        }
                    }
                }
            }
        }, 6);
    }

    // unused
    public static void forceRemoveWaitingPlayer(final Player player) {
        player.getControllerManager().forceStop();
    }

    public static void removeWaitingPlayer(final Player player, final int team) {
        waiting[team].remove(player);
        setCape(player, null);
        setHood(player, null);
        player.setNextWorldTile(new WorldTile(CastleWarsConstants.LOBBY, 2));
        if (playingGame != null && waiting[team].size() == 0
                && getPlaying()[team].size() == 0) {
            destroyPlayingGame(); // cancels if 0 players playing/waiting on any
            // of the tea
        }
    }

    public static void refreshTimeLeft(final Player player) {
        player.getPackets().sendConfig(380, playingGame == null ? 0 : playingGame.getMinutesLeft() - (player.getControllerManager().getController() instanceof CastleWarsPlaying ? 5 : 0));
    }

    public static void startGame() {
        for (int i = 0; i < waiting.length; i++) {
            for (final Player player : waiting[i].toArray(new Player[waiting[i].size()])) {
                joinPlayingGame(player, i);
            }
        }
    }

    public static void forceRemovePlayingPlayer(final Player player) {
        player.getControllerManager().forceStop();
    }

    public static void removePlayingPlayer(final Player player, final int team) {
        getPlaying()[team].remove(player);
        player.reset();
        player.setCanPvp(false);
        // remove the items
        setCape(player, null);
        setHood(player, null);
        final int weaponId = player.getEquipment().getWeaponId();
        if (weaponId == 4037 || weaponId == 4039) {
            CastleWars.setWeapon(player, null);
            CastleWars.dropFlag(player.getLastWorldTile(),
                    weaponId == 4037 ? CastleWarsConstants.SARADOMIN
                            : CastleWarsConstants.ZAMORAK);
        }
        player.closeInterfaces();
        player.getInventory().deleteItem(4049, Integer.MAX_VALUE); // bandages
        player.getInventory().deleteItem(4053, Integer.MAX_VALUE); // barricades

        player.getHintIconsManager().removeUnsavedHintIcon();
        player.getMusicsManager().reset();
        player.setNextWorldTile(new WorldTile(CastleWarsConstants.LOBBY, 2));
        if (playingGame != null && waiting[team].size() == 0
                && getPlaying()[team].size() == 0) {
            destroyPlayingGame(); // cancels if 0 players playing/waiting on any
            // of the tea
        }
    }

    public static void joinPlayingGame(final Player player, final int team) {
        playingGame.refresh(player);
        waiting[team].remove(player);
        player.getControllerManager().removeControlerWithoutCheck();
        player.getPackets().closeInterface(
                player.getInterfaceManager().hasRezizableScreen() ? 34 : 0);
        getPlaying()[team].add(player);
        player.setCanPvp(true);
        player.getControllerManager().startController(CastleWarsPlaying.class, team);
        player.setNextWorldTile(new WorldTile(team == CastleWarsConstants.ZAMORAK ? CastleWarsConstants.ZAMO_BASE
                : CastleWarsConstants.SARA_BASE, 1));
    }

    public static void endGame(final int winner) {
        if (winner != -2) {
            seasonWins[winner]++;
        }
        leavePlayersSafely(winner);
    }

    public static void refreshAllPlayersTime() {
        for (final List<Player> element : waiting) {
            for (final Player player : element) {
                refreshTimeLeft(player);
            }
        }
        for (int i = 0; i < getPlaying().length; i++) {
            for (final Player player : getPlaying()[i]) {
                player.getMusicsManager().playMusic(i == CastleWarsConstants.ZAMORAK ? 845 : 314);
                refreshTimeLeft(player);
            }
        }
    }

    public static void refreshAllPlayersPlaying() {
        for (final List<Player> element : getPlaying()) {
            for (final Player player : element) {
                playingGame.refresh(player);
            }
        }
    }

    public static void addHintIcon(final int team, final Player target) {
        for (final Player player : getPlaying()[team]) {
            player.getHintIconsManager().addHintIcon(target, 0, -1, false);
        }
    }

    public static void removeHintIcon(final int team) {
        for (final Player player : getPlaying()[team]) {
            player.getHintIconsManager().removeUnsavedHintIcon();
        }
    }

    public static void addScore(final Player player, final int team,
                                final int flagTeam) {
        if (playingGame == null)
            return;
        playingGame.addScore(player, team, flagTeam);
    }

    public static void takeFlag(final Player player, final int team,
                                final int flagTeam, final WorldObject object, final boolean droped) {
        if (playingGame == null)
            return;
        playingGame.takeFlag(player, team, flagTeam, object, droped);
    }

    public static void dropFlag(final WorldTile tile, final int flagTeam) {
        if (playingGame == null)
            return;
        playingGame.dropFlag(tile, flagTeam);
    }

    public static void removeBarricade(final int team,
                                       final CastleWarBarricade npc) {
        if (playingGame == null)
            return;
        playingGame.removeBarricade(team, npc);
    }

    public static void addBarricade(final int team, final Player player) {
        if (playingGame == null)
            return;
        playingGame.addBarricade(team, player);
    }

    public static boolean isBarricadeAt(final WorldTile tile) {
        if (playingGame == null)
            return false;
        return playingGame.isBarricadeAt(tile);
    }

    public static List<Player>[] getPlaying() {
        return playing;
    }
}
