package com.rs.content.ectofuntus;

import com.rs.content.actions.Action;
import com.rs.player.Player;
import com.rs.world.Animation;

/**
 * @author John (FuzzyAvacado) on 12/13/2015.
 */
public class SlimeBucketFill extends Action {

    @Override
    public boolean start(Player player) {
        return true;
    }

    @Override
    public boolean process(Player player) {
        return player.getInventory().containsItem(Ectofuntus.EMPTY_BUCKET, 1);
    }

    @Override
    public int processWithDelay(Player player) {
        if (fillBucket(player)) {
            return 1;
        }
        return 1;
    }

    @Override
    public void stop(Player player) {

    }

    public boolean fillBucket(Player player) {
        if (player.getInventory().containsItem(Ectofuntus.EMPTY_BUCKET, 1)) {
            player.setNextAnimation(new Animation(4471));
            player.getInventory().deleteItem(Ectofuntus.EMPTY_BUCKET, 1);
            player.getInventory().addItem(Ectofuntus.BUCKET_OF_SLIME, 1);
            return true;
        }
        return false;
    }
}
