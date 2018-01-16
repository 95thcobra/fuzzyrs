package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.points.PlayerPoints;
import com.rs.player.content.LividFarm;

public class Niles extends Dialogue {

    int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827, "Hello, " + player.getDisplayName()
                        + ". I can exchange your Livid Farm Points.",
                "You have currently: " + player.getPlayerPoints().getPoints(PlayerPoints.LIVID_POINTS) + ".");
    }

    /*
     * (non-Javadoc)
     *
     * @see Dialogue#run(int, int)
     */
    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                stage = 0;
                sendPlayerDialogue(9827,
                        "Alright, what can I exchange then with my Livid Farm Points?");
                break;
            case 0:
                sendOptionsDialogue(
                        "Livid Points Exchange",
                        "Magical Orb - Livid Plant boosting, XP Focusion options. (2,800)",
                        "Citharede (2,500)", "Coming soon.", "Coming soon.",
                        "Nevermind");
                stage = 2;
                break;
            case 2:
                if (stage == 2) {
                    if (componentId == OPTION_1) {
                        LividFarm.OrbPayment(player);
                    } else if (componentId == OPTION_2) {
                        LividFarm.HighLanderSet(player);
                    } else if (componentId == OPTION_3) {
                        player.getInterfaceManager().closeChatBoxInterface();
                    } else if (componentId == OPTION_4) {
                        player.getInterfaceManager().closeChatBoxInterface();
                    } else if (componentId == OPTION_4) {
                        player.getInterfaceManager().closeChatBoxInterface();
                    }
                }
                break;
            case 3:
                end();
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }

}
