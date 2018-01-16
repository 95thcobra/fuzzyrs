package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.SkillsDialogue;
import com.rs.content.actions.skills.cooking.Cooking;
import com.rs.content.dialogues.Dialogue;
import com.rs.world.WorldObject;

public class CookingD extends Dialogue {

    private Cooking.Cookables cooking;
    private WorldObject object;

    @Override
    public void start() {
        this.cooking = (Cooking.Cookables) parameters[0];
        this.object = (WorldObject) parameters[1];

        SkillsDialogue
                .sendSkillsDialogue(
                        player,
                        SkillsDialogue.COOK,
                        "Choose how many you wish to cook,<br>then click on the item to begin.",
                        player.getInventory().getItems()
                                .getNumberOf(cooking.getRawItem()),
                        new int[]{cooking.getProduct().getId()}, null);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        player.getActionManager().setAction(
                new Cooking(object, cooking.getRawItem(), SkillsDialogue
                        .getQuantity(player)));
        end();
    }

    @Override
    public void finish() {

    }

}
