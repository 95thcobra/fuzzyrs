package com.rs.content.customskills.sailing.ships;

import com.rs.content.customskills.CustomSkills;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.economy.shops.Shop;
import com.rs.content.economy.shops.ShopCurrency;
import com.rs.content.economy.shops.ShopData;
import com.rs.content.economy.shops.ShopItem;
import com.rs.player.Player;
import com.rs.world.item.Item;

/**
 * @author John (FuzzyAvacado) on 12/18/2015.
 */
public class ShipShop extends Shop {

    public ShipShop(ShopData shopData) {
        super(shopData);
    }


    @Override
    public void handleBuy(Player player, int slotId, int quantity, ShopItem shopItem) {
        Ships ship = Ships.values()[slotId + 1];
        int price = ship.getBasePrice();
        int reqs = ship.getRequirements();
        if (player.getCustomSkills().getLevel(CustomSkills.SAILING) >= reqs) {
            if (!player.getSailingManager().hasShip(ship)) {
                if (player.hasMoney(price)) {
                    if (player.getSailingManager().getPlayerShips().size() == 0) {
                        player.getInventory().addItem(29997, 1);
                        player.sendMessage("You have received a sailing map for your first ship purchase!");
                    }
                    player.takeMoney(price);
                    player.getSailingManager().addShip(ship);
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "You bought a " + ship.toString().toLowerCase().replace("_", " ") + " for " + price + " " + ShopCurrency.GOLD.toString());
                } else {
                    player.getPackets().sendGameMessage("You do not have the necessary funds to complete this purchase.");
                }
            } else {
                player.getPackets().sendGameMessage("You already own one of these ships!");
            }
        } else {
            player.getPackets().sendGameMessage("You do not have the level requirements to purchase that ship! You need a Sailing level of " + reqs + ".");
        }
    }

    @Override
    public void handleSell(Player player, int slotId, int quantity, Item item, int price, int originalId) {
        player.getPackets().sendGameMessage("You cannot sell your ship to this store! I recommend you sell it to other players.");
    }

    @Override
    public void sendValue(Player player, int slotId) {
        player.getPackets().sendGameMessage("You can not sell to this shop!");
    }

    @Override
    public void sendInventory(Player player) {
        player.getInterfaceManager().sendInventoryInterface(1266);
        player.getPackets().sendItems(93, player.getInventory().getItems());
        player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 28, 0,
                1, 2, 3, 4, 5);
        player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 1, "Examine");
    }

    @Override
    public void restoreItems() {

    }

    @Override
    public void sendInfo(final Player player, final int slotId) {
        if (player.isBuying) {
            if (slotId >= getStoreSize())
                return;
            Ships ship = Ships.values()[slotId + 1];
            if (ship == null)
                return;
            String name = ship.toString().toLowerCase().replace("_", " ");
            player.getTemporaryAttributtes().put("BuySelectedSlot", slotId);
            quantity = 1;
            player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
            player.getPackets().sendGlobalConfig(1876, 0);
            player.getPackets().sendConfig(2562, 1);
            player.getPackets().sendConfig(2563, slotId);
            player.getPackets().sendConfig(2564, quantity);
            player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 40, "This is the mighty " + name + "! It has " + ship.getStorageSize() + " slots for storage.");
            player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 43, "This is the mighty " + name + "! It has " + ship.getStorageSize() + " slots for storage.");
            int price = ship.getBasePrice();
            player.getPackets().sendGameMessage("You can buy the " + name + " for " + price + " " + shopData.getCurrency().toString());
        }
    }

}
