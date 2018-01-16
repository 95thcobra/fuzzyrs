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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KalphiteQueenCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Kalphite Queen"};
    }

    private void attackMageTarget(final List<Player> arrayList,
                                  final Entity fromEntity, final NPC startTile, final Entity t) {
        final Entity target = t == null ? getTarget(arrayList, fromEntity,
                startTile) : t;
        if (target == null)
            return;
        if (target instanceof Player) {
            arrayList.add((Player) target);
        }
        World.sendProjectile(fromEntity, target, 280,
                fromEntity == startTile ? 70 : 20, 20, 60, 30, 0, 0);
        CombatScript.delayHit(
                startTile,
                0,
                target,
                CombatScript.getMagicHit(
                        startTile,
                        CombatScript.getRandomMaxHit(startTile, startTile.getMaxHit(),
                                NPCCombatDefinitions.MAGE, target)));
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                target.setNextGraphics(new Graphics(281));
                attackMageTarget(arrayList, target, startTile, null);
            }
        });
    }

    private Player getTarget(final List<Player> list, final Entity fromEntity,
                             final WorldTile startTile) {
        if (fromEntity == null)
            return null;
        final ArrayList<Player> added = new ArrayList<Player>();
        for (final int regionId : fromEntity.getMapRegionsIds()) {
            final List<Integer> playersIndexes = World.getRegion(regionId)
                    .getPlayerIndexes();
            if (playersIndexes == null) {
                continue;
            }
            for (final Integer playerIndex : playersIndexes) {
                final Player player = World.getPlayers().get(playerIndex);
                if (player == null || list.contains(player)
                        || !player.withinDistance(fromEntity)
                        || !player.withinDistance(startTile)) {
                    continue;
                }
                added.add(player);
            }
        }
        if (added.isEmpty())
            return null;
        Collections.sort(added, new Comparator<Player>() {

            @Override
            public int compare(final Player o1, final Player o2) {
                if (o1 == null)
                    return 1;
                if (o2 == null)
                    return -1;
                if (Utils.getDistance(o1, fromEntity) > Utils.getDistance(o2,
                        fromEntity))
                    return 1;
                else if (Utils.getDistance(o1, fromEntity) < Utils.getDistance(
                        o2, fromEntity))
                    return -1;
                else
                    return 0;
            }
        });
        return added.get(0);

    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = Utils.random(3);
        if (attackStyle == 0) {
            final int distanceX = target.getX() - npc.getX();
            final int distanceY = target.getY() - npc.getY();
            final int size = npc.getSize();
            if (distanceX > size || distanceX < -1 || distanceY > size
                    || distanceY < -1) {
                attackStyle = Utils.random(2); // set mage
            } else {
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                CombatScript.delayHit(
                        npc,
                        0,
                        target,
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                        NPCCombatDefinitions.MELEE, target)));
                return defs.getAttackDelay();
            }
        }
        npc.setNextAnimation(new Animation(npc.getId() == 1158 ? 6240 : 6234));
        if (attackStyle == 1) { // range easy one
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        2,
                        t,
                        CombatScript.getRangeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                        NPCCombatDefinitions.RANGE, t)));
                World.sendProjectile(npc, t, 288, 46, 31, 50, 30, 16, 0);
            }
        } else {
            npc.setNextGraphics(new Graphics(npc.getId() == 1158 ? 278 : 279));
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    attackMageTarget(new ArrayList<Player>(), npc, npc, target);
                }

            });
        }
        return defs.getAttackDelay();
    }
}
