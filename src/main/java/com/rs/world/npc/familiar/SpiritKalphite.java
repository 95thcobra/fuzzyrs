package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class SpiritKalphite extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 6110983138725755250L;

    public SpiritKalphite(final Player owner, final Summoning.Pouches pouch,
                          final WorldTile tile, final int mapAreaNameHash,
                          final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Sandstorm";
    }

    @Override
    public String getSpecialDescription() {
        return "Attacks up to five opponents with a max damage of 50.";
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
