package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class ItemMessage extends Dialogue {

    @Override
    public void start() {
        sendEntityDialogue(SEND_1_TEXT_CHAT, new String[]{"",
                (String) parameters[0]}, IS_ITEM, (Integer) parameters[1], 1);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        end();
    }

    @Override
    public void finish() {

    }

}
