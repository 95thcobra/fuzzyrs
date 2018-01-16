package com.rs.content.customskills.sailing.dialogues;

/**
 * @author John (FuzzyAvacado) on 3/12/2016.
 */
public class PreUpgradeShipDialogue extends PickShipDialogue {

    @Override
    public void complete() {
        if (shipChoice != -1 && ships.size() - 1 >= shipChoice) {
            player.getSailingManager().levelUpShip(ships.get(shipChoice));
        }
    }

}
