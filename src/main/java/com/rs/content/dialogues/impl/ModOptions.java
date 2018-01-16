package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.PlayerRank;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;
import com.rs.player.content.TicketSystem;
import com.rs.world.World;
import com.rs.world.WorldTile;

public class ModOptions extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Moderator Options", "Start Meeting",
                    "End Meeting", "Summon P-Mods", "Accept Ticket",
                    "Close Ticket");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                for (final Player staff : World.getPlayers()) {
                    if (staff.getRank() == PlayerRank.PLAYER) {
                        continue;
                    }
                    staff.setNextWorldTile(new WorldTile(2848, 5151, 0));
                    staff.getPackets().sendGameMessage(
                            "You been teleported for a staff meeting by "
                                    + player.getDisplayName());
                }
            }
            if (componentId == OPTION_2) {
                for (final Player staff : World.getPlayers()) {
                    if (staff.getRank() == PlayerRank.PLAYER) {
                        continue;
                    }
                    staff.setNextWorldTile(new WorldTile(
                            SettingsManager.getSettings().RESPAWN_PLAYER_LOCATION));
                    staff.getPackets().sendGameMessage(
                            "You been teleported for a staff meeting by "
                                    + player.getDisplayName());
                }
            }
            if (componentId == OPTION_3) {
                for (final Player staff : World.getPlayers()) {
                    if (staff.getRank() == PlayerRank.PLAYER) {
                        continue;
                    }
                    staff.setNextWorldTile(new WorldTile(2848, 5151, 0));
                    staff.getPackets().sendGameMessage(
                            "You been teleported for a staff meeting by "
                                    + player.getDisplayName());
                }
            }
            if (componentId == OPTION_4) {
                TicketSystem.answerTicket(player);
            }
            end();
            if (componentId == OPTION_5) {
                TicketSystem.removeTicket(player);
            }
            end();
        }
    }

    @Override
    public void finish() {

    }

}