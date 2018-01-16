package com.rs.core.net.handlers.inventory;

import com.rs.core.net.handlers.PacketHandler;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.pet.Pet;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOnNpcHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final NPC npc = (NPC) parameters[0];
        final Item item = (Item) parameters[1];
        if (item == null)
            return true;
        player.setCoordsEvent(new CoordsEvent(npc, () -> {
            if (!player.getInventory().containsItem(item.getId(),
                    item.getAmount()))
                return;
            if (npc instanceof Pet) {
                player.faceEntity(npc);
                player.getPetManager().eat(item.getId(), (Pet) npc);
            }
        }, npc.getSize()));
        return false;
    }
}
