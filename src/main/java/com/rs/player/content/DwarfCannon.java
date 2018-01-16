package com.rs.player.content;

import com.rs.content.actions.skills.Skills;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.io.Serializable;

public class DwarfCannon implements Serializable {

    private static final long serialVersionUID = 3173899548160709203L;

    public int[] CANNON = {6, 7, 8, 9};
    public int[] ITEMS = {6, 8, 10, 12, 2};
    public int[] ROYAL_CANNON = {29408, 29403, 29404, 29405};
    public int[] ROYAL_ITEMS = {20498, 20499, 20500, 20501, 2};
    private WorldObject lastObject;
    private int cannonBalls = 0;
    private boolean first = true;
    private Player player;
    private boolean hasCannon = false;
    private boolean isFiring = false;
    private int cannonDirection;
    private boolean firstFire = true;
    private boolean loadedOnce = false;
    private boolean rotating;
    private String owner;
    private boolean settingUp = false;
    private WorldObject object;

    /**
     * Initializes the player variable
     *
     * @param player
     */
    public DwarfCannon(final Player player) {
        this.setPlayer(player);
    }

    /**
     * Checks the players location.
     */

    public static boolean isAtRockCrabs(final WorldTile tile) {
        return (tile.getX() >= 2689 && tile.getX() <= 2728
                && tile.getY() >= 3709 && tile.getY() <= 3731);
    }

    public void checkLocation() {
        /*if (player.getUsername().equalsIgnoreCase(GameConstants.OWNER)) {
            cannonSetup();
		}
		if (isAtRockCrabs(player)) {*/
        cannonSetup();
        /*} else if (player.completedDwarfCannonQuest == false) {
			player.getPackets().sendGameMessage(
					"You must have completed The Dwarf Cannon quest.");
		} else {
			player.getPackets().sendGameMessage(
					"You must be in a Cannon location.");
		}*/
    }

    public void checkRoyalLocation() {
        if (isAtRockCrabs(player)) {
            cannonSetupRoyal();
        } else {
            player.getPackets()
                    .sendGameMessage(
                            "You must have completed the Dwarf Cannon quest and be in a Cannon location, type ::cannon.");
        }
    }

    /**
     * Checks if player lost cannon
     */

    public void lostCannon() {
        if (player.hasSetupCannon) {
            if (player.getInventory().getFreeSlots() < 4) {
                if (retainsCannonBalls()) {
                    player.getBank().addItem(2, getCannonBalls(), true);
                }
                player.getBank().addItem(6, 1, true);
                player.getBank().addItem(8, 1, true);
                player.getBank().addItem(10, 1, true);
                player.getBank().addItem(12, 1, true);
                player.hasSetupCannon = false;
                player.getPackets().sendGameMessage(
                        "<col=1B9EE0>Your cannon has been added to your bank.");
            } else {
                if (retainsCannonBalls()) {
                    player.getInventory().addItem(2, getCannonBalls());
                }
                player.getInventory().addItem(6, 1);
                player.getInventory().addItem(8, 1);
                player.getInventory().addItem(10, 1);
                player.getInventory().addItem(12, 1);
                player.hasSetupCannon = false;
                player.getPackets()
                        .sendGameMessage(
                                "<col=1B9EE0>Your cannon has been placed in your inventory.");
            }
        }
    }

    public void lostRoyalCannon() {
        if (player.hasSetupRoyalCannon) {
            if (player.getInventory().getFreeSlots() < 4) {
                if (retainsCannonBalls()) {
                    player.getBank().addItem(2, getCannonBalls(), true);
                }
                player.getBank().addItem(20498, 1, true);
                player.getBank().addItem(20499, 1, true);
                player.getBank().addItem(20500, 1, true);
                player.getBank().addItem(20501, 1, true);
                player.hasSetupRoyalCannon = false;
                player.getPackets().sendGameMessage(
                        "<col=1B9EE0>Your cannon has been added to your bank.");
            } else {
                if (retainsCannonBalls()) {
                    player.getInventory().addItem(2, getCannonBalls());
                }
                player.getInventory().addItem(20498, 1);
                player.getInventory().addItem(20499, 1);
                player.getInventory().addItem(20500, 1);
                player.getInventory().addItem(20501, 1);
                player.hasSetupRoyalCannon = false;
                player.getPackets()
                        .sendGameMessage(
                                "<col=1B9EE0>Your cannon has been placed in your inventory.");
            }
        }
    }

