package com.rs.player;

import com.rs.core.utils.Utils;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OwnedObjectManager {

	public static final AtomicLong keyMaker = new AtomicLong();

	private static final Map<String, OwnedObjectManager> ownedObjects = new ConcurrentHashMap<String, OwnedObjectManager>();
	private final WorldObject[] objects;
	private final long cycleTime;
	private final String managerKey;
	private Player player;
	private int count;
	private long lifeTime;

	private OwnedObjectManager(final Player player,
							   final WorldObject[] objects, final long cycleTime) {
		managerKey = player.getUsername() + "_" + keyMaker.getAndIncrement();
		this.cycleTime = cycleTime;
		this.objects = objects;
		this.player = player;
		spawnObject();
		player.getOwnedObjectManagerKeys().add(managerKey);
		ownedObjects.put(managerKey, this);
	}

	public static void processAll() {
		for (final OwnedObjectManager object : ownedObjects.values()) {
			object.process();
		}
	}

	public static boolean isPlayerObject(final Player player,
			final WorldObject object) {
		for (final Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			final OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX()
					&& manager.getCurrentObject().getY() == object.getY()
					&& manager.getCurrentObject().getPlane() == object
							.getPlane()
					&& manager.getCurrentObject().getId() == object.getId())
				return true;
		}
		return false;
	}

	public static boolean convertIntoObject(final WorldObject object,
			final WorldObject toObject, final ConvertEvent event) {
		for (final OwnedObjectManager manager : ownedObjects.values()) {
			if (manager.getCurrentObject().getX() == toObject.getX()
					&& manager.getCurrentObject().getY() == toObject.getY()
					&& manager.getCurrentObject().getPlane() == toObject
							.getPlane()
					&& manager.getCurrentObject().getId() == object.getId()) {
				if (event != null && !event.canConvert(manager.player))
					return false;
				manager.convertIntoObject(toObject);
				return true;
			}
		}
		return false;
	}

	public static boolean removeObject(final Player player,
			final WorldObject object) {
		for (final Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			final OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX()
					&& manager.getCurrentObject().getY() == object.getY()
					&& manager.getCurrentObject().getPlane() == object
							.getPlane()
					&& manager.getCurrentObject().getId() == object.getId()) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						manager.delete();
					}
				});
				return true;
			}
		}
		return false;
	}

	public static void linkKeys(final Player player) {
		for (final Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			final OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			manager.player = player;
		}
	}

	public static void addOwnedObjectManager(final Player player,
			final WorldObject[] objects, final long cycleTime) {
		new OwnedObjectManager(player, objects, cycleTime);
	}

	public static int getObjectsforValue(final Player player, final int objectId) {
		int count = 0;
		for (final Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			final OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getId() == objectId) {
				count++;
			}
		}
		return count;
	}

	public void reset() {
		for (final OwnedObjectManager object : ownedObjects.values()) {
			object.delete();
		}
	}

	public void resetLifeTime() {
		this.lifeTime = Utils.currentTimeMillis() + cycleTime;
	}

	public boolean forceMoveNextStage() {
		if (count != -1) {
			destroyObject(objects[count]);
		}
		count++;
		if (count == objects.length) {
			remove();
			return false;
		}
		spawnObject();
		return true;
	}

	private void spawnObject() {
		World.spawnObject(objects[count], true);
		resetLifeTime();
	}

	public void convertIntoObject(final WorldObject object) {
		destroyObject(objects[count]);
		objects[count] = object;
		spawnObject();
	}

	private void remove() {
		ownedObjects.remove(managerKey);
		if (player != null) {
			player.getOwnedObjectManagerKeys().remove(managerKey);
		}
	}

	public void delete() {
		destroyObject(objects[count]);
		remove();
	}

	public void process() {
		if (Utils.currentTimeMillis() > lifeTime) {
			forceMoveNextStage();
		}
	}

	public WorldObject getCurrentObject() {
		return objects[count];
	}

	public void destroyObject(final WorldObject object) {
		World.destroySpawnedObject(object);
	}

	public interface ConvertEvent {

		boolean canConvert(Player player);

	}

}
