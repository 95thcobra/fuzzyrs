package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Geysertitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -707448797034175432L;

    public Geysertitan(final Player owner, final Pouches pouch,
                       final WorldTile tile, final int mapAreaNameHash,
                       final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Boil";
    }

    @Override
    public String getSpecialDescription() {
        return "Increases the titan's combat by 60 in the next combat tick.";
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
