package com.rs.content.minigames.soulwars;

import com.rs.player.Player;
import com.rs.world.World;
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
public class AreaTask extends GameTask {

    private ArrayList<Player> players = new ArrayList<>(500);

    public AreaTask(PlayerType type) {
        super(ExecutionType.FIXED_DELAY, 0, 1000 * (type.equals(PlayerType.OUTSIDE_LOBBY) ? 60 : type.equals(PlayerType.IN_GAME) ? 2 : 5), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            if (World.getSoulWars().decrementMinute()) {
                for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
                    Player player = it.next();
                    if (player != null
                            && !player.hasFinished()
                            && player.getControllerManager().getController() instanceof SoulWarsAreaController)
                        player.getControllerManager().sendInterfaces();
                    else
                        it.remove();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
