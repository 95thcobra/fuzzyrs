package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Bronzeminotaur extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -4657392160246588028L;

    public Bronzeminotaur(final Player owner, final Pouches pouch,
                          final WorldTile tile, final int mapAreaNameHash,
                          final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Bull Rush";
    }

    @Override
    public String getSpecialDescription() {
        return "A magical attack doing up to 40 life points of damage while stunning an opponent.";
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
        final Player player = (Player) object;
        player.setNextGraphics(new Graphics(1316));
        player.setNextAnimation(new Animation(7660));
        return true;
    }
}
