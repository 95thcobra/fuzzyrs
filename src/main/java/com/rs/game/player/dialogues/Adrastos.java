package com.rs.game.player.dialogues;

public class Adrastos extends Dialogue {

	@Override
	public void start() {
		player.lock();
		player.getDialogueManager().startDialogue("SimpleMessage", "All new accounts have been locked at this moment.");
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
