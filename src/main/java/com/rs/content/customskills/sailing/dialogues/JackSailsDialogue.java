package com.rs.content.customskills.sailing.dialogues;

import com.rs.Server;
import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.DialogueExpressions;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.core.settings.SettingsManager;

/**
 * @author John (FuzzyAvacado) on 3/12/2016.
 */
public class JackSailsDialogue extends Dialogue {

    public static final int NPC_ID = 4551;
    private static final int SHOP_ID = 25;
    private static final String[] INITIAL_OPTIONS = {"I would like to buy a ship", "I would like to upgrade my ship", "I want to look at my ships attributes", "Can I make money from sailing?", "Nevermind"};

    @Override
    public void start() {
        sendNPCDialogue(NPC_ID, DialogueExpressions.CALM_TALK.getId(), "Eyy " + player.getDisplayName() + "! My name is Jack. Jack Sails. I am the best sailor in all of " + Server.getInstance().getSettingsManager().getSettings().getServerName() + ". What would ya like to know about sailing?");
        stage = 0;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 0:
                sendOptionsDialogue("Choose an option:", INITIAL_OPTIONS);
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), INITIAL_OPTIONS[0]);
                        stage = 2;
                        break;
                    case OPTION_2:
                    case OPTION_3:
                    case OPTION_4:
                    case OPTION_5:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), INITIAL_OPTIONS[componentId - 12]);
                        stage = (byte) (componentId - 10);
                        break;
                }
                break;
            case 2:
                ShopsManager.openShop(player, SHOP_ID);
                end();
                break;
            case 3:
                player.getDialogueManager().startDialogue(PreUpgradeShipDialogue.class);
                break;
            case 4:
                player.getDialogueManager().startDialogue(ShowAttributesDialogue.class);
                break;
            case 5:
                sendNPCDialogue(NPC_ID, DialogueExpressions.TALKING_ALOT.getId(), "Of course ya can mate! You can do jobs for traders to deliver cargo across the world of " + Server.getInstance().getSettingsManager().getSettings().getServerName() + ".");
                stage = 7;
                break;
            case 6:
                sendNPCDialogue(NPC_ID, DialogueExpressions.HAPPY_TALKING.getId(), "Alright mate. Thanks for saying hello " + player.getDisplayName() + ". Have a great day!");
                stage = 8;
                break;
            case 7:
                sendNPCDialogue(NPC_ID, DialogueExpressions.TALKING_ALOT.getId(), "Many of these ports have traders which buy and sell cargo and you can do jobs for them to deliver cargo to earn gold and sailing xp!");
                stage = 0;
                break;
            case 8:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
