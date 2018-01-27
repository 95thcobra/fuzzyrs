package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.others.DreadNip;

public class DreadNipCombat extends CombatScript {

    private final String[] DREADNIP_ATTACK_MESSAGE = {
            "Your dreadnip stunned its target!",
            "Your dreadnip poisened its target!"};

    @Override
    public Object[] getKeys() {
        return new Object[]{14416};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final DreadNip dreadNip = (DreadNip) npc;
        if (dreadNip.getTicks() <= 3)
            return 0;
        npc.setNextAnimation(new Animation(-1));
        final int attackStyle = Utils.random(2);
        switch (attackStyle) {
            case 0:
                break;
            case 1:
                final int secondsDelay = 5 + Utils.getRandom(3);
                target.setFreezeDelay(secondsDelay);
                if (target instanceof Player) {
                    final Player player = (Player) target;
                    player.getActionManager().addActionDelay(secondsDelay);
                } else {
                    final NPC npcTarget = (NPC) target;
                    npcTarget.getCombat().setCombatDelay(
                            npcTarget.getCombat().getCombatDelay() + secondsDelay);
                }
                break;
            case 2:
                target.getPoison().makePoisoned(108);
                break;
        }
        if (attackStyle != 0) {
            dreadNip.getOwner().getPackets()
                    .sendGameMessage(DREADNIP_ATTACK_MESSAGE[attackStyle - 1]);
        }
        CombatScript.delayHit(
                npc,
                0,
                target,
                new Hit(npc, CombatScript.getRandomMaxHit(npc, 550,
                        NPCCombatDefinitions.MELEE, target),
                        HitLook.REGULAR_DAMAGE));
        return 5;
    }
}
