package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class sendURL extends Dialogue {
    @Override
    public void start() {
        sendDialogue("A Staff Moderator has requested you visit this link.",
                "(" + parameters[0] + ")");

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        final boolean opened = false;
        switch (stage) {
            case -1:
                stage = 0;
                sendOptionsDialogue("Accept this link?", "Accept the link.",
                        "Reject the link.");
                break;
            case 0:
                if (componentId == OPTION_1) {
                    player.getPackets().sendOpenURL((String) parameters[0]);
                    end();
                } else if (componentId == OPTION_2) {
                    end();
                }
                if (!opened)
                    return;
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