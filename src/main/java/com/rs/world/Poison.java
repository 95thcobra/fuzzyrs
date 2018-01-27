package com.rs.world;

import com.rs.entity.Entity;
import com.rs.utils.Utils;
import com.rs.player.Player;

import java.io.Serializable;

public final class Poison implements Serializable {

	private static final long serialVersionUID = -6324477860776313690L;

	private transient Entity entity;
	private int poisonDamage;
	private int poisonCount;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(final Entity entity) {
		this.entity = entity;
	}

	public void makePoisoned(final int startDamage) {
		if (poisonDamage > startDamage)
			return;
		if (entity instanceof Player) {
			final Player player = ((Player) entity);
			if (player.getPoisonImmune() > Utils.currentTimeMillis())
				return;
			if (poisonDamage == 0) {
				player.getPackets().sendGameMessage("You are poisoned.");
			}
		}
		poisonDamage = startDamage;
		refresh();
	}

	public void processPoison() {
		if (!entity.isDead() && isPoisoned()) {
			if (poisonCount > 0) {
				poisonCount--;
				return;
			}
			boolean heal = false;
			if (entity instanceof Player) {
				final Player player = ((Player) entity);
				// inter opened we dont poison while inter opened like at rs
				if (player.getInterfaceManager().containsScreenInter())
					return;
				if (player.getAuraManager().hasPoisonPurge()) {
					heal = true;
				}
			}
			entity.applyHit(new Hit(entity, poisonDamage,
					heal ? Hit.HitLook.HEALED_DAMAGE : Hit.HitLook.POISON_DAMAGE));
			poisonDamage -= 2;
			if (isPoisoned()) {
				poisonCount = 30;
				return;
			}
			reset();
		}
	}

	public void reset() {
		poisonDamage = 0;
		poisonCount = 0;
		refresh();
	}

	public void refresh() {
		if (entity instanceof Player) {
			final Player player = ((Player) entity);
			player.getPackets().sendConfig(102, isPoisoned() ? 1 : 0);
		}
	}

	public boolean isPoisoned() {
		return poisonDamage >= 1;
	}
}
