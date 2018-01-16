package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.Combat;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class LeatherDragonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Green dragon", "Blue dragon", "Red dragon",
                "Black dragon", 742, 14548};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final int size = npc.getSize();
        if (distanceX > size || distanceX < -1 || distanceY > size
                || distanceY < -1)
            return 0;
        if (Utils.getRandom(3) != 0) {
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                    NPCCombatDefinitions.MELEE, target)));
        } else {
            int damage = Utils.getRandom(650);
            npc.setNextAnimation(new Animation(12259));
            npc.setNextGraphics(new Graphics(1, 0, 100));
            final Player player = target instanceof Player ? (Player) target
                    : null;
            if (Combat.hasAntiDragProtection(target)
                    || (player != null && (player.getPrayer()
                    .usingPrayer(0, 17) || player.getPrayer()
                    .usingPrayer(1, 7)))) {
                damage = 0;
                player.getPackets()
                        .sendGameMessage(
                                "Your "
                                        + (Combat.hasAntiDragProtection(target) ? "shield"
                                        : "prayer")
                                        + " absorb's most of the dragon's breath!",
                                true);
            }
            if (player != null
                    && player.getFireImmune() > Utils.currentTimeMillis()) {
                if (damage != 0) {
                    damage = Utils.getRandom(50);
                }
            } else if (damage == 0) {
                damage = Utils.getRandom(50);
            } else if (player != null) {
                player.getPackets().sendGameMessage(
                        "You are hit by the dragon's fiery breath!", true);
            }
            CombatScript.delayHit(npc, 1, target, CombatScript.getRegularHit(npc, damage));
        }
        return defs.getAttackDelay();
    }
}
