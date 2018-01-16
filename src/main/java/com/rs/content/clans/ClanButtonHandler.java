package com.rs.content.clans;

import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class ClanButtonHandler {

    public static void handle(Player player, int interfaceId, int componentId, int slotId) {
        if (interfaceId == 1089) {
            if (componentId == 30)
                player.getTemporaryAttributtes().put("clanflagselection",
                        slotId);
            else if (componentId == 26) {
                Integer flag = (Integer) player.getTemporaryAttributtes()
                        .remove("clanflagselection");
                player.stopAll();
                if (flag != null)
                    ClansManager.setClanFlagInterface(player, flag);
            }
        } else if (interfaceId == 1096) {
            if (componentId == 41)
                ClansManager.viewClammateDetails(player, slotId);
            else if (componentId == 94)
                ClansManager.switchGuestsInChatCanEnterInterface(player);
            else if (componentId == 95)
                ClansManager.switchGuestsInChatCanTalkInterface(player);
            else if (componentId == 96)
                ClansManager.switchRecruitingInterface(player);
            else if (componentId == 97)
                ClansManager.switchClanTimeInterface(player);
            else if (componentId == 124)
                ClansManager.openClanMottifInterface(player);
            else if (componentId == 131)
                ClansManager.openClanMottoInterface(player);
            else if (componentId == 240)
                ClansManager.setTimeZoneInterface(player, -720 + slotId * 10);
            else if (componentId == 262)
                player.getTemporaryAttributtes().put("editclanmatejob", slotId);
            else if (componentId == 276)
                player.getTemporaryAttributtes()
                        .put("editclanmaterank", slotId);
            else if (componentId == 309)
                ClansManager.kickClanmate(player);
            else if (componentId == 318)
                ClansManager.saveClanmateDetails(player);
            else if (componentId == 290)
                ClansManager.setWorldIdInterface(player, slotId);
            else if (componentId == 297)
                ClansManager.openForumThreadInterface(player);
            else if (componentId == 346)
                ClansManager.openNationalFlagInterface(player);
            else if (componentId == 113)
                ClansManager.showClanSettingsClanMates(player);
            else if (componentId == 120)
                ClansManager.showClanSettingsSettings(player);
            else if (componentId == 386)
                ClansManager.showClanSettingsPermissions(player);
            else if (componentId >= 395 && componentId <= 475) {
                int selectedRank = (componentId - 395) / 8;
                if (selectedRank == 10)
                    selectedRank = 125;
                else if (selectedRank > 5)
                    selectedRank = 100 + selectedRank - 6;
                ClansManager.selectPermissionRank(player, selectedRank);
            } else if (componentId == 489)
                ClansManager.selectPermissionTab(player, 1);
            else if (componentId == 498)
                ClansManager.selectPermissionTab(player, 2);
            else if (componentId == 506)
                ClansManager.selectPermissionTab(player, 3);
            else if (componentId == 514)
                ClansManager.selectPermissionTab(player, 4);
            else if (componentId == 522)
                ClansManager.selectPermissionTab(player, 5);
        } else if (interfaceId == 1105) {
            if (componentId == 63 || componentId == 66)
                ClansManager.setClanMottifTextureInterface(player,
                        componentId == 66, slotId);
            else if (componentId == 189) {
                player.getPackets().sendHideIComponent(1105, 35, false);
                player.getPackets().sendHideIComponent(1105, 36, false);
                player.getPackets().sendHideIComponent(1105, 37, false);
                player.getPackets().sendHideIComponent(1105, 37, false);
                player.getPackets().sendHideIComponent(1105, 38, false);
                player.getPackets().sendHideIComponent(1105, 39, false);
                player.getPackets().sendHideIComponent(1105, 43, false);
                player.getPackets().sendHideIComponent(1105, 44, false);
                player.getPackets().sendHideIComponent(1105, 45, false);
            } else if (componentId == 177) {
                player.getPackets().sendHideIComponent(1105, 62, false);
                player.getPackets().sendHideIComponent(1105, 63, false);
                player.getPackets().sendHideIComponent(1105, 69, false);
            } else if (componentId == 35)
                ClansManager.openSetMottifColor(player, 0);
            else if (componentId == 80)
                ClansManager.openSetMottifColor(player, 1);
            else if (componentId == 92)
                ClansManager.openSetMottifColor(player, 2);
            else if (componentId == 104)
                ClansManager.openSetMottifColor(player, 3);// try
            else if (componentId == 120)
                player.stopAll();
        } else if (interfaceId == 1110) {
            if (componentId == 82)
                ClansManager.joinClanChatChannel(player);
            else if (componentId == 75)
                ClansManager.openClanDetails(player);
            else if (componentId == 78)
                ClansManager.openClanSettings(player);
            else if (componentId == 91)
                ClansManager.joinGuestClanChat(player);
            else if (componentId == 95)
                ClansManager.banPlayer(player);
            else if (componentId == 99)
                ClansManager.unbanPlayer(player);
            else if (componentId == 11)
                ClansManager.unbanPlayer(player, slotId);
            else if (componentId == 109)
                ClansManager.leaveClan(player);
        }
    }
}
