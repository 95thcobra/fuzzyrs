package com.rs.core.net.handlers.inventory;

import com.rs.content.actions.skills.firemaking.Firemaking;
import com.rs.content.actions.skills.runecrafting.Runecrafting;
import com.rs.content.minigames.duel.DuelController;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.net.handlers.button.ButtonHandler;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.Dicing;
import com.rs.world.item.Item;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionTwoHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        final int itemId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final long time = Utils.currentTimeMillis();
        if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time) {
            return true;
        }
        if (itemId == 7927 || itemId == 6583 || itemId == 22560 && !DuelController.isAtDuelArena(player) && !player.canSpawn()) {
            int npcId = -1;
            player.stopAll();
            player.lock();
            player.getInterfaceManager().sendInventoryInterface(375);
            player.getTemporaryAttributtes().put("RingNPC", Boolean.TRUE);
            switch (itemId) {
                case 7927:
                    npcId = 3692;
                    break;
                case 6583:
                case 22560:
                    npcId = 2626;
                    break;
            }
            player.getAppearance().transformIntoNPC(npcId);
        } else if (Firemaking.isFiremaking(player, itemId)) {
            return true;
        } else if (itemId >= 5509 && itemId <= 5514) {
            int pouch = -1;
            if (itemId == 5509) {
                pouch = 0;
            }
            if (itemId == 5510) {
                pouch = 1;
            }
            if (itemId == 5512) {
                pouch = 2;
            }
            if (itemId == 5514) {
                pouch = 3;
            }
            Runecrafting.emptyPouch(player, pouch);
            player.stopAll(false);
        } else if (itemId >= 15086 && itemId <= 15100) {
            Dicing.handleRoll(player, itemId, true);
        } else if (ButtonHandler.sendWear(player, slotId, item.getId())) {
            ButtonHandler.refreshEquipBonuses(player);
        }
        return false;
    }
}
