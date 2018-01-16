package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.SkillsDialogue;
import com.rs.content.actions.skills.firemaking.Bonfire;
import com.rs.content.dialogues.Dialogue;
import com.rs.world.WorldObject;

public class BonfireD extends Dialogue {

    private Bonfire.Log[] logs;
    private WorldObject object;

    @Override
    public void start() {
        this.logs = (Bonfire.Log[]) parameters[0];
        this.object = (WorldObject) parameters[1];
        final int[] ids = new int[logs.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = logs[i].getLogId();
        }
        SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT,
                "Which logs do you want to add to the bonfire?", -1, ids, null,
                false);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        final int slot = SkillsDialogue.getItemSlot(componentId);
        if (slot >= logs.length || slot < 0)
            return;
        player.getActionManager().setAction(new Bonfire(logs[slot], object));
        end();
    }

    @Override
    public void finish() {

    }

}
