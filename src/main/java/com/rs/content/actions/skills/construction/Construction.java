package com.rs.content.actions.skills.construction;

import com.rs.content.actions.skills.Skills;
import com.rs.server.net.io.InputStream;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;

/**
 * 
 * @author Adam
 *
 */
public class Construction {
	public static Player p;
	public static int modifier = 1;

	{

		p.getInterfaceManager().closeScreenInterface();
		p.sendMessage(" You must have a saw and a hammer in your inventory to construct this item");
	}

	public static void tableConfigs(final Player p, final String name,
			final int exp, final int id, final int plank, final int amount) {
		p.getInventory().deleteItem(plank, amount);
		p.getInterfaceManager().closeScreenInterface();
		p.setNextAnimation(new Animation(7276));
		p.getInventory().addItem(id, 1);
		p.getSkills().addXp(22, exp * modifier);
		p.sendMessage("you have packed a new " + name + " !");
	}

	public static void tableCloseMsg(final Player player, final String string) {
		player.getInterfaceManager().closeScreenInterface();
		player.sendMessage("Minimum " + string + " required.");
	}

	public static void lvlClose(final Player player, final int lvl) {
		player.getInterfaceManager().closeScreenInterface();

		player.sendMessage("Level" + lvl
				+ " Construction is required to construct this item.");
	}

	public static boolean Level(final Player player, final int lvl) {
		if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= lvl)
			return true;
		else {
			lvlClose(player, lvl);
			return false;
		}
	}

	public static void handleButtons(final Player player,
			final InputStream stream, final int packetId) {
		final int interfaceHash = stream.readIntV2();
		final int interfaceId = interfaceHash >> 16;
		if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		if (player.isDead()
				|| !player.getInterfaceManager().containsInterface(interfaceId))
			return;
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 68916
				&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		final int slotId = stream.readUnsignedShort();
		if (!player.getControllerManager().processButtonClick(interfaceId,
				componentId, slotId, packetId))
			return;
		// Start consturction
	}

	public static void handleConstructionButtons(final Player p,
			final int componentId) {
		switch (componentId) {
		case 45:
			if (p.getInventory().containsItem(960, 2)) {
				tableConfigs(p, "Rocking Chair", 96, 8500, 960, 2);
			} else {
				tableCloseMsg(p, "2 Planks");
			}
			break;

		case 46:
			if (Level(p, 29) == true) {
				if (p.getInventory().containsItem(8778, 2)) {
					tableConfigs(p, "Oak bookcase", 180, 8512, 8778, 2);
				} else {
					tableCloseMsg(p, "2 Oak planks");
				}
				break;
			}

		case 47:
			if (Level(p, 36) == true) {
				if (p.getInventory().containsItem(8778, 2)
						&& p.getInventory().containsItem(2353, 1)) {
					tableConfigs(p, "Dragon Bitter barrel", 224, 8524, 8778, 2);
					p.getInventory().deleteItem(2353, 1);
				} else {
					tableCloseMsg(p, "2 Oak planks and 1 Steel bar");
				}
			}
			break;

		case 48:
			if (Level(p, 52) == true) {
				if (p.getInventory().containsItem(8780, 3)) {
					tableConfigs(p, "Teak kitchen table", 270, 8532, 8780, 3);
				} else {
					tableCloseMsg(p, "3 Teak planks");
				}
			}
			break;
		case 49:
			if (Level(p, 52) == true) {
				if (p.getInventory().containsItem(8782, 6)) {
					tableConfigs(p, "Mahogany table", 840, 8558, 8782, 6);
				} else {
					tableCloseMsg(p, "6 Mahogany planks");
				}
			}
			break;
		case 50:// p.getInventory().containsItem
			if (Level(p, 61) == true) {
				if (p.getInventory().containsItem(8782, 4)
						&& p.getInventory().containsItem(8784, 4)) {
					tableConfigs(p, "Gilded bench", 1760, 8574, 8782, 4);
					p.getInventory().deleteItem(8784, 4);
				} else {
					tableCloseMsg(p, "4 Mahogany planks and 4 Gold leaves");
				}
			}
			break;

		case 51:
			if (Level(p, 53) == true) {
				if (p.getInventory().containsItem(8782, 3)) {
					tableConfigs(p, "Mahogany 4-Poster", 450, 8586, 8782, 3);
				} else {
					tableCloseMsg(p, "3 Mahogany planks");
				}
			}
			break;

		case 52:
			if (Level(p, 64) == true) {
				if (p.getInventory().containsItem(8782, 2)) {
					tableConfigs(p, "Mahogany dresser", 281, 8606, 8782, 2);
				} else {
					tableCloseMsg(p, "2 Mahogany planks");
				}
			}
			break;

		case 53:
			if (Level(p, 87) == true) {
				if (p.getInventory().containsItem(8782, 3)
						&& p.getInventory().containsItem(8784, 1)) {
					tableConfigs(p, "Gilded wardrobe", 720, 8622, 8782, 3);
					p.getInventory().deleteItem(8784, 1);
				} else {
					tableCloseMsg(p, "3 Mahogany planks and 1 Gold leaf");
				}
			}
			break;
		case 54:
			if (Level(p, 85) == true) {
				if (p.getInventory().containsItem(8782, 2)
						&& p.getInventory().containsItem(8784, 1)) {
					tableConfigs(p, "Gilded clock", 602, 8594, 8782, 2);
					p.getInventory().deleteItem(8784, 1);
				} else {
					tableCloseMsg(p, "2 Mahogany planks and 1 Gold leaf");
				}
			}
			break;
		case 55:
			if (Level(p, 90) == true) {
				if (p.getInventory().containsItem(8786, 1)) {
					tableConfigs(p, "Marble cape rack", 500, 9847, 8786, 1);
				} else {
					tableCloseMsg(p, "1 Marble block");
				}
			}
			break;
		case 56:
			if (Level(p, 96) == true) {
				if (p.getInventory().containsItem(8786, 1)) {
					tableConfigs(p, "Marble magic wardrobe", 550, 9858, 8786, 1);
				} else {
					tableCloseMsg(p, "1 Marble block");
				}
			}
			break;
		case 57:
			if (Level(p, 82) == true) {
				if (p.getInventory().containsItem(8782, 3)) {
					tableConfigs(p, "Mahogany armour case", 420, 9861, 8782, 3);
				} else {
					tableCloseMsg(p, "3 Mahogany planks");
				}
			}
			break;
		case 58:
			if (Level(p, 84) == true) {
				if (p.getInventory().containsItem(8782, 2)) {
					tableConfigs(p, "Mahogany treasure chest", 280, 9864, 8782,
							2);
				} else {
					tableCloseMsg(p, "2 Mahogany planks");
				}
			}
			break;
		case 59:
			if (Level(p, 80) == true) {
				if (p.getInventory().containsItem(8782, 2)) {
					tableConfigs(p, "Mahogany costume box", 280, 9867, 8782, 2);
				} else {
					tableCloseMsg(p, "2 Mahogany planks");
				}
			}
			break;
		case 60:
			if (Level(p, 80) == true) {
				if (p.getInventory().containsItem(8782, 2)) {
					tableConfigs(p, "Mahogany toy box", 280, 9851, 8782, 2);
				} else {
					tableCloseMsg(p, "2 Mahogany planks");
				}
			}
			break;
		}
	}

}
