package com.rs.content.actions.impl;

import com.rs.content.actions.Action;
import com.rs.content.dialogues.impl.GrilleGoatsDialogue;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.item.Item;

public class CowMilkingAction extends Action {

	public static final int EMPTY_BUCKET = 1925;

	public CowMilkingAction() {

	}

	@Override
	public boolean start(final Player player) {
		if (!player.getInventory().containsItem(EMPTY_BUCKET, 1)) {
			player.getDialogueManager().startDialogue(GrilleGoatsDialogue.class);
			return false;
		}
		return true;
	}

	@Override
	public boolean process(final Player player) {
		return player.getInventory().hasFreeSlots()
				&& player.getInventory().containsItem(EMPTY_BUCKET, 1);
	}

	@Override
	public int processWithDelay(final Player player) {
		player.setNextAnimation(new Animation(2305));
		player.getInventory().deleteItem(new Item(EMPTY_BUCKET, 1));
		player.getInventory().addItem(new Item(1927));
		player.getPackets().sendGameMessage("You milk the cow.");
		return 5;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

}
