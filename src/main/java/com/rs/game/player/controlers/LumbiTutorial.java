package com.rs.game.player.controlers;

import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class LumbiTutorial extends Controler {

	private static final int MINING_INSTRUCTOR = 948;

	@Override
	public void start() {
		refreshStage();
		player.getPackets().sendMusicEffect(13);
		player.getMusicsManager().forcePlayMusic(239); //Brain Battle
		
	}

	public NPC findNPC(int id) {
		// as it may be far away
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}

	@Override
	public void process() {
		if (getStage() == 1 && player.getPrayer().isAncientCurses())
			updateProgress();
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if ((id == 1 && getStage() == 1) || (id == 3043 && getStage() == 2))
			return true;
		return false;
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		return false;
	}

	@Override
	public boolean processObjectClick3(WorldObject object) {
		return false;
	}

	public void refreshStage() {
		int stage = getStage();
		if (stage == 0) {
			NPC adrastos = findNPC(MINING_INSTRUCTOR);
			if (adrastos != null)
				player.getHintIconsManager().addHintIcon(adrastos, 0, -1, false);
		} else if (stage == 1) {
		//	player.getHintIconsManager().addHintIcon(3081, 9502, 0, 125, 4, 0, -1, false);;
		} else if (stage == 2) {
			player.getHintIconsManager().addHintIcon(3081, 9502, 0, 125, 4, 0, -1, false);
		}
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
			player.getInterfaceManager().closeCombatStyles();
			player.getInterfaceManager().closeTaskSystem();
			player.getInterfaceManager().closeSkills();
			//player.getInterfaceManager().closeInventory();
			player.getInterfaceManager().closeEquipment();
			player.getInterfaceManager().closePrayerBook();
			player.getInterfaceManager().closeMagicBook();
			player.getInterfaceManager().closeEmotes();
			player.getInterfaceManager().closeFriends();
			player.getInterfaceManager().closeQuestTab();
			player.getInterfaceManager().closeFriendsChat();
			player.getInterfaceManager().closeClanChat();
			player.getInterfaceManager().closeSettings();
			player.getInterfaceManager().closeMusic();
			player.getInterfaceManager().closeNotes();
		int stage = getStage();
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		if (stage == 0) {
			player.getPackets().sendIComponentText(372, 0, "Learning The Basics");
			player.getPackets()
					.sendIComponentText(372, 1,
							"To start the tutorial use your left mouse button and click on the");
			player.getPackets().sendIComponentText(372, 2,
					"Mining instructor in this room. He is indicated by a flashing");
			player.getPackets()
					.sendIComponentText(372, 3,
							"yellow arrow above his head. If you can't see him use your");
			player.getPackets().sendIComponentText(372, 4,
					"keyboard arrow keys to rotate the camera.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 1) {
			player.getHintIconsManager().addHintIcon(3081, 9502, 0, 125, 4, 0, -1, false);
			player.getPackets().sendIComponentText(372, 0, "Finding Survival Items");
			player.getPackets().sendIComponentText(372, 1,
					"");
			player.getPackets().sendIComponentText(372, 2,
					"Find the crate and search it.");
			player.getPackets().sendIComponentText(372, 3, "Use the arrow keys on your keyboard to");
			player.getPackets().sendIComponentText(372, 4, "rotate the camera if you can't see it.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 2) {
			player.getPackets().sendIComponentText(372, 0, "Getting Started");
			player.getPackets().sendIComponentText(372, 1,
					"");
			player.getPackets().sendIComponentText(372, 2,
					"Mining is one of the greatest ways to earn money throughout");
			player.getPackets().sendIComponentText(372, 3, "your journey here. You will need ore to create bars,");
			player.getPackets().sendIComponentText(372, 4, "and then the bars to create armour to survive out there.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		}
	}

	public void updateProgress() {
		setStage(getStage() + 1);
		if (getStage() == 2) {
			player.getDialogueManager().startDialogue("MiningInstructor",
					MINING_INSTRUCTOR, this);
		}
		refreshStage();
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == MINING_INSTRUCTOR) {
			player.getDialogueManager().startDialogue("MiningInstructor",
					MINING_INSTRUCTOR, this);
		}
		return false;
	}

	public void setStage(int stage) {
		getArguments()[0] = stage;
	}

	public int getStage() {
		if (getArguments() == null)
			setArguments(new Object[] { 0 }); // index 0 = stage
		return (Integer) getArguments()[0];
	}

	/*
	 * return remove controler
	 */
	@Override
	public boolean login() {
		start();
		return false;
	}

	/*
	 * return remove controler
	 */
	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		return false;
	}
	
	@Override
	public boolean canPlayerOption1(Player target) {
		return false;
	}

	@Override
	public boolean keepCombating(Entity target) {
		return false;
	}

	@Override
	public boolean canAttack(Entity target) {
		return false;
	}

	@Override
	public boolean canHit(Entity target) {
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public void forceClose() {
		player.getSkills().addXp(Skills.ATTACK, 13034431);
		player.getSkills().addXp(Skills.STRENGTH, 13034431);
		player.getSkills().addXp(Skills.DEFENCE, 13034431);
		player.getSkills().addXp(Skills.HITPOINTS, 13034431);
		player.getSkills().addXp(Skills.RANGE, 13034431);
		player.getSkills().addXp(Skills.MAGIC, 13034431);
		player.getSkills().addXp(Skills.PRAYER, 13034431);
		player.getSkills().addXp(Skills.SUMMONING, 13034431);
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		player.getInventory().addItem(1856, 1);
		player.getPackets().sendGameMessage(
				"Congratulations! You finished the start tutorial.");
		player.getPackets()
				.sendGameMessage(
						"You've received a guide book. Use it if you have questions or talk with other players.");
		player.getPackets().sendGameMessage("or talk with other players.");

		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInterfaceManager().sendInterfaces();
				player.getInterfaceManager()
						.closeReplacedRealChatBoxInterface();
				player.getDialogueManager().startDialogue("MiningInstructor",
						MINING_INSTRUCTOR, null);
			}
		});
	}
}
