package com.rs.content.minigames.cagegame;

/**
 * @author John (FuzzyAvacado) on 1/7/2016.
 */

import com.rs.core.utils.Utils;
import com.rs.player.Equipment;
import com.rs.player.Player;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

import java.util.LinkedList;

/**
 * @author Tyler
 */
public class CageGame {
    /**
     * Random Cage teleports.
     */
    public static final WorldTile[] TELEPORTS = {new WorldTile(3395, 3511, 0),
            new WorldTile(3395, 3524, 0), new WorldTile(3388, 3524, 0),
            new WorldTile(3382, 3524, 0), new WorldTile(3381, 3516, 0),
            new WorldTile(3388, 3518, 0), new WorldTile(3381, 3509, 0),
            new WorldTile(3395, 3518, 0)};
    /**
     * Lobby.
     */
    public static final WorldTile LOBBY = new WorldTile(3222, 3222, 0); // Change it to what you want, lumby was a test.
    /**
     * Players needed to start cage.
     */
    public static final int PLAYERS_NEEDED = 2;
    /**
     * Cage lobby/Game players.
     */
    private static final LinkedList<Player> lobbyPlayers = new LinkedList<>();
    private static final LinkedList<Player> gamePlayers = new LinkedList<>();
    /**
     * Armour for cage game.
     */
    public static int WEAPON[] = {18349, 18351, 18353, 14484};
    public static int PLATEBODY[] = {13458, 1117, 17259, 17257};
    public static int PLATELEGS[] = {16687, 16689, 1075, 11726};
    public static int HELMET[] = {16711, 16709, 1139};
    /**
     * Cage timers.
     */
    private static boolean gameStarted = false;
    private static long gameTimer;
    private static long startTime;

    /**
     * Checks if the game is currently started.
     *
     * @return isGameStarted?
     */
    public static boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Set the game started.
     *
     * @param gameStarted
     */
    public static void setGameStarted(boolean gameStarted) {
        CageGame.gameStarted = gameStarted;
    }

