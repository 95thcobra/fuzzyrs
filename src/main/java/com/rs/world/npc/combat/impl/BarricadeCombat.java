package com.rs.world.npc.combat.impl;

import com.rs.entity.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;

public class BarricadeCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        // TODO Auto-generated method stub
        return new Object[]{"Barricade"};
    }

    /*
     * empty
     */
    @Override
    public int attack(final NPC npc, final Entity target) {
        return 0;
    }

}
