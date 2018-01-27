package com.rs.content.minigames;

import com.rs.content.actions.skills.Skills;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.controlers.Controller;
import com.rs.server.Server;
import com.rs.world.Animation;
import com.rs.world.ForceMovement;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.gametask.GameTask;
import com.rs.task.gametask.GameTaskManager;
import com.rs.task.gametask.GameTaskType;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class BrimhavenAgility extends Controller {

	private static final List<Player> players = new ArrayList<Player>();
	private static PlayingGame currentGame;
	//private static BladesManager bladesManager;

	private static void removePlayer(final Player player) {
		synchronized (players) {
			players.remove(player);
			if (player.getSize() == 0) {
				cancelGame();
			}
		}
		player.getHintIconsManager().removeUnsavedHintIcon();
		if (player.getTemporaryAttributtes().remove("BrimhavenAgility") != null) {
			player.getPackets().sendConfigByFile(4456, 0);
		}
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
	}

	private static void startGame() {
		// starts at 0 so that it selects a taggedDispenser
		Server.getInstance().getGameTaskManager().scheduleTask(
				currentGame = new PlayingGame(GameTask.ExecutionType.FIXED_RATE, 0, 60, TimeUnit.SECONDS));
		/*GameTaskManager.scheduleTask(
				bladesManager = new BladesManager(GameTask.ExecutionType.FIXED_RATE, 5000, 5000, TimeUnit.MILLISECONDS)); // TODO right
																	// time atm
																	// they move
																	// each
																	// 5seconds*/
	}

	private static void cancelGame() {
		currentGame.cancel(true);
		//bladesManager.cancel(true);
		PlayingGame.taggedDispenser = null;
		currentGame = null;
		//bladesManager = null;
	}

	private void addPlayer(final Player player) {
		synchronized (players) {
			players.add(player);
			if (players.size() == 1) {
				startGame();
			} else {
				PlayingGame.addIcon(player);
			}
		}
		sendInterfaces();
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 3581 || object.getId() == 3608) {
			if (PlayingGame.taggedDispenser == null
					|| PlayingGame.taggedDispenser.getTileHash() != object
							.getTileHash())
				return false;
			final Integer stage = (Integer) player.getTemporaryAttributtes()
					.get("BrimhavenAgility");
			if (stage == null) {
				player.getTemporaryAttributtes().put("BrimhavenAgility", 0); // clicked
				player.getPackets().sendConfigByFile(4456, 1); // ready to get
																// tickets
				player.getPackets()
						.sendGameMessage(
								"You get tickets by tagging more than one pillar in a row. Tag the next pillar!");
			} else if (stage == 0) {
				player.getPackets()
						.sendGameMessage(
								"You have already tagged this pillar, wait until the arrow moves again.");
			} else {
				if (!player.getInventory().hasFreeSlots()
						&& !player.getInventory().containsOneItem(2996)) {
					player.getPackets().sendGameMessage(
							"Not enough space in your inventory.");
					return false;
				}
				player.getTemporaryAttributtes().put("BrimhavenAgility", 0); // clicked
				player.getInventory().addItem(2996, 1);
			}
			return false;
		} else if (object.getId() == 3583) {
			final int rotationY = object.getY() == 9559 ? -1 : 1;
			player.lock();
			player.setNextFaceWorldTile(new WorldTile(player.getX(), player
					.getY() - 1, 3));
			player.setNextAnimation(new Animation(1121));
			WorldTasksManager.schedule(new WorldTask() {
				int index = 0;

				@Override
				public void run() {
					if (index++ >= 7) {
						player.unlock();
						player.getSkills().addXp(Skills.AGILITY, 13.5);
						player.setNextAnimation(new Animation(-1));
						this.stop();
						return;
					}
					player.setNextAnimation(new Animation(1122));
					final WorldTile tile = new WorldTile(player.getX(), player
							.getY() - rotationY, player.getPlane());
					player.setNextForceMovement(new ForceMovement(tile, 1, 1));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.setNextWorldTile(new WorldTile(tile));
							this.stop();
						}
					}, 0, 1);
				}
			}, 1, 1);
			return false;
		} else if (object.getId() == 3553) {
			player.getAppearance().setRenderEmote(155);
			final WorldTile tile = new WorldTile(player.getX(), player.getY()
					- object.getRotation(), player.getPlane());
			player.setNextForceMovement(new ForceMovement(tile, 1, 1));
			return false;
		} else if (object.getId() == 3551) {
			player.getAppearance().setRenderEmote(155);
			WorldTasksManager.schedule(new WorldTask() {
				int index = 0;

				@Override
				public void run() {
					if (index++ >= 7) {
						player.unlock();
						player.getSkills().addXp(Skills.AGILITY, 5);
						player.getAppearance().setRenderEmote(-1);
						this.stop();
						return;
					}
					final WorldTile tile = new WorldTile(player.getX(), player
							.getY() - object.getRotation(), player.getPlane());
					player.addWalkStep(tile.getX(), tile.getY(), player.getX(),
							player.getY(), false);
				}
			}, 0, 0);
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		addPlayer(player);
	}

	@Override
	public boolean logout() {
		removePlayer(player);
		return false;
	}

	@Override
	public boolean login() {
		addPlayer(player);
		return false; // so doesnt remove script
	}

	@Override
	public void magicTeleported(final int type) {
		removePlayer(player);
		removeControler();
	}

	@Override
	public void forceClose() {
		removePlayer(player);
	}

	@Override
	public boolean sendDeath() {
		removePlayer(player);
		removeControler();
		return true;
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 5);
	}

	private static class BladesManager extends GameTask {

		public BladesManager(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
			super(executionType, initialDelay, tick, timeUnit);
		}

		@Override
		public void run() {
			/*
			 * for (Player target : players) { for (WorldObject object:
			 * World.getRegion(target.getRegionId()).getObjects()) { if
			 * (object.getMessageIcon() == 3569 || object.getMessageIcon() == 3568) {
			 * target.getPackets().sendObjectAnimation(object, new
			 * Animation(1)); } } }
			 */
		}
	}

    @GameTaskType(GameTaskManager.GameTaskType.FAST)
	private static class PlayingGame extends GameTask {

		private static WorldTile taggedDispenser;

		public PlayingGame(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
			super(executionType, initialDelay, tick, timeUnit);
		}

		private static WorldTile getNextDispenser() {
			while (true) {
				final WorldTile tile = new WorldTile(
						2761 + 11 * Utils.random(5),
						9546 + 11 * Utils.random(5), 3);
				if (!(tile.getX() == 2805 && tile.getY() == 9590)
						&& !(taggedDispenser != null && tile
						.equals(taggedDispenser)))
					return tile;
			}
		}

		private static void addIcon(final Player player) {
			final Integer stage = (Integer) player.getTemporaryAttributtes()
					.get("BrimhavenAgility");
			if (stage != null)
				if (stage == -1) {
					player.getTemporaryAttributtes().remove("BrimhavenAgility"); // didnt
					// click
					player.getPackets().sendConfigByFile(4456, 0);
				} else {
					player.getTemporaryAttributtes()
							.put("BrimhavenAgility", -1); // clicked
				}
			if (taggedDispenser == null)
				return;
			player.getHintIconsManager().addHintIcon(taggedDispenser.getX(),
					taggedDispenser.getY(), taggedDispenser.getPlane(), 65, 2,
					0, -1, false);
		}

		@Override
		public void run() { // selects dispenser
			taggedDispenser = getNextDispenser();
			synchronized (players) {
				players.forEach(BrimhavenAgility.PlayingGame::addIcon);
			}
		}

	}
}
