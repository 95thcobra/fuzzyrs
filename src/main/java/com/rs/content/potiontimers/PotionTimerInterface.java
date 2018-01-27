package com.rs.content.potiontimers;

import com.rs.player.Player;
import com.rs.server.Server;

/**
 * @author John (FuzzyAvacado) on 12/9/2015.
 */
public class PotionTimerInterface {

    public static final int POTION_TIMER_INTERFACE_ID = 3000;

    public static void sendTimerInterface(Player player) {
        player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, POTION_TIMER_INTERFACE_ID);
        for (PotionType t : PotionType.values()) {
            player.getPackets().sendIComponentText(POTION_TIMER_INTERFACE_ID, t.getTextId(), "");
        }
    }

    public static void startTimerTask(Player player, PotionType type) {
        Server.getInstance().getGameTaskManager().scheduleTask(new PotionTimerTask(player, type));
    }
}
