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
import com.rs.game.player.controlers.LumbiTutorial;
import com.rs.game.player.cutscenes.actions.MovePlayerAction;
import com.rs.game.player.cutscenes.actions.NPCAnimationAction;
import com.rs.game.player.cutscenes.actions.NPCGraphicAction;


public class AccountSettings extends Dialogue {
	private int Ariane = 13930;
	private int npcId = 13930;

	@Override
	public void start() {
	
			stage = 2;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hello, I can adjust your account's settings."}, IS_NPC, npcId, 9847);
		
				
	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			  sendDialogue(SEND_5_OPTIONS, "Pick an Option",
                      "Change Password", "Recovery Questions", "Bank Pin - N/A",
                      "Change Display name(?)", "Nevermind.");
			  stage = 3;
		
		} else if (stage == 3) {
			 if (componentId == 1){
				 sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
							new String[] { player.getDisplayName(),
									"I'd like to change my password." },
							IS_PLAYER, player.getIndex(), 9827);
				 stage = 10;
			 }       else if (componentId == 2){
			
				
			 }else if (componentId == 3){
		end();
 }       else if (componentId == 4) {
end();
 }       else if (componentId == 5) {
	 		end();
	 
         }
		} else if (stage == 10) { //magics
			
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Alright mate."}, IS_NPC, 13930, 9847);
		stage = 11;
		}	 else if (stage == 11) {
		
			
			
		}
	
	
	
	
	
	
	
	
	}
	
	
	
                        
                        @Override
public void finish() {
}

 }
