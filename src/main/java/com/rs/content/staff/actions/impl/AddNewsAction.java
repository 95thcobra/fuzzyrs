package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.player.Player;
import com.rs.world.task.gametask.impl.ServerNewsTask;

import java.io.IOException;

/**
 * @author FuzzyAvacado
 */
public class AddNewsAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        try {
            ServerNewsTask.addNews(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
