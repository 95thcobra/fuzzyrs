package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.dialogues.impl.EmptyInventory;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "empty")
public class EmptyInventoryCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getDialogueManager().startDialogue(EmptyInventory.class);
    }
}
