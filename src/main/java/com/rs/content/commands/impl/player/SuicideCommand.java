package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.Hit;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "suicide")
public class SuicideCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (!player.canSpawn()) {
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>You can't suicide while you're in this area.</col>");
            return;
        }
        player.setNextForceTalk(new ForceTalk("BOOOOOM!"));
        player.setNextGraphics(new Graphics(2140));
        player.setNextGraphics(new Graphics(608));
        player.applyHit(new Hit(player, player.getHitpoints(), Hit.HitLook.CRITICAL_DAMAGE));
    }
}
