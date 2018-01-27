package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.Combat;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class MetalDragonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Bronze dragon", "Iron dragon", "Steel dragon",
                "Mithril dragon"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Player player = target instanceof Player ? (Player) target : null;
        int damage;
        switch (Utils.getRandom(1)) {
            case 0:
                if (npc.withinDistance(target, 3)) {
                    damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                            NPCCombatDefinitions.MELEE, target);
                    npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                    CombatScript.delayHit(npc, 0, target, CombatScript.getMeleeHit(npc, damage));
                } else {
                    damage = Utils.getRandom(650);
                    if (Combat.hasAntiDragProtection(target)
                            || (player != null && (player.getPrayer().usingPrayer(
                            0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
                        damage = (int) (damage * 0.6);
                        player.getPackets()
                                .sendGameMessage(
                                        "Your "
                                                + (Combat
                                                .hasAntiDragProtection(target) ? "shield"
                                                : "prayer")
                                                + " absorbs most of the dragon's breath!",
                                        true);
                    } else if (player != null
                            && ((!Combat.hasAntiDragProtection(target)
                            || !player.getPrayer().usingPrayer(0, 17) || !player
                            .getPrayer().usingPrayer(1, 7)) && player
                            .getFireImmune() > Utils.currentTimeMillis())) {
                        damage = Utils.getRandom(164);
                        player.getPackets().sendGameMessage(
                                "Your potion absorbs most of the dragon's breath!",
                                true);
                    }
                    npc.setNextAnimation(new Animation(13160));
                    World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
                    CombatScript.delayHit(npc, 1, target, CombatScript.getRegularHit(npc, damage));
                }
                break;
            case 1:
                if (npc.withinDistance(target, 3)) {
                    damage = Utils.getRandom(650);
                    if (Combat.hasAntiDragProtection(target)
                            || (player != null && (player.getPrayer().usingPrayer(
                            0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
                        damage = (int) (damage * 0.6);
                        if (player != null) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "Your "
                                                    + (Combat
                                                    .hasAntiDragProtection(target) ? "shield"
                                                    : "prayer")
                                                    + " absorbs most of the dragon's breath!",
                                            true);
                        }
                    } else if (player != null
                            && ((!Combat.hasAntiDragProtection(target)
                            || !player.getPrayer().usingPrayer(0, 17) || !player
                            .getPrayer().usingPrayer(1, 7)))
                            && player.getFireImmune() > Utils.currentTimeMillis()) {
                        damage = Utils.getRandom(164);
                        player.getPackets()
                                .sendGameMessage(
                                        "Your potion fully protects you from the heat of the dragon's breath!",
                                        true);
                    }
                    npc.setNextAnimation(new Animation(13164));
                    npc.setNextGraphics(new Graphics(2465));
                    CombatScript.delayHit(npc, 1, target, CombatScript.getRegularHit(npc, damage));
                } else {
                    damage = Utils.getRandom(650);
                    if (Combat.hasAntiDragProtection(target)
                            || (player != null && (player.getPrayer().usingPrayer(
                            0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
                        damage = 0;
                        if (player != null) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "Your "
                                                    + (Combat
                                                    .hasAntiDragProtection(target) ? "shield"
                                                    : "prayer")
                                                    + " absorbs most of the dragon's breath!",
                                            true);
                        }
                    } else if (player != null
                            && ((!Combat.hasAntiDragProtection(target)
                            || !player.getPrayer().usingPrayer(0, 17) || !player
                            .getPrayer().usingPrayer(1, 7)))
                            && player.getFireImmune() > Utils.currentTimeMillis()) {
                        damage = Utils.getRandom(164);
                        player.getPackets()
                                .sendGameMessage(
                                        "Your potion fully protects you from the heat of the dragon's breath!",
                                        true);
                    }
                    npc.setNextAnimation(new Animation(13160));
                    World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
                    CombatScript.delayHit(npc, 1, target, CombatScript.getRegularHit(npc, damage));
                }
                break;
        }
        return defs.getAttackDelay();
    }

}
