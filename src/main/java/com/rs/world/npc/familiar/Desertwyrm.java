package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Desertwyrm extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 678861520073043877L;

    public Desertwyrm(final Player owner, final Summoning.Pouches pouch,
                      final WorldTile tile, final int mapAreaNameHash,
                      final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Electric Lash";
    }

    @Override
    public String getSpecialDescription() {
        return "Attacks the player's opponent inflicting up to 50 damage instead of 40 damage. ";
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
