package com.rs.player.controlers;

import com.rs.content.dialogues.impl.DagonHai;
import com.rs.player.Player;
import com.rs.player.content.Magic;
import com.rs.world.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

public class BorkController extends Controller {

	public static int borkStage;
	public NPC bork;
	int stage = 0;

	@Override
	public void start() {
		borkStage = (int) getArguments()[0];
		bork = (NPC) getArguments()[1];
		process();
	}

	@Override
	public void process() {
		if (borkStage == 0) {
			if (stage == 0) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3114,
						5528, 0));
			}
			if (stage == 5) {
				sendInterfaces();
			}
			if (stage == 18) {
				player.getPackets().closeInterface(
						player.getInterfaceManager().hasRezizableScreen() ? 1
								: 11);
				player.getDialogueManager().startDialogue(DagonHai.class, 7137,
						player, -1);
				player.getPackets()
						.sendGameMessage(
								"The choas teleporter transports you to an unknown portal.");
				removeControler();
			}
		} else if (borkStage == 1) {
			if (stage == 4) {
				sendInterfaces();
				bork.setCantInteract(true);
			} else if (stage == 14) {
				World.spawnNPC(7135, new WorldTile(bork, 1), -1, true, true);
				World.spawnNPC(7135, new WorldTile(bork, 1), -1, true, true);
				World.spawnNPC(7135, new WorldTile(bork, 1), -1, true, true);
				player.getPackets().closeInterface(
						player.getInterfaceManager().hasRezizableScreen() ? 1
								: 11);
				bork.setCantInteract(false);
				bork.setNextForceTalk(new ForceTalk(
						"Destroy the intruder, my Legions!"));
				removeControler();
			}
		}
		stage++;
	}

	@Override
	public void sendInterfaces() {
		if (borkStage == 0) {
			player.getInterfaceManager().sendTab(
					player.getInterfaceManager().hasRezizableScreen() ? 1 : 11,
					692);
		} else if (borkStage == 1) {
			for (final Entity t : bork.getPossibleTargets()) {
				final Player pl = (Player) t;
				pl.getInterfaceManager().sendTab(
						pl.getInterfaceManager().hasRezizableScreen() ? 1 : 11,
						691);
			}
		}
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		return true;
	}

	@Override
	public boolean keepCombating(final Entity target) {
		return !(borkStage == 1 && stage == 4);
	}

	@Override
	public boolean canEquip(final int slotId, final int itemId) {
		return !(borkStage == 1 && stage == 4);
	}

	@Override
	public boolean canAttack(final Entity target) {
		return !(borkStage == 1 && stage == 4);
	}

	@Override
	public boolean canMove(final int dir) {
		return !(borkStage == 1 && stage == 4);
	}

}
