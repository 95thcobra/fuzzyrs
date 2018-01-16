package com.rs.content.cutscenes.impl;

import java.util.ArrayList;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;
import com.rs.content.cutscenes.actions.CutsceneAction;
import com.rs.content.cutscenes.actions.LookCameraAction;
import com.rs.content.cutscenes.actions.PosCameraAction;

public class TowersPkCutscene extends Cutscene {

	@Override
	public CutsceneAction[] getActions(final Player player) {
		final ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();

		actionsList.add(new PosCameraAction(getX(player, player.getX() - 5),
				getY(player, player.getY() + 7), 8000, 6, 6, -1));
		actionsList.add(new LookCameraAction(getX(player, player.getX()), getY(
				player, player.getY() + 7), 6000, 6, 6, 10));

		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

}
