package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "shop", rank = PlayerRank.ADMIN)
public class OpenShopCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        ShopsManager.openShop(player, Integer.parseInt(cmd[1]));
    }
}
