package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "item", rank = PlayerRank.ADMIN)
public class SpawnItemCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        try {
            final int itemId = Integer.valueOf(cmd[1]);
            final ItemDefinitions defs = ItemDefinitions
                    .getItemDefinitions(itemId);
            if (defs.isLended())
                return;
            player.getInventory().addItem(itemId,
                    cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
        } catch (final NumberFormatException e) {
            player.getPackets().sendGameMessage(
                    "Use: ::item id (optional:amount)");
        }
    }
}
