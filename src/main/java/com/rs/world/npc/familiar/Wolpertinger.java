package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Wolpertinger extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 4097036858996221680L;

    public Wolpertinger(final Player owner, final Summoning.Pouches pouch,
                        final WorldTile tile, final int mapAreaNameHash,
                        final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Magic Focus";
    }

    @Override
    public String getSpecialDescription() {
        return "Boosts your restistance towards magic by 5% while also boosting your magic by 7%.";
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
        final Player player = (Player) object;
        int newLevel = player.getSkills().getLevel(Skills.MAGIC) + 7;
        if (newLevel > player.getSkills().getLevelForXp(Skills.MAGIC) + 7) {
            newLevel = player.getSkills().getLevelForXp(Skills.MAGIC) + 7;
        }
        player.setNextGraphics(new Graphics(1300));
        player.setNextAnimation(new Animation(7660));
        player.getSkills().set(Skills.MAGIC, newLevel);
        return true;
    }
}
