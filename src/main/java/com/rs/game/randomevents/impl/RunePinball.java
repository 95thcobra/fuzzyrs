package com.rs.game.randomevents.impl;

import java.util.Random;
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
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.randomevents.RandomEvent;
//import com.rs.game.randomevents.impl.DrillDemon.Stages;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class RunePinball extends RandomEvent {
	
	private int MYSETIROUS_MAN = 410;
	
	public enum Stages {
		LOADING, RUNNING, DESTROYING;
	}
	
	private int[] boundChunks;
	
	private Stages stage;
	
	public void loadPinballRoom() {
		stage = Stages.LOADING;
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(2, 2); 
				RegionBuilder.copyMap(245, 629, boundChunks[0], boundChunks[1], 3, 3, new int[1], new int[1]);
				player.reset();
				player.setNextWorldTile(new WorldTile(getWorldTile(12, 11))); 
				player.setNextAnimation(new Animation(-1));
				player.setNextGraphics(new Graphics(74));
				Dialogue.sendNPCDialogueNoContinue(
						player, MYSETIROUS_MAN, 9827,
						"Welcome to the Rune Pinball random event "+player.getDisplayName()+", start off by",
						"tagging a "+getNextPin()+" to gain points, when you reach "+getEventPinsNeeded()+" points you may leave.");
				WorldTasksManager.schedule(new WorldTask()  {
					@Override
					public void run() {
						World.spawnNPC(3913, getWorldTile(13, 9), -1, false);
						World.spawnNPC(3912, getWorldTile(11, 9), -1, false);
						for (NPC npc : World.getNPCs()) {
							if (npc == null)
								continue;
							npc.faceEntity(player);
						}
						player.getPackets().sendBlackOut(2);
						player.unlock(); 
						stage = Stages.RUNNING;
					}

				}, 1);
			}
		});
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
	
	private int pinsNeeded;
	
	public int getEventPinsNeeded() {
		return pinsNeeded = 8 + Utils.random(6);
	}
	private int nextPin;
	
	public void generate() {
		Random r = new Random();
		int n = r.nextInt(4);
		nextPin = n;;
	}
	
	public void refreshInterfaces() {
		player.getInterfaceManager().sendOverlay(263, true);
		player.getPackets().sendIComponentText(263, 1, "1");
	}
	
	
	public String getNextPin() {
		switch(nextPin) {
		case 1:
			return "air";
		case 2:
			return "earth";
		case 3:
			return "fire";
		case 4:
			return "nature";
		}
		return "water";
	}
	
	private int[] objects = { 15001, 15003, 15005, 15007, 15009 };
	
	private boolean canLeave;
	
	public boolean canLeaveArena() {
		return canLeave;
	}
	
	public boolean setCanLeaveArena(boolean bool) {
		return canLeave = bool;
	}
	
	private int points;
	
	public int getPoints() {
		return points;
	}
	
	public int setPoints(int pts) {
		return this.points = pts;
	}
	
	private int[] pins = { 0, 1, 2, 3, 4, 5 };

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 15010) {
			if (!canLeaveArena()) {
				Dialogue.closeNoContinueDialogue(player);
				player.getPackets().sendGameMessage("You can't leave yet, finish the minigame first.");
				return false;
			}
			Dialogue.closeNoContinueDialogue(player);
			finish();
			return false;
		}
		if (getPoints() == 10) {
			Dialogue.closeNoContinueDialogue(player);
			Dialogue.sendNPCDialogueNoContinue(
					player, MYSETIROUS_MAN, 9827, 
					"Well done "+player.getDisplayName()+", you've completed the event.",
						"You can now leave the arena by the cave behind Tilt and Flippa.");
			setCanLeaveArena(true);
			return false; 			
		}
		if (object.getId() >= objects[0] && object.getId() <= objects[4]) {
			if (canLeaveArena()) {
				Dialogue.sendNPCDialogueNoContinue(
						player, MYSETIROUS_MAN, 9827, "You don't need to tag anymore pins, you can leave now.");
				return false;
			}
			if (nextPin != pins[nextPin]) {
				Dialogue.sendNPCDialogueNoContinue(
						player, MYSETIROUS_MAN, 9827, "You tagged the wrong pin, try again. Tag "
								+(getNextPin() == "air" ? "an" : "a")+" "+getNextPin()+" pin.");
				return false;
			}
			Dialogue.closeNoContinueDialogue(player);
			points++;
			player.getPackets().sendIComponentText(263, 1, "Score: "+getPoints());
			generate();
			Dialogue.sendNPCDialogueNoContinue(
					player, MYSETIROUS_MAN, 9827, 
						"Well done "+player.getDisplayName()+", now tag "
								+(getNextPin() == "air" ? "an" : "a")+" "+getNextPin()+" pin.");
			return false;
		}
		return true;
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
					World.spawnNPC(MYSETIROUS_MAN, getPlayerTiles(), 0, false);
					for (NPC npc : World.getNPCs()) {
						if (npc == null) 
							continue;
						if (npc.getId() == MYSETIROUS_MAN) {
							npc.setNextGraphics(new Graphics(74));
							player.faceEntity(npc);
							npc.faceEntity(player);
							npc.setNextForceTalk(new ForceTalk(player.getDisplayName()+"! You're coming with me!"));
						}
					}
				} else if (loop == 2) {
					refreshInterfaces();
					generate();
					loadPinballRoom();
					for (NPC npc : World.getNPCs()) {
						if (npc == null)
							continue;
						if (npc.getId() == MYSETIROUS_MAN) {
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
	public void finish() {
		player.setEventState(false);
		setCanLeaveArena(false);
		destroyMap();
		player.getPackets().sendGameMessage("Congratulations, you've completed the Rune Pinball random event.");
		player.setNextWorldTile(player.getTilesBeforeEvent());
		player.getInterfaceManager().closeOverlay(true);
		Dialogue.closeNoContinueDialogue(player);
		player.closeInterfaces();
		if (player.getInventory().getFreeSlots() >= 1) {
			player.getInventory().addItem(6183, 1);		
		} else {
			World.addGroundItem(new Item(6183, 1), new WorldTile(player), player, true, 180,true);
		}
	}

}
