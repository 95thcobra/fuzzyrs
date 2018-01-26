package com.rs.player.controlers;

import com.rs.Server;
import com.rs.content.dialogues.impl.QuestGuide;
import com.rs.core.settings.SettingsManager;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public class StartTutorial extends Controller {

	private static final int QUEST_GUIDE_NPC = 13927;

	@Override
	public void start() {
		player.setYellOff(true);
		refreshStage();
		player.getMusicsManager().forcePlayMusic(62); // Newbie Melody
	}

	public NPC findNPC(final int id) {
		// as it may be far away
		for (final NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id) {
				continue;
			}
			return npc;
		}
		return null;
	}

	@Override
	public void process() {
		if (getStage() == 1 && player.getPrayer().isAncientCurses()) {
			updateProgress();
		}
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		final int id = object.getId();
		return (id == 47120 && getStage() == 1);
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		final int id = object.getId();
		if ((id == 4874 && getStage() == 2)) {
			forceClose();
		}
		return true;
	}

	@Override
	public boolean processObjectClick3(final WorldObject object) {
		return false;
	}

	public void refreshStage() {
		final int stage = getStage();
		if (stage == 0) {
			final NPC guide = findNPC(QUEST_GUIDE_NPC);
			if (guide != null) {
				player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
			}
		} else if (stage == 1) {
			player.getHintIconsManager().addHintIcon(2983, 9676, 0, 100, 0, 0,
					-1, false);
		} else if (stage == 2) {
			player.getHintIconsManager().addHintIcon(2983, 9672, 0, 100, 0, 0,
					-1, false);
		}
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		final int stage = getStage();
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		if (stage == 0) {
			player.getPackets().sendIComponentText(372, 0,
					"Learning the Basics");
			player.getPackets()
			.sendIComponentText(372, 1,
					"To start the tutorial use your left mouse button to click on ");
			player.getPackets().sendIComponentText(372, 2,
					"Ariana. She is indicated with a");
			player.getPackets()
			.sendIComponentText(372, 3,
					"yellow arrow above his head. If you can't see her use your");
			player.getPackets().sendIComponentText(372, 4,
					"keyboard arrow keys to rotate the view.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 1) {
			player.getPackets().sendIComponentText(372, 0,
					"Learning the Basics");
			player.getPackets().sendIComponentText(372, 1,
					"Click on Zaros Altar and switch your prayer book");
			player.getPackets().sendIComponentText(372, 2,
					"to ancient curses prayers book.");
			player.getPackets().sendIComponentText(372, 3,
					"You can change your Spellbook after this");
			player.getPackets().sendIComponentText(372, 4,
					"tutorial at the same altar as where you");
			player.getPackets().sendIComponentText(372, 5,
					"change your prayer book.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 2) {
			player.getPackets().sendIComponentText(372, 0,
					"Learning the Basics");
			player.getPackets().sendIComponentText(372, 1,
					"Follow the Flashing Arrow on the minimap.");
			player.getPackets().sendIComponentText(372, 2,
					"Then click on the object to start thieving.");
			player.getPackets().sendIComponentText(372, 3,
					"You can sell these supplies at the general");
			player.getPackets().sendIComponentText(372, 4,
					"store for a decent price.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		}
	}

	public void updateProgress() {
		setStage(getStage() + 1);
		if (getStage() == 2) {
			player.getDialogueManager().startDialogue(QuestGuide.class,
					QUEST_GUIDE_NPC, this);
		}
		refreshStage();
	}

	@Override
	public boolean processNPCClick1(final NPC npc) {
		if (npc.getId() == QUEST_GUIDE_NPC) {
			player.getDialogueManager().startDialogue(QuestGuide.class,
					QUEST_GUIDE_NPC, this);
		}
		return false;
	}

	public int getStage() {
		if (getArguments() == null) {
			setArguments(new Object[] { 0 }); // index 0 = stage
		}
		return (Integer) getArguments()[0];
	}

	public void setStage(final int stage) {
		getArguments()[0] = stage;
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
	public boolean processMagicTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public boolean keepCombating(final Entity target) {
		return false;
	}

	@Override
	public boolean canAttack(final Entity target) {
		return false;
	}

	@Override
	public boolean canHit(final Entity target) {
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processObjectTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public void forceClose() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		player.getPackets().sendGameMessage("Enjoy your stay! ;)");
		player.getControllerManager().removeControlerWithoutCheck();
		World.sendWorldMessage("<col=00aaff " + player.getDisplayName()
				+ " has just joined " + Server.getInstance().getSettingsManager().getSettings().getServerName() + "! <col>", false);

		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInterfaceManager().sendInterfaces();
				player.getInterfaceManager()
						.closeReplacedRealChatBoxInterface();
				player.getDialogueManager().startDialogue(QuestGuide.class,
						QUEST_GUIDE_NPC, null);
			}
		});
	}
}