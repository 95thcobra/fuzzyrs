package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class PkerCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{15174};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(8)) {
                case 0:
                    npc.setNextForceTalk(new ForceTalk("Gl Mate"));
                    break;
                case 1:
                    npc.setNextForceTalk(new ForceTalk("Stop safing!"));
                    break;
                case 2:
                    npc.setNextForceTalk(new ForceTalk("Awwwww"));
                    break;
                case 3:
                    npc.setNextForceTalk(new ForceTalk("Gf?"));
                    break;
                case 4:
                    npc.setNextForceTalk(new ForceTalk("Bring it on!"));
                    break;
                case 5:
                    npc.setNextForceTalk(new ForceTalk("Food left?"));
                    break;
                case 6:
                    npc.setNextForceTalk(new ForceTalk("Pray off noob!"));
                    break;
                case 7:
                    npc.setNextForceTalk(new ForceTalk("2gp"));
                    break;
                case 8:
                    npc.setNextForceTalk(new ForceTalk("Smd Gaiboy"));
                    break;
            }
        }
        if (Utils.getRandom(3) == 0) {
            npc.setNextAnimation(new Animation(829)); // eating 2 rocktails at a
            // time xd
            npc.heal(460);
        }
        if (Utils.getRandom(2) == 0) { // Melee - Special Attack
            npc.setNextAnimation(new Animation(1062)); // i know this is a dds
            // special attack and he
            // isn't wearing it...
            npc.setNextGraphics(new Graphics(252, 0, 100)); // don't make it hit
            // higher then 300,
            // could end very
            // very badly
            npc.playSound(2537, 1);
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 345,
                                        NPCCombatDefinitions.MELEE, target)),
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 345,
                                        NPCCombatDefinitions.MELEE, target)));
            }
        } else { // Melee - Whip Attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
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