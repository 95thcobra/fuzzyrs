package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TokHaarKetDillCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"TokHaar-Ket-Dill"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.random(6) == 0) {
            CombatScript.delayHit(npc, 0, target,
                    CombatScript.getRegularHit(npc, Utils.random(defs.getMaxHit() + 1)));
            target.setNextGraphics(new Graphics(2999));
            if (target instanceof Player) {
                final Player playerTarget = (Player) target;
                playerTarget.getPackets().sendGameMessage(
                        "The TokHaar-Ket-Dill slams it's tail to the ground.");
            }
        } else {
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                    defs.getAttackStyle(), target)));
        }
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        return defs.getAttackDelay();
    }
}
