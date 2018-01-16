package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.SkillsDialogue;
import com.rs.content.actions.skills.crafting.FlaxCrafting;
import com.rs.content.dialogues.Dialogue;

public class FlaxCraftingD extends Dialogue {

    /**
     * If you have more than one flax, this dialogue pops up
     *
     * @author BongoProd
     */

    private FlaxCrafting.Orb orb;

    @Override
    public void start() {
        this.orb = (FlaxCrafting.Orb) parameters[0];
        SkillsDialogue
                .sendSkillsDialogue(
                        player,
                        SkillsDialogue.MAKE,
                        "Choose how many you wish to make,<br>then click on the item to begin.",
                        player.getInventory().getItems()
                                .getNumberOf(orb.getUnMade()),
                        new int[]{orb.getUnMade()}, null);

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        player.getActionManager().setAction(
                new FlaxCrafting(orb, SkillsDialogue.getQuantity(player)));

        end();
    }

    @Override
    public void finish() {

    }

}