    /**
     * Starts the game.
     */
    public static void startGame() {
        setGameStarted(true);
        for (Player gamePlayers : getGamePlayers()) {
            gamePlayers.getCombatDefinitions().setAutoCastSpell(0);
            gamePlayers.getCombatDefinitions().refreshAutoCastSpell();
            gamePlayers.getBank().depositAllBob(false);
            gamePlayers.getBank().depositAllEquipment(false);
            gamePlayers.getBank().depositAllInventory(false);
            gamePlayers.sendMessage("The game has begun! All of your items have been banked.");
            gamePlayers.sendMessage("Good luck to you all, may the best fighter win!");
            gamePlayers.setNextWorldTile(TELEPORTS[Utils.random(TELEPORTS.length)]);
            gameTimer = Utils.currentTimeMillis();
            gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getHelmet()), new Item(getHelmet()));
            gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getWeapon()), new Item(getWeapon()));
            gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getPlatebody()), new Item(getPlatebody()));
            gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getPlatelegs()), new Item(getPlatelegs()));
            gamePlayers.getEquipment().init();
            gamePlayers.getAppearance().generateAppearenceData();
            gamePlayers.setCanPvp(true);
        }
    }

    /**
     * Process the actual "cage" game.
     * Processes every 30 seconds.
     */
    public static void processCage() {
        if (CageGame.isGameStarted()) {
            if (getGamePlayers().size() <= 1) {
                endGame();
            }
            if (CageGame.getGameTimer() + 300000 < Utils.currentTimeMillis()) {
                for (Player gamers : CageGame.getGamePlayers()) {
                    gamers.sendMessage("Sorry! Time has ran out!");
                    endGame();
                }
            }
        }
        if (getLobbyPlayers().size() < PLAYERS_NEEDED && !isGameStarted()) {
            if (getStartTime() + 30000 < Utils.currentTimeMillis()) {
                for (Player players : getLobbyPlayers()) {
                    players.sendMessage(CageGame.getLobbyPlayers().size() == 1 ? "<col=3366FF><shad=000000>There is currently <shad=FFFFFF>[1]</shad><shad=000000> player in the lobby!" : "<col=3366FF><shad=000000>There is currently : <shad=FFFFFF>[" + CageGame.getLobbyPlayers().size() + "]</shad><shad=000000> players in the lobby!");
                    players.sendMessage("<col=3366FF><shad=000000>There needs to be at least <shad=FFFFFF>[" + PLAYERS_NEEDED + "]</shad><shad=000000> in the lobby to start the game!");
                    setStartTime(Utils.currentTimeMillis());
                }
            }
        } else if (getLobbyPlayers().size() >= PLAYERS_NEEDED && !isGameStarted()) {
            if (getStartTime() + 30000 < Utils.currentTimeMillis()) {
                getGamePlayers().addAll(lobbyPlayers);
                getLobbyPlayers().removeAll(lobbyPlayers);
                startGame();
                setStartTime(Utils.currentTimeMillis());
            }
        }
    }

    /**
     * Ends the cage game.
     */
    public static void endGame() {
        for (Player gamers : getGamePlayers()) {
            if (gamers == null)
                continue;
            Player winner = getGamePlayers().poll();
            if (winner == null)
                continue;
            gamers.setCanPvp(false);
            gamers.getEquipment().reset();
            winner.sendMessage("Congratulations! You've won!");
            winner.getBank().addItem(995, 100000, true);//100k prize.
            winner.setNextWorldTile(LOBBY);
            gamers.sendMessage("Game over!");
            setGameStarted(false);
            gamers.getAppearance().generateAppearenceData();
        }
    }

    /**
     * Gets the gameTimer.
     *
     * @return gameTimer
     */
    public static long getGameTimer() {
        return gameTimer;
    }

    /**
     * Set the gameTimer.
     *
     * @param gameTimer
     */
    public static void setGameTimer(long gameTimer) {
        CageGame.gameTimer = gameTimer;
    }

    /**
     * Adds a player to the "game".
     *
     * @param player
     */
    public static void addGamePlayer(Player player) {
        if (!getGamePlayers().contains(player))
            getGamePlayers().add(player);
    }

    /**
     * Adds a player to the "lobby".
     *
     * @param player
     */
    public static void addLobbyPlayer(Player player) {
        if (!getLobbyPlayers().contains(player))
            getLobbyPlayers().add(player);
    }

    /**
     * Removes a player from the "lobby".
     *
     * @param player
     */
    public static void removeLobbyPlayer(Player player) {
        getLobbyPlayers().remove(player);
    }

    /**
     * Gets all the players in the LinkedList<Player> gamePlayers.
     *
     * @return gamePlayers
     */
    public static LinkedList<Player> getGamePlayers() {
        return gamePlayers;
    }

    /**
     * Gets all the players in the LinkedList<Player> lobbyPlayers.
     *
     * @return lobbyPlayers
     */
    public static LinkedList<Player> getLobbyPlayers() {
        return lobbyPlayers;
    }

    /**
     * Gets the random weapon.
     *
     * @return weapon
     */
    public static int getWeapon() {
        return WEAPON[(int) (Math.random() * WEAPON.length)];
    }

    /**
     * Get the random plateBody.
     *
     * @return platebody
     */
    public static int getPlatebody() {
        return PLATEBODY[(int) (Math.random() * PLATEBODY.length)];
    }

    /**
     * Gets the random platelegs.
     *
     * @return platelegs
     */
    public static int getPlatelegs() {
        return PLATELEGS[(int) (Math.random() * PLATELEGS.length)];
    }

    /**
     * Gets the random helmet.
     *
     * @return helmet.
     */
    public static int getHelmet() {
        return HELMET[(int) (Math.random() * HELMET.length)];
    }

    /**
     * Gets the startTime.
     *
     * @return startTime
     */
    public static long getStartTime() {
        return startTime;
    }

    /**
     * Sets the startTime.
     *
     * @param startTime
     */
    public static void setStartTime(long startTime) {
        CageGame.startTime = startTime;
    }
}
