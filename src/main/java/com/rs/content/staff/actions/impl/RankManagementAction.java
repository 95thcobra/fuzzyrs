package com.rs.content.staff.actions.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.StaffPanelDialogue;
import com.rs.content.staff.actions.StaffAction;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.world.World;

import java.util.Optional;

/**
 * @author FuzzyAvacado
 */
public class RankManagementAction extends Dialogue implements StaffAction {

    private PlayerRank[] playerRanks;
    private Player target;

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target == null) {
            Optional<Player> targetOptional = Server.getInstance().getPlayerFileManager().load(value);
            if (targetOptional.isPresent()) {
                target = targetOptional.get();
                target.setUsername(Utils
                        .formatPlayerNameForProtocol(value));
            }
        }
        if (target == null) {
            player.sendMessage("Could not find player by name!");
            return;
        }
        this.target = target;
        playerRanks = PlayerRank.values();
        player.getDialogueManager().startDialogue(this);
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }

    @Override
    public void start() {
        sendOptionsDialogue("Rank Management", playerRanks[0].toString(), playerRanks[1].toString(),
                playerRanks[2].toString(), playerRanks[3].toString(), "Next");
        stage = 1;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 1:
                if (componentId == OPTION_1) {
                    setTargetRank(playerRanks[0]);
                } else if (componentId == OPTION_2) {
                    setTargetRank(playerRanks[1]);
                } else if (componentId == OPTION_3) {
                    setTargetRank(playerRanks[2]);
                } else if (componentId == OPTION_4) {
                    setTargetRank(playerRanks[3]);
                } else if (componentId == OPTION_5) {
                    sendOptionsDialogue("Rank Management", playerRanks[4].toString(), playerRanks[5].toString(),
                            playerRanks[6].toString(), playerRanks[7].toString(), "Back");
                    stage = 2;
                }
                break;
            case 2:
                if (componentId == OPTION_1) {
                    setTargetRank(playerRanks[4]);
                } else if (componentId == OPTION_2) {
                    setTargetRank(playerRanks[5]);
                } else if (componentId == OPTION_3) {
                    setTargetRank(playerRanks[6]);
                } else if (componentId == OPTION_4) {
                    setTargetRank(playerRanks[7]);
                } else if (componentId == OPTION_5) {
                    end();
                    player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
                }
                break;
        }
    }

    @Override
    public void finish() {

    }

    private void setTargetRank(PlayerRank playerRank) {
        target.setRank(playerRank);
        target.logout(false);
        end();
        player.sendMessage("You have promoted " + target.getDisplayName() + " to " + playerRank.name() + ".");
    }
}
