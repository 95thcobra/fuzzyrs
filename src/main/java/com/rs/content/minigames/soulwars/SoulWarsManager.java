package com.rs.content.minigames.soulwars;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.content.player.points.PlayerPoints;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Utils;
import com.rs.player.Equipment;
import com.rs.player.Player;
import com.rs.player.content.Foods;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Savions Sw, the legendary.
 */
public final class SoulWarsManager {

    public final static int BANDAGE_ID = 14648,

    BARRICADE_ID = 14649,

    EXPLOSIVE_POTION_ID = 14650,

    TEAM_CAPE_INDEX = 14641,

    SOUL_FRAGMENT = 14646,

    BONES = 3187,

    REQUIRED_TEAM_MEMBERS = 5,

    JELLY = 8599,

    PYREFRIEND = 8598,

    GHOST = 8623,

    AVATAR_INDEX = 8596;

    public static AtomicInteger MINUTES_BEFORE_NEXT_GAME = new AtomicInteger(3);
    public static int ZEAL_MODIFIER = 1;
    private final HashMap<PlayerType, GameTask> tasks = new HashMap<>(PlayerType.values().length);

    public void start() {
        startTask(PlayerType.OUTSIDE_LOBBY, new AreaTask(PlayerType.OUTSIDE_LOBBY));
        startTask(PlayerType.INSIDE_LOBBY, new LobbyTask(PlayerType.INSIDE_LOBBY));
        startTask(PlayerType.IN_GAME, new SoulWarsGameTask(PlayerType.IN_GAME));
    }

    public void passBarrier(PlayerType currentType, Player player,
                            WorldObject object) {
        if (currentType.equals(PlayerType.OUTSIDE_LOBBY)
                && !canEnterLobby(player))
            return;
        switch (object.getId()) {
            case 42015:
            case 42018:
                int id = player.getEquipment().getCapeId();
                id -= TEAM_CAPE_INDEX;
                if (id < 0 || id > 1)
                    return;
                final Teams team = Teams.values()[id];
                if ((team.equals(Teams.RED) && object.getId() == 42015) || (team.equals(Teams.BLUE) && object.getId() == 42018)
                        || player.getAppearance().getTransformedNpcId() != -1)
                    return;
                int x = player.getX() + object.getX() - player.getX();
                if (object.getId() == 42015 ? (object.getX() >= player.getX()) : (object.getX() <= player.getX()))
                    x = object.getX() + (object.getId() == 42015 ? 1 : -1);
                player.addWalkSteps(x, object.getY(), -1, false);
                player.lock(1);
                break;
            case 42019:
            case 42020:
                if (player.getAppearance().getTransformedNpcId() != -1
                        || (object.getId() == 42020 && object.getY() <= player
                        .getY())
                        || (object.getId() == 42019 && object.getY() >= player
                        .getY()))
                    return;
                player.addWalkSteps(object.getX(), object.getY(), -1, false);
                player.lock(1);
                break;
            case 42029:
            case 42030:
                x = player.getX() + object.getX() - player.getX();
                if (object.getId() == 42029 ? (object.getX() >= player.getX()) : (object.getX() <= player.getX()))
                    x = object.getX() + (object.getId() == 42029 ? 1 : -1);
                player.addWalkSteps(x, object.getY(), -1, false);
                player.lock(1);
                break;
            case 42031:
                final Teams nextTeam = nextJoiningTeam();
                player.setNextWorldTile(calculateRandomLocation(nextTeam, PlayerType.INSIDE_LOBBY));
                enterLobby(player, nextTeam);
                return;
        }
        if (player.getControllerManager().getController() != null && player.getControllerManager().getController() instanceof GameController) {
            Boolean bool = (Boolean) player.getTemporaryAttributtes().get("sw_safe_zone");
            if (bool != null)
                player.getTemporaryAttributtes().put("sw_safe_zone", !bool);
        }
        switch (currentType) {
            case OUTSIDE_LOBBY:
                enterLobby(player, object.getId() == 42029 ? Teams.BLUE : Teams.RED);
                break;
            case INSIDE_LOBBY:
                ((LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY)).getPlayers().remove(player);
                resetPlayer(player, PlayerType.OUTSIDE_LOBBY, false);
                break;
            default:
                break;
        }
    }

