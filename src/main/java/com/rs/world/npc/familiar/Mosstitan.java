package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Mosstitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 2779054495849433214L;

    public Mosstitan(final Player owner, final Summoning.Pouches pouch,
                     final WorldTile tile, final int mapAreaNameHash,
                     final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Titan's Constitution ";
    }

    @Override
    public String getSpecialDescription() {
        return "Defence by 12.5%, and it can also increase a player's Life Points 80 points higher than their max Life Points.";
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
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        int newLevel = getOwner().getSkills().getLevel(Skills.DEFENCE)
                + (getOwner().getSkills().getLevelForXp(Skills.DEFENCE) / (int) 12.5);
        if (newLevel > getOwner().getSkills().getLevelForXp(Skills.DEFENCE)
                + (int) 12.5) {
            newLevel = getOwner().getSkills().getLevelForXp(Skills.DEFENCE)
                    + (int) 12.5;
        }
        getOwner().setNextGraphics(new Graphics(2011));
        getOwner().setNextAnimation(new Animation(7660));
        getOwner().getSkills().set(Skills.DEFENCE, newLevel);
        return true;
    }
}
