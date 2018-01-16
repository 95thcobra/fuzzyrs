package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Thornysnail extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -1147053487269627345L;

    public Thornysnail(final Player owner, final Summoning.Pouches pouch,
                       final WorldTile tile, final int mapAreaNameHash,
                       final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Slime Spray";
    }

    @Override
    public String getSpecialDescription() {
        return "Inflicts up to 80 damage against your opponent.";
    }

    @Override
    public int getBOBSize() {
        return 3;
    }

    @Override
    public int getSpecialAmount() {
        return 0;
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