    private boolean canEnterLobby(Player player) {
        if (player.getEquipment().getCapeId() != -1) {
            player.getPackets().sendGameMessage("You cannot join the waiting lobby, remove your cape from your equipment.");
            return false;
        }
        if (player.getFamiliar() != null) {
            player.getPackets().sendGameMessage("You're not allowed to enter with that familiar.");
            return false;
        }
        if (player.getSkills().getTotalLevel() < 250) {
            player.getPackets().sendGameMessage("You need a total level of 250 to play SoulWars.");
            return false;
        }
        for (final Summoning.Pouches pouch : Summoning.Pouches.values()) {
            if (pouch == null)
                continue;
            if (player.getInventory().containsItem(pouch.getPouchId(), 1)) {
                player.getPackets().sendGameMessage("You cannot enter with having pouches in your inventory!");
                return false;
            }
        }
        for (Item item : player.getInventory().getItems().getItems()) {
            if (item == null)
                continue;
            if (Foods.Food.forId(item.getId()) != null) {
                player.getPackets().sendGameMessage(
                        "You cannot bring food into this arena.");
                return false;
            }
            /*final ItemDefinitions defs = ItemDefinitions.getItemDefinitions(item.getId());
            if (defs != null) {
                String name = defs.getName();
                if (name != null) {
                    for (final String disabled : Settings.FORBIDDEN_SOUL_WARS_ITEMS) {
                        if (name.toLowerCase().contains(disabled.toLowerCase())) {
                            player.getPackets().sendGameMessage("You cannot bring your " + name.toLowerCase() + " into this area, bank it.");
                            return false;
                        }
                    }
                }
            }*/
        }
        /*for (final Item item : player.getEquipment().getItems().getItems()) {
            if (item != null) {
                final ItemDefinitions defs = ItemDefinitions.getItemDefinitions(item.getId());
                if (defs != null) {
                    String name = ItemDefinitions.getItemDefinitions(item.getId()).getName();
                    if (name != null) {
                        for (final String disabled : Settings.FORBIDDEN_SOUL_WARS_ITEMS) {
                            if (name.toLowerCase().contains(disabled.toLowerCase())) {
                                player.getPackets().sendGameMessage("You cannot bring your " + name.toLowerCase() + " into this area, take it off your equipment and bank it.");
                                return false;
                            }
                        }
                    }
                }
            }
        }*/
        return true;
    }

    private void enterLobby(Player player, Teams team) {
        player.getControllerManager().startController(LobbyController.class);
        player.getEquipment()
                .getItems()
                .set(Equipment.SLOT_CAPE,
                        new Item(TEAM_CAPE_INDEX + team.ordinal()));
        player.getEquipment().refresh(Equipment.SLOT_CAPE);
        player.getAppearance().generateAppearenceData();
        player.getPackets().sendGameMessage("You join the " + team.toString().toLowerCase() + " team.");
    }

