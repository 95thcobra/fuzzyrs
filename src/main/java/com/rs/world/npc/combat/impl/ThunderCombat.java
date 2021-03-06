package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class ThunderCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{11872};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(3)) {
                case 0:
                    npc.setNextForceTalk(new ForceTalk("RAAAAAARRRRRRGGGGHHHH"));
                    break;
                case 1:
                    npc.setNextForceTalk(new ForceTalk(
                            "You're going straight to hell!"));
                    break;
                case 2:
                    String name = "";
                    if (target instanceof Player) {
                        name = ((Player) target).getDisplayName();
                    }
                    npc.setNextForceTalk(new ForceTalk("I'm going to crush you, "
                            + name));
                    break;
                case 3:
                    name = "";
                    if (target instanceof Player) {
                        name = ((Player) target).getDisplayName();
                    }
                    npc.setNextForceTalk(new ForceTalk("Die with pain, " + name));
                    break;
            }
        }
        if (Utils.getRandom(1) == 0) { // mage magical attack
            npc.setCapDamage(800);
            npc.setNextAnimation(new Animation(14525));
            npc.setNextForceTalk(new ForceTalk("FUS RO DAH"));
            npc.playSound(168, 2);
            for (final Entity t : npc.getPossibleTargets()) {
                if (!t.withinDistance(npc, 18)) {
                    continue;
                }
                final int damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.MAGE, t);
                if (damage > 0) {
                    CombatScript.delayHit(npc, 1, t, CombatScript.getMagicHit(npc, damage));
                    t.setNextGraphics(new Graphics(3428));
                }
            }

        } else { // melee attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            npc.setCapDamage(800);
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                    NPCCombatDefinitions.MELEE, target)));
        }
        return defs.getAttackDelay();
    }
}
