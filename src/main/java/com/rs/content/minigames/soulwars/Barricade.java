package com.rs.content.minigames.soulwars;

import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class Barricade extends NPC {

    private static final long serialVersionUID = -8915522229140299002L;
    private int corruptionx;

    public Barricade(WorldTile tile, int team) {
        super(1532, tile, -1, true, true);
        corruptionx = team;
        setCantFollowUnderCombat(true);
    }

    @Override
    public void processNPC() {
        if (isDead())
            return;
        cancelFaceEntityNoCheck();
    }

    public void litFire() {
        transformIntoNPC(1533);
        sendDeath(this);
    }

    public void explode() {
        sendDeath(this);
    }

    @Override
    public void sendDeath(Entity killer) {
        resetWalkSteps();
        getCombat().removeTarget();
        if (this.getId() != 1533) {
            setNextAnimation(null);
            reset();
            setLocation(getRespawnTile());
            finish();
        } else {
            super.sendDeath(killer);
        }
        ((SoulWarsGameTask) World.getSoulWars().getTasks().get(SoulWarsManager.PlayerType.IN_GAME)).removeBarricade(this, corruptionx);
    }
}
