package com.rs.game.player;

import java.io.Serializable;

import com.rs.game.WorldObject;
import com.rs.game.npc.NPC;
import com.rs.game.randomevents.RandomEvent;
import com.rs.game.randomevents.RandomEventHandler;

public class RandomEventManager implements Serializable {

	private static final long serialVersionUID = -6285492005339142224L;

	private transient Player player;
	
	private transient boolean inited;
	
	private transient RandomEvent randomEvent;
	
	private Object[] lastEventParams;
	
	private String lastRandomEvent;
	
	public void process() {
		if (randomEvent == null || !inited)
			return;
		randomEvent.process();
	}
	
	public RandomEventManager() {
		lastRandomEvent = "";
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public RandomEvent getRandomEvent() {
		return randomEvent;
	}
	
	public void startEvent(Object key, Object... parameters) {
		if (randomEvent != null)
			forceStop();
		randomEvent = (RandomEvent) (key instanceof RandomEvent ? key : RandomEventHandler.getRandomEvent(key));
		if (randomEvent == null)
			return;
		randomEvent.setPlayer(player);
		lastEventParams = parameters;
		lastRandomEvent = (String) key;
		randomEvent.start();
		inited = true;
	}
	
	public void forceStop() {
		if (randomEvent != null) {
			randomEvent.forceClose();
			randomEvent = null;
		}
		lastEventParams = null;
		lastRandomEvent = null;
		inited = false;
	}

	public void removeEventWithoutCheck() {
		randomEvent = null;
		lastEventParams = null;
		lastRandomEvent = null;
		inited = false;
	}

	public Object[] getLastEventParams() {
		return lastEventParams;
	}

	public void setLastEventParams(Object[] lastCP) {
		this.lastEventParams = lastCP;
	}
	
	public boolean processObjectClick1(WorldObject object) {
		if (randomEvent == null || !inited)
			return true;
		return randomEvent.processObjectClick1(object);
	}

	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (randomEvent == null || !inited)
			return true;
		return randomEvent.processButtonClick(interfaceId, componentId, slotId,
				packetId);
	}

	public boolean processNPCClick1(NPC npc) {
		if (randomEvent == null || !inited)
			return true;
		return randomEvent.processNPCClick1(npc);
	}
	
}
