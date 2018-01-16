package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;

public class PosCameraAction extends CutsceneAction {

	private final int moveLocalX;
	private final int moveLocalY;
	private final int moveZ;
	private final int speed;
	private final int speed2;

	public PosCameraAction(final int moveLocalX, final int moveLocalY,
			final int moveZ, final int speed, final int speed2,
			final int actionDelay) {
		super(-1, actionDelay);
		this.moveLocalX = moveLocalX;
		this.moveLocalY = moveLocalY;
		this.moveZ = moveZ;
		this.speed = speed;
		this.speed2 = speed2;
	}

	public PosCameraAction(final int moveLocalX, final int moveLocalY,
			final int moveZ, final int actionDelay) {
		this(moveLocalX, moveLocalY, moveZ, -1, -1, actionDelay);
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		player.getPackets().sendCameraPos(scene.getLocalX(player, moveLocalX),
				scene.getLocalY(player, moveLocalY), moveZ, speed, speed2);
	}

}
