package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.LumbiTutorial;
import com.rs.game.player.cutscenes.actions.MovePlayerAction;


public class QuestGuide extends Dialogue {

	private int npcId = 949;

	@Override
	public void start() {
	
			stage = 2;
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hey mate!",
					"I am the quest guide, and I tell you about the quests,",
					"We have here at Adrastos." }, IS_NPC, 949, 9847);
		
				
	}
	@Override
	public void run(int interfaceId, int componentId) {
		/*if (stage == 1) { not being used lol
		
			stage = 2;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Wow ur so c00l." },
					IS_PLAYER, player.getIndex(), 9827);
		} else*/ if (stage == 2) {
			  sendDialogue(SEND_5_OPTIONS, "Pick a Quest",
                      "Recipe for Disaster", "Desert Treasure", "Temple at Sennisten",
                      "Lunar Diplomacy", "More Options");
			  stage = 3;
		} else if (stage == 3) {
			 if (componentId == 1){
				end();	
				 player.getInterfaceManager().sendInterface(
							Integer.valueOf("145"));
 }       else if (componentId == 2){ //stage = 20 - 29
		sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Desert Treasure is a medium quest,",
				"you need to talk to the Archaeologist in the desert,",
				"to start this quest." }, IS_NPC, npcId, 9847);
		stage = 20;
 }else if (componentId == 3){
	 stage = 30;
	 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
 					NPCDefinitions.getNPCDefinitions(npcId).name,
 					"Temple at Sennisten is a grandmaster quest,",
 					"you need to talk to Ali the Wise in Nardah",
 					"to start this quest." }, IS_NPC, npcId, 9847);
 }       else if (componentId == 4) {
	stage = 40;
	 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Lunar Diplomacy is a medium quest,",
				"you need to talk to blah blah and masterbate",
				"gircat sucks dick" }, IS_NPC, npcId, 9847);
 }       else if (componentId == 5) { //more options
	 sendDialogue(SEND_5_OPTIONS, "Pick a Quest",
             "Nomads requiem", "Ritual of Mahjewrat", "Priest in Peril",
             "Coming soon", "Coming soon");
	  stage = 4;
         }
		} else if (stage == 4) {
			 if (componentId == 1){
				 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Nomads Requiem is a Grand Master quest,",
							"you need to talk to blah blah and masterbate",
							"gircat sucks dick" }, IS_NPC, npcId, 9847);
 }       else if (componentId == 2){
		sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Ritual of Mahjewrat is a medium quest,",
				"you need to talk to blah blah and masterbate",
				"gircat sucks dick" }, IS_NPC, npcId, 9847);
         }else if (componentId == 3){
        	 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
 					NPCDefinitions.getNPCDefinitions(npcId).name,
 					"Priest in Peril is a easy quest,",
 					"blah blah",
 					"gircat sucks dick" }, IS_NPC, npcId, 9847);
 }       else if (componentId == 4) {
	stage = 40;
	/* sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Lunar Diplomacy is a medium quest,",
				"you need to talk to blah blah and masterbate",
				"gircat sucks dick" }, IS_NPC, npcId, 9847);*/
 }       else if (componentId == 5) { //more options
	/* sendDialogue(SEND_5_OPTIONS, "Pick a Quest",
             "Nomads requiem", "Ritual of Mahjewrat", "Coming soon",
             "Coming soon", "Coming soon");
	  stage = 5;*/
 }
		} else if (stage == 20){
			  sendDialogue(SEND_2_OPTIONS, "Pick an Option",
                      "Get more information", "No thanks");
			  stage = 21;
		} else if (stage == 21) {
			if (componentId == 1) {
				stage = 22;
				sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"I would like to hear some more information." },
						IS_PLAYER, player.getIndex(), 9827);
			} else if (componentId == 2) {
			
				end();
			}
			
		} else if (stage == 22) {
			stage = 23;
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Alright great, you will need multiple requirements,",
					"for this quest (check quest tab).",
					"You need to have completed Priest in Peril." }, IS_NPC, npcId, 9847);
		} else if (stage == 23) {
			stage = 24;
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You will need to fight 4 bosses for 4 gems,",
					"They are not easy, so be prepared to use food.",
					"The rewards are 20k magic XP, and ancient magics." }, IS_NPC, npcId, 9847);
		} else if (stage == 24) { //have until stage 29 for more info :P
		
			end();
			
		} else if (stage == 30) {
			 sendDialogue(SEND_2_OPTIONS, "Pick an Option",
                     "Get more information", "No thanks");
			  stage = 31;
		} else if (stage == 31) {
			if (componentId == 1) {
				stage = 32;
				sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"I would like to hear some more information." },
						IS_PLAYER, player.getIndex(), 9827);
			} else if (componentId == 2) {
				end();
			}
		} else if (stage == 32) {
			stage = 33;
			sendEntityDialogue(Dialogue.SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Alright, awesome. This quest is a hard quest,",
					"it will require a lot of time and skills.",
					"You must have completed Desert Treasure,",
					"to start this quest."}, IS_NPC, npcId, 9847);
		} else if (stage == 33) {
			stage = 34;
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"There are two parts to this quest,",
					"that lead up to one goal, getting the",
					"FrostenHorn."}, IS_NPC, npcId, 9847);
		} else if (stage == 34) {
			stage = 35;
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"After you complete the quest,",
					"you will have full access to the Ancient Curses,",
					"you can activate them at Ariane."}, IS_NPC, npcId, 9847);
		} else if (stage == 35) {
			end();
		}
		
		
	
	
	
	
	
	
	
	
	}
	
	
	
                        
                        @Override
public void finish() {
}

 }
