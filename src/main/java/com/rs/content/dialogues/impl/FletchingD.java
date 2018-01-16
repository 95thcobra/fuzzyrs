package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.SkillsDialogue;
import com.rs.content.actions.skills.SkillsDialogue.ItemNameFilter;
import com.rs.content.actions.skills.fletching.Fletching;
import com.rs.content.actions.skills.fletching.Fletching.Fletch;
import com.rs.content.dialogues.Dialogue;

public class FletchingD extends Dialogue {

    private Fletch items;

    // componentId, amount, option

    @Override
    public void start() {
        items = (Fletch) parameters[0];
        final boolean maxQuantityTen = Fletching.maxMakeQuantityTen(items);
        SkillsDialogue
                .sendSkillsDialogue(
                        player,
                        maxQuantityTen ? SkillsDialogue.MAKE_NO_ALL_NO_CUSTOM
                                : SkillsDialogue.MAKE,
                        "Choose how many you wish to make,<br>then click on the item to begin.",
                        maxQuantityTen ? 10 : 28, items.getProduct(),
                        maxQuantityTen ? null : (ItemNameFilter) name -> name.replace(" (u)", ""));
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        final int option = SkillsDialogue.getItemSlot(componentId);
        if (option > items.getProduct().length) {
            end();
            return;
        }
        int quantity = SkillsDialogue.getQuantity(player);
        final int invQuantity = player.getInventory().getItems()
                .getNumberOf(items.getId());
        if (quantity > invQuantity) {
            quantity = invQuantity;
        }
        player.getActionManager().setAction(
                new Fletching(items, option, quantity));
        end();
    }

    @Override
    public void finish() {
    }

}
