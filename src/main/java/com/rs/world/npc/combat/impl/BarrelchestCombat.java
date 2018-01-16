package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class BarrelchestCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{5666};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int attackStyle = Utils.getRandom(1);
        switch (attackStyle) {
            case 0:// main attack
            case 1:// melee attack
                int damage = 438;// normal
                for (final Entity e : npc.getPossibleTargets()) {
                    if (e instanceof Player
                            && (((Player) e).getPrayer().usingPrayer(0, 19) || ((Player) e)
                            .getPrayer().usingPrayer(1, 9))) {
                        final Player player = (Player) e;
                        damage = 0;
                        player.getPrayer().drainPrayer((Math.round(damage / 20)));
                        player.setPrayerDelay(Utils.getRandom(5) + 5);
                        player.getPackets()
                                .sendGameMessage(
                                        "Barrelchest slams through your protection prayer!");
                    }
                    npc.setNextAnimation(new Animation(damage <= 463 ? 5894 : 5894));
                    CombatScript.delayHit(
                            npc,
                            0,
                            e,
                            CombatScript.getMeleeHit(
                                    npc,
                                    CombatScript.getRandomMaxHit(npc, damage,
                                            NPCCombatDefinitions.MELEE, e)));
                }
                break;
        }
        return defs.getAttackDelay();
    }
}
