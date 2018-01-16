package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.LumbiTutorial;
import com.rs.game.player.cutscenes.actions.MovePlayerAction;


public class QuestHolder extends Dialogue {

	private int npcId = 949;

	@Override
	public void start() {
	
			stage = 1;
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hey mate!",
					"I am the quest guide, and I tell you about the quests,",
					"We have here at Adrastos." }, IS_NPC, 949, 9847);
		
				
	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
		
			stage = 2;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Wow ur so c00l." },
					IS_PLAYER, player.getIndex(), 9827);
		} else if (stage == 2) {
			  sendDialogue(SEND_5_OPTIONS, "Pick a Quest",
                      "Recipe for Disaster", "Desert Treasure", "Temple at Sennisten",
                      "Lunar Diplomacy", "More Options");
			  stage = 3;
		} else if (stage == 3) {
			 if (componentId == 1){
				 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Recipe for Disaster is a LONG quest,",
							"you need to talk to blah blah and masterbate",
							"gircat sucks dick" }, IS_NPC, npcId, 9847);
 }       else if (componentId == 2){
		sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Desert Treasure is a medium quest,",
				"you need to talk to blah blah and masterbate",
				"gircat sucks dick" }, IS_NPC, npcId, 9847);
         }else if (componentId == 3){
        	 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
 					NPCDefinitions.getNPCDefinitions(npcId).name,
 					"Temple at Sennisten is a grandmaster quest,",
 					"you need to talk to blah blah and masterbate",
 					"gircat sucks dick" }, IS_NPC, npcId, 9847);
 }       else if (componentId == 4) {
	stage = 40;
	 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Lunar Diplomacy is a medium quest,",
				"you need to talk to blah blah and masterbate",
				"gircat sucks dick" }, IS_NPC, npcId, 9847);
 }       else if (componentId == 5) { //more options
	 sendDialogue(SEND_5_OPTIONS, "Pick a Quest",
             "Nomads requiem", "Ritual of Mahjewrat", "Coming soon",
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
        	 /*sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
 					NPCDefinitions.getNPCDefinitions(npcId).name,
 					"Temple at Sennisten is a grandmaster quest,",
 					"you need to talk to blah blah and masterbate",
 					"gircat sucks dick" }, IS_NPC, npcId, 9847);*/
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
		}
		
		
	
	
	
	
	
	
	
	
	}
	
	
	
                        
                        @Override
public void finish() {
}

 }
