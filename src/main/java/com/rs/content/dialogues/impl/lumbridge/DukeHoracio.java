package com.rs.content.dialogues.impl.lumbridge;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.types.SimpleMessage;

/**
 * @author Kethsi/Tom
 *         <p>
 *         Handles DukeHoracio Dialogue, for the Anti-dragon shield.
 */
public class DukeHoracio extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827, "Greetings. Welcome to my castle.");
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "I seek a shield that will protect me from dragonbreath.",
                        "Nevermind.");
                stage = 0;
                break;
            case 0:
                if (componentId == OPTION_1) {
                    sendPlayerDialogue(9827, "I seek a shield that will protect me from dragonbreath.");
                    stage = 2;
                } else if (componentId == OPTION_2) {
                    end();
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9827, "A knight going on a dragon quest, hmm? What dragon do you intend to slay?");
                stage = 3;
                break;
            case 3:
            /*if (player.getQuestManager().getQuestStage(Quests.DRAGON_SLAYER)) { //todo
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"I seek a shield that will protect me from dragonbreath.",
					"Nevermind.");
			} else {*/
                sendPlayerDialogue(9827, "Oh, no dragon in particular. I just feel like killing a dragon.");
                //}
                stage = 4;
                break;
            case 4:
                //sendNPCDialogue(npcId, 9827, "Of course. Now you've slain Elvarg, you've earned the right to call the shield your own!");
                sendNPCDialogue(npcId, 9827, "Of course, why not.");
                stage = 5;
                break;
            case 5:
                if (player.getInventory().containsItem(1540, 1)) {
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "Seems like you already have a Anti-dragon shield.");
                    stage = 6;
                    return;
                }
                if (!player.getInventory().hasFreeSlots()) {
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "You don't have enough inventory slots.");
                    stage = 6;
                    return;
                }
                sendEntityDialogue(IS_ITEM, 1540, 1, "The Duke hands you the shield.");
                player.getInventory().addItem(1540, 1);
                stage = 6;
                break;
            case 6:
                end();
                break;
        }
    }

    public void finish() {
    }
}
