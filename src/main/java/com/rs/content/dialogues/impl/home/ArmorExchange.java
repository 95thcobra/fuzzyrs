package com.rs.content.dialogues.impl.home;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.DialogueExpressions;
import com.rs.content.economy.exchange.itemsets.ItemSets;

/**
 * @author John (FuzzyAvacado) on 3/5/2016.
 */
public class ArmorExchange extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, DialogueExpressions.HAPPY_TALKING.getId(), "Greetings. What can I do for you today?");
        stage = 0;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "I would like to exchange my sets for the items.",
                        "I would like to exchange my items for the sets.",
                        "Never mind.");
                stage = 1;
                break;
            case 1:
                if (componentId == OPTION_1) {
                    sendPlayerDialogue(DialogueExpressions.HAPPY_TALKING.getId(), "I would like to exchange my sets for the items.");
                    stage = 2;
                } else if (componentId == OPTION_2) {
                    sendPlayerDialogue(DialogueExpressions.HAPPY_TALKING.getId(), "I would like to exchange my items for the sets.");
                    stage = 3;
                } else if (componentId == OPTION_3) {
                    sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "Never mind.");
                    stage = 4;
                }
                break;
            case 2:
                player.getTemporaryAttributtes().put("armor_trade", true);
                sendNPCDialogue(npcId, DialogueExpressions.TALKING_ALOT.getId(), "No problem! All you have to do is right click on the sets you want to exchange in your inventory and choose \"Exchange\". When you are done exchanging sets, you can hit continue!");
                ItemSets.openSets(player);
                stage = 5;
                break;
            case 3:
                player.getTemporaryAttributtes().put("armor_trade", false);
                sendNPCDialogue(npcId, DialogueExpressions.TALKING_ALOT.getId(), "No problem! All you have to do is right click on the item you want to exchange in your inventory and choose \"Exchange\". When you are done exchanging sets, you can hit continue!");
                ItemSets.openSets(player);
                stage = 5;
                break;
            case 4:
                sendNPCDialogue(npcId, DialogueExpressions.HAPPY_TALKING.getId(), "Ok no problem. See you around " + player.getDisplayName() + "!");
                stage = 7;
                break;
            case 5:
                player.getTemporaryAttributtes().remove("armor_trade");
                player.getInterfaceManager().closeInventoryInterface();
                sendNPCDialogue(npcId, DialogueExpressions.HAPPY_TALKING.getId(), "Thank you for your business! Have a great day " + player.getDisplayName() + "!");
                stage = 7;
                break;
            case 7:
                end();
                break;

        }
    }

    @Override
    public void finish() {

    }
}
