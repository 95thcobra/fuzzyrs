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


public class Ariane extends Dialogue {
	private int Ariane = 13930;
	private int npcId = 13930;

	@Override
	public void start() {
	
			stage = 2;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hello, I can change your spell/prayer books."}, IS_NPC, 13930, 9847);
		
				
	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			  sendDialogue(SEND_5_OPTIONS, "Pick an Option",
                      "Change Magic Book", "Change Prayer Book", "Restore Pray/HP",
                      "Restore All - Members", "Nevermind.");
			  stage = 3;
		
		} else if (stage == 3) {
			 if (componentId == 1){
				  sendDialogue(SEND_4_OPTIONS, "Choose a Spell Book",
	                      "Ancient Spell Book", "Lunar Spell Book", "Normal Spell Book",
	                      "Nevermind");
				  stage = 10;
			 }       else if (componentId == 2){
				 if (!player.getPrayer().isAncientCurses()) {
					 player.getPrayer().setPrayerBook(true);
					 sendDialogue(
								SEND_1_TEXT_CHAT,
								"",
								"You switch to Curse Prayers.");
				 } else {
					 player.getPrayer().setPrayerBook(false);
					 sendDialogue(
								SEND_1_TEXT_CHAT,
								"",
								"You switch to Normal Prayers.");
				 }
				 /* sendDialogue(SEND_3_OPTIONS, "Choose a Prayer Book",
	                      "Curses", "Normal", "Nevermind");
				
				 stage = 20;*/
			 }else if (componentId == 3){
				 player.setNextGraphics(new Graphics(2232));
				 new NPCGraphicAction(Ariane, new Graphics(2220), 3);
					
				 player.NonDonatorReset();
        		end();
 }       else if (componentId == 4) {
	 player.setNextGraphics(new Graphics(2258));
	 new NPCGraphicAction(Ariane, new Graphics(2223), 3);		
	 player.DonatorReset();
	 		end();
 }       else if (componentId == 5) {
	 		end();
	 
         }
		} else if (stage == 10) { //magics
			if (componentId == 1) {
				player.getCombatDefinitions().setSpellBook(1);
			} else if (componentId == 2) {
				player.getCombatDefinitions().setSpellBook(2);
			} else if (componentId == 3) {
				player.getCombatDefinitions().setSpellBook(0);
			} else if (componentId == 4) {
				end();
			}
		}
		
		
	
	
	
	
	
	
	
	
	}
	
	
	
                        
                        @Override
public void finish() {
}

 }
