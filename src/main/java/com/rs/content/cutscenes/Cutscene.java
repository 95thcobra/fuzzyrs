package com.rs.content.cutscenes;

import com.rs.server.Server;
import com.rs.core.cores.CoresManager;
import com.rs.core.utils.Logger;
import com.rs.player.InterfaceManager;
import com.rs.player.Player;
import com.rs.content.cutscenes.actions.CutsceneAction;
import com.rs.world.RegionBuilder;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public abstract class Cutscene {

	private int stage;
	private Object[] cache;
	private CutsceneAction[] actions;
	private int delay;
	private boolean constructingRegion;
	private int[] currentMapData;
	private WorldTile endTile;

    public Cutscene() {

	}

	public static int getX(final Player player, final int x) {
		return new WorldTile(x, 0, 0).getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
	}

	public static int getY(final Player player, final int y) {
		return new WorldTile(0, y, 0).getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
	}

	public abstract boolean hiddenMinimap();

	public abstract CutsceneAction[] getActions(Player player);

	public final void stopCutscene(final Player player) {
		if (player.getX() != endTile.getX() || player.getY() != endTile.getY()
				|| player.getPlane() != endTile.getPlane()) {
			player.setNextWorldTile(endTile);
		}
		if (hiddenMinimap()) {
			player.getPackets().sendBlackOut(0); // unblack
		}
		player.getPackets().sendConfig(1241, 0);
		player.getPackets().sendResetCamera();
		player.setLargeSceneView(false);
		player.unlock();
		deleteCache();
		if (currentMapData != null) {
			CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
				@Override
				public void run() {
					try {
						if (currentMapData != null) {
							RegionBuilder.destroyMap(currentMapData[0],
									currentMapData[1], currentMapData[1],
									currentMapData[2]);
						}
					} catch (final Throwable e) {
						Logger.handle(e);
					}
				}
			});
		}
	}

	public final void startCutscene(final Player player) {
		if (hiddenMinimap()) {
			player.getPackets().sendBlackOut(2); // minimap
		}
		player.getPackets().sendConfig(1241, 1);
		player.setLargeSceneView(true);
		player.lock();
		player.stopAll(true, false);
	}

	public void constructArea(final Player player, final int baseChunkX,
			final int baseChunkY, final int widthChunks, final int heightChunks) {
		constructingRegion = true;
		player.getPackets().sendWindowsPane(56, 0);
		CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final int[] oldData = currentMapData;
					final int[] mapBaseChunks = RegionBuilder
							.findEmptyChunkBound(widthChunks, heightChunks);
					RegionBuilder.copyAllPlanesMap(baseChunkX, baseChunkY,
							mapBaseChunks[0], mapBaseChunks[1], widthChunks,
							heightChunks);
					currentMapData = new int[] { mapBaseChunks[0],
							mapBaseChunks[1], widthChunks, heightChunks };
					player.setNextWorldTile(new WorldTile(getBaseX()
							+ widthChunks * 4, +getBaseY() + heightChunks * 4,
							0));
					constructingRegion = false;
					if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
						Logger.info(this, "Bases: " + getBaseX() + ", "
								+ getBaseY());
					}
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {

							CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
								@Override
								public void run() {
									player.getPackets()
											.sendWindowsPane(
													player.getInterfaceManager()
															.hasRezizableScreen() ? InterfaceManager.RESIZABLE_WINDOW_ID
															: InterfaceManager.FIXED_WINDOW_ID,
													0);
									if (oldData != null) {
										RegionBuilder.destroyMap(oldData[0],
												oldData[1], oldData[1],
												oldData[2]);
									}
								}
							});
						}

					}, 1);
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}
		});
	}

	public int getLocalX(final Player player, final int x) {
		if (currentMapData == null)
			return x;
		return getX(player, getBaseX() + x);
	}

	public int getLocalY(final Player player, final int y) {
		if (currentMapData == null)
			return y;
		return getY(player, getBaseY() + y);
	}

	public int getBaseX() {
		return currentMapData == null ? 0 : currentMapData[0] << 3;
	}

	public int getBaseY() {
		return currentMapData == null ? 0 : currentMapData[1] << 3;
	}

	public final void logout(final Player player) {
		stopCutscene(player);
	}

	public final boolean process(final Player player) {
		if (delay > 0) {
			delay--;
			return true;
		}
		while (true) {
			if (constructingRegion)
				return true;
			if (stage == actions.length) {
				stopCutscene(player);
				return false;
			} else if (stage == 0) {
				startCutscene(player);
			}
			final CutsceneAction action = actions[stage++];
			action.process(player, cache);
			final int delay = action.getActionDelay();
			if (delay == -1) {
				continue;
			}
			this.delay = delay;
			return true;
		}
	}

	public void deleteCache() {
		for (final Object object : cache) {
			destroyCache(object);
		}
	}

	public void destroyCache(final Object object) {
		if (object instanceof NPC) {
			final NPC n = (NPC) object;
			n.finish();
		}
	}

	public final void createCache(final Player player) {
		actions = getActions(player);
		endTile = new WorldTile(player);
		int lastIndex = 0;
		for (final CutsceneAction action : actions) {
			if (action.getCachedObjectIndex() > lastIndex) {
				lastIndex = action.getCachedObjectIndex();
			}
		}
		cache = new Object[lastIndex + 1];
		cache[0] = this;
	}
}
