package com.rs.content.minigames.clanwars;

import com.rs.player.Player;
import com.rs.player.combat.PlayerCombat;
import com.rs.player.content.Foods.Food;
import com.rs.player.content.Pots.Pot;
import com.rs.player.controlers.Controller;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * A controler subclass handling players in the clan wars activity.
 * 
 * @author Emperor
 *
 */
public final class WarController extends Controller {

	/**
	 * The clan wars instance.
	 */
	private transient ClanWars clanWars;

	/**
	 * Constructs a new {@code WarController} {@code Object}.
	 */
	public WarController() {
		/*
		 * empty.
		 */
	}

	@Override
	public void start() {
		this.clanWars = (ClanWars) super.getArguments()[0];
		player.setCanPvp(true);
		player.getPackets().sendPlayerOption("Attack", 1, true);
		moved();
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					if (clanWars.get(ClanWars.Rules.ITEMS_LOST)) {
						final Player killer = player
								.getMostDamageReceivedSourcePlayer();
						if (killer != null) {
							killer.removeDamage(player);
							killer.increaseKillCount(player);
							player.sendItemsOnDeath(killer);
						}
						player.getEquipment().init();
						player.getInventory().init();
					}
					player.reset();
					if (clanWars.getFirstTeam() == player.getClanManager()) {
						player.setNextWorldTile(clanWars.getBaseLocation()
								.transform(
										clanWars.getAreaType()
												.getFirstDeathOffsetX(),
										clanWars.getAreaType()
												.getFirstDeathOffsetY(), 0));
						clanWars.getFirstPlayers().remove(player);
						clanWars.getFirstViewers().add(player);
						final int firstKills = clanWars.getKills() & 0xFFFF;
						final int secondKills = (clanWars.getKills() >> 24 & 0xFFFF) + 1;
						clanWars.setKills(firstKills | (secondKills << 24));
					} else {
						final WorldTile northEast = clanWars.getBaseLocation()
								.transform(
										clanWars.getAreaType()
												.getNorthEastTile().getX()
												- clanWars.getAreaType()
												.getSouthWestTile()
												.getX(),
										clanWars.getAreaType()
												.getNorthEastTile().getY()
												- clanWars.getAreaType()
												.getSouthWestTile()
												.getY(), 0);
						player.setNextWorldTile(northEast.transform(clanWars
										.getAreaType().getSecondDeathOffsetX(),
								clanWars.getAreaType().getSecondDeathOffsetY(),
								0));
						clanWars.getSecondPlayers().remove(player);
						clanWars.getSecondViewers().add(player);
						final int firstKills = (clanWars.getKills() & 0xFFFF) + 1;
						final int secondKills = clanWars.getKills() >> 24 & 0xFFFF;
						clanWars.setKills(firstKills | (secondKills << 24));
					}
					clanWars.updateWar();
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean canEat(final Food food) {
		if (clanWars.get(ClanWars.Rules.NO_FOOD)) {
			player.getPackets().sendGameMessage(
					"Food has been disabled during this war.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canPot(final Pot pot) {
		if (clanWars.get(ClanWars.Rules.NO_POTIONS)) {
			player.getPackets().sendGameMessage(
					"Potions has been disabled during this war.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		player.getPackets().sendGameMessage(
				"You can't teleport out of a clan war!");
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		player.getPackets().sendGameMessage(
				"You can't teleport out of a clan war!");
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		switch (object.getId()) {
		case 38697:
		case 28140:
			clanWars.getFirstViewers().remove(player);
			clanWars.getSecondViewers().remove(player);
		case 38696:
		case 38695:
		case 28139:
		case 38694:
		case 28214:
			clanWars.leave(player, true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canAttack(final Entity target) {
		if (!clanWars.getFirstPlayers().contains(player)
				&& !clanWars.getSecondPlayers().contains(player))
			return false;
		if (clanWars.getFirstPlayers().contains(player)
				&& clanWars.getFirstPlayers().contains(target)) {
			player.getPackets().sendGameMessage(
					"You can't attack players in your own team.");
			return false;
		}
		if (clanWars.getSecondPlayers().contains(player)
				&& clanWars.getSecondPlayers().contains(target)) {
			player.getPackets().sendGameMessage(
					"You can't attack players in your own team.");
			return false;
		}
		return clanWars.getClanWarsTask().isStarted();
	}

	@Override
	public boolean keepCombating(final Entity victim) {
		final boolean isRanging = PlayerCombat.isRanging(player) != 0;
		if (player.getCombatDefinitions().getSpellId() > 0) {
			switch (clanWars.getMagicRuleCount()) {
			case 1: // Standard spells only.
				if (player.getCombatDefinitions().getSpellBook() != 0) {
					player.getPackets().sendGameMessage(
							"You can only use modern spells during this war!");
					return false;
				}
				break;
			case 2: // Bind/Snare/Entangle only.
				if (player.getCombatDefinitions().getSpellBook() != 0) {
					player.getPackets().sendGameMessage(
							"You can only use binding spells during this war!");
					return false;
				}
				switch (player.getCombatDefinitions().getSpellId()) {
				case 36:
				case 55:
				case 81:
					break;
				default:
					player.getPackets().sendGameMessage(
							"You can only use binding spells during this war!");
					return false;
				}
				break;
			case 3: // No magic at all.
				player.getPackets().sendGameMessage(
						"Magic combat is not allowed during this war!");
				return false;
			}
		}
		if (isRanging && clanWars.get(ClanWars.Rules.NO_RANGE)) {
			player.getPackets().sendGameMessage(
					"Ranged combat is not allowed during this war!");
			return false;
		}
		if (!isRanging && clanWars.get(ClanWars.Rules.NO_MELEE)
				&& player.getCombatDefinitions().getSpellId() <= 0) {
			player.getPackets().sendGameMessage(
					"Melee combat is not allowed during this war!");
			return false;
		}
		return true;
	}

	@Override
	public void moved() {
		switch (clanWars.getAreaType()) {
		case PLATEAU:
		case TURRETS:
			player.setForceMultiArea(true);
			break;
		case FORSAKEN_QUARRY:
			final WorldTile northEast = clanWars
					.getBaseLocation()
					.transform(
							clanWars.getAreaType().getNorthEastTile().getX()
									- clanWars.getAreaType().getSouthWestTile()
											.getX(),
							clanWars.getAreaType().getNorthEastTile().getY()
									- clanWars.getAreaType().getSouthWestTile()
											.getY(), 0).transform(-16, -16, 0);
			final WorldTile southWest = clanWars.getBaseLocation().transform(
					16, 16, 0);
			player.setForceMultiArea(player.getX() >= southWest.getX()
					&& player.getY() >= southWest.getY()
					&& player.getX() <= northEast.getX()
					&& player.getY() <= northEast.getY());
			break;
		}
	}

	@Override
	public void forceClose() {
		player.setCanPvp(false);
		player.getPackets().sendPlayerOption("null", 1, true);
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(2992, 9676, 0));
		return true;
	}

}