package com.rs.game.player.content;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Aug 12, 2014 at 6:21:50 PM.
 */

public class GodSwords {
	public static int BandosGs = 11696;
	public static int BandosHilt = 11704;
	public static int ArmadylGs = 11694;
	public static int ArmadylHilt = 11702;
	public static int SaraGs = 11698;
	public static int SaraHilt = 11706;
	public static int ZammyGs = 11700;
	public static int ZammyHilt = 11708;

	public static int Blade = 11690;

	public static void Dismantling(Player player, int slotId, int itemId, Item item) {

		if (player.getInventory().getFreeSlots() > 2) {
			if (!player.isCanPvp()) {
				if (itemId == ArmadylGs) {
					player.getInventory().addItem(ArmadylHilt, 1);
					player.getInventory().addItem(Blade, 1);
					player.getInventory().deleteItem(ArmadylGs, 1);
				} else if (itemId == BandosGs) {
					player.getInventory().addItem(BandosHilt, 1);
					player.getInventory().addItem(Blade, 1);
					player.getInventory().deleteItem(BandosGs, 1);
				} else if (itemId == SaraGs) {
					player.getInventory().addItem(SaraHilt, 1);
					player.getInventory().addItem(Blade, 1);
					player.getInventory().deleteItem(SaraGs, 1);
				} else if (itemId == ZammyGs) {
					player.getInventory().addItem(ZammyHilt, 1);
					player.getInventory().addItem(Blade, 1);
					player.getInventory().deleteItem(ZammyGs, 1);
				}
			} else {
				player.sm("You can not do this in the wilderness.");
				return;
			}
		} else {
			player.sm("You do not have enough inventory spaces.");
			return;
		}
	}
}
