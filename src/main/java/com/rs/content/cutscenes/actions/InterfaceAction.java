package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * Handles an interface showing up cutscene action.
 * 
 * @author Emperor
 *
 */
public final class InterfaceAction extends CutsceneAction {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The delay.
	 */
	private final int delay;

	/**
	 * Constructs a new {@code InterfaceAction} {@code Object}.
	 * 
	 * @param interfaceId
	 *            The interface id.
	 * @param actionDelay
	 *            The action delay.
	 */
	public InterfaceAction(final int interfaceId, final int actionDelay) {
		super(-1, actionDelay);
		this.interfaceId = interfaceId;
		this.delay = actionDelay;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		player.getInterfaceManager().sendInterface(interfaceId);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInterfaceManager().closeScreenInterface();
			}
		}, delay);
	}

}