package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.controlers.BorkController;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class BorkCombat extends CombatScript {

    public boolean spawnOrk = false;

    @Override
    public Object[] getKeys() {
        return new Object[]{"Bork"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions cdef = npc.getCombatDefinitions();
        if (npc.getHitpoints() <= (cdef.getHitpoints() * 0.4) && !spawnOrk) {
            final Player player = (Player) target;
            npc.setNextForceTalk(new ForceTalk("Come to my aid, brothers!"));
            player.getControllerManager()
                    .startController(BorkController.class, 1, npc);
            spawnOrk = true;
        }
        npc.setNextAnimation(new Animation(Utils.getRandom(1) == 0 ? cdef
                .getAttackEmote() : 8757));
        CombatScript.delayHit(
                npc,
                0,
                target,
                CombatScript.getMeleeHit(npc,
                        CombatScript.getRandomMaxHit(npc, cdef.getMaxHit(), -1, target)));
        return cdef.getAttackDelay();
    }

}
