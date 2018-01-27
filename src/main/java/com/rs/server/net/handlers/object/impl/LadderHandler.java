package com.rs.server.net.handlers.object.impl;

import com.rs.content.dialogues.impl.ClimbEmoteStairs;
import com.rs.server.net.handlers.PacketHandler;
import com.rs.player.Player;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class LadderHandler implements PacketHandler {
    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final WorldObject object = (WorldObject) parameters[0];
        final int optionId = (int) parameters[1];
        final String option = object.getDefinitions().getOption(optionId);
        if (option.equalsIgnoreCase("Climb-up")) {
            if (player.getPlane() == 3)
                return false;
            player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
        } else if (option.equalsIgnoreCase("Climb-down")) {
            if (player.getPlane() == 0)
                return false;
            player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
        } else if (option.equalsIgnoreCase("Climb")) {
            if (player.getPlane() == 3 || player.getPlane() == 0)
                return false;
            player.getDialogueManager().startDialogue(ClimbEmoteStairs.class, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1),
                    new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), "Climb up the ladder.", "Climb down the ladder.", 828);
        } else
            return false;
        return true;
    }
}
