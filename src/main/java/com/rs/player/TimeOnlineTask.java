package com.rs.player;

import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class TimeOnlineTask extends GameTask {

	/*
	 * 
	 * Represents Time Spent Online handler, each 60 minutes adds +1 hour to
	 * your time online.
	 */

	private Player player;

	public TimeOnlineTask(final Player player) {
		super(ExecutionType.FIXED_RATE, 1, 1, TimeUnit.MINUTES);
		this.player = player;
	}

	@Override
	public void run() {
		player.time++;
	}
}