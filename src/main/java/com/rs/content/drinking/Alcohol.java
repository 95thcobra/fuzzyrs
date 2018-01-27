package com.rs.content.drinking;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.world.Animation;
import com.rs.world.item.Item;

/**
 * Gets a Player drunk.
 * Created by Arham 4 on 7/7/14.
 */
public class Alcohol {

    private static final int DRUNK = 10, POISONING = 20, DEATH = 28;

    /**
     * Can the Player drink?
     *
     * @param item The item being drunk.
     * @return If the Player can drink.
     */
    public static boolean canDrink(Player player, Item item, int slot) {
        if (player.getFoodDelay() > Utils.currentTimeMillis())
            return false;
        int itemId = item.getId();
        for (Drinks d : Drinks.values()) {
            if (d.id == itemId) {
                player.setNextAnimation(new Animation(1327));
                player.getActionManager().setActionDelay(1000);
                player.addFoodDelay(1000);
                player.getInventory().getItems().set(slot, d.afterItem == 0 ? null : new Item(d.afterItem, 1));
                player.getInventory().refresh(slot);
                final int hp = player.getHitpoints();
                player.heal(d.heal * 10, 0);
                if (player.getHitpoints() > hp) {
                    player.getPackets().sendGameMessage("It heals some health.");
                }
                player.getInventory().refresh();
                player.sendMessage("You drink a " + d.name().toLowerCase() + ". Don't drink to much!");
                intake(player);
                return true;
            }
        }
        return false;
    }

    /**
     * Intakes the alcohol.
     *
     * @param player The Player drinking.
     * @param
     */
    public static void intake(final Player player) {
        player.setAlcoholIntake(player.getAlcoholIntake() + 1);
        if (player.getAlcoholIntake() < DRUNK) {
            return;
        }
        switch (player.getAlcoholIntake()) {
            case DRUNK:
                player.sendMessage("You start to feel a bit dizzy...");
                Server.getInstance().getGameTaskManager().scheduleTask(new DrunkGameTask(player));
                break;

            case POISONING:
                player.sendMessage("You start to feel horrible. What if...no...maybe...you have alcohol poisoning?");
                player.getPoison().makePoisoned(50);
                Server.getInstance().getGameTaskManager().scheduleTask(new DrunkGameTask(player));
                break;

            case DEATH:
                player.sendMessage("Your liver starts to shut down...you moron.");
                player.getPoison().makePoisoned(200);
                Server.getInstance().getGameTaskManager().scheduleTask(new DrunkGameTask(player));
                break;
        }
        player.getAppearance().setRenderEmote(290);
        player.getPackets().sendSound(4580, 0, 1);
    }

    public enum Drinks {

        BEER(1917, 4, 1919);

        private final int id;
        private final int heal;
        private final int afterItem;

        Drinks(int id, int heal, int afterItem) {
            this.id = id;
            this.heal = heal;
            this.afterItem = afterItem;
        }
    }
}
