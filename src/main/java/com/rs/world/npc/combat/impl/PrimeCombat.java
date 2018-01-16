package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.HashMap;

public class PrimeCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{111111};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int attackStyle = Utils.getRandom(5);

        if (Utils.getRandom(10) == 0) {
            final ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
            final HashMap<String, int[]> tiles = new HashMap<String, int[]>();
            for (final Entity t : possibleTargets) {
                if (t instanceof Player) {
                    final Player p = (Player) t;
                }
                final String key = t.getX() + "_" + t.getY();
                if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
                    tiles.put(key, new int[]{t.getX(), t.getY()});
                    World.sendProjectile(npc, new WorldTile(t.getX(), t.getY(),
                            npc.getPlane()), 1900, 34, 0, 30, 35, 16, 0);
                }
            }
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    final ArrayList<Entity> possibleTargets = npc
                            .getPossibleTargets();
                    for (final int[] tile : tiles.values()) {

                        World.sendGraphics(null, new Graphics(1),
                                new WorldTile(tile[0], tile[1], 0));
                        for (final Entity t : possibleTargets)
                            if (t.getX() == tile[0] && t.getY() == tile[1]) {
                                t.applyHit(new Hit(npc,
                                        Utils.getRandom(150) + 200,
                                        Hit.HitLook.MAGIC_DAMAGE));
                            }
                    }
                    stop();
                }

            }, 5);
        } else if (Utils.getRandom(10) == 0) {

        }
        if (attackStyle == 0) { // normal mage move
            npc.setNextAnimation(new Animation(2854));
            CombatScript.delayHit(
                    npc,
                    2,
                    target,
                    getMagicHit(
                            npc,
                            getRandomMaxHit(npc, 291,
                                    NPCCombatDefinitions.MAGE, target)));
            World.sendProjectile(npc, target, 2963, 34, 16, 40, 35, 16, 0);
        } else if (attackStyle == 1) { // normal mage move
            npc.setNextAnimation(new Animation(2855));
            CombatScript.delayHit(
                    npc,
                    2,
                    target,
                    getMagicHit(
                            npc,
                            getRandomMaxHit(npc, 291,
                                    NPCCombatDefinitions.MAGE, target)));
            World.sendProjectile(npc, target, 1904, 34, 16, 30, 35, 16, 0);

            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                }

            }, 2);

        } else if (attackStyle == 2) {
            npc.setNextAnimation(new Animation(2855));
            World.sendProjectile(npc, target, 1899, 34, 16, 30, 95, 16, 0);
            CombatScript.delayHit(
                    npc,
                    4,
                    target,
                    getMagicHit(
                            npc,
                            getRandomMaxHit(npc, 317,
                                    NPCCombatDefinitions.MAGE, target)));
        } else if (attackStyle == 3) {
            npc.setNextAnimation(new Animation(2855));

            CombatScript.delayHit(
                    npc,
                    2,
                    target,
                    getMagicHit(npc, target.getMaxHitpoints() - 1 > 200 ? 200
                            : target.getMaxHitpoints() - 1));
        } else if (attackStyle == 4) {
            /*
			 * 11364 - even better k0 move. fire balls from sky into everyone
			 * 80% max hp or gfx 2600 everyone near dies
			 */
            npc.setNextAnimation(new Animation(2854));
            npc.setCantInteract(true);
            npc.getCombat().removeTarget();
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    for (final Entity t : npc.getPossibleTargets()) {
                        t.applyHit(new Hit(npc, (int) (t.getHitpoints() * Math
                                .random()), Hit.HitLook.MAGIC_DAMAGE, 0));
                    }
                    npc.setCantInteract(false);
                    npc.setTarget(target);
                }

            }, 4);
            return 0;
        } else if (attackStyle == 5) {
            npc.setCantInteract(true);
            npc.setNextAnimation(new Animation(2854));
            npc.getCombat().removeTarget();
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    npc.setCantInteract(false);
                    npc.setTarget(target);
                    final int size = npc.getSize();
                    final int[][] dirs = Utils.getCoordOffsetsNear(size);
                    for (int dir = 0; dir < dirs[0].length; dir++) {
                        final WorldTile tile = new WorldTile(new WorldTile(
                                target.getX() + dirs[0][dir], target.getY()
                                + dirs[1][dir], target.getPlane()));
                        if (World.canMoveNPC(tile.getPlane(), tile.getX(),
                                tile.getY(), size)) { // if found done
                            npc.setNextWorldTile(tile);
                        }
                    }
                }
            }, 3);
            return defs.getAttackDelay();
        }

        return defs.getAttackDelay();
    }
}