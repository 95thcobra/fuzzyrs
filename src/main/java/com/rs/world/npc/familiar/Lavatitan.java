package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Lavatitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -778365732778023700L;

    public Lavatitan(final Player owner, final Summoning.Pouches pouch,
                     final WorldTile tile, final int mapAreaNameHash,
                     final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Ebon Thunder";
    }

    @Override
    public String getSpecialDescription() {
        return "Possibly drain an enemy's special attack energy by 10%";
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
