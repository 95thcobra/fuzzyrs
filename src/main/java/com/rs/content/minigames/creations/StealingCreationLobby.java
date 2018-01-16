package com.rs.content.minigames.creations;

import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.player.controlers.Controller;
import com.rs.world.WorldTile;

/**
 * @author Richard
 * @author Khaled
 *
 */
public class StealingCreationLobby extends Controller {

	@Override
	public void start() {
		if ((boolean) getArguments()[0]) {
			StealingCreation.getRedTeam().add(player);
		} else {
			StealingCreation.getBlueTeam().add(player);
		}
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 27, 804);// TODO find correct one
		StealingCreation.updateInterfaces();
	}

	// TODO object click for exit

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue(SimpleMessage.class,
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue(SimpleMessage.class,
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public void magicTeleported(final int type) {
		player.getControllerManager().forceStop();
	}

	@Override
	public void forceClose() {
		if ((boolean) getArguments()[0]) {
			StealingCreation.getRedTeam().remove(player);
		} else {
			StealingCreation.getBlueTeam().remove(player);
		}
		StealingCreation.updateInterfaces();
		player.closeInterfaces();
	}
}
