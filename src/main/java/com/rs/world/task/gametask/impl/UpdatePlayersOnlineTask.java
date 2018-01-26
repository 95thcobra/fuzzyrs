package com.rs.world.task.gametask.impl;

import com.rs.Server;
import com.rs.core.settings.SettingsManager;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class UpdatePlayersOnlineTask extends GameTask {

    public UpdatePlayersOnlineTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 2, 2, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        try {
            URL url = new URL(Server.getInstance().getSettingsManager().getSettings().getPlayersOnlineLink());
            url.openStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
