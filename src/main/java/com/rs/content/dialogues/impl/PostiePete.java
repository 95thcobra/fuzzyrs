package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;

public class PostiePete extends Dialogue {

    public int ID1 = 4565;
    public int Amount1 = 1;
    public int ID2 = 10734;
    public int Amount2 = 1;
    private int npcId;

    @Override
    public void start() {
        sendEntityDialogue(SEND_2_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello, have you found the Easter eggs of jake?"},
                IS_NPC, npcId, 9713);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Do you have Jake's eggs?", "Yes I do",
                    "No I haven't");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getInventory().containsItem(12638, 1))
                    if (player.getInventory().containsItem(12639, 1))
                        if (player.getInventory().containsItem(12640, 1)) {
                            player.getInventory().deleteItem(12638, 1);
                            player.getInventory().deleteItem(12639, 1);
                            player.getInventory().deleteItem(12640, 1);
                            player.getInventory().addItem(4565, 1);
                            player.getInventory().addItem(10734, 1);
                            player.getAppearance().setTitle(107);
                            end();
                        } else {

                            player.sendMessage("Talk to the Easter Bunny for the hints of the eggs.");
                            end();
                        }
            } else if (componentId == OPTION_2) {
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}