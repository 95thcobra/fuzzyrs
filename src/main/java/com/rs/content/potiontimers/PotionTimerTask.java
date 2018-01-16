package com.rs.content.potiontimers;

import com.rs.player.Player;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author John (FuzzyAvacado) on 12/10/2015.
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class PotionTimerTask extends GameTask {

    private Player player;
    private PotionType potionType;
    private int minutes, seconds;

    public PotionTimerTask(Player player, PotionType potionType) {
        super(ExecutionType.FIXED_DELAY, 1, 1, TimeUnit.SECONDS);
        this.player = player;
        this.potionType = potionType;
        this.minutes = potionType.getMins();
        this.seconds = potionType.getSeconds();
        player.getPackets().sendHideIComponent(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, potionType.getImageId(), false);
    }

    @Override
    public void run() {
        if (minutes >= 0 && seconds >= 0) {
            player.getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, potionType.getTextId(), "" + getTime() + "");
        } else {
            hideInterface();
            this.cancel(true);
        }
    }

    public void hideInterface() {
        player.getPackets().sendHideIComponent(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, potionType.getImageId(), true);
        player.getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, potionType.getTextId(), "");
    }

    public String getTime() {
        if (seconds <= 0 && minutes > 0) {
            minutes--;
            seconds = 60;
        }
        seconds--;
        return minutes + "m " + seconds + "s";
    }
}
