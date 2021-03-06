package com.rs.server.net.handlers.inventory;

import com.rs.server.net.handlers.PacketHandler;
import com.rs.player.Player;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionEightHandler implements PacketHandler {
    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        player.getInventory().sendExamine(slotId);
        return false;
    }
}
