package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Bullant extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 4667052662212699631L;

    public Bullant(final Player owner, final Pouches pouch,
                   final WorldTile tile, final int mapAreaNameHash,
                   final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Unburden";
    }

    @Override
    public String getSpecialDescription() {
        return "Restores the owner's run energy by half of their Agility level.";
    }

    @Override
    public int getBOBSize() {
        return 30;
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
        if (player.getRunEnergy() == 100) {
            player.getPackets().sendGameMessage(
                    "This wouldn't effect you at all.");
            return false;
        }
        final int agilityLevel = getOwner().getSkills()
                .getLevel(Skills.AGILITY);
        final int runEnergy = player.getRunEnergy()
                + (Math.round(agilityLevel / 2));
        player.setNextGraphics(new Graphics(1300));
        player.setNextAnimation(new Animation(7660));
        player.setRunEnergy(runEnergy > 100 ? 100 : runEnergy);
        return true;
    }
}
