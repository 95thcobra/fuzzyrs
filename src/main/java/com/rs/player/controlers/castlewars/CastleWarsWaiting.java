package com.rs.player.controlers.castlewars;

import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.minigames.castlewars.CastleWars;
import com.rs.content.minigames.castlewars.CastleWarsConstants;
import com.rs.player.Equipment;
import com.rs.player.controlers.Controller;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

public class CastleWarsWaiting extends Controller {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	// You can't leave just like that!

	public void leave() {
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 34 : 0);
		CastleWars.removeWaitingPlayer(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 34 : 0, 57);
	}

	@Override
	public boolean processButtonClick(final int interfaceId,
			final int componentId, final int slotId, final int packetId) {
		if (interfaceId == 387) {
			if (componentId == 9 || componentId == 6) {
				player.getPackets().sendGameMessage(
						"You can't remove your team's colours.");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canEquip(final int slotId, final int itemId) {
		if (slotId == Equipment.SLOT_CAPE || slotId == Equipment.SLOT_HAT) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		removeControler();
		leave();
		return true;
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(CastleWarsConstants.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(final WorldTile toTile) {
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		final int id = object.getId();
		if (id == 4389 || id == 4390) {
			removeControler();
			leave();
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(final int type) {
		removeControler();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}
