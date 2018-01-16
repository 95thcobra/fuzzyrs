package com.rs.content.customskills.sailing.ships;

import com.rs.content.customskills.sailing.dialogues.UpgradeShipDialogue;
import com.rs.player.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John (FuzzyAvacado) on 12/15/2015.
 */
public class PlayerShip implements Serializable {

    private static final long serialVersionUID = 2300159002849727695L;

    private final Map<Integer, Integer> shipAttributes;
    private Ships ship;

    public PlayerShip() {
        this.ship = Ships.NONE;
        this.shipAttributes = new HashMap<>();
        setDefaultAttributes();
    }

    private void setDefaultAttributes() {
        for (ShipLevels s : ShipLevels.values()) {
            setAttribute(s.ordinal(), 0);
        }
    }

    public void levelUp(Player player) {
        player.getDialogueManager().startDialogue(UpgradeShipDialogue.class, ship);
    }

    public int getAttribute(int integer) {
        return shipAttributes.get(integer);
    }

    public int getAttribute(ShipLevels attribute) {
        return shipAttributes.get(attribute.ordinal());
    }

    public void setAttribute(int key, int value) {
        shipAttributes.put(key, value);
    }

    public void setAttribute(ShipLevels attribute, int value) {
        shipAttributes.put(attribute.ordinal(), value);
    }

    public Ships getShip() {
        return ship;
    }

    public void setShip(Ships ship) {
        this.ship = ship;
    }
}
