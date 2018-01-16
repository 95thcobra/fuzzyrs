package com.rs.game.player.dialogues;//package com.rs.game.player.dialogues;
//
//import com.rs.Settings;
//import com.rs.cache.loaders.NPCDefinitions;
//import com.rs.game.World;
//import com.rs.game.WorldTile;
//import com.rs.game.player.Player;
//import com.rs.game.player.content.Magic;
//import com.rs.game.player.controlers.LumbiTutorial;
//import com.rs.game.player.cutscenes.actions.MovePlayerAction;
//
///**
// * Ozan. Starter Tutorial NPC.
// * 
// * @author Raghav
// * 
// */
//public class OzanD extends Dialogue {
//
//	private int npcId;
//
//	@Override
//	public void start() {
//		npcId = (int) parameters[0];
//		LumbiTutorial.tutorialstage = (int) parameters[1];
//		switch (LumbiTutorial.tutorialstage) {
//		case 0:
//			stage = 1;
//			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"Greetings, " + player.getDisplayName() + "!",
//					"Welcome to " + Settings.SERVER_NAME + ". I'm Ozan.",
//					"I'm here to guide you." }, IS_NPC, npcId, 9847);
//			
//					
//			break;
//		
//		}
//	}
//
//	@Override
//	public void run(int interfaceId, int componentId) {
//		switch (stage) {
//		case 1:
//			stage = 2;
//			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
//					new String[] { player.getDisplayName(),
//							"Hello, Ozan. Your help will be appreciated." },
//					IS_PLAYER, player.getIndex(), 9827);
//		break;
//		case 2:
//			stage = 3;
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"Well, without further ado,",
//					"let's begin!" }, IS_NPC, npcId, 9847);
//			break;
//		case 3:
//			stage = 4;
//			player.lock();
//			
//			setTutorialStage(1);
//			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
//					new String[] { player.getDisplayName(),
//							"Alright!" },
//					IS_PLAYER, player.getIndex(), 9827);
//		
//			break;
//		
//		case 4: 
//		//	player.teleportPlayer(3368, 3268, 0);
//			stage = 5;
//			setTutorialStage(2);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is the duel arena fuck face",
//					"this is where you lose your bank." }, IS_NPC, npcId, 9847);
//	
//			
//			break;
//	/*	case 5:
//			setTutorialStage(3);
//			stage = 6;
//			player.teleportPlayer(3370, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is Edgeville,",
//					"this is where you slay niggers." }, IS_NPC, npcId, 9847);
//			break;*/
//		case 5:
//			stage = 6;
//			player.lock();
//		//	player.teleportPlayer(3370, 3268, 0);
//			setTutorialStage(3);
//			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
//					new String[] { player.getDisplayName(),
//							"Alright!" },
//					IS_PLAYER, player.getIndex(), 9827);
//		
//			break;
//		case 6:
//			stage = 7;
//			setTutorialStage(4);
//			//player.teleportPlayer(3375, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is the Grand Exchange,",
//					"this is where you buy food stamps." }, IS_NPC, npcId, 9847);
//			break;
//		case 7:
//			stage = 8;
//			setTutorialStage(5);
//			player.teleportPlayer(3380, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is Castle Wars,",
//					"You slay eachother like tramps." }, IS_NPC, npcId, 9847);
//			break;
//		case 8:
//			stage = 9;
//			setTutorialStage(6);
//			player.teleportPlayer(3375, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is the teleporter,",
//					"He teleports you around the world." }, IS_NPC, npcId, 9847);
//			break;
//		case 9:
//			stage = 10;
//			setTutorialStage(7);
//			player.teleportPlayer(3350, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is the quest guide,",
//					"he will inform your ass about quests we have at",
//					"Adrastos." }, IS_NPC, npcId, 9847);
//			break;
//		case 10:
//			stage = 11;
//			setTutorialStage(8);
//			player.setNextWorldTile(new WorldTile(3350, 3268, 0));
//			//player.teleportPlayer(3350, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"This is the market,",
//					"You trade your items and your sperm here." }, IS_NPC, npcId, 9847);
//			break;
//		case 11:
//			stage = 12;
//			setTutorialStage(9);
//			player.teleportPlayer(3360, 3268, 0);
//			sendEntityDialogue(Dialogue.SEND_2_TEXT_CHAT, new String[] {
//					NPCDefinitions.getNPCDefinitions(npcId).name,
//					"Well, that's all for now!",
//					"You can always ask a moderator or an admin for help." }, IS_NPC, npcId, 9847);
//			break;
//		case 12:
//			stage = 13;
//			setTutorialStage(10);
//			player.teleportPlayer(3360, 3268, 0);
//			player.hasDoneTutorial = true;
//			player.getInterfaceManager().sendCombatStyles();
//			player.getInterfaceManager().sendTaskSystem();
//			player.getInterfaceManager().sendSkills();
//			player.getInterfaceManager().sendInventory();
//			player.getInterfaceManager().sendEquipment();
//			player.getInterfaceManager().sendPrayerBook();
//			player.getInterfaceManager().sendMagicBook();
//			player.getInterfaceManager().sendEmotes();
//			player.getInterfaceManager().sendFriends();
//			player.getInterfaceManager().sendQuestTab();
//			player.getInterfaceManager().sendFriendsChat();
//			player.getInterfaceManager().sendClanChat();
//			player.getInterfaceManager().sendSettings();
//			player.getInterfaceManager().sendMusic();
//			player.getInterfaceManager().sendNotes();
//			break;
//		
//		
//		
//		
//		
//		
//		
//		
//		}
//	}
//
//	public void setTutorialStage(int tutorialstage) {
//		LumbiTutorial.tutorialstage = tutorialstage;
//	}
//	public int getTutorialStage() {
//		return LumbiTutorial.tutorialstage;
//	}
//	@Override
//	public void finish() {
//
//	}
//
//}
