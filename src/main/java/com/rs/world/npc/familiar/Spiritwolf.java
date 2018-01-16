package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Spiritwolf extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 2691875962052924796L;

    public Spiritwolf(final Player owner, final Summoning.Pouches pouch,
                      final WorldTile tile, final int mapAreaNameHash,
                      final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Howl";
    }

    @Override
    public String getSpecialDescription() {
        return "Scares non-player opponents, causing them to retreat. However, this lasts for only a few seconds.";
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
        final Player player = (Player) object;
        player.setNextAnimation(new Animation(7660));
        player.setNextGraphics(new Graphics(1316));
        return true;
    }
}
