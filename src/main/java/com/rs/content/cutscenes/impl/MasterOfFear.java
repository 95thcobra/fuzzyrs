package com.rs.content.cutscenes.impl;

import java.util.ArrayList;

import com.rs.content.cutscenes.Cutscene;
import com.rs.content.cutscenes.actions.CutsceneAction;
import com.rs.content.cutscenes.actions.PosCameraAction;
import com.rs.player.Player;
import com.rs.content.cutscenes.actions.InterfaceAction;
import com.rs.content.cutscenes.actions.LookCameraAction;

public class MasterOfFear extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public CutsceneAction[] getActions(final Player player) {
		final ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new InterfaceAction(115, 2));
		actionsList.add(new PosCameraAction(getX(player, player.getX() + 5),
				getY(player, player.getY() + 3), 1500, -1));
		actionsList.add(new LookCameraAction(getX(player, player.getX() - 2),
				getY(player, player.getY()), 1500, 5));
		return actionsList.toArray(new CutsceneAction[0]);
	}
}
