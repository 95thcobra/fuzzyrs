package com.rs.world.npc.slayer;

import com.rs.player.Player;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class GanodermicBeast extends NPC {

    private boolean sprayed;

    public GanodermicBeast(final int id, final WorldTile tile,
                           final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                           final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    public boolean isSprayed() {
        return sprayed;
    }

    public void setSprayed(final boolean spray) {
        sprayed = spray;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        int damage = hit.getDamage();
        if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
            if (damage > 0) {
                damage *= (int) 1.75;
            }
        }
        hit.setDamage(damage);
        super.handleIngoingHit(hit);
    }

    public void startSpray(final Player player) {
        if (isSprayed()) {
            player.getPackets().sendGameMessage(
                    "This NPC has already been sprayed with neem oil.");
            return;
        }
        final NPC before = this;
        transformIntoNPC(14697);
        setSprayed(true);
        this.setHitpoints(before.getHitpoints());
        setNextForceTalk(new ForceTalk("Rarghh"));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if (isDead()) {
                    stop();
                }
                transformIntoNPC(14696);
                setSprayed(false);
            }
        }, 120);
    }

    @Override
    public void sendDeath(final Entity source) {
        transformIntoNPC(14696);
        setSprayed(false);
        super.sendDeath(source);
    }
}