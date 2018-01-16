package com.rs.game.randomevents.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.randomevents.RandomEvent;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class EvilBob extends RandomEvent {
	
	public enum Stages {
		LOADING, RUNNING, DESTROYING;
	}
	
	private int[] boundChunks;
	
	private Stages stage;
	
	public void loadEvilBobIsland() {
		stage = Stages.LOADING;
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(2, 2); 
				RegionBuilder.copyMap(423, 592, boundChunks[0], boundChunks[1], 10, 10, new int[1], new int[1]);
				player.stopAll();
				player.setNextWorldTile(getWorldTile(37, 39)); 
				player.setNextAnimation(new Animation(-1));
				player.setNextGraphics(new Graphics(74));
				World.spawnNPC(2481, getWorldTile(40, 40), -1, false);
				for (NPC npc : World.getNPCs()) {
					if (npc == null)
						continue;
					if (npc.getId() == 2478) {
						npc.faceEntity(player);
						player.faceEntity(npc);
						
					}
				}
				WorldTasksManager.schedule(new WorldTask()  {
					@Override
					public void run() {
						player.unlock(); 
						stage = Stages.RUNNING;
					}

				}, 1);
			}
		});
	}
	
	private int randomLevelNeeded;
	
	public boolean handleItemOnObject(Player player, WorldObject object, Item item) {
		if (item.getId() == 6200 || item.getId() == 6204 && object.getId() == 8985) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You can't uncook a raw fish!");
			return true;
		}
		if (item.getId() == 6202 && object.getId() == 8985) {
			randomLevelNeeded = Utils.random(1, player.getSkills().getLevel(Skills.COOKING) + Utils.random(16));
			if (player.getSkills().getLevel(Skills.COOKING) < randomLevelNeeded) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You need a fishing level of "+randomLevelNeeded+" to uncook a this raw fish.");
				return true;
			}
		}
		if (item.getId() == 6206 && object.getId() == 8985) {
			player.getPackets().sendGameMessage("You uncook the raw fish, like the servant told you, now you need to feed use it on bob and put him to sleep.");
			player.getInventory().deleteItem(6206, 1);
			player.getInventory().addItem(6200, 1);
			setCanFeedBob(true);
			return false;
		}
		return false;
	}
	
	private boolean feedBob = false;
	
	private boolean canFeedBob() {
		return feedBob;
	}
	
	private boolean setCanFeedBob(boolean bool) {
		return feedBob = bool;
	}
	
	public boolean handleItemOnNPC(Player player, NPC npc, Item item) {
		if (item.getId() != 6200) {
			if (!canFeedBob()) {
				player.getDialogueManager().startDialogue("You can't feed Bob this type of fish, you don't wan't to kill the poor cat!");
				return true;
			}
			player.getInventory().deleteItem(6200, 1);
			return false;
		}
		return false;
	}
	
	public void destroyMap() {
		if(stage != Stages.RUNNING) 
			return;
		stage = Stages.DESTROYING;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}
	
	public WorldTile getWorldTile(int mapX, int mapY) {
		 return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
	}
	
	@Override
	public void start() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setEventState(true);
					player.tilesBeforeEvent(player.getX(), player.getY(), player.getPlane());
					World.spawnNPC(2478, getPlayerTiles(), 0, false);
					for (NPC npc : World.getNPCs()) {
						if (npc == null) 
							continue;
						if (npc.getId() == 2478) {
							npc.setNextGraphics(new Graphics(74));
							player.faceEntity(npc);
							npc.faceEntity(player);
							npc.setNextForceTalk(new ForceTalk("Meow!"));
							player.setNextForceTalk(new ForceTalk("No... what? Nooooooooooooo!"));
						}
					}
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Welcome to ScapeRune.");
//					System.out.println("Spawned player @ "+player.getWorldTile()+".");
				} else if (loop == 2) {
					loadEvilBobIsland();
					for (NPC npc : World.getNPCs()) {
						if (npc == null)
							continue;
						if (npc.getId() == 2478) {
							npc.finish();
						}
					}
				}
				loop++;
			}
		}, 0, 1);
	}
		
	@Override
	public void finish() {
		destroyMap();
		player.getPackets().sendGameMessage("Welcome back to RuneScape.");
		
	}

}
