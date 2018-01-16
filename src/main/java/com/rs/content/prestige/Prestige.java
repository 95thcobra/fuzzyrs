package com.rs.content.prestige;

import com.rs.content.economy.shops.ShopsManager;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.World;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class Prestige {

    public static void canPrestige(Player player) {
        for (int i = 0; i < 7; i++) {
            if (player.getSkills().getLevel(i) >= 99) {
                if (i == 6) {
                    setPrestige(player);
                }
            } else {
                player.sendMessage("You must have 99 in Attack, Strength, Defence, Hitpoints, Range, Mage and Prayer in order to prestige.");
                return;
            }
        }
    }

    /**
     * Increases the Players Prestige Level
     **/
    private static void setPrestige(Player player) {
        if (player.getPrestigeLevel() == 25) {
            player.sendMessage("We are sorry but you may only prestige 25 times.");
            return;
        }
        player.setPrestigeLevel(player.getPrestigeLevel() + 1);
        resetSkills(player);
        player.setNextAnimation(new Animation(1914));
        player.setNextGraphics(new Graphics(92));
        player.setNextForceTalk(new ForceTalk("Arguuhhhhhh"));
        player.getPackets().sendGameMessage("You feel a force reach into your soul, You are now Prestige " + player.getPrestigeLevel());
        World.sendWorldMessage("<img=7><col=ff0000>News: " + player.getDisplayName() + " has just prestiged! they have now prestiged " + player.getPrestigeLevel() + " times.", false);
    }

    /**
     * Resets the Skills that are required to prestige.
     **/
    private static void resetSkills(Player player) {
        for (int i = 0; i < 7; i++) {
            player.getSkills().set(i, 1);
            player.getSkills().setXp(i, 1);
            player.getSkills().init();
        }
        player.getSkills().set(3, 10);
        player.getSkills().setXp(3, 1154);
    }

    /**
     * Opens prestige shop.
     * TODO make shops
     **/
    public static void prestigeShops(Player player) {
        int prestigeLevel = player.getPrestigeLevel();
        switch (prestigeLevel) {
            case 1:
                ShopsManager.openShop(player, 35);
            case 2:
                ShopsManager.openShop(player, 36);
            case 3:
                ShopsManager.openShop(player, 37);
            case 4:
                ShopsManager.openShop(player, 38);
            case 5:
                ShopsManager.openShop(player, 39);
                break;
            default:
                player.getPackets().sendGameMessage("You need to have prestiged to gain access to this shop.");
        }
    }
}
