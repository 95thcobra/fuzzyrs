package com.rs.content.minigames.soulwars;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */

import com.rs.player.Equipment;
import com.rs.player.Inventory;
import com.rs.player.controlers.Controller;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

import static com.rs.content.minigames.soulwars.SoulWarsManager.*;

/**
 * @author Savions Sw
 */
public class LobbyController extends Controller {

    private boolean burnNiggers; //added by james

    @Override
    public void start() {
        burnNiggers = player.getInterfaceManager().hasRezizableScreen();
        player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 27 : 11, 837);
        ((LobbyTask) World.getSoulWars().getTasks().get(PlayerType.INSIDE_LOBBY)).getPlayers().add(player);
    }

    @Override
    public void sendInterfaces() {
        if (burnNiggers != player.getInterfaceManager().hasRezizableScreen()) {
            burnNiggers = player.getInterfaceManager().hasRezizableScreen();
            player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 27 : 11, 837);
        }
        final int minutes = MINUTES_BEFORE_NEXT_GAME.get();
        boolean noGame = minutes < 4;
        int blue, red;
        if (noGame) {
            LobbyTask task = (LobbyTask) World.getSoulWars().getTasks().get(PlayerType.INSIDE_LOBBY);
            blue = task.getPlayers(Teams.BLUE).size();
            red = task.getPlayers(Teams.RED).size();
        } else {
            SoulWarsGameTask task = (SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME);
            blue = task.getPlayers(Teams.BLUE).size();
            red = task.getPlayers(Teams.RED).size();
        }
        player.getPackets().sendGlobalConfig(632, noGame ? 0 : 1);
        player.getPackets().sendIComponentText(837, 9, "New game: " + minutes + " " + (minutes == 1 ? "min" : "mins"));
        if (noGame) {
            player.getPackets().sendGlobalConfig(633, blue + (10 - REQUIRED_TEAM_MEMBERS));
            player.getPackets().sendGlobalConfig(634, red + (10 - REQUIRED_TEAM_MEMBERS));
        } else {
            player.getPackets().sendIComponentText(837, 3, "" + blue);
            player.getPackets().sendIComponentText(837, 5, "" + red);
        }
    }

    @Override
    public boolean canEquip(int slotId, int itemId) {
        if (slotId == Equipment.SLOT_CAPE) {
            player.getPackets().sendGameMessage("You can't remove your team's colours.");
            return false;
        }
        return true;
    }

    @Override
    public void forceClose() {
        player.getPackets().closeInterface(
                player.getInterfaceManager().hasRezizableScreen() ? 27 : 11);
        ((LobbyTask) World.getSoulWars().getTasks().get(PlayerType.INSIDE_LOBBY)).getPlayers().remove(player);
    }

    @Override
    public boolean processMagicTeleport(WorldTile toTile) {
        player.getPackets().sendGameMessage(
                "You can't just leave like that!");
        return false;
    }

    @Override
    public boolean processItemTeleport(WorldTile toTile) {
        player.getPackets().sendGameMessage(
                "You can't just leave like that!");
        return false;
    }

    @Override
    public void magicTeleported(int type) {
        forceClose();
        removeControler();
    }

    @Override
    public boolean processButtonClick(int interfaceId, int componentId,
                                      int slotId, int packetId) {
        if (interfaceId == 590 & componentId == 8) {
            player.getPackets().sendGameMessage("This is a battleground, not a circus.");
            return false;
        }
        if (interfaceId == 387 && componentId == 9) {
            player.getPackets().sendGameMessage("You can't remove your team's colours.");
            return false;
        }
        if (interfaceId == 667 && componentId == 9) {
            player.getPackets().sendGameMessage("You can't remove your team's colours.");
            return false;
        }
        if (interfaceId == Inventory.INVENTORY_INTERFACE || interfaceId == 670) {
            Item item = player.getInventory().getItem(slotId);
            if (item != null) {
                if (item.getId() == TEAM_CAPE_INDEX || item.getId() == TEAM_CAPE_INDEX + 1) {
                    player.getPackets().sendGameMessage("You can't remove your team's colours.");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        switch (object.getId()) {
            case 42029:
            case 42030:
            case 42031:
                World.getSoulWars().passBarrier(PlayerType.INSIDE_LOBBY, player, object);
                return true;
        }
        return false;
    }

    @Override
    public boolean login() {
        player.getControllerManager().startController(SoulWarsAreaController.class);
        return false;
    }

    @Override
    public boolean logout() {
        World.getSoulWars().resetPlayer(player, PlayerType.INSIDE_LOBBY, true);
        return false;
    }
}
