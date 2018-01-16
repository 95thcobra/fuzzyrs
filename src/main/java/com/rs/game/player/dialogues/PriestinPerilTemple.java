package com.rs.game.player.dialogues;/*package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.player.quests.impl.PriestInPeril;



public class PriestinPerilTemple extends Dialogue {

	private int npcId = 3074;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (!Player.knockedOnDoor) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ehm, umm what do you want?" }, IS_NPC, npcId, 9827);
			stage = 1;
		} /*else 	if (!Player.killedDog && Player.knockedOnDoor) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Kill the dog! We can still here it!" }, IS_NPC, npcId, 9827);
			end();
		}*/
	
	
/*	
	}


	@Override
	public void run(int interfaceId, int componentId) {

		if (stage == 1) {
		stage = 2;
		sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(),
						"I was sent by King Roald to help the temple!" },
				IS_PLAYER, player.getIndex(), 9827);
		
		 
		 }  else if (stage == 2) {
			 stage = 3;
			 sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"You were sent by King Roald?",
						"Oh right, helping the temple.. umm.. there's a large dog",
						"in the dungeon, barking and keeping us from our work." }, IS_NPC, npcId, 9847);
			
		} else if (stage == 3) {
			player.knockedOnDoor = true;
			end();
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Got it!" },
					IS_PLAYER, player.getIndex(), 9827);
		} else if (stage == 11) {
			end(); //got up until 15 to add more.
		} else if (stage == 16) {
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Varrock is a great city,",
					"players come to trade at the Grand Exchange, or",
					"to sit in the bank or do many adventures here." }, IS_NPC, npcId, 9847);//up until 19
		} else if (stage == 20) {
			stage = 21;
			sendEntityDialogue(Dialogue.SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Actually, yes.",
					"We have not heard from the Temple in a while,",
					"can you please check it out?",
					"The temple is to the East of here."}, IS_NPC, npcId, 9847);
		} else if (stage == 21) {
			 sendDialogue(SEND_2_OPTIONS, "Pick an Option",
	                 "Start Priest in Peril", "Maybe later.");
			 
				  stage = 22;
		} else if (stage == 22) {
			if (componentId == 1) {
				stage = 25;
				Player.inProgressPriestinPeril = true;
				Player.startedPriestinPeril = true;
				Player.talkedToRoald = true;
				sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"I'd love to help." },
						IS_PLAYER, player.getIndex(), 9827);
			} else if (componentId == 2) {
				stage = 23;
				sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"No thanks." },
						IS_PLAYER, player.getIndex(), 9827);
			} 
		} else if (stage == 23) {
			end();
		} else if (stage == 25) {
			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Alright, great!",
					"Report back when you make progress." }, IS_NPC, npcId, 9847);
		} else if (stage == 60) {
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"What do I need to do again?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 61;
		} else if (stage == 61) {
			stage = 62;
			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You need to figure out what happened at the temple,",
					"Far east, outside of the Varrock walls." }, IS_NPC, npcId, 9847);
			
		} else if (stage == 62) {
			end();
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Okay, thanks!" },
					IS_PLAYER, player.getIndex(), 9827);
			
		} else if (stage == 63){ 
	
		
		}
		
	 
	 
	 
	 
	 
	
	
	
	
	
	
	}
	
	
	@Override
	public void finish() {

	}

}
	
	*/
