package com.rs.player.controlers.trollinvasion;

import com.rs.content.dialogues.Dialogue;

public class TrollInvasionStart extends Dialogue {

	private boolean complexity;
	private TrollInvasion game;
	private int click;

	@Override
	public void start() {
		game = (TrollInvasion) parameters[0];
		click = (int) parameters[1];

	}

	@Override
	public void run(final int interfaceId, final int componentId) {
		switch (click) {
		case 1:
			doDialogueClickOne(interfaceId, componentId);
			break;
		case 2:
			doDialogueClickTwo(interfaceId, componentId);
		case 3:
			doDialogueClickThree(interfaceId, componentId);
		}

	}

	@Override
	public void finish() {

	}

	public void doDialogueClickOne(final int interfaceId, final int componentId) {

	}

	public void doDialogueClickTwo(final int interfaceId, final int componentId) {

	}

	public void doDialogueClickThree(final int interfaceId,
			final int componentId) {

	}

}
