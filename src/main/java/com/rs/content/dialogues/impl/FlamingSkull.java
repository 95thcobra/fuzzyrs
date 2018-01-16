package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.Equipment;
import com.rs.world.item.Item;

/**
 * Flaming skull color changing.
 *
 * @author Dragonkk
 */
public class FlamingSkull extends Dialogue {

    private static final String COLORS[] = {"Green", "Purple", "Blue", "Red"};

    private Item item;
    private int slot;

    @Override
    public void start() {
        item = (Item) parameters[0];
        slot = (Integer) parameters[1];
        final int index = (item.getId() == 24437 ? 24442 : item.getId()) - 24439;
        sendOptionsDialogue("What colour do you want your skull to be?",
                COLORS[(index + 1) % 4], COLORS[(index + 2) % 4],
                COLORS[(index + 3) % 4]);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        final int index = (item.getId() == 24437 ? 24442 : item.getId()) - 24439;
        int option;
        if (componentId == OPTION_1) {
            option = 1;
        } else if (componentId == OPTION_2) {
            option = 2;
        } else {
            option = 3;
        }
        final int itemId = 24439 + ((index + option) % 4);
        item.setId(itemId == 24442 ? 24437 : itemId);
        if (slot == -1) {
            player.getEquipment().refresh(Equipment.SLOT_HAT);
            player.getAppearance().generateAppearenceData();
        } else {
            player.getInventory().refresh(slot);
        }
        end();
    }

    @Override
    public void finish() {

    }
}
