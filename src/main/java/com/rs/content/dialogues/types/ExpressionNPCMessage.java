package com.rs.content.dialogues.types;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.DialogueExpressions;

/**
 * @author John (FuzzyAvacado) on 3/16/2016.
 */
public class ExpressionNPCMessage extends Dialogue {

    @Override
    public void start() {
        int npcId = (Integer) parameters[0];
        DialogueExpressions expressions = (DialogueExpressions) parameters[1];
        final String[] messages = new String[parameters.length - 2];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = (String) parameters[i + 2];
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
