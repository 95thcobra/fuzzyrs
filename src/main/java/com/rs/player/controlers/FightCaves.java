package com.rs.player.controlers;

import com.rs.Server;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.actions.skills.summoning.pet.Pets;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.core.cores.CoresManager;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.npc.fightcaves.FightCavesNPC;
import com.rs.world.npc.fightcaves.TzKekCaves;
import com.rs.world.npc.fightcaves.TzTok_Jad;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FightCaves extends Controller {

	public static final WorldTile OUTSIDE = new WorldTile(4610, 5130, 0);

	private static final int THHAAR_MEJ_JAL = 2617;

	private static final int[] MUSICS = { 1088, 1082, 1086 };
	private final int[][] WAVES = { { 2745 } };
	public boolean spawned;
	public int selectedMusic;
	private int[] boundChuncks;
	private Stages stage;
	private boolean logoutAtEnd;
	private boolean login;

	public static void enterFightCaves(final Player player) {
		if (player.getFamiliar() != null || player.getPet() != null
				|| Summoning.hasPouch(player) || Pets.hasPet(player)) {
			player.getDialogueManager()
					.startDialogue(SimpleNPCMessage.class, THHAAR_MEJ_JAL,
							"No Kimit-Zil in the pits! This is a fight for YOU, not your friends!");
			return;
		}
		player.getControllerManager().startController(FightCaves.class, 1); // start
																				// at
																				// wave
																				// 1
	}

	public void playMusic() {
		player.getMusicsManager().playMusic(selectedMusic);
	}

	@Override
	public void start() {
		loadCave(false);
	}

	@Override
	public boolean processButtonClick(final int interfaceId,
			final int componentId, final int slotId, final int packetId) {
		if (stage != Stages.RUNNING)
			return false;
		if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if (!logoutAtEnd) {
				logoutAtEnd = true;
				player.getPackets()
						.sendGameMessage(
								"<col=ff0000>You will be logged out automatically at the end of this wave.");
				player.getPackets()
						.sendGameMessage(
								"<col=ff0000>If you log out sooner, you will have to repeat this wave.");
			} else {
				player.forceLogout();
			}
			return false;
		}
		return true;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 9357) {
			if (stage != Stages.RUNNING)
				return false;
			exitCave(1);
			return false;
		}
		return true;
	}

	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean login() {
		loadCave(true);
		return false;
	}

	public void loadCave(final boolean login) {
		this.login = login;
		stage = Stages.LOADING;
		player.lock(); // locks player
		CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				// finds empty map bounds
				boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
				// copys real map into the empty map
				// 552 640
				RegionBuilder.copyAllPlanesMap(552, 640, boundChuncks[0],
						boundChuncks[1], 8);
				// selects a music
				selectedMusic = MUSICS[Utils.random(MUSICS.length)];
				player.setNextWorldTile(!login ? getWorldTile(46, 61)
						: getWorldTile(32, 32));
				// 1delay because player cant walk while teleing :p, + possible
				// issues avoid
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (!login) {
							final WorldTile walkTo = getWorldTile(32, 32);
							player.addWalkSteps(walkTo.getX(), walkTo.getY());
						}
						player.getDialogueManager()
								.startDialogue(SimpleNPCMessage.class,
										THHAAR_MEJ_JAL,
										"You're on your own now, JalYt.<br>Prepare to fight for your life!");
						player.setForceMultiArea(true);
						playMusic();
						player.unlock(); // unlocks player
						stage = Stages.RUNNING;
					}

				}, 1);
				if (!login) {
					/*
					 * lets stress less the worldthread, also fastexecutor used
					 * for mini stuff
					 */
					GameTaskManager.scheduleTask(new StartWaveTask(GameTask.ExecutionType.SCHEDULE, 6000, 0, TimeUnit.MILLISECONDS));
				}
			}
		});
	}

	public WorldTile getSpawnTile() {
		switch (Utils.random(5)) {
		case 0:
			return getWorldTile(11, 16);
		case 1:
			return getWorldTile(51, 25);
		case 2:
			return getWorldTile(10, 50);
		case 3:
			return getWorldTile(46, 49);
		case 4:
		default:
			return getWorldTile(32, 30);
		}
	}

	@Override
	public void moved() {
		if (stage != Stages.RUNNING || !login)
			return;
		login = false;
		setWaveEvent();
	}

	public void startWave() {
		final int currentWave = getCurrentWave();
		if (currentWave > WAVES.length) {
			win();
			return;
		}
		player.getInterfaceManager()
				.sendTab(
						player.getInterfaceManager().hasRezizableScreen() ? 11
								: 0, 316);
		player.getPackets().sendConfig(639, currentWave);
		if (stage != Stages.RUNNING)
			return;
		for (final int id : WAVES[currentWave - 1]) {
			if (id == 2736) {
				new TzKekCaves(id, getSpawnTile());
			} else if (id == 2745) {
				new TzTok_Jad(id, getSpawnTile(), this);
			} else {
				new FightCavesNPC(id, getSpawnTile());
			}
		}
		spawned = true;
	}

	public void spawnHealers() {
		if (stage != Stages.RUNNING)
			return;
		for (int i = 0; i < 4; i++) {
			new FightCavesNPC(2746, getSpawnTile());
		}
	}

	public void win() {
		if (stage != Stages.RUNNING)
			return;
		exitCave(4);
	}

	public void nextWave() {
		playMusic();
		setCurrentWave(getCurrentWave() + 1);
		if (logoutAtEnd) {
			player.forceLogout();
			return;
		}
		setWaveEvent();
	}

	public void setWaveEvent() {
		if (getCurrentWave() == 63) {
			player.getDialogueManager().startDialogue(SimpleNPCMessage.class,
					THHAAR_MEJ_JAL, "Look out, here comes TzTok-Jad!");
		}
		GameTaskManager.scheduleTask(new StartWaveTask(GameTask.ExecutionType.SCHEDULE, 600, 0, TimeUnit.MILLISECONDS));
	}

	@Override
	public void process() {
		if (spawned) {
			final List<Integer> npcs = World.getRegion(player.getRegionId())
					.getNPCsIndexes();
			if (npcs == null || npcs.isEmpty()) {
				spawned = false;
				nextWave();
			}
		}
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"You have been defeated!");
				} else if (loop == 3) {
					player.reset();
					exitCave(1);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void magicTeleported(final int type) {
		exitCave(2);
	}

	/*
	 * logout or not. if didnt logout means lost, 0 logout, 1, normal, 2 tele
	 */
	public void exitCave(final int type) {
		stage = Stages.DESTROYING;
		final WorldTile outside = new WorldTile(OUTSIDE, 2); // radomizes alil
		if (type == 0 || type == 2) {
			player.setLocation(outside);
		} else {
			player.setForceMultiArea(false);
			player.getPackets().closeInterface(
					player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
			if (type == 1 || type == 4) {
				player.setNextWorldTile(outside);
				if (type == 4) {
					for (final Player players : World.getPlayers()) {
						if (players == null) {
							continue;
						}
						players.getPackets().sendGameMessage(
								"<img=6><col=ff8c38>News: "
										+ player.getDisplayName()
										+ " Just completed Fightcaves.");
					}
					player.setCompletedFightCaves();
					player.reset();
					player.getDialogueManager()
							.startDialogue(
									SimpleNPCMessage.class,
									THHAAR_MEJ_JAL,
									"You defeated the Tz Tok-Jad, I am most impressed! Please accept this gift as a reward.");
					player.getPackets()
							.sendGameMessage("You were victorious!!");
					if (!player.getInventory().addItem(6570, 1)) {
						World.addGroundItem(new Item(6570, 1), new WorldTile(
								player), player, true, 180, true);
						World.addGroundItem(new Item(6529, 16064),
								new WorldTile(player), player, true, 180, true);
					} else if (!player.getInventory().addItem(6529, 16064)) {
						World.addGroundItem(new Item(6529, 16064),
								new WorldTile(player), player, true, 180, true);
					}
				} else if (getCurrentWave() == 1) {
					player.getDialogueManager()
							.startDialogue(SimpleNPCMessage.class, THHAAR_MEJ_JAL,
									"Well I suppose you tried... better luck next time.");
				} else {
					int tokkul = getCurrentWave() * 8032 / WAVES.length;
					tokkul *= Server.getInstance().getSettingsManager().getSettings().getDropRate(); // 10x more
					if (!player.getInventory().addItem(6529, tokkul)) {
						World.addGroundItem(new Item(6529, tokkul),
								new WorldTile(player), player, true, 180, true);
					}
					player.getDialogueManager()
							.startDialogue(SimpleNPCMessage.class, THHAAR_MEJ_JAL,
									"Well done in the cave, here, take TokKul as reward.");
					// TODO tokens
				}
			}
			removeControler();
		}
		/*
		 * 1200 delay because of leaving
		 */
		CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder
						.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

	/*
	 * gets worldtile inside the map
	 */
	public WorldTile getWorldTile(final int mapX, final int mapY) {
		return new WorldTile(boundChuncks[0] * 8 + mapX, boundChuncks[1] * 8
				+ mapY, 0);
	}

	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean logout() {
		/*
		 * only can happen if dungeon is loading and system update happens
		 */
		if (stage != Stages.RUNNING)
			return false;
		exitCave(0);
		return false;

	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processObjectTeleport(final WorldTile toTile) {
		return false;
	}

	public int getCurrentWave() {
		if (getArguments() == null || getArguments().length == 0)
			return 0;
		return (Integer) getArguments()[0];
	}

	public void setCurrentWave(final int wave) {
		if (getArguments() == null || getArguments().length == 0) {
			this.setArguments(new Object[1]);
		}
		getArguments()[0] = wave;
	}

	@Override
	public void forceClose() {
		/*
		 * shouldnt happen
		 */
		if (stage != Stages.RUNNING)
			return;
		exitCave(2);
	}

	private enum Stages {
		LOADING, RUNNING, DESTROYING
	}

	@GameTaskType(GameTaskManager.GameTaskType.FAST)
	private class StartWaveTask extends GameTask {

		public StartWaveTask(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
			super(executionType, initialDelay, tick, timeUnit);
		}

		@Override
		public void run() {
			if (stage != Stages.RUNNING)
				return;
			startWave();
		}
	}
}
