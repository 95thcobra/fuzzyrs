package com.rs.content.customskills.sailing.dialogues;

import com.rs.content.customskills.sailing.ships.ShipLevels;
import com.rs.content.customskills.sailing.ships.Ships;
import com.rs.content.dialogues.types.SimpleMessage;

/**
 * @author John (FuzzyAvacado) on 3/12/2016.
 */
public class ShowAttributesDialogue extends PickShipDialogue {

    @Override
    public void complete() {
        Ships ship = ships.get(shipChoice);
        String message = ShipLevels.ATTACK.name() + ": " + ShipLevels.ATTACK.getName(player.getSailingManager().getPlayerShip(ship).getAttribute(ShipLevels.ATTACK))
                + "    " + ShipLevels.DEFENSE.name() + ": " + ShipLevels.DEFENSE.getName(player.getSailingManager().getPlayerShip(ship).getAttribute(ShipLevels.DEFENSE))
                + "    " + ShipLevels.STEALTH.name() + ": " + ShipLevels.STEALTH.getName(player.getSailingManager().getPlayerShip(ship).getAttribute(ShipLevels.STEALTH));
        player.getDialogueManager().startDialogue(SimpleMessage.class, message);
        player.sendMessage(message);
    }
}
