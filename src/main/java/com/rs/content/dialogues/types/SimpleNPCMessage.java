package com.rs.content.dialogues.types;

import com.rs.content.dialogues.Dialogue;

public class SimpleNPCMessage extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        final String[] messages = new String[parameters.length - 1];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = (String) parameters[i + 1];
        }
        sendNPCDialogue(npcId, 9827, messages);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        end();
    }

    @Override
    public void finish() {

    }

}
