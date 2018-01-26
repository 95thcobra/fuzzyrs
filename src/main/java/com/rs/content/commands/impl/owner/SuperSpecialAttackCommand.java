package com.rs.content.commands.impl.owner;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "superspec", rank = PlayerRank.OWNER)
public class SuperSpecialAttackCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getCombatDefinitions().resetSpecialAttack();
    }
}
