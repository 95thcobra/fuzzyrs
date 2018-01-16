package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Prayer;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.LumbiTutorial;
import com.rs.game.player.cutscenes.actions.MovePlayerAction;
import com.rs.game.player.cutscenes.actions.NPCAnimationAction;
import com.rs.game.player.cutscenes.actions.NPCGraphicAction;
import com.rs.utils.ShopsHandler;

/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Aug 12, 2014 at 1:15:58 AM.
 * @author Ferret
 */
public class Xuan extends Dialogue {
	private int Xuan = 13727;
	private int npcId = 13727;

	@Override
	public void start() {

		stage = 2;
		sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hello, I can change your title, and show you the loyalty",
		"point shop."}, IS_NPC, npcId, 9847);


	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			sendDialogue(SEND_4_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE,
					"Change title", "Loyalty point shop", "Who are you?", "Nevermind.");
			stage = 3;

		} else if (stage == 3) {
			if (componentId == 1){
				sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
						"I'd like to manage my title." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 10;
			}       else if (componentId == 2){
				ShopsHandler.openShop(player, 13727);
				end();
			} else if (componentId == 3){

			}
		} else if (stage == 10) {
			end();
		}










	}




	@Override
	public void finish() {
	}

}
