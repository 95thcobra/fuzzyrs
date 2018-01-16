package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class SpiritTzKih extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 8469842707500116693L;

    public SpiritTzKih(final Player owner, final Pouches pouch,
                       final WorldTile tile, final int mapAreaNameHash,
                       final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Fireball Assault";
    }

    @Override
    public String getSpecialDescription() {
        return "Has the potential of hitting up to two nearby targets with up to 70 points of damage";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 6;
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