    private Teams nextJoiningTeam() {
        final int minutes = MINUTES_BEFORE_NEXT_GAME.get();
        final SoulWarsGameTask game = (SoulWarsGameTask) tasks.get(PlayerType.IN_GAME);
        final LobbyTask lobby = (LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY);
        if (minutes < 4) {
            if (lobby.getPlayers(Teams.RED).size() > lobby.getPlayers(Teams.BLUE).size())
                return Teams.BLUE;
            else if (lobby.getPlayers(Teams.RED).size() < lobby.getPlayers(Teams.BLUE).size())
                return Teams.RED;
            else
                return Teams.values()[Utils.getRandom(1)];
        } else {
            int[] totalSizes = new int[2];
            for (int index = 0; index < 2; index++)
                totalSizes[index] = game.getPlayers(Teams.values()[index]).size() + lobby.getPlayers(Teams.values()[index]).size();
            if (totalSizes[Teams.RED.ordinal()] > totalSizes[Teams.BLUE.ordinal()])
                return Teams.BLUE;
            else if (totalSizes[Teams.RED.ordinal()] < totalSizes[Teams.BLUE.ordinal()])
                return Teams.RED;
            else
                return Teams.values()[Utils.getRandom(1)];
        }
    }

    public void resetPlayer(Player player, PlayerType type, boolean logout) {
        int id = player.getEquipment().getCapeId();
        id -= TEAM_CAPE_INDEX;
        if (id < 0 || id > 1) // Safety reasons.
            return;
        Teams team = Teams.values()[id];
        if (!logout) {
            player.getControllerManager().startController(SoulWarsAreaController.class);
        }
        player.getEquipment().deleteItem(TEAM_CAPE_INDEX + id, 1);
        player.getEquipment().refresh(Equipment.SLOT_CAPE);
        player.getAppearance().generateAppearenceData();
        player.getInventory().deleteItem(SOUL_FRAGMENT, Integer.MAX_VALUE);
        player.getInventory().deleteItem(BANDAGE_ID, 28);
        player.getInventory().deleteItem(BONES, 28);
        player.getInventory().deleteItem(EXPLOSIVE_POTION_ID, 28);
        player.getInventory().deleteItem(4053, 28);
        player.getInventory().deleteItem(14644, 28);
        player.getInventory().deleteItem(BARRICADE_ID, 28);
        player.getAppearance().transformIntoNPC(-1);
        player.getPackets().sendGameMessage("If you found any bugs please post them on forums in 'bug' section!.");
        if (type.equals(PlayerType.IN_GAME)) {
            player.setCanPvp(false);
            player.getPrayer().reset();
            player.getPoison().reset();
            player.resetReceivedDamage();
            player.setHitpoints(player.getMaxHitpoints());
            player.setRunEnergy(100);
            player.unlock(); //safety reasons
        }
        if (!type.equals(PlayerType.OUTSIDE_LOBBY)) {
            WorldTile random = calculateRandomLocation(team, PlayerType.OUTSIDE_LOBBY);
            if (!logout)
                player.setNextWorldTile(random);
            else
                player.setLocation(random);
        }
    }

    public WorldTile calculateRandomLocation(Teams team, PlayerType type) {
        final WorldTile A = team.equals(Teams.BLUE) ? type.getLocationA()
                : type.getLocationC();
        final WorldTile B = team.equals(Teams.BLUE) ? type.getLocationB()
                : type.getLocationD();
        ArrayList<WorldTile> possibleLocations = new ArrayList<>();
        for (int x = A.getX(); x <= B.getX(); x++) {
            for (int y = A.getY(); y <= B.getY(); y++) {
                if (World.canMoveNPC(0, x, y, 1)) {
                    possibleLocations.add(new WorldTile(x, y, 0));
                }
            }
        }
        return possibleLocations.get(Utils.random(possibleLocations.size()));
    }

