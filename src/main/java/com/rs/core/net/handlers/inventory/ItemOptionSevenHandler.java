package com.rs.core.net.handlers.inventory;

import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionSevenHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        final int itemId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final long time = Utils.currentTimeMillis();
        if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
            return false;
        if (!player.getControllerManager().canDropItem(item))
            return false;
        player.stopAll(false);
        if (item.getDefinitions().isOverSized()) {
            player.getPackets().sendGameMessage("The item appears to be over sized.");
            player.getInventory().deleteItem(item);
            return false;
        }
        if (player.isBurying) {
            player.getPackets().sendGameMessage("You can't drop items while your burying.");
            return false;
        }
        int total = 0;
        final int reqTotal = 400;
        for (int i = 0; i < 25; i++) {
            total += player.getSkills().getLevel(i);
        }
        if (total < reqTotal) {
            player.sendMessage("You must have a total level of 400 to drop items!");
            return false;
        }
        if (player.getPetManager().spawnPet(itemId, true))
            return false;
        player.getInventory().deleteItem(slotId, item);
        if (player.getCharges().degradeCompletly(item))
            return false;
        World.addGroundItem(item, new WorldTile(player), player, false, 180,
                true);
        player.getPackets().sendSound(2739, 0, 1);
        return false;
    }
}
