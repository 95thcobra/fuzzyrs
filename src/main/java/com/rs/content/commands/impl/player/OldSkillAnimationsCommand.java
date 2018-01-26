package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "oldskillanim")
public class OldSkillAnimationsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.setChillBlastMining(false);
        player.setIronFistSmithing(false);
        player.setKarateFletching(false);
        player.setSamuraiCooking(false);
    }
}
