package com.rs.core.net.handlers.object.impl;

import com.rs.content.dialogues.impl.ClimbNoEmoteStairs;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.player.Player;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class StaircaseHandler implements PacketHandler {
    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final WorldObject object = (WorldObject) parameters[0];
        final int optionId = (int) parameters[1];
        final String option = object.getDefinitions().getOption(optionId);
        if (option.equalsIgnoreCase("Climb-up")) {
            if (player.getPlane() == 3)
                return false;
            player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
                    player.getPlane() + 1), 0, 1);
        } else if (option.equalsIgnoreCase("Climb-down")) {
            if (player.getPlane() == 0)
                return false;
            player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
                    player.getPlane() - 1), 0, 1);
        } else if (option.equalsIgnoreCase("Climb")) {
            if (player.getPlane() == 3 || player.getPlane() == 0)
                return false;
            player.getDialogueManager().startDialogue(
                    ClimbNoEmoteStairs.class,
                    new WorldTile(player.getX(), player.getY(), player
                            .getPlane() + 1),
                    new WorldTile(player.getX(), player.getY(), player
                            .getPlane() - 1), "Go up the stairs.",
                    "Go down the stairs.");
        } else
            return false;
        return false;
    }
}
