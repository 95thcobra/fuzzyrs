package com.rs.content.commands.impl.donate;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.Hit;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "edison", donateRank = PlayerRank.DonateRank.EXTREME_DONATOR)
public class EdisonSuicideCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (!player.canSpawn()) {
            player.getPackets().sendGameMessage("<col=ff0000>You can't suicide while you're in this area.</col>");
            return;
        }
        player.setNextAnimation(new Animation(17317));
        player.setNextGraphics(new Graphics(3311));
        player.setNextGraphics(new Graphics(3310));
        player.setNextGraphics(new Graphics(3309));
        player.setNextForceTalk(new ForceTalk(
                "Hey look, I'm Thomas Edison and I invented lightning!"));
        player.applyHit(new Hit(player, player.getHitpoints(),
                Hit.HitLook.CRITICAL_DAMAGE));
    }
}
