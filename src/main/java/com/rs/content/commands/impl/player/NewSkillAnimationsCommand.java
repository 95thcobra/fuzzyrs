package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "newskillanim")
public class NewSkillAnimationsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.ChillBlastMining = true;
        player.IronFistSmithing = true;
        player.KarateFletching = true;
        player.SamuraiCooking = true;
    }
}
