package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Spiritmosquito extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 3249731229258558109L;

    public Spiritmosquito(final Player owner, final Summoning.Pouches pouch,
                          final WorldTile tile, final int mapAreaNameHash,
                          final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Pester";
    }

    @Override
    public String getSpecialDescription() {
        return "Sends a mosquito to your opponent.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 3;
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
