package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Irontitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 6059371477618091701L;

    public Irontitan(final Player owner, final Summoning.Pouches pouch,
                     final WorldTile tile, final int mapAreaNameHash,
                     final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Iron Within";
    }

    @Override
    public String getSpecialDescription() {
        return "Inflicts three melee attacks instead of one in the next attack.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 20;
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
