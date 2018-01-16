package com.rs.content.actions.impl;

import com.rs.content.actions.Action;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

public class PlayerFollow extends Action {

	private final Player target;

	public PlayerFollow(final Player target) {
		this.target = target;
	}

	@Override
	public boolean start(final Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player))
			return true;
		player.setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll(final Player player) {
		if (player.isDead() || player.hasFinished() || target.isDead()
				|| target.hasFinished())
			return false;
		final int distanceX = player.getX() - target.getX();
		final int distanceY = player.getY() - target.getY();
		final int size = target.getSize();
		int maxDistance = 16;
		if (player.getPlane() != target.getPlane()
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return false;
		if (player.getFreezeDelay() >= Utils.currentTimeMillis())
			return true;
		maxDistance = 0;
		if ((!player.clipedProjectile(target, true))
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			if (player.hasWalkSteps()) {
				player.resetWalkSteps();
			}
			if (target.getDirection() == 0) {//north
				player.addWalkStepsInteract(target.getX(), target.getY() + 1,
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 2048) {//north-east
				player.addWalkStepsInteract(target.getX() + 1, target.getY() + 1,
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 4096) {//east
				player.addWalkStepsInteract(target.getX() + 1, target.getY(),
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 6144) {//south-east
				player.addWalkStepsInteract(target.getX() + 1, target.getY() - 1,
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 8192) {//south
				player.addWalkStepsInteract(target.getX(), target.getY() - 1,
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 10240) {//south-west
				player.addWalkStepsInteract(target.getX() - 1, target.getY() - 1,
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 12288) {//west
				player.addWalkStepsInteract(target.getX() - 1, target.getY(),
						target.getRun() ? 2 : 1, size, true);
			}
			if (target.getDirection() == 14336) {//north-west
				player.addWalkStepsInteract(target.getX() - 1, target.getY() + 1,
						target.getRun() ? 2 : 1, size, true);
			}
			return true;
		} else {
			player.resetWalkSteps();
			int lastFaceEntity = target.getLastFaceEntity();
			if (lastFaceEntity >= 32768) {
				lastFaceEntity -= 32768;
				if (lastFaceEntity == player.getIndex()) {
					player.addWalkSteps(target.getLastWorldTile().getX(),
							target.getLastWorldTile().getY(), size, true);
				}
			}
		}

		return true;
	}

	@Override
	public boolean process(final Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.setNextFaceEntity(null);
	}

}
