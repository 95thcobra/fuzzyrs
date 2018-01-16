package com.rs.content.cutscenes.impl;

import java.util.ArrayList;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;
import com.rs.content.cutscenes.actions.CutsceneAction;
import com.rs.content.cutscenes.actions.PosCameraAction;
import com.rs.world.WorldTile;
import com.rs.content.cutscenes.actions.LookCameraAction;

public class NexCutScene extends Cutscene {

	private final WorldTile dir;
	private final int selected;

	public NexCutScene(final WorldTile dir, final int selected) {
		this.dir = dir;
		this.selected = selected;
	}

	@Override
	public boolean hiddenMinimap() {
		return false;
	}

	@Override
	public CutsceneAction[] getActions(final Player player) {
		int xExtra = 0;
		int yExtra = 0;
		if (selected == 0) {
			yExtra -= 7;
		} else if (selected == 2) {
			yExtra += 7;
		} else if (selected == 1) {
			xExtra -= 7;
		} else {
			xExtra += 7;
		}
		final ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new PosCameraAction(getX(player, 2925 + xExtra), getY(
				player, 5203 + yExtra), 2500, -1));
		actionsList.add(new LookCameraAction(getX(player, dir.getX()), getY(
				player, dir.getY()), 2500, 3));
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
