package com.rs.world.npc.qbd;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * Handles the Queen Black Dragon's change armour "attack".
 *
 * @author Emperor
 */
public final class ChangeArmour implements QueenAttack {

    @Override
    public int attack(final QueenBlackDragon npc, final Player victim) {
        npc.switchState(Utils.random(2) < 1 ? QueenState.CRYSTAL_ARMOUR
                : QueenState.HARDEN);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                npc.switchState(QueenState.DEFAULT);
            }
        }, 40);
        npc.getTemporaryAttributtes().put("_last_armour_change",
                npc.getTicks() + Utils.random(41, 100));
        return Utils.random(4, 10);
    }

    @Override
    public boolean canAttack(final QueenBlackDragon npc, final Player victim) {
        final Integer last = (Integer) npc.getTemporaryAttributtes().get(
                "_last_armour_change");
        return last == null || last < npc.getTicks();
    }

}