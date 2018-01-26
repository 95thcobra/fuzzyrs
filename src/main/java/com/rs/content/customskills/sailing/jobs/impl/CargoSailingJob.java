package com.rs.content.customskills.sailing.jobs.impl;

import com.rs.server.Server;
import com.rs.content.customskills.CustomSkills;
import com.rs.content.customskills.sailing.SailingController;
import com.rs.content.customskills.sailing.jobs.SailingJob;
import com.rs.content.dialogues.DialogueExpressions;
import com.rs.content.dialogues.types.ExpressionNPCMessage;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.player.Player;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;

/**
 * @author John (FuzzyAvacado) on 3/13/2016.
 */
public class CargoSailingJob extends SailingJob {

    private static final int[] CARGO_ITEM_IDS = {318, 322, 336, 328, 332, 342, 346, 350, 354, 360, 364, 372, 378, 384, 390, 396, 2133, 2135, 2137, 2139, 3227, 5399, 5401, 5403, 5405, 5407, 15271};
    private ItemsContainer<Item> cargo;

    public CargoSailingJob(Player player, int npcId, int targetNpc) {
        super(player, npcId, targetNpc);
        this.cargo = new ItemsContainer<>((getPlayer().getCustomSkills().getLevel(CustomSkills.SAILING) / 3) + 1, true);
    }

    public ItemsContainer<Item> getCargo() {
        return cargo;
    }

    @Override
    public void start() {
        for (int i = 0; i < cargo.getSize(); i++) {
            Item item = new Item(CARGO_ITEM_IDS[(int) (Math.random() * CARGO_ITEM_IDS.length - 1)], (int) (Math.random() * getPlayer().getCustomSkills().getLevel(CustomSkills.SAILING)) + 30);
            cargo.add(item);
            if (getPlayer().getInventory().getFreeSlots() < 1) {
                getPlayer().getDialogueManager().startDialogue(SimpleNPCMessage.class, getNpcId(), "You do not have enough inventory space to carry the cargo!");
                break;
            }
        }
    }

    @Override
    public void process() {
        if (takeCargo()) {
            end();
        } else {
            getPlayer().getDialogueManager().startDialogue(ExpressionNPCMessage.class, getNpcId(), DialogueExpressions.WHAT_THE_CRAP, "WHERE IS MY CARGO YOU FOOL. YOU HAD ONE JOB " + getPlayer().getDisplayName() + "! BRING ME MY CARGO THIS INSTANT OR ELSE!");
        }
    }

    @Override
    public void finish() {
        int payout = cargo.getUsedSlots() * 1000 * Server.getInstance().getSettingsManager().getSettings().getSailingPayoutMultiplier();
        getPlayer().getMoneyPouch().addMoney(payout, false);
        getPlayer().getDialogueManager().startDialogue(SimpleNPCMessage.class, getNpcId(), "Thank you for your hard work " + getPlayer().getDisplayName() + "! ");
    }

    public boolean takeCargo() {
        if (getPlayer().getInventory().containsItems(cargo.getItems())) {
            getPlayer().getInventory().removeItems(cargo.getItems());
            return true;
        } else {
            SailingController controller = (SailingController) getPlayer().getControllerManager().getController();
            if (controller.getStorage().containsItems(cargo.getItems())) {
                controller.getStorage().empty();
                return true;
            }
        }
        return false;
    }
}