    /**
     * Cannon setup
     */
    public void cannonSetup() {
        if (hasCannon()) {
            player.getPackets().sendGameMessage(
                    "You already have a cannon setup.");
            return;
        }
        if (!getPlayer().getInventory().containsItems(
                new Item[]{new Item(ITEMS[0]), new Item(ITEMS[1]),
                        new Item(ITEMS[2]), new Item(ITEMS[3])})) {
            player.getPackets()
                    .sendGameMessage(
                            "You do not have all the required items to set up the Dwarf Multi-Cannon.");
            return;
        }
        getPlayer().setNextAnimation(new Animation(827));
        setCannon(true);
        setSettingUp(true);
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;

            @Override
            public void run() {
                switch (count) {
                    case 0:
                        getPlayer().lock();
                        setLastObject(new WorldObject(CANNON[1], 10, 0, player));
                        World.spawnObject(getLastObject(), false);
                        player.getPackets().sendGameMessage(
                                "You place the cannon base on the ground...");
                        getPlayer().getInventory().deleteItem(ITEMS[0], 1);
                        break;
                    case 1:
                        World.removeObject(getLastObject(), false);
                        World.getRegion(getLastObject().getRegionId())
                                .removeObject(getLastObject());
                        setLastObject(new WorldObject(CANNON[2], 10, 0, player));
                        World.spawnObject(getLastObject(), false);
                        player.getPackets().sendGameMessage("You add the stand...");
                        getPlayer().getInventory().deleteItem(ITEMS[1], 1);
                        break;
                    case 2:
                        World.removeObject(getLastObject(), false);
                        World.getRegion(getLastObject().getRegionId())
                                .removeObject(getLastObject());
                        setLastObject(new WorldObject(CANNON[3], 10, 0, player));
                        World.spawnObject(getLastObject(), false);
                        player.getPackets()
                                .sendGameMessage("You add the barrel...");
                        getPlayer().getInventory().deleteItem(ITEMS[2], 1);
                        break;
                    case 3:
                        World.removeObject(getLastObject(), false);
                        World.getRegion(getLastObject().getRegionId())
                                .removeObject(getLastObject());
                        setLastObject(new WorldObject(CANNON[0], 10, 0, player));
                        World.spawnObject(getLastObject(), false);
                        setObject(getLastObject());
                        getPlayer().setDwarfCannonObject(getObject());
                        setSettingUp(false);
                        player.getPackets().sendGameMessage(
                                "You add the furnace...");
                        getPlayer().getInventory().deleteItem(ITEMS[3], 1);
                        getPlayer().unlock();
                        player.hasSetupCannon = true;
                        this.stop();
                        break;
                }
                getPlayer().setNextAnimation(new Animation(827));
                count++;
            }

        }, 0, 1);
    }

    public void cannonSetupRoyal() {
        if (hasCannon()) {
            player.getPackets().sendGameMessage(
                    "You already have a cannon setup.");
            return;
        }
        if (!getPlayer().getInventory().containsItems(
                new Item[]{new Item(ROYAL_ITEMS[0]),
                        new Item(ROYAL_ITEMS[1]), new Item(ROYAL_ITEMS[2]),
                        new Item(ROYAL_ITEMS[3])})) {
            player.getPackets()
                    .sendGameMessage(
                            "You do not have all the required items to set up the Dwarf Multi-Cannon.");
            return;
        }
        getPlayer().setNextAnimation(new Animation(827));
        setCannon(true);
        setSettingUp(true);
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;

            @Override
            public void run() {
                switch (count) {
                    case 0:
                        getPlayer().lock();
                        setLastObject(new WorldObject(ROYAL_CANNON[1], 10, 0,
                                player));
                        World.spawnObject(getLastObject(), false);
                        player.getPackets().sendGameMessage(
                                "You place the royal cannon base on the ground...");
                        getPlayer().getInventory().deleteItem(ROYAL_ITEMS[0], 1);
                        break;
                    case 1:
                        World.removeObject(getLastObject(), false);
                        World.getRegion(getLastObject().getRegionId())
                                .removeObject(getLastObject());
                        setLastObject(new WorldObject(ROYAL_CANNON[2], 10, 0,
                                player));
                        World.spawnObject(getLastObject(), false);
                        player.getPackets().sendGameMessage("You add the stand...");
                        getPlayer().getInventory().deleteItem(ROYAL_ITEMS[1], 1);
                        break;
                    case 2:
                        World.removeObject(getLastObject(), false);
                        World.getRegion(getLastObject().getRegionId())
                                .removeObject(getLastObject());
                        setLastObject(new WorldObject(ROYAL_CANNON[3], 10, 0,
                                player));
                        World.spawnObject(getLastObject(), false);
                        player.getPackets()
                                .sendGameMessage("You add the barrel...");
                        getPlayer().getInventory().deleteItem(ROYAL_ITEMS[2], 1);
                        break;
                    case 3:
                        World.removeObject(getLastObject(), false);
                        World.getRegion(getLastObject().getRegionId())
                                .removeObject(getLastObject());
                        setLastObject(new WorldObject(ROYAL_CANNON[0], 10, 0,
                                player));
                        World.spawnObject(getLastObject(), false);
                        setObject(getLastObject());
                        setSettingUp(false);
                        player.getPackets().sendGameMessage(
                                "You add the furnace...");
                        getPlayer().getInventory().deleteItem(ROYAL_ITEMS[3], 1);
                        getPlayer().unlock();
                        player.hasSetupRoyalCannon = true;
                        this.stop();
                        break;
                }
                getPlayer().setNextAnimation(new Animation(827));
                count++;
            }

        }, 0, 1);
    }

    /**
     * Pre-rotation setup check
     *
     * @param object
     */
    public void preRotationSetup(final WorldObject object) {
        if (getObject() != object) {
            player.getPackets().sendGameMessage(
                    "You are not the owner of this Dwarf Cannon.");
            return;
        }
        if (getCannonBalls() < 1) {
            player.getPackets()
                    .sendGameMessage("Your cannon has no ammo left!");
            setFiring(false);
            setRotating(false);
            loadDwarfCannon(object);
            return;
        }
        if (!isFirst()) {
            setFirst(true);
        }
        if (!isRotating()) {
            setRotating(true);
            startRotation(object);
        } else {
            player.getPackets().sendGameMessage("You can only load it once.");
        }
    }

    /**
     * Starts the rotation after pre-setup
     *
     * @param object
     */
    public void startRotation(final WorldObject object) {
        WorldTasksManager.schedule(new WorldTask() {
            int count = (hasLoadedOnce() ? getCannonDirection() : 0);

            @Override
            public void run() {
                if (!isRotating()) {
                    this.stop();
                } else if (isRotating()) {
                    switch (count) {
                        case 0:
                            if (isFirst()) {
                                setLoadedOnce(true);
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(305));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(303));
                            }
                            setCannonDirection(0);
                            break;
                        case 1:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(307));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(305));
                            }
                            setCannonDirection(1);
                            break;
                        case 2:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(289));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(307));
                            }
                            setCannonDirection(2);
                            break;
                        case 3:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(184));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(289));
                            }
                            setCannonDirection(3);
                            break;
                        case 4:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(182));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(184));
                            }
                            setCannonDirection(4);
                            break;
                        case 5:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(178));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(182));
                            }
                            setCannonDirection(5);
                            break;
                        case 6:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(291));
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(178));
                            }
                            setCannonDirection(6);
                            break;
                        case 7:
                            if (isFirst()) {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(303));
                                setFirst(false);
                            } else {
                                World.sendObjectAnimation(getPlayer(), object,
                                        new Animation(291));
                            }
                            setCannonDirection(7);
                            count = -1;
                            break;
                    }
                }
                count++;
                if (fireDwarfCannon(object)) {
                    if (!retainsCannonBalls()) {
                        player.getPackets()
                                .sendGameMessage(
                                        "Your cannon has ran out of ammo!");
                        setFiring(false);
                        setRotating(false);
                        setLoadedOnce(false);
                        setFirstFire(false);
                        this.stop();
                    }
                }
            }
        }, 0, 0);
    }

    /**
     * Picks up and removes the dwarf cannon from the gametask
     *
     * @param stage
     * @param object
     */
    public void pickUpDwarfCannon(final int stage, final WorldObject object) {
        if (getObject() != object) {
            player.getPackets().sendGameMessage(
                    "You are not the owner of this Dwarf Cannon.");
            return;
        }
        if (isSettingUp()) {
            player.getPackets()
                    .sendGameMessage(
                            "Please finish setting up your current cannon before picking it up.");
            return;
        }
        if (stage == 0) {
            getPlayer().getInventory().addItem(ITEMS[0], 1);
            getPlayer().getInventory().addItem(ITEMS[1], 1);
            getPlayer().getInventory().addItem(ITEMS[2], 1);
            getPlayer().getInventory().addItem(ITEMS[3], 1);
            if (getCannonBalls() < 1) {

            } else {
                getPlayer().getInventory().addItem(2, cannonBalls);
            }
            getPlayer().setNextAnimation(new Animation(827));
            setCannonBalls(0);
            setRotating(false);
            setCannon(false);
            setFiring(false);
            setLoadedOnce(false);
            setFirst(false);
            setCannonDirection(0);
            setObject(null);
            World.removeObject(getLastObject(), true);
            player.hasSetupCannon = false;
        } else if (stage == 1 || stage == 2 || stage == 4) {

        }
    }

    public void pickUpDwarfCannonRoyal(final int stage, final WorldObject object) {
        if (getObject() != object) {
            player.getPackets().sendGameMessage(
                    "You are not the owner of this Royale Dwarf Cannon.");
            return;
        }
        if (isSettingUp()) {
            player.getPackets()
                    .sendGameMessage(
                            "Please finish setting up your current royale cannon before picking it up.");
            return;
        }
        if (stage == 0) {
            getPlayer().getInventory().addItem(ROYAL_ITEMS[0], 1);
            getPlayer().getInventory().addItem(ROYAL_ITEMS[1], 1);
            getPlayer().getInventory().addItem(ROYAL_ITEMS[2], 1);
            getPlayer().getInventory().addItem(ROYAL_ITEMS[3], 1);
            if (getCannonBalls() < 1) {

            } else {
                getPlayer().getInventory().addItem(2, cannonBalls);
            }
            getPlayer().setNextAnimation(new Animation(827));
            setCannonBalls(0);
            setRotating(false);
            setCannon(false);
            setFiring(false);
            setLoadedOnce(false);
            setFirst(false);
            setCannonDirection(0);
            setObject(null);
            World.removeObject(getLastObject(), true);
            player.hasSetupRoyalCannon = false;
        } else if (stage == 1 || stage == 2 || stage == 4) {

        }
    }

    /**
     * Loads the Dwarf Multicannon with ammunition
     */
    public void loadDwarfCannon(final WorldObject object) {
        int ballsToAdd = 0;
        if (getObject() != object) {
            player.getPackets().sendGameMessage(
                    "You are not the owner of this Dwarf Cannon.");
            return;
        }
        if (getCannonBalls() == 0
                && getPlayer().getInventory().containsItem(2, 30)) {
            ballsToAdd = 30;
            getPlayer().getInventory().deleteItem(2, ballsToAdd);
            player.getPackets().sendGameMessage(
                    "You load the cannon with " + ballsToAdd + " cannonball"
                            + (ballsToAdd == 1 ? "" : "s") + ".");
            this.cannonBalls += ballsToAdd;
            setObject(object);
        }
        if (getCannonBalls() < 30
                && getCannonBalls() < 1
                && getPlayer().getInventory().containsItem(2,
                30 - getCannonBalls())) {
            ballsToAdd = 30 - this.cannonBalls;
            getPlayer().getInventory().deleteItem(2, ballsToAdd);
            player.getPackets().sendGameMessage(
                    "You load the cannon with " + ballsToAdd + " cannonball"
                            + (ballsToAdd == 1 ? "" : "s") + ".");
            this.cannonBalls += ballsToAdd;
            setObject(object);
        }
    }

    /**
     * Fires the Dwarf MultiCannon
     */
    public boolean fireDwarfCannon(final WorldObject object) {
        boolean hit = false;
        if (getCannonBalls() == 0) {
            hit = false;
            setFiring(false);
            return false;
        }
        for (final NPC n : World.getNPCs()) {
            final int damage = Utils.getRandom(300);
            final double combatXp = damage / 4;
            final int distanceX = n.getX() - object.getX();
            final int distanceY = n.getY() - object.getY();
            if (n == null || n.isDead()
                    || !n.getDefinitions().hasAttackOption()) {
                continue;
            }
            switch (getCannonDirection()) {
                case 0: // North
                    if ((distanceY <= 8 && distanceY >= 0)
                            && (distanceX >= -1 && distanceX <= 1)) {
                        hit = true;
                    }
                    break;
                case 1: // North East
                    if ((distanceY <= 8 && distanceY >= 0)
                            && (distanceX <= 8 && distanceX >= 0)) {
                        hit = true;
                    }
                    break;
                case 2: // East
                    if ((distanceY <= 1 && distanceY >= -1)
                            && (distanceX <= 8 && distanceX >= 0)) {
                        hit = true;
                    }
                    break;
                case 3: // SgetPackets.sendGameMessageh East
                    if ((distanceY >= -8 && distanceY <= 0)
                            && (distanceX <= 8 && distanceX >= 0)) {
                        hit = true;
                    }
                    break;
                case 4: // SgetPackets.sendGameMessageh
                    if ((distanceY >= -8 && distanceY <= 0)
                            && (distanceX <= 1 && distanceX >= -1)) {
                        hit = true;
                    }
                    break;
                case 5: // SgetPackets.sendGameMessageh West
                    if ((distanceY >= -8 && distanceY <= 0)
                            && (distanceX >= -8 && distanceX <= 0)) {
                        hit = true;
                    }
                    break;
                case 6: // West
                    if ((distanceY >= -1 && distanceY <= 1)
                            && (distanceX >= -8 && distanceX <= 0)) {
                        hit = true;
                    }
                    break;
                case 7: // North West
                    if ((distanceY <= 8 && distanceY >= 0)
                            && (distanceX >= -8 && distanceX <= 0)) {
                        hit = true;
                    }
                    break;
                default:
                    hit = false;
                    break;
            }
            if (hit) {
                this.cannonBalls -= 1;
                n.getCombat().setTarget(getPlayer());
                World.sendProjectile(getPlayer(), object, n, 53, 52, 52, 30, 0,
                        0, 2);
                n.applyHit(new Hit(getPlayer(), damage, Hit.HitLook.CANNON_DAMAGE));
                getPlayer().getSkills().addXp(Skills.RANGE, combatXp / 2);
                return true;
            }
        }
        return false;
    }

    public void removeDwarfCannon() {
        if (getLastObject() != null) {
            setRotating(false);
            setCannon(false);
            setFiring(false);
            setLoadedOnce(false);
            setObject(null);
            setFirstFire(false);
            setCannonDirection(0);
            World.getRegion(getLastObject().getRegionId()).removeObject(
                    getLastObject());
        }
    }

    public int getCannonDirection() {
        return cannonDirection;
    }

    public void setCannonDirection(final int cannonDirection) {
        this.cannonDirection = cannonDirection;
    }

    public boolean hasLoadedOnce() {
        return loadedOnce;
    }

    public void setLoadedOnce(final boolean loadedOnce) {
        this.loadedOnce = loadedOnce;
    }

    public boolean retainsCannonBalls() {
        return cannonBalls > 0;
    }

    /**
     * @return the rotating
     */
    public boolean isRotating() {
        return rotating;
    }

    /**
     * @param rotating the rotating to set
     */
    public void setRotating(final boolean rotating) {
        this.rotating = rotating;
    }

    public boolean hasCannon() {
        return hasCannon;
    }

    public void setCannon(final boolean hasCannon) {
        this.hasCannon = hasCannon;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(final boolean first) {
        this.first = first;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(final boolean isFiring) {
        this.isFiring = isFiring;
    }

    public boolean isFirstFire() {
        return firstFire;
    }

    public void setFirstFire(final boolean firstFire) {
        this.firstFire = firstFire;
    }

    public WorldObject getLastObject() {
        return lastObject;
    }

    public void setLastObject(final WorldObject lastObject) {
        this.lastObject = lastObject;
    }

    public boolean isSettingUp() {
        return settingUp;
    }

    public void setSettingUp(final boolean settingUp) {
        this.settingUp = settingUp;
    }

    public int getCannonBalls() {
        return cannonBalls;
    }

    public void setCannonBalls(final int cannonBalls) {
        this.cannonBalls = cannonBalls;
    }

    public WorldObject getObject() {
        return object;
    }

    public void setObject(final WorldObject object) {
        this.object = object;
    }
}