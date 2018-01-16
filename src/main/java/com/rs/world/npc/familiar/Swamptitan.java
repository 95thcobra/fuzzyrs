package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Swamptitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -6073150798974730997L;

    public Swamptitan(final Player owner, final Summoning.Pouches pouch,
                      final WorldTile tile, final int mapAreaNameHash,
                      final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Swamp Plague";
    }

    @Override
    public String getSpecialDescription() {
        return "Inflicts a magical attack on near by opponents and attempts to poison them as well.";
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
