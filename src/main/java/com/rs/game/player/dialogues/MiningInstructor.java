package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.controlers.LumbiTutorial;

public class MiningInstructor extends Dialogue {

	int npcId;
	LumbiTutorial controler;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (LumbiTutorial) parameters[1];
		if (controler == null) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You now know everything to succeed in this",
							"dangerous world. Go prove what you are worth now!" },
					IS_NPC, npcId, 9827);
			stage = 7;
		} else {
			int s = controler.getStage();
			if (s == 0) {
				sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Greetings, " + player.getDisplayName() + ". Welcome to " + Settings.SERVER_NAME + ".",
						"I am the Mining instructor and I will be guiding you",
				"through this portion of the tutorial." }, IS_NPC, npcId, 9827);
			} else if (s == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"You're making wonderful progress my friend!" }, IS_NPC, npcId,
						9827);
				stage = 5;
			}
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Follow the instructions that you are given to", "complete this tutorial successfully." },
					IS_NPC, npcId, 9827);
		} else if (stage == 0) {
			stage = 1;
			sendEntityDialogue(
					SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Without any further information, lets begin." }, IS_NPC,
					npcId, 9827);
		} else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Search the crate indicated by a flashing yellow",
							"arrow above it." }, IS_NPC, npcId,
					9827);
			controler.updateProgress();
		} else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"To continue, walk up and mine some Tin ore." }, IS_NPC,
					npcId, 9827);
			player.getHintIconsManager().addHintIcon(3077, 9503, 0, 125, 4, 0, -1, false);
		} else {

			end();
		}

	}

	@Override
	public void finish() {

	}

}
