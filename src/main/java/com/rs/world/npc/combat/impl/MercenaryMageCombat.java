package com.rs.world.npc.combat.impl;

import com.rs.entity.Entity;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.Hit.HitLook;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class MercenaryMageCombat extends CombatScript {

    public static final String[] ATTACKS = new String[]{"SMAAAAAASH!",
            "DARD MAD!", "IM A PEDOPHILE!", "RAAARGH!",
            "IS THIS ALL YOU'VE GOT?"};

    @Override
    public Object[] getKeys() {
        return new Object[]{8335};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final int attackStyle = Utils.random(5);
        if (attackStyle == 0) {
            npc.setNextAnimation(new Animation(1979));
            final WorldTile center = new WorldTile(target);
            World.sendGraphics(npc, new Graphics(2929), center);
            npc.setNextForceTalk(new ForceTalk("I CLOGGED THE TOILET"));
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    for (final Player player : World.getPlayers()) { // lets
                        // just
                        // loop
                        // all
                        // players
                        // for
                        // massive
                        // moves
                        if (player == null || player.isDead()
                                || player.hasFinished()) {
                            continue;
                        }
                        if (player.withinDistance(center, 3)) {
                            if (!player.getMusicsManager().hasMusic(843)) {
                                player.getMusicsManager().playMusic(843);
                            }
                            CombatScript.delayHit(npc, 0, player,
                                    new Hit(npc, Utils.random(250),
                                            HitLook.MAGIC_DAMAGE));
                        }
                    }
                }

            }, 4);
        } else if (attackStyle == 1) {
            npc.setNextAnimation(new Animation(1979));
            final WorldTile center = new WorldTile(target);
            World.sendGraphics(npc, new Graphics(2191), center);
            npc.setNextForceTalk(new ForceTalk("BAN HAMMER!"));
            WorldTasksManager.schedule(new WorldTask() {
                int count = 0;

                @Override
                public void run() {
                    for (final Player player : World.getPlayers()) { // lets
                        // just
                        // loop
                        // all
                        // players
                        // for
                        // massive
                        // moves
                        if (player == null || player.isDead()
                                || player.hasFinished()) {
                            continue;
                        }
                        if (player.withinDistance(center, 1)) {
                            CombatScript.delayHit(npc, 0, player,
                                    new Hit(npc, Utils.random(100),
                                            HitLook.MAGIC_DAMAGE));
                        }
                    }
                    if (count++ == 10) {
                        stop();
                        return;
                    }
                }
            }, 0, 0);
        } else if (attackStyle == 2) {
            npc.setNextAnimation(new Animation(1979));
            final int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
            final WorldTile center = new WorldTile(npc.getX()
                    + Utils.DIRECTION_DELTA_X[dir] * 5, npc.getY()
                    + Utils.DIRECTION_DELTA_Y[dir] * 5, 0);
            npc.setNextForceTalk(new ForceTalk("GET THE FUCK OUT OF MY HOOD!"));
            WorldTasksManager.schedule(new WorldTask() {
                int count = 0;

                @Override
                public void run() {
                    for (final Player player : World.getPlayers()) { // lets
                        // just
                        // loop
                        // all
                        // players
                        // for
                        // massive
                        // moves
                        if (Utils.DIRECTION_DELTA_X[dir] == 0) {
                            if (player.getX() != center.getX()) {
                                continue;
                            }
                        }
                        if (Utils.DIRECTION_DELTA_Y[dir] == 0) {
                            if (player.getY() != center.getY()) {
                                continue;
                            }
                        }
                        if (Utils.DIRECTION_DELTA_X[dir] != 0) {
                            if (Math.abs(player.getX() - center.getX()) > 5) {
                                continue;
                            }
                        }
                        if (Utils.DIRECTION_DELTA_Y[dir] != 0) {
                            if (Math.abs(player.getY() - center.getY()) > 5) {
                                continue;
                            }
                        }
                        CombatScript.delayHit(npc, 0, player, new Hit(npc,
                                Utils.random(250), HitLook.MAGIC_DAMAGE));
                    }
                    if (count++ == 5) {
                        stop();
                        return;
                    }
                }
            }, 0, 0);
            World.sendProjectile(npc, center, 2196, 0, 0, 5, 35, 0, 0);
        } else if (attackStyle == 3) {
            CombatScript.delayHit(
                    npc,
                    2,
                    target,
                    CombatScript.getMagicHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, Utils.random(250),
                                    NPCCombatDefinitions.MAGE, target)));
            World.sendProjectile(npc, target, 2873, 34, 16, 40, 35, 16, 0);
            npc.setNextAnimation(new Animation(14221));
            npc.setNextForceTalk(new ForceTalk(ATTACKS[Utils
                    .random(ATTACKS.length)]));
        } else if (attackStyle == 4) {
            npc.setNextAnimation(new Animation(1979));
            npc.setNextGraphics(new Graphics(444));

        }
        return 5;
    }

}
