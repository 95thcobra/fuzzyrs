package com.rs.content.minigames.soulwars;

import com.rs.player.Player;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static com.rs.content.minigames.soulwars.SoulWarsManager.*;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class LobbyTask extends GameTask {

    private ArrayList<Player> players = new ArrayList<>(500);

    public LobbyTask(PlayerType type) {
        super(ExecutionType.FIXED_DELAY, 0, 1000 * (type.equals(PlayerType.OUTSIDE_LOBBY) ? 60 : type.equals(PlayerType.IN_GAME) ? 2 : 5), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
                Player player = it.next();
                if (player != null && player.getControllerManager().getController() instanceof LobbyController)
                    player.getControllerManager().sendInterfaces();
                else
                    it.remove();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> getPlayers(Teams team) {
        ArrayList<Player> members = new ArrayList<>(players.size());
        for (Player player : players) {
            if (player != null) {
                final int cape = player.getEquipment().getCapeId()
                        - TEAM_CAPE_INDEX;
                if (cape < 0 || cape > 1)
                    continue;
                if (Teams.values()[cape].equals(team))
                    members.add(player);
            }
        }
        return members;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
