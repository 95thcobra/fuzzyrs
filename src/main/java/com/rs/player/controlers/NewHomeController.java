package com.rs.player.controlers;

import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.player.content.PlayerLook;
import com.rs.world.*;
import com.rs.world.item.FloorItem;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public class NewHomeController extends Controller {

	private static Item[] STARTER_ITEMS = {new Item(995, 25000)};
	private NPC NPC1, NPC2;

	@Override
	public void start() {
		update();
		sendInterfaces();
	}

	public void update() {
		final int stage = getStage();
		if (stage == 0) {
			player.getAppearance().switchHidden(); // hides player
			PlayerLook.openCharacterCustomizing(player);
		} else if (stage == 1) {
			player.getCutscenesManager().play("HomeCutScene");
		} else if (stage == 2) {
			player.getInterfaceManager().sendInterface(1079);
			player.setCloseInterfacesEvent(new Runnable() {
				@Override
				public void run() {
					nextStage();
				}
			});
		} else if (stage == 3) {
			startNPCActions();
		} else if (stage == 4) {
			getDroppedItems();
		} else if (stage == 5) {
			sendGoodActions();
		}
	}

	private void getDroppedItems() {
		player.lock();
		for (final Item item : STARTER_ITEMS) {
			World.addGroundItem(item, NPC1, player, true, 120 * 1000, true);
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.getDialogueManager().startDialogue(SimpleMessage.class,
						"You pick up the items.");
				for (final Item item : STARTER_ITEMS) {
					final FloorItem floorItem = World.getRegion(
							player.getRegionId()).getGroundItem(item.getId(),
							NPC1, player);
					if (floorItem == null)
						return;
					World.removeGroundItem(player, floorItem);
					player.getInventory().addItem(item);
				}
				player.unlock();
				update();
			}
		});
	}

	private void sendGoodActions() {
		player.getDialogueManager().startDialogue(SimpleNPCMessage.class, 1,
				"Thank you, please keep the items for yourself.");
		NPC2.setNextAnimation(new Animation(0x35F));// waving bye
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {// teleporting away
				NPC2.setNextAnimation(new Animation(2111));
				NPC2.setNextGraphics(new Graphics(184));
			}
		});
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				nextStage();
				NPC2.finish();
				NPC1.finish();// might as well get rid of the instance
			}
		});

	}

	private void startNPCActions() {
		player.lock();
		NPC1 = new NPC(1, new WorldTile(2966, 3386, 0), -1, true, true);
		NPC2 = new NPC(1, new WorldTile(2966, 3387, 0), -1, true, true);
		NPC1.faceEntity(NPC2);
		NPC1.setNextAnimation(new Animation(5078));
		NPC2.setNextGraphics(new Graphics(875));
		NPC1.setRandomWalk(false);
		NPC2.setRandomWalk(false);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				NPC1.addWalkSteps(2974, 3390);
				NPC2.addWalkSteps(2974, 3390);
				NPC1.setRun(true);
				NPC2.faceEntity(NPC1);
				NPC2.addWalkSteps(NPC1.getX(), NPC2.getY());
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						NPC1.setNextForceTalk(new ForceTalk(
								"Muahahahaha, its all mine!"));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								NPC2.setNextForceTalk(new ForceTalk(
										"Somone please help!"));
								NPC2.faceEntity(player);
								NPC2.addWalkSteps(player.getX(), player.getY());
								WorldTasksManager.schedule(new WorldTask() {

									@Override
									public void run() {
										player.getDialogueManager()
												.startDialogue(
														SimpleNPCMessage.class, 1,
														"Please sir, help me capture the man who stole my belongings");
									}
								});
								WorldTasksManager.schedule(new WorldTask() {

									@Override
									public void run() {
										player.getHintIconsManager()
												.addHintIcon(NPC1, 0, -1, false);
									}
								});
								player.unlock();
							}
						}, 2);
					}
				});
			}
		}, 1);
	}

	public void nextStage() {
		final int stage = increaseStage();
		if (stage == 1) {
			player.getAppearance().switchHidden(); // unhides player
		}
		update();
		sendInterfaces();
	}

	@Override
	public void process() {
		if (getStage() == 1 && !player.getCutscenesManager().hasCutscene()
				|| getStage() == 3 && NPC1.hasFinished()) {
			nextStage();
		}
	}

	@Override
	public boolean processButtonClick(final int interfaceId,
			final int componentId, final int slotId, final int packetId) {
		if (getStage() == 0 && interfaceId == 1028 && componentId == 117) {
			nextStage();
		}
		return true;
	}

	@Override
	public boolean login() {
		return true;
	}

	@Override
	public boolean logout() {
		if (NPC1 != null) {
			NPC1.finish();
		}
		if (NPC2 != null) {
			NPC2.finish();
		}
		return false;
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You cannot teleport during the tutorial.");
		return false;
	}

	@Override
	public boolean keepCombating(final Entity target) {
		return true;
	}

	@Override
	public boolean canAttack(final Entity target) {
		if (getStage() == 3 && target == NPC1)
			return true;
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You cannot attack during the tutorial. ");
		return false;
	}

	@Override
	public boolean canHit(final Entity target) {
		return true;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You cannot teleport during the tutorial.");
		return false;
	}

	@Override
	public boolean processObjectTeleport(final WorldTile toTile) {
		player.getDialogueManager().startDialogue(SimpleMessage.class,
				"You cannot teleport during the tutorial.");
		return false;
	}

	@Override
	public void sendInterfaces() {
		final int stage = getStage();
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		if (stage == 1) {
			player.getPackets().sendIComponentText(372, 0, "The beginning");
			player.getPackets().sendIComponentText(372, 1,
					"Please wait, the scene will be over shortly.");
			player.getPackets().sendIComponentText(372, 2, "");
			player.getPackets().sendIComponentText(372, 3, "");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 2) {
			player.getPackets().sendIComponentText(372, 0, "The basics");
			player.getPackets().sendIComponentText(372, 1,
					"This gives you a basic idea of how to function.");
			player.getPackets().sendIComponentText(372, 2,
					"Crtl + Click is how to run, or toggle the run button.");
			player.getPackets().sendIComponentText(372, 3,
					"Use the arrow keys to change your camera's view.");
			player.getPackets().sendIComponentText(372, 4,
					"Click on the map or minimap to move.");
			player.getPackets().sendIComponentText(372, 5,
					"The minimap is located on the right hand corner.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 3) {
			player.getPackets().sendIComponentText(372, 0, "Retreiving Items");
			player.getPackets().sendIComponentText(372, 1,
					"The man has asked you for his help.");
			player.getPackets().sendIComponentText(372, 2,
					"Go to the flashing icon and click the attack option.");
			player.getPackets().sendIComponentText(372, 3, "");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 4) {
			player.getPackets().sendIComponentText(372, 0, "Retreiving Items");
			player.getPackets().sendIComponentText(372, 1,
					"Pick up the items from the ground.");
			player.getPackets().sendIComponentText(372, 2,
					"I'm sure the man is very greatfull.");
			player.getPackets().sendIComponentText(372, 3, "");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		}
	}

	public int increaseStage() {
		final int stage = getStage() + 1;
		setStage(stage);
		return stage;
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

	public NPC findNPC(final int id) {
		for (final NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id) {
				continue;
			}
			return npc;
		}
		return null;
	}
}
