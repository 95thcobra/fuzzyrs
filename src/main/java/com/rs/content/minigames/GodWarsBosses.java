package com.rs.content.minigames;

import com.rs.world.npc.godwars.GodWarMinion;

public final class GodWarsBosses {

	public static final GodWarMinion[] graardorMinions = new GodWarMinion[3];
	public static final GodWarMinion[] commanderMinions = new GodWarMinion[3];
	public static final GodWarMinion[] zamorakMinions = new GodWarMinion[3];
	public static final GodWarMinion[] armadylMinions = new GodWarMinion[3];

	private GodWarsBosses() {

	}

	public static void respawnBandosMinions() {
		for (final GodWarMinion minion : graardorMinions) {
			if (minion.hasFinished() || minion.isDead()) {
				minion.respawn();
			}
		}
	}

	public static void respawnSaradominMinions() {
		for (final GodWarMinion minion : commanderMinions) {
			if (minion.hasFinished() || minion.isDead()) {
				minion.respawn();
			}
		}
	}

	public static void respawnZammyMinions() {
		for (final GodWarMinion minion : zamorakMinions) {
			if (minion.hasFinished() || minion.isDead()) {
				minion.respawn();
			}
		}
	}

	public static void respawnArmadylMinions() {
		for (final GodWarMinion minion : armadylMinions) {
			if (minion.hasFinished() || minion.isDead()) {
				minion.respawn();
			}
		}
	}
}
