package com.rs.world.npc.others;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class DreadNip extends NPC {

    public static final String[] DREADNIP_MESSAGES = {
            "Your dreadnip couldn't attack so it left.",
            "The dreadnip gave up as you were too far away.",
            "Your dreadnip served its purpose and fled."};

    private final Player owner;
    private int ticks;

    public DreadNip(final Player owner, final int id, final WorldTile tile,
                    final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
        this.owner = owner;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (owner == null || owner.hasFinished()) {
            finish(-1);
            return;
        } else if (getCombat().getTarget() == null
                || getCombat().getTarget().isDead()) {
            finish(0);
            return;
        } else if (Utils.getDistance(owner, this) >= 10) {
            finish(1);
            return;
        } else if (ticks++ == 33) {
            finish(2);
            return;
        }
    }

    private void finish(final int index) {
        if (index != -1) {
            owner.getPackets().sendGameMessage(DREADNIP_MESSAGES[index]);
            owner.getTemporaryAttributtes().remove("hasDN");
        }
        this.finish();
    }

    public Player getOwner() {
        return owner;
    }

    public int getTicks() {
        return ticks;
    }
}
