package com.rs.content.commands.impl.owner;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.item.Item;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "copy", rank = PlayerRank.OWNER)
public class CopyGearCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String username = "";
        for (int i = 1; i < cmd.length; i++) {
            username = cmd[i] + ((i == cmd.length - 1) ? "" : " ");
        }
        final Player p2 = World.getPlayerByDisplayName(username);
        if (p2 == null) {
            return;
        }
        final Item[] items = p2.getEquipment().getItems()
                .getItemsCopy();
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            player.getEquipment().getItems().set(i, items[i]);
            player.getEquipment().refresh(i);
        }
        player.getAppearance().generateAppearenceData();
    }
}
