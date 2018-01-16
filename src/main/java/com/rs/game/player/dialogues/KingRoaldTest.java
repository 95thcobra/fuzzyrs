package com.rs.game.player.dialogues;//package com.rs.game.player.dialogues;
//
//import com.rs.cache.loaders.NPCDefinitions;
//import com.rs.game.player.Player;
//import com.rs.game.player.quests.impl.PriestinPeril;
//
//public class KingRoaldTest extends Dialogue {
//
//	private int npcId;
//
//	@Override
//	public void start() {
//		npcId = (Integer) parameters[0];
//		if (Player.startedPriestinPeril == false) {
//			sendEntityDialogue(SEND_1_TEXT_CHAT,
//					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
//							"Start quest." }, IS_NPC, npcId, 9827);
//			stage = 1;
//		} else 	if (Player.startedPriestinPeril == true) {
//			sendEntityDialogue(SEND_1_TEXT_CHAT,
//					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
//							"Already started quest." }, IS_NPC, npcId, 9827);
//			stage = 2;
//		}
//	}
//	@Override
//	public void run(int interfaceId, int componentId) {
//		switch (stage) {
//		case 1:
//			sendEntityDialogue(SEND_1_TEXT_CHAT,
//					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
//							"blah." }, IS_NPC, npcId, 9827);
//			PriestinPeril.handleProgressQuest(player);
//		
//			break;
//		case 2:
//			sendEntityDialogue(SEND_1_TEXT_CHAT,
//					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
//							"blah blah blah" }, IS_NPC, npcId, 9827);
//			break;
//		
//		}
//	}
//
//	@Override
//	public void finish() {
//
//	}
//	public int getPriestinPeril() {
//		return player.PriestinPeril;
//	}
//}