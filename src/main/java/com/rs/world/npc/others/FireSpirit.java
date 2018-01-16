package com.rs.world.npc.others;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class FireSpirit extends NPC {

    private final Player target;
    private final long createTime;

    public FireSpirit(final WorldTile tile, final Player target) {
        super(15451, tile, -1, true, true);
        this.target = target;
        createTime = Utils.currentTimeMillis();
    }

    @Override
    public void processNPC() {
        if (target.hasFinished()
                || createTime + 60000 < Utils.currentTimeMillis()) {
            finish();
        }
    }

    public void giveReward(final Player player) {
        if (player != target || player.isLocked())
            return;
        player.lock();
        player.setNextAnimation(new Animation(16705));
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                player.unlock();
                player.getInventory().addItem(
                        new Item(12158, Utils.random(1, 6)));
                player.getInventory().addItem(
                        new Item(12159, Utils.random(1, 6)));
                player.getInventory().addItem(
                        new Item(12160, Utils.random(1, 6)));
                player.getInventory().addItem(
                        new Item(12163, Utils.random(1, 6)));
                player.getPackets()
                        .sendGameMessage(
                                "The fire spirit gives you a reward to say thank you for freeing it, before disappearing.");
                finish();

            }

        }, 2);
    }

    @Override
    public boolean withinDistance(final Player tile, final int distance) {
        return tile == target && super.withinDistance(tile, distance);
    }

}
