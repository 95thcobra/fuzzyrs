package com.rs.player;

import com.rs.Server;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.player.content.Foods.Food;
import com.rs.player.content.Pots.Pot;
import com.rs.player.controlers.Controller;
import com.rs.world.Entity;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;

import java.io.Serializable;

public final class ControllerManager implements Serializable {

	private static final long serialVersionUID = 2084691334731830796L;

	private transient Player player;
    private transient Controller controller;
    private transient boolean inited;
	private Object[] lastControlerArguments;

    private Class<? extends Controller> lastController;

    @SuppressWarnings("unchecked")
    public ControllerManager() {
        try {
            lastController = (Class<? extends Controller>) Class.forName(Server.getInstance().getSettingsManager().getSettings().getStartController());
        } catch (ClassNotFoundException e) {
            //nothing lol
        }
    }

	public void setPlayer(final Player player) {
		this.player = player;
	}

    public Controller getController() {
        return controller;
    }

    public void startController(Class<? extends Controller> c, final Object... parameters) {
        if (controller != null) {
            forceStop();
		}
        try {
            controller = c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.handle(e);
        }
        if (controller == null)
            return;
        controller.setPlayer(player);
        lastControlerArguments = parameters;
        lastController = c;
        controller.start();
        inited = true;
	}

	public void login() {
        if (lastController == null)
            return;
        try {
            controller = lastController.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.handle(e);
        }
        if (controller == null) {
            forceStop();
			return;
		}
        controller.setPlayer(player);
        if (controller.login()) {
            forceStop();
		} else {
			inited = true;
		}
	}

	public void logout() {
        if (controller == null)
            return;
        if (controller.logout()) {
            forceStop();
		}
	}

	public boolean canMove(final int dir) {
        return controller == null || !inited || controller.canMove(dir);
    }

    public boolean checkWalkStep(final int lastX, final int lastY, final int nextX, final int nextY) {
        return controller == null || !inited || controller.checkWalkStep(lastX, lastY, nextX, nextY);
    }

	public boolean keepCombating(final Entity target) {
        return controller == null || !inited || controller.keepCombating(target);
    }

	public boolean canEquip(final int slotId, final int itemId) {
        return controller == null || !inited || controller.canEquip(slotId, itemId);
    }

	public boolean canAddInventoryItem(final int itemId, final int amount) {
        return controller == null || !inited || controller.canAddInventoryItem(itemId, amount);
    }

	public void trackXP(final int skillId, final int addedXp) {
        if (controller == null || !inited)
            return;
        controller.trackXP(skillId, addedXp);
    }

	public boolean canDeleteInventoryItem(final int itemId, final int amount) {
        return controller == null || !inited || controller.canDeleteInventoryItem(itemId, amount);
    }

	public boolean canUseItemOnItem(final Item itemUsed, final Item usedWith) {
        return controller == null || !inited || controller.canUseItemOnItem(itemUsed, usedWith);
    }

	public boolean canAttack(final Entity entity) {
        return controller == null || !inited || controller.canAttack(entity);
    }

	public boolean canPlayerOption1(final Player target) {
        return controller == null || !inited || controller.canPlayerOption1(target);
    }

	public boolean canHit(final Entity entity) {
        return controller == null || !inited || controller.canHit(entity);
    }

	public void moved() {
        if (controller == null || !inited)
            return;
        controller.moved();
    }

	public void magicTeleported(final int type) {
        if (controller == null || !inited)
            return;
        controller.magicTeleported(type);
    }

	public void sendInterfaces() {
        if (controller == null || !inited)
            return;
        controller.sendInterfaces();
    }

	public void process() {
        if (controller == null || !inited)
            return;
        controller.process();
    }

	public boolean sendDeath() {
        return controller == null || !inited || controller.sendDeath();
    }

	public boolean canEat(final Food food) {
        return controller == null || !inited || controller.canEat(food);
    }

	public boolean canPot(final Pot pot) {
        return controller == null || !inited || controller.canPot(pot);
    }

	public boolean useDialogueScript(final Object key) {
        return controller == null || !inited || controller.useDialogueScript(key);
    }

	public boolean processMagicTeleport(final WorldTile toTile) {
        return controller == null || !inited || controller.processMagicTeleport(toTile);
    }

	public boolean processItemTeleport(final WorldTile toTile) {
        return controller == null || !inited || controller.processItemTeleport(toTile);
    }

	public boolean processObjectTeleport(final WorldTile toTile) {
        return controller == null || !inited || controller.processObjectTeleport(toTile);
    }

	public boolean processObjectClick1(final WorldObject object) {
        return controller == null || !inited || controller.processObjectClick1(object);
    }

	public boolean processButtonClick(final int interfaceId,
			final int componentId, final int slotId, final int packetId) {
        return controller == null || !inited || controller.processButtonClick(interfaceId, componentId, slotId, packetId);
    }

	public boolean processNPCClick1(final NPC npc) {
        return controller == null || !inited || controller.processNPCClick1(npc);
    }

	public boolean canSummonFamiliar() {
        return controller == null || !inited || controller.canSummonFamiliar();
    }

	public boolean processNPCClick2(final NPC npc) {
        return controller == null || !inited || controller.processNPCClick2(npc);
    }

	public boolean processNPCClick3(final NPC npc) {
        return controller == null || !inited || controller.processNPCClick3(npc);
    }

	public boolean processNPCClick4(final NPC npc) {
        return controller == null || !inited || controller.processNPCClick4(npc);
    }

	public boolean processObjectClick2(final WorldObject object) {
        return controller == null || !inited || controller.processObjectClick2(object);
    }

	public boolean processObjectClick3(final WorldObject object) {
        return controller == null || !inited || controller.processObjectClick3(object);
    }

	public boolean processItemOnNPC(final NPC npc, final Item item) {
        return controller == null || !inited || controller.processItemOnNPC(npc, item);
    }

	public boolean canDropItem(final Item item) {
        return controller == null || !inited || controller.canDropItem(item);
    }

	public void forceStop() {
        if (controller != null) {
            controller.forceClose();
            controller = null;
        }
		lastControlerArguments = null;
        lastController = null;
        inited = false;
	}

	public void removeControlerWithoutCheck() {
        controller = null;
        lastControlerArguments = null;
        lastController = null;
        inited = false;
	}

	public Object[] getLastControlerArguments() {
		return lastControlerArguments;
	}

	public void setLastControlerArguments(final Object[] lastControlerArguments) {
		this.lastControlerArguments = lastControlerArguments;
	}

	public boolean processObjectClick4(final WorldObject object) {
		return true; // unused atm
	}

	public boolean processObjectClick5(final WorldObject object) {
        return controller == null || !inited || controller.processObjectClick5(object);
    }

    public boolean canRemove(int slotId, int id) {
        return controller == null || !inited || controller.canRemove(slotId, id);
    }
}
