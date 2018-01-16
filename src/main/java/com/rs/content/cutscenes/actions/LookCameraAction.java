package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;

public class LookCameraAction extends CutsceneAction {

	private final int viewLocalX;
	private final int viewLocalY;
	private final int viewZ;
	private final int speed;
	private final int speed2;

	public LookCameraAction(final int viewLocalX, final int viewLocalY,
			final int viewZ, final int speed, final int speed2,
			final int actionDelay) {
		super(-1, actionDelay);
		this.viewLocalX = viewLocalX;
		this.viewLocalY = viewLocalY;
		this.viewZ = viewZ;
		this.speed = speed;
		this.speed2 = speed2;
	}

	public LookCameraAction(final int viewLocalX, final int viewLocalY,
			final int viewZ, final int actionDelay) {
		this(viewLocalX, viewLocalY, viewZ, -1, -1, actionDelay);
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		player.getPackets().sendCameraLook(scene.getLocalX(player, viewLocalX),
				scene.getLocalY(player, viewLocalY), viewZ, speed, speed2);
	}

}