    public boolean decrementMinute() {
        if (tasks.size() < 3 || (MINUTES_BEFORE_NEXT_GAME.get() <= 3 &&
                (((LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY)).getPlayers(Teams.BLUE).size() < REQUIRED_TEAM_MEMBERS ||
                        ((LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY)).getPlayers(Teams.RED).size() < REQUIRED_TEAM_MEMBERS)))
            return false;
        if (MINUTES_BEFORE_NEXT_GAME.get() > 3 && (((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getPlayers(Teams.BLUE).size() < 1 || ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getPlayers(Teams.RED).size() < 1)) {
            endGame();
            MINUTES_BEFORE_NEXT_GAME.set(3);
            return false;
        }
        int decrement = MINUTES_BEFORE_NEXT_GAME.decrementAndGet();
        if (decrement >= 3 + 10)
            sendPlayers(false);
        if (decrement == 3)
            endGame();
        if (decrement == 0) {
            sendPlayers(true);
            MINUTES_BEFORE_NEXT_GAME.set(23);
        }
        return true;
    }

    private void endGame() {
        ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getAvatars()[0].resetReceivedDamage();
        ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getAvatars()[1].resetReceivedDamage();
        final int blue = ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getAvatarDies(Teams.BLUE),
                red = ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getAvatarDies(Teams.RED);
        final Teams winningTeam = red > blue ? Teams.BLUE : blue > red ? Teams.RED : null;
        final String name = winningTeam == null ? "unknown" : winningTeam.equals(Teams.BLUE) ? "<col=337FB5>blue</col>" : "<col=F00004>red</col>";
        for (Iterator<Player> it = ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getPlayers().iterator(); it.hasNext(); ) {
            Player player = it.next();
            it.remove();
            if (player == null || player.hasFinished())
                continue;
            int id = player.getEquipment().getCapeId();
            id -= TEAM_CAPE_INDEX;
            if (id < 0 || id > 1) // Safety reasons.
                return;
            final Teams team = Teams.values()[id];
            resetPlayer(player, PlayerType.IN_GAME, false);
            final int zeals = (winningTeam == null ? 3 : winningTeam.equals(team) ? 4 : 2) * ZEAL_MODIFIER;
            final String message = winningTeam == null ? "The game was a draw, you received " + zeals + " zeals for parcitipating."
                    : "The "
                    + name
                    + " team was victorious! You received "
                    + (winningTeam.equals(team) ? zeals + " zeals for winning!"
                    : zeals + " zeals for losing,");
            player.getPlayerPoints().addPoints(PlayerPoints.ZEALS, zeals);
            player.getDialogueManager().startDialogue(SimpleNPCMessage.class, team.equals(Teams.RED) ? 8528 : 8526, message);
            player.getPackets().sendGameMessage(message);
        }
        ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).reset();
    }

    private void sendPlayers(boolean create) {
        if (create)
            ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).start();
        final ArrayList<Player> blue = ((LobbyTask) tasks
                .get(PlayerType.INSIDE_LOBBY)).getPlayers(Teams.BLUE), red = ((LobbyTask) tasks
                .get(PlayerType.INSIDE_LOBBY)).getPlayers(Teams.RED);
        if (SettingsManager.getSettings().DEBUG) {
            for (Player player : blue) {
                addPlayerToGame(player, Teams.BLUE);
            }
            blue.clear();
            for (Player player : red) {
                addPlayerToGame(player, Teams.RED);
            }
            red.clear();
        }
        int size;
        if (create) {
            size = blue.size() > red.size() ? red.size() : blue.size();
            if (size < 1)
                return;
            for (int i = 0; i < 2; i++) {
                int index = 0;
                for (Iterator<Player> it = (i == 0 ? red.iterator() : blue.iterator()); it.hasNext(); ) {
                    if (index++ >= size) {
                        break;
                    }
                    Player player = it.next();
                    if (player != null && !player.hasFinished() && !player.isLocked())
                        addPlayerToGame(player, Teams.values()[i]);
                    it.remove();
                }
            }
        } else {
            final ArrayList<Player> gameBlue = ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getPlayers(Teams.BLUE),
                    gameRed = ((SoulWarsGameTask) tasks.get(PlayerType.IN_GAME)).getPlayers(Teams.RED), lobbyBlue = ((LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY)).getPlayers(Teams.BLUE),
                    lobbyRed = ((LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY)).getPlayers(Teams.RED);
            size = gameBlue.size() > gameRed.size() ? gameBlue.size() - gameRed.size() : gameRed.size() - gameBlue.size();
            final int lobbySize = lobbyBlue.size() > lobbyRed.size() ? lobbyRed.size() : lobbyBlue.size();
            final int[] takeOutEachTeam = new int[2];
            takeOutEachTeam[0] = lobbySize + (gameBlue.size() > gameRed.size() ? size + lobbySize > lobbyRed.size() ? lobbyRed.size() : size + lobbySize : 0);
            takeOutEachTeam[1] = lobbySize + (gameRed.size() > gameBlue.size() ? size + lobbySize > lobbyBlue.size() ? lobbyBlue.size() : size + lobbySize : 0);
            if (takeOutEachTeam[0] == 0 && takeOutEachTeam[1] == 0 && gameRed.size() > 0)
                takeOutEachTeam[0] = gameRed.size();
            if (takeOutEachTeam[1] == 0 && takeOutEachTeam[0] == 0 && gameBlue.size() > 0)
                takeOutEachTeam[1] = gameBlue.size();
            for (int index = 0; index < 2; index++) {
                final ArrayList<Player> players = index == 0 ? lobbyRed : lobbyBlue;
                int playerIndex = 0;
                for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
                    if (playerIndex++ == takeOutEachTeam[index])
                        break;
                    Player player = it.next();
                    if (player != null)
                        addPlayerToGame(player, Teams.values()[index]);
                    it.remove();
                }
            }
        }
        for (Iterator<Player> it = ((LobbyTask) tasks.get(PlayerType.INSIDE_LOBBY)).getPlayers().iterator(); it.hasNext(); ) {
            Player player = it.next();
            if (player != null)
                player.getPackets().sendGameMessage("You now have a higher priority to enter a game of Soul Wars.");
            else
                it.remove();
        }
    }

    private void addPlayerToGame(Player player, Teams team) {
        SoulWarsGameTask task = (SoulWarsGameTask) tasks.get(PlayerType.IN_GAME);
        if (task.getPlayers().contains(player) || player.getControllerManager().getController() instanceof GameController)
            return;
        player.getControllerManager().startController(GameController.class, team.ordinal());
        task.getPlayers().add(player);
    }

    private void startTask(PlayerType type, GameTask task) {
        tasks.put(type, task);
        GameTaskManager.scheduleTask(task);
    }

    public HashMap<PlayerType, GameTask> getTasks() {
        return tasks;
    }

    public enum Teams {
        RED, BLUE
    }

    public enum PlayerType {

        OUTSIDE_LOBBY(new WorldTile(1884, 3166, 0),
                new WorldTile(1888, 3174, 0), new WorldTile(1892, 3166, 0),
                new WorldTile(1896, 3174, 0)),

        INSIDE_LOBBY(new WorldTile(1870, 3158, 0),
                new WorldTile(1879, 3166, 0), new WorldTile(1900, 3157, 0),
                new WorldTile(1909, 3166, 0)),

        IN_GAME(new WorldTile(1816, 3220, 0), new WorldTile(1823, 3230, 0),
                new WorldTile(1951, 3234, 0), new WorldTile(1958, 3244, 0));

        private final WorldTile LOCATION_A;

        private final WorldTile LOCATION_B;

        private final WorldTile LOCATION_C;

        private final WorldTile LOCATION_D;

        PlayerType(WorldTile a, WorldTile b, WorldTile c, WorldTile d) {
            this.LOCATION_A = a;
            this.LOCATION_B = b;
            this.LOCATION_C = c;
            this.LOCATION_D = d;
        }

        public final WorldTile getLocationA() {
            return LOCATION_A;
        }

        public final WorldTile getLocationB() {
            return LOCATION_B;
        }

        public final WorldTile getLocationC() {
            return LOCATION_C;
        }

        public final WorldTile getLocationD() {
            return LOCATION_D;
        }
    }
}
