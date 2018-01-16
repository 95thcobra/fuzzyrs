package com.rs.content.staff;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.staff.actions.impl.*;

/**
 * @author FuzzyAvacado
 */
public class StaffPanelDialogue extends Dialogue {

    @Override
    public void start() {
        player.getTemporaryAttributtes().put("using_cp", Boolean.TRUE);
        sendOptionsDialogue("Staff Control Panel", "Punishment", "Forgiveness",
                "Teleport/Spawn", "Server", "Player");
        stage = 1;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 1:
                if (componentId == OPTION_1) {
                    sendOptionsDialogue("Punishment", "Ban", "Mute",
                            "Jail", "IPBan", "Back");
                    stage = 2;
                } else if (componentId == OPTION_2) {
                    sendOptionsDialogue("Forgiveness", "UnBan", "UnMute",
                            "UnJail", "UnIpBan", "Back");
                    stage = 3;
                } else if (componentId == OPTION_3) {
                    sendOptionsDialogue("Teleport/Spawn", "Spawn Npc", "Spawn Object", "Teleport to",
                            "Teleport to me", "Back");
                    stage = 4;
                } else if (componentId == OPTION_4) {
                    sendOptionsDialogue("Server", "Add news", "Initialize Shops",
                            "Initialize Server Settings", "Server Message", "Back");
                    stage = 5;
                } else if (componentId == OPTION_5) {
                    sendOptionsDialogue("Player", "Get Info", "Change display name",
                            "Check Bank", "Check Inventory", "Next Page");
                    stage = 6;
                }
                break;
            case 2:
                if (componentId == OPTION_1) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, BanAction.class);
                    end();
                } else if (componentId == OPTION_2) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, MuteAction.class);
                    end();
                } else if (componentId == OPTION_3) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, JailAction.class);
                    end();
                } else if (componentId == OPTION_4) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, IPBanAction.class);
                    end();
                } else if (componentId == OPTION_5) {
                    end();
                    player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
                }
                break;
            case 3:
                if (componentId == OPTION_1) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, UnBanAction.class);
                    end();
                } else if (componentId == OPTION_2) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, UnMuteAction.class);
                    end();
                } else if (componentId == OPTION_3) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, UnJailAction.class);
                    end();
                } else if (componentId == OPTION_4) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, UnIPBanAction.class);
                    end();
                } else if (componentId == OPTION_5) {
                    end();
                    player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
                }
                break;
            case 4:
                if (componentId == OPTION_1) {
                    player.getPackets().sendInputIntegerScript(true, "Enter id: ");
                    StaffPanelHandler.put(player, SpawnNpcAction.class);
                    end();
                } else if (componentId == OPTION_2) {
                    player.getPackets().sendInputIntegerScript(true, "Enter id: ");
                    StaffPanelHandler.put(player, SpawnObjectAction.class);
                    end();
                } else if (componentId == OPTION_3) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, TeleportToAction.class);
                    end();
                } else if (componentId == OPTION_4) {
                    player.getPackets().sendInputNameScript("Enter Player name: ");
                    StaffPanelHandler.put(player, TeleportToMeAction.class);
                    end();
                } else if (componentId == OPTION_5) {
                    end();
                    player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
                }
                break;
            case 5:
                if (componentId == OPTION_1) {
                    player.getPackets().sendInputLongTextScript("Enter news line to add: ");
                    StaffPanelHandler.put(player, AddNewsAction.class);
                    end();
                } else if (componentId == OPTION_2) {
                    StaffPanelHandler.put(player, InitializeShops.class);
                    StaffPanelHandler.handle(player, "");
                    end();
                } else if (componentId == OPTION_3) {
                    StaffPanelHandler.put(player, InitializeServerSettings.class);
                    StaffPanelHandler.handle(player, "");
                    end();
                } else if (componentId == OPTION_4) {
                    player.getPackets().sendInputLongTextScript("Enter server message: ");
                    StaffPanelHandler.put(player, ServerMessageAction.class);
                    end();
                } else if (componentId == OPTION_5) {
                    end();
                    player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
                }
                break;
            case 6:
                if (componentId == OPTION_1) {
                    player.getPackets().sendInputNameScript("Enter player name: ");
                    StaffPanelHandler.put(player, GetInfoAction.class);
                    end();
                } else if (componentId == OPTION_2) {
                    player.getPackets().sendInputNameScript("Enter player name and new display name seperated by colon (ex: scrublord:cooldude23) ");
                    StaffPanelHandler.put(player, ChangeDisplayName.class);
                    end();
                } else if (componentId == OPTION_3) {
                    player.getPackets().sendInputNameScript("Enter player name: ");
                    StaffPanelHandler.put(player, CheckBankAction.class);
                    end();
                } else if (componentId == OPTION_4) {
                    player.getPackets().sendInputNameScript("Enter player name: ");
                    StaffPanelHandler.put(player, CheckInventoryAction.class);
                    end();
                } else if (componentId == OPTION_5) {
                    sendOptionsDialogue("Player", "Empty Bank", "Empty Inventory", "Rank Management", "Back");
                    stage = 7;
                }
                break;
            case 7:
                if (componentId == OPTION_1) {
                    player.getPackets().sendInputNameScript("Enter player name: ");
                    StaffPanelHandler.put(player, EmptyBankAction.class);
                    end();
                } else if (componentId == OPTION_2) {
                    player.getPackets().sendInputNameScript("Enter player name: ");
                    StaffPanelHandler.put(player, EmptyInventoryAction.class);
                    end();
                } else if (componentId == OPTION_3) {
                    player.getPackets().sendInputNameScript("Enter player name: ");
                    StaffPanelHandler.put(player, RankManagementAction.class);
                    end();
                } else if (componentId == OPTION_4) {
                    end();
                    player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
                }
                break;
        }
    }

    @Override
    public void finish() {

    }
}
