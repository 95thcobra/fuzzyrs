package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.DisplayNameAction;
import com.rs.player.content.PlayerLook;
import com.rs.player.content.TicketSystem;

public class Noticeboard extends Dialogue {

    public Noticeboard() {
    }

    @Override
    public void start() {
        stage = 1;
        sendOptionsDialogue("What you would like to do?",
                "View my character settings", "Leave Noticeboard");

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            if (componentId == OPTION_1) {
                sendOptionsDialogue("Character Settings",
                        "I want to change my display name (50M).",
                        "I want back my original display name.",
                        "I would like to open my bank.",
                        "I want to send a help ticket to adminstrator.",
                        "Next page");
                stage = 2;
            } else if (componentId == OPTION_2) {
                player.getInterfaceManager().closeChatBoxInterface();
                player.getPackets().sendGameMessage(
                        "You have left the noticeboard.");
            }
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                DisplayNameAction.ProcessChange(player);
            } else if (componentId == OPTION_2) {
                DisplayNameAction.RemoveDisplay(player);
            } else if (componentId == OPTION_3) {
                player.getBank().openBank();
                player.getInterfaceManager().closeChatBoxInterface();
            } else if (componentId == OPTION_4) {
                TicketSystem.requestTicket(player);
                player.getInterfaceManager().closeChatBoxInterface();
                player.getPackets().sendGameMessage(
                        "You have sent a ticket to the staff, please wait.");
            } else if (componentId == OPTION_5) {
                sendOptionsDialogue("Character Settings",
                        "I would like to change my hair.",
                        "I would like to change my clothes.",
                        "I'd like to change my skin colour or gender.",
                        "Close Noticeboard.");
                stage = 3;
            }
        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                PlayerLook.openHairdresserSalon(player);
                player.getPackets().sendGameMessage(
                        "Make sure you don't wear ANY items!");
                player.getInterfaceManager().closeChatBoxInterface();
            } else if (componentId == OPTION_2) {
                player.getPackets().sendGameMessage(
                        "Make sure you don't wear ANY items!");
                PlayerLook.openThessaliasMakeOver(player);
                player.getInterfaceManager().closeChatBoxInterface();
            } else if (componentId == OPTION_3) {
                player.getPackets().sendGameMessage(
                        "Make sure you don't wear ANY items!");
                PlayerLook.openMageMakeOver(player);
                player.getInterfaceManager().closeChatBoxInterface();
            } else if (componentId == OPTION_4) {
                player.getPackets().sendGameMessage(
                        "You have closed the noticeboard.");
            }
        }

    }

    @Override
    public void finish() {
    }

}