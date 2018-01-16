package com.rs.content.dialogues.types;

import com.rs.content.dialogues.Dialogue;

public class SimpleMessage extends Dialogue {

    @Override
    public void start() {
        final String[] messages = new String[parameters.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = (String) parameters[i];
        }
        sendDialogue(messages);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        end();
    }

    @Override
    public void finish() {

    }

}
