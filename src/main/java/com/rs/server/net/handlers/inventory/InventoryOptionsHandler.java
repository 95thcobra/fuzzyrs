package com.rs.server.net.handlers.inventory;

import com.rs.player.ClueScrolls;
import com.rs.player.Player;
import com.rs.player.controlers.Barrows;
import com.rs.world.Animation;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class InventoryOptionsHandler {

    public static void dig(final Player player) {
        player.resetWalkSteps();
        player.setNextAnimation(new Animation(830));
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                player.unlock();
                if (Barrows.digIntoGrave(player))
                    return;
                if (player.getX() == 3005 && player.getY() == 3376
                        || player.getX() == 2999 && player.getY() == 3375
                        || player.getX() == 2996 && player.getY() == 3377
                        || player.getX() == 2989 && player.getY() == 3378
                        || player.getX() == 2987 && player.getY() == 3387
                        || player.getX() == 2984 && player.getY() == 3387) {
                    // mole
                    player.setNextWorldTile(new WorldTile(1752, 5137, 0));
                    player.getPackets()
                            .sendGameMessage(
                                    "You seem to have dropped down into a network of mole tunnels.");
                    return;
                }
                if (ClueScrolls.digSpot(player))
                    return;
                player.getPackets().sendGameMessage("You find nothing.");
            }

        });
    }

    /*
     * returns the other
     */
    public static Item contains(final int id1, final Item item1,
                                final Item item2) {
        if (item1.getId() == id1)
            return item2;
        if (item2.getId() == id1)
            return item1;
        return null;
    }

    public static boolean contains(final int id1, final int id2,
                                   final Item... items) {
        boolean containsId1 = false;
        boolean containsId2 = false;
        for (final Item item : items) {
            if (item.getId() == id1) {
                containsId1 = true;
            } else if (item.getId() == id2) {
                containsId2 = true;
            }
        }
        return containsId1 && containsId2;
    }

}
