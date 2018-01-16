package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "god", rank = PlayerRank.OWNER)
public class GodModeCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.setHitpoints(Short.MAX_VALUE);
        player.getEquipment().setEquipmentHpIncrease(
                Short.MAX_VALUE - 990);
        for (int i = 0; i < 10; i++) {
            player.getCombatDefinitions().getBonuses()[i] = 100000;
        }
        for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++) {
            player.getCombatDefinitions().getBonuses()[i] = 100000;
        }
    }
}
