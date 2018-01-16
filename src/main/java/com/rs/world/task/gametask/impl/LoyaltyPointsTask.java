package com.rs.world.task.gametask.impl;

import com.rs.content.player.points.PlayerPoints;
import com.rs.player.Player;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class LoyaltyPointsTask extends GameTask {

    private Player player;

    public LoyaltyPointsTask(Player player) {
        super(ExecutionType.FIXED_RATE, 30, 30, TimeUnit.MINUTES);
        this.player = player;
    }

    public void refreshLoyaltyText() {
        player.getPackets().sendIComponentText(551, 69, "Loyalty Points");
        player.getPackets().sendIComponentText(551, 70, "" + player.getPlayerPoints().getPoints(PlayerPoints.LOYALTY_POINTS) + "");
    }

    @Override
    public void run() {
        player.getPlayerPoints().addPoints(PlayerPoints.LOYALTY_POINTS, 250);
        player.getPackets().sendGameMessage("<col=008000>You have recieved 250 loyalty points for playing for 30 minutes!");
        player.getPackets().sendGameMessage("<col=008000>You now have " + player.getPlayerPoints().getPoints(PlayerPoints.LOYALTY_POINTS) + " Loyalty Points!");
        refreshLoyaltyText();
    }
}