package com.rs.content.staff;

import com.rs.content.staff.actions.impl.BanAction;
import com.rs.content.staff.actions.impl.IPBanAction;
import com.rs.content.staff.actions.impl.JailAction;
import com.rs.content.staff.actions.impl.MuteAction;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class StaffInterface {


    private static boolean ipBan;
    private static boolean jail;
    private static boolean mute;
    private static boolean ban;

    private static Player target;

    public static void handleButtons(Player player, int componentId) {
        if (componentId != 2) {
            jail = false;
            mute = false;
            ban = false;
            ipBan = false;
        }
        if (componentId == 9 || componentId == 7) {
            jail = true;
        }
        if (componentId == 8 || componentId == 10) {
            mute = true;
        }
        if (componentId == 16 || componentId == 15) {
            ban = true;
        }
        if (componentId == 18 || componentId == 19) {
            ipBan = true;
        }
        if (componentId == 2) {
            if (jail) {
                StaffPanelHandler.put(player, JailAction.class);
            } else if (mute) {
                StaffPanelHandler.put(player, MuteAction.class);
            } else if (ban) {
                StaffPanelHandler.put(player, BanAction.class);
            } else if (ipBan) {
                StaffPanelHandler.put(player, IPBanAction.class);
            } else {
                return;
            }
            StaffPanelHandler.handle(player, target.getDisplayName());
        }
    }

    public static void sendInterface(Player p, Player target) {
        p.getPackets().sendIComponentText(31, 3, "<img=1> Admin Quick Panel");
        p.getPackets().sendIComponentText(31, 13, "Player: " + target.getDisplayName());
        p.getPackets().sendIComponentText(31, 14, "");
        p.getPackets().sendIComponentText(31, 9, "Jail");
        p.getPackets().sendIComponentText(31, 10, "Mute");
        p.getPackets().sendIComponentText(31, 16, "Ban");
        p.getPackets().sendIComponentText(31, 19, "IPBan");
        p.getPackets().sendIComponentText(31, 11, "");
        p.getPackets().sendIComponentText(31, 12, "");
        p.getPackets().sendIComponentText(31, 17, "");
        p.getPackets().sendIComponentText(31, 20, "");
        p.getPackets().sendIComponentText(31, 5, "Confirm");
        StaffInterface.target = target;
        p.getInterfaceManager().sendInterface(31);
    }

}
