package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.fishing.Fishing.Fish;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.player.content.Foods.Food;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

public class Bunyip extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 7203440350875823581L;

    public Bunyip(final Player owner, final Summoning.Pouches pouch,
                  final WorldTile tile, final int mapAreaNameHash,
                  final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Swallow Whole";
    }

    @Override
    public String getSpecialDescription() {
        return "Eat an uncooked fish and gain the correct number of life points corresponding to the fish eaten if you have the cooking level to cook the fish.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 7;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ITEM;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        final Item item = getOwner().getInventory().getItem((Integer) object);
        if (item == null)
            return false;
        for (final Fish fish : Fish.values()) {
            if (fish.getId() == item.getId()) {
                if (getOwner().getSkills().getLevel(Skills.COOKING) < fish
                        .getLevel()) {
                    getOwner()
                            .getPackets()
                            .sendGameMessage(
                                    "Your cooking level is not high enough for the bunyip to eat this fish.");
                    return false;
                } else {
                    getOwner().setNextGraphics(new Graphics(1316));
                    getOwner().setNextAnimation(new Animation(7660));
                    getOwner().heal(Food.forId(item.getId()).getHeal());
                    getOwner().getInventory().deleteItem(item.getId(),
                            item.getAmount());
                    return true;// stop here
                }
            } else {
                getOwner().getPackets().sendGameMessage(
                        "Your bunyip cannot eat this.");
                return false;
            }
        }
        return true;
    }
}
