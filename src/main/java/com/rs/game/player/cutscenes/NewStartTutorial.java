package com.rs.game.player.cutscenes;

import java.util.ArrayList;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.player.Player;
import com.rs.game.player.cutscenes.actions.ConstructMapAction;
import com.rs.game.player.cutscenes.actions.CreateNPCAction;
import com.rs.game.player.cutscenes.actions.CutsceneAction;
import com.rs.game.player.cutscenes.actions.DestroyCachedObjectAction;
import com.rs.game.player.cutscenes.actions.LookCameraAction;
import com.rs.game.player.cutscenes.actions.MoveNPCAction;
import com.rs.game.player.cutscenes.actions.MovePlayerAction;
import com.rs.game.player.cutscenes.actions.NPCAnimationAction;
import com.rs.game.player.cutscenes.actions.NPCFaceTileAction;
import com.rs.game.player.cutscenes.actions.NPCForceTalkAction;
import com.rs.game.player.cutscenes.actions.NPCGraphicAction;
import com.rs.game.player.cutscenes.actions.PlayerAnimationAction;
import com.rs.game.player.cutscenes.actions.PlayerFaceTileAction;
import com.rs.game.player.cutscenes.actions.PlayerForceTalkAction;
import com.rs.game.player.cutscenes.actions.PlayerGraphicAction;
import com.rs.game.player.cutscenes.actions.PlayerMusicEffectAction;
import com.rs.game.player.cutscenes.actions.PosCameraAction;

public class NewStartTutorial extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	private static int Ozan = 1;

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new MovePlayerAction(10, 0, 0, Player.WALK_MOVE_TYPE, 0)); // out
		
		
		
		
		
		
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
