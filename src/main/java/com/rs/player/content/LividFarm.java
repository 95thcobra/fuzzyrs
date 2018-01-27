package com.rs.player.content;

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.content.player.points.PlayerPoints;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;

/**
 * Do not remove the authority of this file.
 *
 * @author Nexon / Fuzen Seth
 */
public class LividFarm {
    /**
     * Main Data
     */
    public static int bucket = 20933, orb = 6950, logs = 1511, LADY = 7530,
            plant = 20704, bunchedplant = 20705, exp = 50, othskill = 50;
    /*
     * XP Boosting rates
     */
    public static int boostedxp = 1125;

    /**
     * @param player
     */
    public static void TakeLogs(final Player player) {
        player.getPackets().sendGameMessage(
                "You have taken a log from the log pile.");
        player.getInventory().addItem(logs, 1);
        player.lock(1);
        player.setLividfarm(false);
        player.setNextAnimation(new Animation(832));
    }

    /**
     * @param player
     */
    public static void MakePlants(final Player player) {
        if (player.getInventory().containsItem(bucket, 1)) {
            player.lock(3);
            player.setNextAnimation(new Animation(4853));
            player.getPackets().sendGameMessage("You have grown livid plant.");
            player.getInventory().addItem(plant, 1);

            player.setNextAnimation(new Animation(2282));
            player.getInventory().refresh();
        } else {

        }
        if (player.getInventory().containsItem(orb, 1)) {
            player.lock(3);
            player.getPackets().sendGameMessage("You have grown livid plant.");
            player.getInventory().addItem(plant, Utils.random(3));
            player.getInventory().refresh();
            player.setNextAnimation(new Animation(778));
            player.setNextGraphics(new Graphics(2039));
        } else {
            /*
             * This is supposed to do nothing.
			 */
        }
    }

    public static void bunchPlants(final Player player) {
        player.getInventory().deleteItem(plant, 1);
        player.getInventory().addItem(bunchedplant, 1);
        player.getSkills().addXp(Skills.HERBLORE, othskill);
        player.getInventory().refresh();
        player.getPackets().sendGameMessage("You have bunched the plant.");
    }

    /**
     * Orb: Autobunches all the plants
     *
     * @param player
     */
    public static void OrbBunch(final Player player) {
        if (player.getInventory().containsItem(plant, 27)) {
            player.getInventory().deleteItem(plant, 27);
            player.getInventory().addItem(bunchedplant, 27);
            player.getInventory().refresh();
            player.setNextAnimation(new Animation(778));
            player.getInterfaceManager().closeChatBoxInterface();
            player.setNextGraphics(new Graphics(2039));
            player.getPackets()
                    .sendGameMessage(
                            "You use power of your magical orb, all your plants are bunched.");
        } else {
            player.getPackets().sendGameMessage(
                    "You must have 27 plants to insta-bunch.");
        }
    }

    /**
     * @param player
     */
    public static void deposit(final Player player) {
        if (player.getInventory().containsItem(bunchedplant, 27)) {
            player.getSkills().addXp(Skills.FARMING, boostedxp);
            player.setNextAnimation(new Animation(780));
            player.getSkills().addXp(Skills.MAGIC, othskill);
            player.getSkills().addXp(Skills.CRAFTING, othskill);
            player.getInventory().deleteItem(bunchedplant, 27);
            player.getInventory().refresh();
            player.getPlayerPoints().addPoints(PlayerPoints.LIVID_POINTS, 75);
            player.getPackets().sendGameMessage(
                    "You receive points, you have now: " + player.getPlayerPoints().getPoints(PlayerPoints.LIVID_POINTS)
                            + ".");
            player.getDialogueManager().startDialogue(
                    SimpleNPCMessage.class,
                    LADY,
                    "You're so helpful, " + player.getDisplayName()
                            + ". Thank you!");
        } else {
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>You must have 27 bunched plants to deposit them.");
        }
    }

    /**
     * @param player
     */
    public static void takemoreLogs(final Player player) {
        player.getPackets().sendGameMessage(
                "You have taken five logs from the log pile.");
        player.lock(2);
        player.getInventory().addItem(1511, 5);
        player.setLividfarm(false);
        player.setNextAnimation(new Animation(832));
    }

    /**
     * @param player
     */
    public static void CheckforLogs(final Player player) {
        if (player.getInventory().containsItem(logs, 28)) {
            player.getInventory().deleteItem(logs, 28);
            player.getInventory().addItem(bucket, 1);
            player.setLividfarm(true);
            player.getDialogueManager().startDialogue(
                    SimpleNPCMessage.class,
                    LADY,
                    "Thank you for the logs! You should start farming plants now, "
                            + player.getDisplayName() + ".");
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>Congratulations! You can now start with the minigame.");
        } else {
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>You must get 28 logs for Lady Servil to get started.");
        }
    }

    /*
     * Player-owned experience settings, after reaching 80+ farming.
     */
    public static void setCrafting(final Player player) {
        player.setLividcraft(true);
        player.getPackets().sendGameMessage(
                "You will be gaining now Crafting experience only.");
        player.setLividmagic(false);
        player.setLividfarming(true);
    }

    public static void setMagic(final Player player) {
        player.setLividcraft(false);
        player.getPackets().sendGameMessage(
                "You will be gaining now Magic experience only.");
        player.setLividmagic(true);
        player.setLividfarming(false);
    }

    public static void setFarming(final Player player) {
        player.setLividcraft(false);
        player.getPackets().sendGameMessage(
                "You will be gaining now Crafting experience only.");
        player.setLividmagic(false);
        player.setLividfarming(true);
    }

    /*
     * Item Points handling
     */
    public static void HighLanderSet(final Player player) {
        if (player.getPlayerPoints().getPoints(PlayerPoints.LIVID_POINTS) >= 2000) {
            player.getPlayerPoints().removePoints(PlayerPoints.LIVID_POINTS, 2000);
            player.getInterfaceManager().closeChatBoxInterface();
            player.getInventory().addItem(22414, 1);
            player.getInventory().addItem(22413, 1);
            player.getInventory().addItem(22415, 1);
            player.getInventory().addItem(22418, 1);
            player.getInventory().refresh();
            player.getPackets().sendGameMessage(
                    "Your payment was succesful, current points: "
                            + player.getPlayerPoints().getPoints(PlayerPoints.LIVID_POINTS) + ".");
        } else {
            player.getInterfaceManager().closeChatBoxInterface();
            player.getPackets()
                    .sendGameMessage(
                            "You have no enough points. Citharede set costs 2,000 livid points.");
        }
    }

    public static void OrbPayment(final Player player) {
        if (player.getPlayerPoints().getPoints(PlayerPoints.LIVID_POINTS) >= 2800) {
            player.getPlayerPoints().removePoints(PlayerPoints.LIVID_POINTS, 2800);
            player.getInterfaceManager().closeChatBoxInterface();
            player.getInventory().addItem(orb, 1);
            player.getInventory().refresh();
            player.getPackets().sendGameMessage(
                    "Your payment was succesful, current points: "
                            + player.getPlayerPoints().getPoints(PlayerPoints.LIVID_POINTS) + ".");

        } else {
            player.getInterfaceManager().closeChatBoxInterface();
            player.getPackets()
                    .sendGameMessage(
                            "You have no enough points. Livid Orb costs 2,800 livid points.");

        }
    }
}
