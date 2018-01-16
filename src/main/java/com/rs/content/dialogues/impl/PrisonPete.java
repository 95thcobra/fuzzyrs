package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class PrisonPete extends Dialogue {

    public int ID1 = 4565;
    public int Amount1 = 1;
    public int ID2 = 10734;
    public int Amount2 = 1;
    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "If you can get me a lockpick, I might be able to help you.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Prison Pete might return the favor",
                    "Here you go", "Sorry I don't got one");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getInventory().containsItem(1523, 1)) {
                    player.getInventory().deleteItem(1523, 1);
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            3281, 3029, 0));
                    end();
                } else {

                    player.sendMessage("You should try to get a lockpick.");
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            3304, 3123, 0));
                    end();
                }
            } else if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3304,
                        3123, 0));
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}