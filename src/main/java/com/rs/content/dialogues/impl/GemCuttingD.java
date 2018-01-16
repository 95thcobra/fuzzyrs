package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.SkillsDialogue;
import com.rs.content.actions.skills.crafting.GemCutting;
import com.rs.content.dialogues.Dialogue;

public class GemCuttingD extends Dialogue {

    private GemCutting.Gem gem;

    @Override
    public void start() {
        this.gem = (GemCutting.Gem) parameters[0];
        SkillsDialogue
                .sendSkillsDialogue(
                        player,
                        SkillsDialogue.CUT,
                        "Choose how many you wish to cut,<br>then click on the item to begin.",
                        player.getInventory().getItems()
                                .getNumberOf(gem.getUncut()),
                        new int[]{gem.getUncut()}, null);

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        player.getActionManager().setAction(
                new GemCutting(gem, SkillsDialogue.getQuantity(player)));
        end();
    }

    @Override
    public void finish() {

    }

}
