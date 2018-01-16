package com.rs.game.player.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.controlers.Controler;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * 
 * @author Josh'
 *
 */
public class DwarfCannon implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8749385052048151334L;

	private transient Player player;
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public DwarfCannon() {
	}
	
	private enum CannonSetup {
		
		FIRST_STAGE("You place the cannon base on the ground.", 7, new Item(6)),

		SECOND_STAGE("You add the stand.", 8, new Item(8)),

		THIRD_STAGE("You add the barrels.", 9, new Item(10)),
		
		FOURTH_STAGE("You add the furnace.", 6, new Item(12));
		
		private String message;

		private int objectId;

		private Item item;

		private CannonSetup(String message, int objectId, Item item) {
			this.message = message;
			this.objectId = objectId;
			this.item = item;
		}

		public String getmessage() {
			return message;
		}

		public int getObjectId() {
			return objectId;
		}

		public Item getSetupItem() {
			return item;
		}
	}
	
    private int[] CANNON_ITEMS = { 6, 8, 10, 12 };

	private int[] moveAnimations = { 305, 307, 289, 184, 182, 178, 291, 303 };

	private int[] fireAnimations = { 292, 304, 306, 288, 183, 180, 1, 290 };

	private int[][] directionDetails = { { 8, 0, -1, 1 }, { 8, 0, 8, 0 }, 
			{ 1, -1, 8, 0}, { -8, 0, 8, 0 },  { -8, 0, 1, -1 }, 
			{ -8 , 0, -8, 0 }, { -1, 1, 8, 0 },  { 8, 0, -8, 0 } };
	
    public boolean canSetupDwarfMCannon(int x, int y, int plane, int size) {
		for (int tileX = x; tileX < x + size; tileX++)
		    for (int tileY = y; tileY < y + size;)
		    	return false;
		return true;
    }
	
    private WorldObject object;
    
	private void setCannon(WorldObject object) {
		this.object = object;
	}
	
	public void fireCannon(boolean start) {
		if (start) {
			if (player.getCannonBalls() < 1) {
				player.getPackets().sendGameMessage("You need to have some cannonballs in your cannon in order to fire it!");
				return;
			}
			setDirection(0);
		}
		WorldTasksManager.schedule(new WorldTask() {
			boolean hasFired = false;
			@Override
			public void run() {
				hasFired = false;
				if (getNpcsInRegion(getCannon(), 8) != null) {
					for (final NPC n : getNpcsInRegion(getCannon(), 8)) {
						if (n == null) {
							continue;
						}
						int x = n.getX() - object.getX();
						int y = n.getY() - object.getY();
						switch(getDirection()) {
					    case 0:
					    case 7:
							if ((y <= directionDetails[getDirection()][0] && y >= directionDetails[getDirection()][1])
									&& (x >= directionDetails[getDirection()][2] && x <= directionDetails[getDirection()][3])) {
								hasFired = true;
							}
							break;
					    case 1:
					    case 2:
							if ((y <= directionDetails[getDirection()][0] && y >= directionDetails[getDirection()][1])
									&& (x <= directionDetails[getDirection()][2] && x >= directionDetails[getDirection()][3])) {
								hasFired = true;
							}
							break;
					    case 3:
					    case 4:
							if ((y >= directionDetails[getDirection()][0] && y <= directionDetails[getDirection()][1])
									&& (x <= directionDetails[getDirection()][2] && x >= directionDetails[getDirection()][3])) {
								hasFired = true;
							}
					    	break;
					    case 5:
					    case 6:
							if ((y >= directionDetails[getDirection()][0] && y <= directionDetails[getDirection()][1])
									&& (x >= directionDetails[getDirection()][2] && x <= directionDetails[getDirection()][3])) {
								hasFired = true;
							}
					    	break;
						}
						if (hasFired) {
							player.setCannonBalls(player.getCannonBalls() - 1);
							World.sendObjectAnimation(getCannon(), 
									new Animation(fireAnimations[getDirection()]));
							World.sendProjectile(player, getCalculatedFireTile(), n, 53, 52, 52, 30, 0, 0, 2);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									if (n.isDead()) {
										super.stop();
									}
									int damage = Utils.random(Utils.getRandom(
											player.getSkills().getLevel(Skills.RANGE)), 
											(251 + player.getSkills().getLevel(Skills.RANGE)));
									double experience = (double) damage / 5;
									n.getCombat().setTarget(player);
									n.applyHit(new Hit(player, damage, HitLook.CANNON_DAMAGE));
									player.getSkills().addXp(Skills.RANGE, (experience / 2));
								}
							}, 1);
							break;
						}
					}
				}
				World.sendObjectAnimation(getCannon(), 
						new Animation(moveAnimations[getDirection()]));
				if (direction++ >= 7) {
					setDirection(0);
				}
			}
		}, 0, 0);
	}	
    public void pickupCannon(int stage, WorldObject object) {
    	if (!(getCannonOwnerByUsername() != player.getDisplayName())) {
    		player.getPackets().sendGameMessage("You can't pickup someone elses cannon.");
    		return;
    	}
		int space = player.getCannonBalls() > 0 ? stage + 1 : stage;
		if (player.getInventory().getFreeSlots() < space) {
			player.getPackets().sendGameMessage("You need atleast " + space + " spaces in your inventory to pickup your cannon.");
			return;
		}
		player.getPackets().sendGameMessage("You pick up the cannon. It's really heavy.");
		player.setHasDwarfCannon(false);
		player.stopAll();//stops combat
		for (int i = 0; i < stage; i++)
			player.getInventory().addItem(CANNON_ITEMS[i], 1);
		if (player.getCannonBalls() > 0) {
			player.getInventory().addItem(2, player.getCannonBalls());
			player.setCannonBalls(0);
		}
		getCannon().setLife(-1);
		World.removeObject(getCannon(), false);
		
    }	
	
	private String cannonOwner;
	
	private String getCannonOwnerByUsername() {
		return cannonOwner;
	}
	
	private String setCannonToUsername() {
		return cannonOwner = player.getDisplayName();
	}
	
	public void setUpDwarfCannon(final WorldTile tiles) {
		if (player.getHasDwarfCannon()) {
		    player.getPackets().sendGameMessage("You already have a cannon set up!");
		    return;
		}
		Controler c = player.getControlerManager().getControler();
		if (c != null && !(c instanceof Wilderness)) {
		    player.getPackets().sendGameMessage("You can't set up your cannon here.");
		    return;
		}
		int itemsChecked = 0;
		for (int i : CANNON_ITEMS) {
			if (!player.getInventory().containsItem(i, 1)) 
				break;
			itemsChecked ++;
		}
		/*WorldTile tile = player.transform(-3, -3, 0);
		if (!canSetupDwarfMCannon(tile.getPlane(), tile.getX(), tile.getY(), 3)) {
		    player.getPackets().sendGameMessage("There isn't enough space to setup your cannon here.");
		    return;
		}*/
		WorldTasksManager.schedule(new WorldTask() {
			int loops = 0;
			WorldObject object = null;
			@SuppressWarnings("unused")
			@Override
			public void run() {
				if (player == null) {
					if (object != null) {
					    World.removeObject(object, false);
					    World.getRegion(object.getRegionId()).removeObject(object);
					}
					super.stop();
				}
				if (loops == 3) {
					player.setHasDwarfCannon(true);
					setCannonToUsername();
					System.out.println("cannon placed in world, by "+player.getDisplayName());
					setCannon(object);
					player.unlock();
					super.stop();
				}
				if (loops > 0) {
				    World.removeObject(object, false);
				    World.getRegion(object.getRegionId()).removeObject(object);
				}
				object = new WorldObject(getCannonSetupStage(loops).getObjectId(), 10, 0, tiles);
				World.spawnObject(object, false);
				player.faceObject(object);
				player.getPackets().sendGameMessage(getCannonSetupStage(loops).getmessage());
				player.getInventory().deleteItem(getCannonSetupStage(loops).getSetupItem());
				player.setNextAnimation(new Animation(827));
				loops++;
			}
		}, 0, 1);
	}
	
	private CannonSetup getCannonSetupStage(int stage) {
		return CannonSetup.values()[stage];
	}
	
	public NPC[] getNpcsInRegion(WorldTile tile, int distance) {
		List<NPC> npcs = new ArrayList<NPC>();
		Region region = World.getRegion(tile.getRegionId());
		List<Integer> npcIndices = region.getNPCsIndexes();
		if (npcIndices == null)
			return null;
		for (int npcIndex : npcIndices) {
			NPC n = World.getNPCs().get(npcIndex);
			if (n == null) {
				continue;
			}
			if (n.isDead() || n.hasFinished() || !n.withinDistance(tile, distance) 
					|| !n.getDefinitions().hasAttackOption()) {
				continue;
			}
			npcs.add(n);
		}
		return (npcs.size() > 0 ? npcs.toArray(new NPC[npcs.size()]) : null);
	}
	
	public WorldObject getCannon() {
		return object;
	}
	
	private int direction = 0;
	
	private int getDirection() {
		return direction;
	}
	
	private void setDirection(int direction) {
		this.direction = direction;
	}
	
	private WorldTile getCalculatedFireTile() {
		switch(getDirection()) {
		case 0:
			return new WorldTile(getCannon().getX() + 1, getCannon().getY() + 2, getCannon().getPlane());
		case 1:
			return new WorldTile(getCannon().getX() + 2, getCannon().getY() + 2, getCannon().getPlane());
		case 2:
			return new WorldTile(getCannon().getX() + 2, getCannon().getY() + 1, getCannon().getPlane());
		case 3:
			return new WorldTile(getCannon().getX() + 2, getCannon().getY(), getCannon().getPlane());
		case 4:
			return new WorldTile(getCannon().getX() + 1, getCannon().getY(), getCannon().getPlane());
		case 5:
			return new WorldTile(getCannon().getX(), getCannon().getY(), getCannon().getPlane());//
		case 6:
			return new WorldTile(getCannon().getX(), getCannon().getY() + 1, getCannon().getPlane());
		case 7:
			return new WorldTile(getCannon().getX(), getCannon().getY() + 2, getCannon().getPlane());
		default:
			return new WorldTile(getCannon().getX() + 1, getCannon().getY() + 1, getCannon().getPlane());
		}
	}
	
	public void addBalls() {
		if (player.getCannonBalls() < 30) {
		    int amt = player.getInventory().getNumerOf(2);
		    if (amt == 0) {
		    	player.getPackets().sendGameMessage("You need to load your cannon with cannon balls before firing it!");
		    }
		    int add = 30 - player.getCannonBalls();
			if (amt > add)
				amt = add;
			player.setCannonBalls(amt);
			player.getInventory().deleteItem(2, amt);
			player.getPackets().sendGameMessage("You load the cannon with " + amt + " cannon balls.");
		 }
		 player.getPackets().sendGameMessage("Your cannon is full.");
	}
}