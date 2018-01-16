package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Vampirebat extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 586089784797828590L;

    public Vampirebat(final Player owner, final Summoning.Pouches pouch,
                      final WorldTile tile, final int mapAreaNameHash,
                      final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Vampyre Touch";
    }

    @Override
    public String getSpecialDescription() {
        return "Deals damage to your opponents, with a maximum hit of 120. It also has a chance of healing your lifepoints by 20. ";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 4;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        return false;
    }
}
