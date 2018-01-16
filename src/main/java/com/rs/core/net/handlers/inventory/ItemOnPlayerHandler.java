package com.rs.core.net.handlers.inventory;

import com.rs.content.christmas.cracker.ChristmasCrackerDialogue;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOnPlayerHandler implements PacketHandler {
    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final Player usedOn = (Player) parameters[0];
        final int itemId = (int) parameters[1];
        player.setCoordsEvent(new CoordsEvent(usedOn, () -> {
            player.faceEntity(usedOn);
            if (usedOn.getInterfaceManager().containsScreenInter()) {
                player.sendMessage(usedOn.getDisplayName() + " is busy.");
                return;
            }
            switch (itemId) {
                case 962:// Christmas cracker
                    if (player.getInventory().getFreeSlots() < 3
                            || usedOn.getInventory().getFreeSlots() < 3) {
                        player.sendMessage((player.getInventory()
                                .getFreeSlots() < 3 ? "You do"
                                : "The other player does")
                                + " not have enough inventory space to open this cracker.");
                        return;
                    }
                    player.getDialogueManager().startDialogue(ChristmasCrackerDialogue.class, usedOn, itemId);
                    break;
                default:
                    player.sendMessage("Nothing interesting happens.");
                    break;
            }
        }, usedOn.getSize()));
        return false;
    }
}
