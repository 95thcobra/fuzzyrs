package com.rs.content.dialogues;

import com.rs.player.Player;

public class DialogueManager {

    private final Player player;
    private Dialogue lastDialogue;

    public DialogueManager(final Player player) {
        this.player = player;
    }

    public void startDialogue(final Class<? extends Dialogue> c, final Object... parameters) {
        try {
            startDialogue(c.newInstance(), parameters);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void startDialogue(final Dialogue dialogue, final Object... parameters) {
        if (!player.getControllerManager().useDialogueScript(dialogue.getClass().getSimpleName()))
            return;
        if (lastDialogue != null) {
            lastDialogue.finish();
        }
        lastDialogue = dialogue;
        lastDialogue.parameters = parameters;
        lastDialogue.setPlayer(player);
        lastDialogue.start();
    }

    public void continueDialogue(final int interfaceId, final int componentId) {
        if (lastDialogue == null)
            return;
        lastDialogue.run(interfaceId, componentId);
    }

    public void finishDialogue() {
        if (lastDialogue == null)
            return;
        lastDialogue.finish();
        lastDialogue = null;
        if (player.getInterfaceManager().containsChatBoxInter()) {
            player.getInterfaceManager().closeChatBoxInterface();
        }
    }

}
