package com.rs.core.net.handlers.inventory;

import com.rs.content.dialogues.impl.DiceBag;
import com.rs.content.dialogues.impl.FlamingSkull;
import com.rs.content.items.KitManager;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.utils.Utils;
import com.rs.player.Equipment;
import com.rs.player.Player;
import com.rs.player.content.Magic;
import com.rs.player.content.SkillCapeCustomizer;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionThreeHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        final int itemId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final long time = Utils.currentTimeMillis();
        if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
            return true;
        player.stopAll(false);
        if (player.getToolBelt().addItem(item)) {
            return true;
        }
        if (KitManager.KitsMade.kitsMade.containsKey(itemId)) {
            KitManager.splitItem(player, itemId);
        } else if (itemId == 20767 || itemId == 20769 || itemId == 20771
                || itemId == 20708) {
            SkillCapeCustomizer.startCustomizing(player, itemId);
        } else if (itemId >= 15084 && itemId <= 15100) {
            player.getDialogueManager().startDialogue(DiceBag.class, itemId);
        } else if (itemId == 15707) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3449, 3708, 0));
        } else if (itemId == 24437 || itemId == 24439 || itemId == 24440
                || itemId == 24441) {
            player.getDialogueManager().startDialogue(FlamingSkull.class, item,
                    slotId);
        } else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA) {
            player.getAuraManager().sendTimeRemaining(itemId);
        }
        return false;
    }
}
