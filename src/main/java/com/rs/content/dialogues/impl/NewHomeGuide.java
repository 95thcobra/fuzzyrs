package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.player.controlers.NewHomeController;

public class NewHomeGuide extends Dialogue {

    private NewHomeController controler;
    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        controler = (NewHomeController) parameters[1];
        final int s = controler.getStage();
        if (s == 0) {
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Greetings! I see you are a new arrival in this land. My",
                            "job is welcome all new visitors. So Welcome!"},
                    IS_NPC, npcId, 9827);
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Anyways, you might be wondering, who made this server.",
                            "The devlopers are currently, Dragonkk, Cjay0091, Sonicforce41, and Cjay0091"},
                    IS_NPC, npcId, 9827);
        }
    }

    @Override
    public void finish() {

    }

}
