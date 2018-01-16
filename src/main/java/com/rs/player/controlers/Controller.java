package com.rs.player.controlers;

import com.rs.player.Player;
import com.rs.player.content.Foods.Food;
import com.rs.player.content.Pots.Pot;
import com.rs.world.Entity;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;

public abstract class Controller {

	// private static final long serialVersionUID = 8384350746724116339L;

	protected Player player;

	public Player getPlayer() {
		return player;
	}

	public final void setPlayer(final Player player) {
		this.player = player;
	}

	public final Object[] getArguments() {
		return player.getControllerManager().getLastControlerArguments();
	}

	public final void setArguments(final Object[] objects) {
		player.getControllerManager().setLastControlerArguments(objects);
	}

	public final void removeControler() {
		player.getControllerManager().removeControlerWithoutCheck();
	}

	public abstract void start();

	public boolean canEat(final Food food) {
		return true;
	}

	public boolean canPot(final Pot pot) {
		return true;
	}

	/**
	 * after the normal checks, extra checks, only called when you attacking
	 */
	public boolean keepCombating(final Entity target) {
		return true;
	}

	public boolean canEquip(final int slotId, final int itemId) {
		return true;
	}

	/**
	 * after the normal checks, extra checks, only called when you start trying
	 * to attack
	 */
	public boolean canAttack(final Entity target) {
		return true;
	}

	public void trackXP(final int skillId, final int addedXp) {

	}

	public boolean canDeleteInventoryItem(final int itemId, final int amount) {
		return true;
	}

	public boolean canUseItemOnItem(final Item itemUsed, final Item usedWith) {
		return true;
	}

	public boolean canAddInventoryItem(final int itemId, final int amount) {
		return true;
	}

	public boolean canPlayerOption1(final Player target) {
		return true;
	}

	/**
	 * hits as ice barrage and that on multi areas
	 */
	public boolean canHit(final Entity entity) {
		return true;
	}

	/**
	 * processes every gametask ticket, usualy not used
	 */
	public void process() {

	}

	public void moved() {

	}

	public boolean processItemOnObject(WorldObject object, Item item) {
		return true;
	}
	/**
	 * called once teleport is performed
	 */
	public void magicTeleported(final int type) {

	}

	public void sendInterfaces() {

	}

	/**
	 * return can use script
	 */
	public boolean useDialogueScript(final Object key) {
		return true;
	}

	/**
	 * return can teleport
	 */
	public boolean processMagicTeleport(final WorldTile toTile) {
		return true;
	}

	/**
	 * return can teleport
	 */
	public boolean processItemTeleport(final WorldTile toTile) {
		return true;
	}

	/**
	 * return can teleport
	 */
	public boolean processObjectTeleport(final WorldTile toTile) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick1(final WorldObject object) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processButtonClick(final int interfaceId,
			final int componentId, final int slotId, final int packetId) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick1(final NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick2(final NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick3(final NPC npc) {
		return true;
	}

	public boolean processNPCClick4(final NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick2(final WorldObject object) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick3(final WorldObject object) {
		return true;
	}

	public boolean processObjectClick5(final WorldObject object) {
		return true;
	}

	/**
	 * return let default death
	 */
	public boolean sendDeath() {
		return true;
	}

	/**
	 * return can move that step
	 */
	public boolean canMove(final int dir) {
		return true;
	}

	/**
	 * return can set that step
	 */
	public boolean checkWalkStep(final int lastX, final int lastY,
			final int nextX, final int nextY) {
		return true;
	}

	/**
	 * return remove controler
	 */
	public boolean login() {
		return true;
	}

	/**
	 * return remove controler
	 */
	public boolean logout() {
		return true;
	}

	public void forceClose() {
	}

	public boolean processItemOnNPC(final NPC npc, final Item item) {
		return true;
	}

	public boolean canDropItem(final Item item) {
		return true;
	}

	public boolean canSummonFamiliar() {
		return true;
	}

	public boolean canRemove(int slotId, int id) {
		return true;
	}
}
