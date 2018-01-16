package com.rs.game.randomevents.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.RegionBuilder;
import com.rs.game.WorldTile;
import com.rs.game.randomevents.RandomEvent;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class DrillDemon extends RandomEvent {

	public enum Stages {
		LOADING, RUNNING, DESTROYING;
	}
	
	private int[] boundChunks;
	
	private Stages stage;
	
	public void loadDrillDemonMap() {
		stage = Stages.LOADING;
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(2, 2); 
				RegionBuilder.copyMap(392, 600, boundChunks[0], boundChunks[1], 8, 8, new int[1], new int[1]);
				player.stopAll();
				player.setNextWorldTile(getWorldTile(12, 11)); 
				player.setNextAnimation(new Animation(-1));
				player.setNextGraphics(new Graphics(74));
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
		loadDrillDemonMap();
		
	}

	@Override
	public void finish() {
		destroyMap();
		
	}
	

}
