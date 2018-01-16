package com.rs.content.cutscenes.impl;

import java.util.ArrayList;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;
import com.rs.content.cutscenes.actions.CutsceneAction;
import com.rs.content.cutscenes.actions.LookCameraAction;
import com.rs.content.cutscenes.actions.PosCameraAction;

public class EdgeWilderness extends Cutscene {

	@Override
	public CutsceneAction[] getActions(final Player player) {
		final ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();

		actionsList.add(new PosCameraAction(80, 75, 5000, 6, 6, -1));
		actionsList.add(new LookCameraAction(30, 75, 1000, 6, 6, 10));
		actionsList.add(new PosCameraAction(30, 75, 5000, 3, 3, 10));

		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

}
