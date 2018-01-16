package com.rs.content.customskills.sailing.dialogues;

import com.rs.content.customskills.sailing.ships.ShipLevels;
import com.rs.content.customskills.sailing.ships.Ships;
import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.economy.shops.ShopCurrency;
import com.rs.world.Graphics;

/**
 * @author John (FuzzyAvacado) on 12/16/2015.
 */
public class UpgradeShipDialogue extends Dialogue {

    private ShipLevels shipLevels;
    private Ships ship;
    private int currentLevel, nextLevel, price;

    @Override
    public void start() {
        ship = (Ships) parameters[0];
        sendOptionsDialogue("Choose an upgrade path", ShipLevels.ATTACK.name(), ShipLevels.DEFENSE.name(), ShipLevels.STEALTH.name());
        stage = 0;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        shipLevels = ShipLevels.ATTACK;
                        break;
                    case OPTION_2:
                        shipLevels = ShipLevels.DEFENSE;
                        break;
                    case OPTION_3:
                        shipLevels = ShipLevels.STEALTH;
                        break;
                }
                currentLevel = player.getSailingManager().getPlayerShip(ship).getAttribute(shipLevels);
                nextLevel = currentLevel + 1;
                price = nextLevel * 1000000;
                if (currentLevel == shipLevels.getNames().length - 1) {
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "Your ship already has the best upgrade in " + shipLevels.toString().toLowerCase() + "!");
                    return;
                }
                sendOptionsDialogue("Do you want to upgrade your ship's " + shipLevels.toString().toLowerCase() + "?", "Yes : " + price + " " + ShopCurrency.GOLD.toString(), "No");
                stage = 1;
                break;
            case 1:
                if (componentId == OPTION_1) {
                    if (player.hasMoney(price)) {
                        player.takeMoney(price);
                        player.getDialogueManager().startDialogue(SimpleMessage.class, "You upgraded your ship's " + shipLevels.name() + " from " + shipLevels.getName(currentLevel) + " to " + shipLevels.getName(nextLevel) + " for " + price + " " + ShopCurrency.GOLD.toString());
                        player.sendMessage("You upgraded your ship's " + shipLevels.name() + " from " + shipLevels.getName(currentLevel) + " to " + shipLevels.getName(nextLevel) + " for " + price + " " + ShopCurrency.GOLD.toString());
                        player.getSailingManager().getPlayerShip(ship).setAttribute(shipLevels, nextLevel);
                        player.setNextGraphics(new Graphics(199));
                    } else {
                        player.getDialogueManager().startDialogue(SimpleMessage.class, "You do not have enough money to upgrade your ship!");
                        player.sendMessage("You do not have enough money to upgrade your ship!");
                    }
                }
                break;
        }
    }

    @Override
    public void finish() {

    }
}
