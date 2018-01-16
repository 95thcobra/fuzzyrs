package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.SkillsDialogue;
import com.rs.content.actions.skills.crafting.LeatherCrafting;
import com.rs.content.actions.skills.crafting.LeatherCrafting.LeatherData;
import com.rs.content.dialogues.Dialogue;

public class LeatherCraftingD extends Dialogue {

    @Override
    public void start() {
        final int[] items = new int[parameters.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = ((LeatherData) parameters[i]).getFinalProduct();
        }

        SkillsDialogue
                .sendSkillsDialogue(
                        player,
                        SkillsDialogue.MAKE,
                        "Choose how many you wish to make,<br>then click on the item to begin.",
                        28, items, null);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        final int option = SkillsDialogue.getItemSlot(componentId);
        if (option > parameters.length) {
            end();
            return;
        }
        final LeatherData data = (LeatherData) parameters[option];
        int quantity = SkillsDialogue.getQuantity(player);
        final int invQuantity = player.getInventory().getItems()
                .getNumberOf(data.getLeatherId());
        if (quantity > invQuantity) {
            quantity = invQuantity;
        }
        player.getActionManager()
                .setAction(new LeatherCrafting(data, quantity));
        end();
    }

    @Override
    public void finish() {

    }

}
