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
        player.ChillBlastMining = false;
        player.IronFistSmithing = false;
        player.KarateFletching = false;
        player.SamuraiCooking = false;
    }
}
