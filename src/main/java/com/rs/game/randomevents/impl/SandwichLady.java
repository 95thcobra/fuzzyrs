package com.rs.game.randomevents.impl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.randomevents.RandomEvent;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * 
 * @author Josh'
 *
 */
public class SandwichLady extends RandomEvent {

	private int sandwichId;
	
	private String sandwich;
	
	public static final String CORRECT_SANDWICH = "You've chosen the correct sandwich, you can use the portal at the side of the room to return to your previous location.";
	public static final String INCORRECT_SANDWICH = "You've chosen the incorrect sandwich, get out of my sight.";
	
	private void generate() {
		Random random = new Random();
		int n = random.nextInt(7);
		sandwichId = n;
	}
	
	private String getSandwich() {
		switch (sandwichId) {
		case 1:
			return sandwich = "baguette";
		case 2:
			return sandwich = "triangle sandwich";
		case 3:
			return sandwich = "square sandwich";
		case 4:
			return sandwich = "bread roll";
		case 5:
			return sandwich = "meat pie";
		case 6: 
			return sandwich = "doughnut";
		case 7: 
			return sandwich = "chocolate bar";
		}
		return sandwich;
	}
	
	private int[] getSandwichId = { 6961, 6962, 6965, 2327, 14665, 1973 };
	
	private boolean portalUnlocked = false;
	
	public boolean getPortalUnlocked() {
		return portalUnlocked;
	}
	
	public boolean setPortalUnlocked(boolean bool) {
		return portalUnlocked = bool;
	}
	
	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (interfaceId == 297) {
			if (getPortalUnlocked()) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 8629, CORRECT_SANDWICH);
				return false;
			}
			if (componentId == 10) {
				if (sandwichId == 1)  {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			else if (componentId == 12) {
				if (sandwichId == 2) {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			else if (componentId == 14) {
				if (sandwichId == 3) {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			else if (componentId == 16) {
				if (sandwichId == 4) {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			else if (componentId == 18) {
				if (sandwichId == 5) {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			else if (componentId == 20) {
				if (sandwichId == 6) {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			else if (componentId == 22) {
				if (sandwichId == 7) {
					player.getPackets().sendGameMessage(CORRECT_SANDWICH);
					player.getInventory().addItem(getSandwichId[sandwichId - 1], 1);
					player.closeInterfaces();
					setPortalUnlocked(true);
					return false;
				}
			}
			player.closeInterfaces();
			finish();
			return false;
		}
		return true;
	}
			
	public WorldTile getRandomTeleportTiles() {
		return new WorldTile(player.getX() + (Utils.random(1, 200)), player.getY() + (Utils.random(1, 150)), player.getPlane());
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
					World.spawnNPC(8629, getPlayerTiles(), 0, false);
					for (NPC npc : World.getNPCs()) {
						if (npc == null) 
							continue;
						if (npc.getId() == 8629) {
							npc.setNextGraphics(new Graphics(74));
							player.faceEntity(npc);
							npc.faceEntity(player);
							npc.setNextForceTalk(new ForceTalk(player.getDisplayName()+"! You're coming with me!"));
						}
					}
				} else if (loop == 1) {
					generate();
				} else if (loop == 2) {
					loadSandwichRoom();
					for (NPC npc : World.getNPCs()) {
						if (npc == null)
							continue;
						if (npc.getId() == 8629) {
							npc.finish();
							npc.setNextGraphics(new Graphics(74));
						}
					}
				}
				loop++;
			}
		}, 0, 1);
	}
	
	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 8629) {
			player.getInterfaceManager().sendInterface(297);
			player.getDialogueManager().startDialogue("SimpleNPCMessage", 8629, "Choose a "+getSandwich()+" and you may leave.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 11373) {
			if (getPortalUnlocked()) {
				player.stopAll();
				player.setNextWorldTile(new WorldTile(player.getTilesBeforeEvent()));//ok
				return false;
			}
			return false;
		}
		return true;
	}
	
	public enum Stages {
		LOADING, RUNNING, DESTROYING;
	}
	
	private Stages stage;
	
	public int[] boundChunks;
	
	public WorldTile getWorldTile(int mapX, int mapY) {
		 return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
	}
	
	private void loadSandwichRoom() {
		stage = Stages.LOADING;
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
			   boundChunks = RegionBuilder.findEmptyChunkBound(8, 8); 
			   RegionBuilder.copyAllPlanesMap(568, 576, boundChunks[0], boundChunks[1], 40);
			   //RegionBuilder.copyAllPlanesMap(238, 640, boundChunks[0], boundChunks[1], 8);
			   player.setNextWorldTile(getWorldTile(5, 5));
			   World.spawnNPC(8629, getWorldTile(8, 6), -1, false);
			   for (NPC npc : World.getNPCs()) {
				   if (npc == null)
					   continue;
				   if (npc.getId() == 8629) {
					   npc.setNextForceTalk(new ForceTalk("Choose a "+getSandwich()+" and you can go."));
				   }
			   }
			}
		});
		WorldTasksManager.schedule(new WorldTask()  {
			@Override
			public void run() {
				player.unlock();
				stage = Stages.RUNNING;
			}
		}, 1);
	}
	
	public void destroyArena() {
		if(stage != Stages.RUNNING) 
			return;
		stage = Stages.DESTROYING;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
				for (NPC npc : World.getNPCs()) {
					if (npc == null)
						continue;
					if (npc.getId() == 8629)
						npc.finish();
				}
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

	@Override
	public void finish() {
		player.setNextWorldTile(new WorldTile(player.getRandomTilesBeforeEvent()));
		player.getDialogueManager().startDialogue("SimpleNPCMessage", 8629, INCORRECT_SANDWICH+player.getDisplayName()+".");
		setPortalUnlocked(false);		
		destroyArena();
	}

}

	