package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;
import com.rs.player.content.FriendChatsManager;
import com.rs.player.content.Magic;
import com.rs.world.ForceTalk;
import com.rs.world.WorldTile;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "dzone")
public class DiceZoneCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
                3028, 9678, 0));
        FriendChatsManager.joinChat("dicers", player);
        player.getPackets()
                .sendGameMessage(
                        "Join the 'Dicers' friends chat if you're going to gamble.");
        player.setNextForceTalk(new ForceTalk(
                "If you are going to gamble, don't spend it all!"));
    }
}
