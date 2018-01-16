package com.rs.core.net.handlers.inventory;

import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.SkillCapeCustomizer;
import com.rs.world.item.Item;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionFourHandler implements PacketHandler {
    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        final int itemId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final long time = Utils.currentTimeMillis();
        if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
            return true;
        player.stopAll(false);
        if (itemId == 20708) {
            SkillCapeCustomizer.startCustomizing(player, itemId);
        }
        return false;
    }
}
