package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Spiritscorpion extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 6369083931716875985L;

    public Spiritscorpion(final Player owner, final Pouches pouch,
                          final WorldTile tile, final int mapAreaNameHash,
                          final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Venom Shot";
    }

    @Override
    public String getSpecialDescription() {
        return "Chance of next Ranged attack becoming mildly poisonous, given that the Ranged weapon being used can be poisoned";
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
