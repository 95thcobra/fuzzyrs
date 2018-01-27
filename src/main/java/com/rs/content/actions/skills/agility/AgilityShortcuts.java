package com.rs.content.actions.skills.agility;

import com.rs.content.actions.skills.Skills;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * Agility shortcuts
 * 
 * @author FuzzyAvacado
 */
public class AgilityShortcuts {

	/**
	 * Perfect ge tunnel
	 * @param player
	 * @param objectX
	 * @param objectY
	 */
	public static void doGETunnel(Player player, final int objectX,
			final int objectY) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 21) {
			player.sendMessage("You have to have at least 21 agility to climb through this tunnel!");
			return;
		}
		WorldTasksManager.schedule(new WorldTask() {
			int timer;

			@Override
			public void run() {
				if (timer == 0) {
					player.lock();
					player.addWalkSteps(objectX == 3143 ? 3143 : 3139, 3516);
					player.setNextAnimation(new Animation(2589));
				} else if (timer == 1) {
					player.setNextAnimation(new Animation(2591));
					player.setNextWorldTile(new WorldTile(
							objectX == 3143 ? 3139 : 3143,
							objectX == 3143 ? 3516 : 3514, 0));
				} else if (timer == 2) {
					player.addWalkSteps(objectX == 3143 ? 3138 : 3144,
							objectX == 3143 ? 3516 : 3514);
				} else if (timer == 3) {
					player.unlock();
				}
				timer++;
			}

		}, 0, 0);
	}
}
