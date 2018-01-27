package com.rs.world.npc.fightpits;

import com.rs.content.minigames.FightPits;
import com.rs.entity.Entity;
import com.rs.world.*;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class TzKekPits extends FightPitsNPC {

    public TzKekPits(final int id, final WorldTile tile) {
        super(id, tile);
    }

    @Override
    public void sendDeath(final Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        final WorldTile tile = this;
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                    setNextGraphics(new Graphics(2924 + getSize()));
                } else if (loop >= defs.getDeathDelay()) {
                    reset();
                    FightPits.addNPC(new FightPitsNPC(2738, tile));
                    if (World.canMoveNPC(getPlane(), tile.getX() + 1,
                            tile.getY(), 1)) {
                        tile.moveLocation(1, 0, 0);
                    } else if (World.canMoveNPC(getPlane(), tile.getX() - 1,
                            tile.getY(), 1)) {
                        tile.moveLocation(-1, 0, 0);
                    } else if (World.canMoveNPC(getPlane(), tile.getX(),
                            tile.getY() - 1, 1)) {
                        tile.moveLocation(0, -1, 0);
                    } else if (World.canMoveNPC(getPlane(), tile.getX(),
                            tile.getY() + 1, 1)) {
                        tile.moveLocation(0, 1, 0);
                    }
                    FightPits.addNPC(new FightPitsNPC(2738, tile));
                    finish();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public void removeHitpoints(final Hit hit) {
        super.removeHitpoints(hit);
        if (hit.getLook() != Hit.HitLook.MELEE_DAMAGE || hit.getSource() == null)
            return;
        hit.getSource().applyHit(new Hit(this, 10, Hit.HitLook.REGULAR_DAMAGE));
    }
}
