package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Granitecrab extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 649164679697311630L;

    public Granitecrab(final Player owner, final Summoning.Pouches pouch,
                       final WorldTile tile, final int mapAreaNameHash,
                       final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Stony Shell";
    }

    @Override
    public String getSpecialDescription() {
        return "Increases your restance to all attacks by four.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 12;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        final Player player = (Player) object;
        int newLevel = player.getSkills().getLevel(Skills.DEFENCE) + 4;
        if (newLevel > player.getSkills().getLevelForXp(Skills.DEFENCE) + 4) {
            newLevel = player.getSkills().getLevelForXp(Skills.DEFENCE) + 4;
        }
        player.setNextGraphics(new Graphics(1300));
        player.setNextAnimation(new Animation(7660));
        setNextGraphics(new Graphics(8108));
        setNextAnimation(new Animation(1326));
        player.getSkills().set(Skills.DEFENCE, newLevel);
        return true;
    }

}
