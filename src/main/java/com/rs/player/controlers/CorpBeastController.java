package com.rs.player.controlers;

import com.rs.Server;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public class CorpBeastController extends Controller {

	@Override
	public void start() {

	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 37929 || object.getId() == 38811) {
			removeControler();
			player.stopAll();
			player.setNextWorldTile(new WorldTile(2974, 4384, player.getPlane()));
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(final int type) {
		removeControler();
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you are dead!");
				} else if (loop == 3) {
					final Player killer = player
							.getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						killer.removeDamage(player);
						killer.increaseKillCount(player);
					}
					player.sendItemsOnDeath(player);
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setNextWorldTile(new WorldTile(
							Server.getInstance().getSettingsManager().getSettings().getRespawnPlayerLocation()));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					removeControler();
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

}
