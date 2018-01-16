package com.rs.core.net.handlers.inventory;

import com.rs.content.actions.skills.runecrafting.Runecrafting;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.dialogues.impl.Transportation;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionSixHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        final int itemId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final long time = Utils.currentTimeMillis();
        if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time) {
            return true;
        }
        player.stopAll(false);
        final Summoning.Pouches pouches = Summoning.Pouches.forId(itemId);
        if (pouches != null) {
            Summoning.spawnFamiliar(player, pouches);
            return true;
        }
        if (player.getToolBelt().addItem(item)) {
            return true;
        } else if (itemId == 1438) {
            Runecrafting.locate(player, 3127, 3405);
        } else if (itemId == 1440) {
            Runecrafting.locate(player, 3306, 3474);
        } else if (itemId == 1442) {
            Runecrafting.locate(player, 3313, 3255);
        } else if (itemId == 1444) {
            Runecrafting.locate(player, 3185, 3165);
        } else if (itemId == 1446) {
            Runecrafting.locate(player, 3053, 3445);
        } else if (itemId == 1448) {
            Runecrafting.locate(player, 2982, 3514);
        } else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
                && itemId <= 10362) {
            player.getDialogueManager().startDialogue(Transportation.class,
                    "Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
                    new WorldTile(2918, 3176, 0), "Draynor Village",
                    new WorldTile(3105, 3251, 0), "Al Kharid",
                    new WorldTile(3293, 3163, 0), itemId);
        } else if (itemId == 995) {
            final int amount = player.getInventory().getItems()
                    .getNumberOf(995);
            if (!player.canSpawn()) {
                player.getPackets().sendGameMessage(
                        "You can't add money to your money pouch here.");
                return true;
            }
            if (player.getMoneyPouchValue() + amount > 0) {
                player.getInventory().deleteItem(995, amount);
                player.getPackets().sendRunScript(5561, 1, amount);
                player.getMoneyPouch().addMoney(amount, false);
            } else {
                player.getPackets().sendGameMessage(
                        "Your money pouch is currently full.");
            }
        } else if (itemId == 1704 || itemId == 10352) {
            player.getPackets()
                    .sendGameMessage(
                            "The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
        } else if (itemId >= 3853 && itemId <= 3867) {
            player.getDialogueManager().startDialogue(Transportation.class,
                    "Burthrope Games Room", new WorldTile(2880, 3559, 0),
                    "Barbarian Outpost", new WorldTile(2519, 3571, 0),
                    "Gamers' Grotto", new WorldTile(2970, 9679, 0),
                    "Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
        }
        return false;
    }
}
