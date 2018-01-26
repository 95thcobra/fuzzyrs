package com.rs.core.net.handlers.inventory;

import com.rs.Server;
import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.fletching.Fletching;
import com.rs.content.actions.skills.herblore.HerbCleaning;
import com.rs.content.actions.skills.hunter.BoxAction;
import com.rs.content.actions.skills.prayer.Burying;
import com.rs.content.actions.skills.runecrafting.Runecrafting;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.christmas.funnyjoke.FunnyJokeHandler;
import com.rs.content.christmas.snowballs.SnowBalls;
import com.rs.content.customskills.sailing.SailingMap;
import com.rs.content.dialogues.impl.*;
import com.rs.content.dialogues.types.SimplePlayerMessage;
import com.rs.content.drinking.Alcohol;
import com.rs.content.player.PlayerRank;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.ClueScrolls;
import com.rs.player.Player;
import com.rs.player.content.*;
import com.rs.player.controlers.FightKiln;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOptionOneHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final int slotId = (int) parameters[0];
        final int itemId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final long time = Utils.currentTimeMillis();
        if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time) {
            return true;
        }
        player.stopAll(false);
        for (final int i : ClueScrolls.ScrollIds) {
            if (itemId == i) {
                if (ClueScrolls.Scrolls.getMap(itemId) != null) {
                    ClueScrolls.showMap(player, ClueScrolls.Scrolls.getMap(itemId));
                    return true;
                }
                if (ClueScrolls.Scrolls.getObjMap(itemId) != null) {
                    ClueScrolls.showObjectMap(player, ClueScrolls.Scrolls.getObjMap(itemId));
                    return true;
                }
            }
        }
        if (Fletching.isFletching(player, itemId) || Alcohol.canDrink(player, item, slotId) || Foods.eat(player, item, slotId) || Pots.pot(player, item, slotId)
                || HerbCleaning.clean(player, item, slotId) || Magic.useTabTeleport(player, itemId)) {
            return true;
        } else if (itemId == 29997) {
            SailingMap.showInterface(player);
        } else if (itemId == 11949) {
            SnowBalls.handleItemClick(player);
        } else if (itemId == FunnyJokeHandler.JOKE_ITEM_ID) {
            player.getFunnyJoke().sendJoke();
        } else if (itemId == 2717) {
            ClueScrolls.giveReward(player);
        } else if (itemId >= 15086 && itemId <= 15100) {
            Dicing.handleRoll(player, itemId, false);
        } else if (itemId >= 5509 && itemId <= 5514) {
            int pouch = -1;
            if (itemId == 5509) {
                pouch = 0;
            }
            if (itemId == 5510) {
                pouch = 1;
            }
            if (itemId == 5512) {
                pouch = 2;
            }
            if (itemId == 5514) {
                pouch = 3;
            }
            Runecrafting.fillPouch(player, pouch);
        } else if (itemId == 299) {
            if (player.isLocked())
                return true;
            if (World.getObject(new WorldTile(player), 10) != null) {
                player.getPackets().sendGameMessage("You cannot plant flowers here..");
                return true;
            }
            final double random = Utils.getRandomDouble(100);
            final WorldTile tile = new WorldTile(player);
            int flower = Utils.random(2980, 2987);
            if (random < 0.2) {
                flower = Utils.random(2987, 2989);
            }
            if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
                if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
                    if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
                        player.addWalkSteps(player.getX(), player.getY() - 1, 1);
            player.getInventory().deleteItem(299, 1);
            final WorldObject flowerObject = new WorldObject(flower, 10, Utils.getRandom(4), tile.getX(), tile.getY(), tile.getPlane());
            World.spawnTemporaryObject(flowerObject, 45000);
            player.lock();
            WorldTasksManager.schedule(new WorldTask() {
                int step;

                @Override
                public void run() {
                    if (player == null || player.hasFinished())
                        stop();
                    if (step == 1) {
                        player.getDialogueManager().startDialogue(FlowerPickup.class, flowerObject);
                        player.setNextFaceWorldTile(tile);
                        player.unlock();
                        stop();
                    }
                    step++;
                }
            }, 0, 0);
        } else if (itemId == 611) {
            player.getLocationCrystal().handleClick();
        } else if (itemId == 20113) {
            player.setNextForceTalk(new ForceTalk("Celebration Time.. Come On!"));
            player.setNextAnimation(new Animation(8525));
            player.setNextGraphics(new Graphics(1765));
        } else if (itemId == 6) {// Cannon
            player.getDwarfCannon().checkLocation();
        } else if (itemId == 15262) { // shard 5000 pack
            player.getInventory().addItem(12183, 5000);
            player.getInventory().deleteItem(15262, 1);
            player.getPackets().sendGameMessage(
                    "You open the pack and you gain 5.000 Spirit Shards.");
        } else if (itemId == 18339) { // coal bag
            player.getInventory().addItem(453, 28);
            player.getInventory().deleteItem(18339, 1);
            player.getPackets().sendGameMessage(
                    "You open the pack and you gain some coal.");
        } else if (itemId == 10587) { // tarn
            player.getSkills().addXp(Skills.SLAYER, 1000);
            player.getInventory().deleteItem(10587, 1);
        } else if (itemId == 20264) {
            player.setNextAnimation(new Animation(445));
            player.setNextGraphics(new Graphics(56));
            player.getSkills().addXp(Skills.PRAYER, 10);
            player.getInventory().deleteItem(20264, 1);
        } else if (itemId == 20226) {
            player.setNextAnimation(new Animation(445));
            player.setNextGraphics(new Graphics(47));
            player.getSkills().addXp(Skills.PRAYER, 30);
            player.getInventory().deleteItem(20226, 1);
        } else if (itemId == 20268) {
            player.setNextAnimation(new Animation(445));
            player.setNextGraphics(new Graphics(40));
            player.getSkills().addXp(Skills.PRAYER, 50);
            player.getInventory().deleteItem(20268, 1);
        } else if (itemId == 10831) {
            player.getDialogueManager().startDialogue(EmptyCashBag.class);
        } else if (itemId == 10833) {
            player.getDialogueManager().startDialogue(SmallCashBag.class);
        } else if (itemId == 10834) {
            player.getDialogueManager().startDialogue(MedCashBag.class);
        } else if (itemId == 10835) {
            player.getDialogueManager().startDialogue(LargeCashBag.class);
        } else if (itemId == 20498) {// Royal Cannon
            if (player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.EXTREME_DONATOR)) {
                player.getDwarfCannon().checkRoyalLocation();
            } else {
                player.getPackets()
                        .sendGameMessage(
                                "You must be an Extreme Donator to set up a Royal Cannon.");
            }
        } else if (itemId == 22370) {
            Summoning.openDreadnipInterface(player);
        } else if (itemId == 6950) {
            //player.getDialogueManager().startDialogue(LividOrb.class);
        } else if (itemId == 20704) {
            LividFarm.bunchPlants(player);
        } else if (itemId == 952) {// spade
            InventoryOptionsHandler.dig(player);
        } else if (Burying.Bone.forId(itemId) != null) {
            Burying.Bone.bury(player, slotId);
        } else if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY
                || itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY
                || itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY
                || itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY) {
            player.getDialogueManager().startDialogue(AncientEffigiesD.class,
                    itemId);
        } else if (itemId == 4155) {
            player.getDialogueManager().startDialogue(EnchantedGemDialouge.class);
        } else if (itemId >= 23653 && itemId <= 23658) {
            FightKiln.useCrystal(player, itemId);
        } else if (itemId == 405) {
            player.getInventory().addItem(995, Utils.random(1000000));
            player.getInventory().deleteItem(405, 1);
            player.getPackets().sendGameMessage(
                    "The casket slowly opens... You find some coins.");
        } else if (itemId == 6542) { // present of crashed stars
            player.getInventory().addItem(995, Utils.random(15000000));
            player.getInventory().deleteItem(6542, 1);
            player.getPackets().sendGameMessage(
                    "You open your present and you find some coins.");
        } else if (itemId == 5074) { // ring nest
            final int[] reward = {1635, 1637, 1639, 1641, 1643};
            final int won = reward[Utils.random(reward.length - 1)];
            player.getInventory().deleteItem(5074, 1);
            player.getInventory().addItem(won, 1);
            player.getPackets().sendGameMessage(
                    "You find something in the Bird's nest.");
        } else if (itemId == 5073) { // tree seed nest
            final int[] reward = {5312, 5313, 5314, 5315, 5316, 5283, 5284,
                    5285, 5286, 5287, 5288, 5289, 5290, 5317};
            final int won = reward[Utils.random(reward.length - 1)];
            player.getInventory().deleteItem(5073, 1);
            player.getInventory().addItem(won, 1);
            player.getPackets().sendGameMessage(
                    "You find something in the Bird's nest.");
        } else if (itemId == 6199) { // mystery box
            final int[] reward = {1038, 1040, 1042, 1044, 1046, 1048, 1053,
                    1055, 1057, 13738, 13740, 13742, 13744, 199, 201, 203, 205,
                    207, 209, 211, 213, 215, 217, 219, 554, 555, 556, 557, 558,
                    559, 560, 561, 562, 563, 564, 565, 566};
            final int won = reward[Utils.random(reward.length - 1)];
            player.getInventory().deleteItem(6199, 1);
            player.getInventory().addItem(won, 1);
            player.getPackets().sendGameMessage(
                    "You find something in the Mystery Box!");
        } else if (itemId == 5070) { // red egg
            player.getInventory().addItem(5076, 1);
            player.getInventory().deleteItem(5070, 1);
            player.getPackets().sendGameMessage(
                    "You find something in the Bird's nest.");
        } else if (itemId == 5071) { // green egg
            player.getInventory().addItem(5077, 1);
            player.getInventory().deleteItem(5071, 1);
            player.getPackets().sendGameMessage(
                    "You find something in the Bird's nest.");
        } else if (itemId == 5072) { // blue egg
            player.getInventory().addItem(5078, 1);
            player.getInventory().deleteItem(5072, 1);
            player.getPackets().sendGameMessage(
                    "You find something in the Bird's nest.");
        } else if (itemId == 24155) { // Double Spin ticket
            player.getPackets()
                    .sendGameMessage(
                            "Congratulations! You have earned 2 spins on the Squeal of Fortune!");
            player.setSpins(player.getSpins() + 2);
            player.getPackets().sendIComponentText(1139, 10,
                    " " + player.getSpins() + " ");
            player.getInventory().deleteItem(24155, 1);
        } else if (itemId == 24154) { // Spin ticket
            player.getPackets()
                    .sendGameMessage(
                            "Congratulations! You have earned a spin on the Squeal of Fortune!");
            player.setSpins(player.getSpins() + 1);
            player.getPackets().sendIComponentText(1139, 10,
                    " " + player.getSpins() + " ");
            player.getInventory().deleteItem(24154, 1);
        } else if (itemId == 23717) {
            player.getSkills().addXp(Skills.ATTACK, 1000);
            player.getInventory().deleteItem(23717, 1);
        } else if (itemId == 23721) {
            player.getSkills().addXp(Skills.STRENGTH, 1000);
            player.getInventory().deleteItem(23721, 1);
        } else if (itemId == 23725) {
            player.getSkills().addXp(Skills.DEFENCE, 1000);
            player.getInventory().deleteItem(23725, 1);
        } else if (itemId == 23729) {
            player.getSkills().addXp(Skills.RANGE, 1000);
            player.getInventory().deleteItem(23729, 1);
        } else if (itemId == 23733) {
            player.getSkills().addXp(Skills.MAGIC, 1000);
            player.getInventory().deleteItem(23733, 1);
        } else if (itemId == 23737) {
            player.getSkills().addXp(Skills.PRAYER, 1000);
            player.getInventory().deleteItem(23737, 1);
        } else if (itemId == 23741) {
            player.getSkills().addXp(Skills.RUNECRAFTING, 1000);
            player.getInventory().deleteItem(23741, 1);
        } else if (itemId == 23745) {
            player.getSkills().addXp(Skills.CONSTRUCTION, 1000);
            player.getInventory().deleteItem(23745, 1);
        } else if (itemId == 23749) {
            player.getSkills().addXp(Skills.DUNGEONEERING, 1000);
            player.getInventory().deleteItem(23749, 1);
        } else if (itemId == 23753) {
            player.getSkills().addXp(Skills.HITPOINTS, 1000);
            player.getInventory().deleteItem(23753, 1);
        } else if (itemId == 23757) {
            player.getSkills().addXp(Skills.AGILITY, 1000);
            player.getInventory().deleteItem(23757, 1);
        } else if (itemId == 23761) {
            player.getSkills().addXp(Skills.HERBLORE, 1000);
            player.getInventory().deleteItem(23761, 1);
        } else if (itemId == 23765) {
            player.getSkills().addXp(Skills.THIEVING, 1000);
            player.getInventory().deleteItem(23765, 1);
        } else if (itemId == 23769) {
            player.getSkills().addXp(Skills.CRAFTING, 1000);
            player.getInventory().deleteItem(23769, 1);
        } else if (itemId == 23774) {
            player.getSkills().addXp(Skills.FLETCHING, 1000);
            player.getInventory().deleteItem(23774, 1);
        } else if (itemId == 23778) {
            player.getSkills().addXp(Skills.SLAYER, 1000);
            player.getInventory().deleteItem(23778, 1);
        } else if (itemId == 23782) {
            player.getSkills().addXp(Skills.HUNTER, 1000);
            player.getInventory().deleteItem(23782, 1);
        } else if (itemId == 23786) {
            player.getSkills().addXp(Skills.MINING, 1000);
            player.getInventory().deleteItem(23786, 1);
        } else if (itemId == 23790) {
            player.getSkills().addXp(Skills.SMITHING, 1000);
            player.getInventory().deleteItem(23790, 1);
        } else if (itemId == 23794) {
            player.getSkills().addXp(Skills.FISHING, 1000);
            player.getInventory().deleteItem(23794, 1);
        } else if (itemId == 23798) {
            player.getSkills().addXp(Skills.COOKING, 1000);
            player.getInventory().deleteItem(23798, 1);
        } else if (itemId == 23802) {
            player.getSkills().addXp(Skills.FIREMAKING, 1000);
            player.getInventory().deleteItem(23802, 1);
        } else if (itemId == 23806) {
            player.getSkills().addXp(Skills.WOODCUTTING, 1000);
            player.getInventory().deleteItem(23806, 1);
        } else if (itemId == 23810) {
            player.getSkills().addXp(Skills.FARMING, 1000);
            player.getInventory().deleteItem(23810, 1);
        } else if (itemId == 23814) {
            player.getSkills().addXp(Skills.SUMMONING, 1000);
            player.getInventory().deleteItem(23814, 1);
        } else if (itemId == 23718) {
            player.getSkills().addXp(Skills.ATTACK, 2000);
            player.getInventory().deleteItem(23718, 1);
        } else if (itemId == 23722) {
            player.getSkills().addXp(Skills.STRENGTH, 2000);
            player.getInventory().deleteItem(23722, 1);
        } else if (itemId == 23726) {
            player.getSkills().addXp(Skills.DEFENCE, 2000);
            player.getInventory().deleteItem(23726, 1);
        } else if (itemId == 23730) {
            player.getSkills().addXp(Skills.RANGE, 2000);
            player.getInventory().deleteItem(23730, 1);
        } else if (itemId == 23734) {
            player.getSkills().addXp(Skills.MAGIC, 2000);
            player.getInventory().deleteItem(23734, 1);
        } else if (itemId == 23738) {
            player.getSkills().addXp(Skills.PRAYER, 2000);
            player.getInventory().deleteItem(23738, 1);
        } else if (itemId == 23742) {
            player.getSkills().addXp(Skills.RUNECRAFTING, 2000);
            player.getInventory().deleteItem(23742, 1);
        } else if (itemId == 23746) {
            player.getSkills().addXp(Skills.CONSTRUCTION, 2000);
            player.getInventory().deleteItem(23746, 1);
        } else if (itemId == 23750) {
            player.getSkills().addXp(Skills.DUNGEONEERING, 2000);
            player.getInventory().deleteItem(23750, 1);
        } else if (itemId == 23754) {
            player.getSkills().addXp(Skills.HITPOINTS, 2000);
            player.getInventory().deleteItem(23754, 1);
        } else if (itemId == 23758) {
            player.getSkills().addXp(Skills.AGILITY, 2000);
            player.getInventory().deleteItem(23758, 1);
        } else if (itemId == 23762) {
            player.getSkills().addXp(Skills.HERBLORE, 2000);
            player.getInventory().deleteItem(23762, 1);
        } else if (itemId == 23766) {
            player.getSkills().addXp(Skills.THIEVING, 2000);
            player.getInventory().deleteItem(23766, 1);
        } else if (itemId == 23770) {
            player.getSkills().addXp(Skills.CRAFTING, 2000);
            player.getInventory().deleteItem(23770, 1);
        } else if (itemId == 23775) {
            player.getSkills().addXp(Skills.FLETCHING, 2000);
            player.getInventory().deleteItem(23775, 1);
        } else if (itemId == 23779) {
            player.getSkills().addXp(Skills.SLAYER, 2000);
            player.getInventory().deleteItem(23779, 1);
        } else if (itemId == 23783) {
            player.getSkills().addXp(Skills.HUNTER, 2000);
            player.getInventory().deleteItem(23783, 1);
        } else if (itemId == 23787) {
            player.getSkills().addXp(Skills.MINING, 2000);
            player.getInventory().deleteItem(23787, 1);
        } else if (itemId == 23791) {
            player.getSkills().addXp(Skills.SMITHING, 2000);
            player.getInventory().deleteItem(23791, 1);
        } else if (itemId == 23795) {
            player.getSkills().addXp(Skills.FISHING, 2000);
            player.getInventory().deleteItem(23795, 1);
        } else if (itemId == 23799) {
            player.getSkills().addXp(Skills.COOKING, 2000);
            player.getInventory().deleteItem(23799, 1);
        } else if (itemId == 23803) {
            player.getSkills().addXp(Skills.FIREMAKING, 2000);
            player.getInventory().deleteItem(23803, 1);
        } else if (itemId == 23807) {
            player.getSkills().addXp(Skills.WOODCUTTING, 2000);
            player.getInventory().deleteItem(23807, 1);
        } else if (itemId == 23811) {
            player.getSkills().addXp(Skills.FARMING, 2000);
            player.getInventory().deleteItem(23811, 1);
        } else if (itemId == 23815) {
            player.getSkills().addXp(Skills.SUMMONING, 2000);
            player.getInventory().deleteItem(23815, 1);
        } else if (itemId == 23719) {
            player.getSkills().addXp(Skills.ATTACK, 4000);
            player.getInventory().deleteItem(23719, 1);
        } else if (itemId == 23723) {
            player.getSkills().addXp(Skills.STRENGTH, 4000);
            player.getInventory().deleteItem(23723, 1);
        } else if (itemId == 23727) {
            player.getSkills().addXp(Skills.DEFENCE, 4000);
            player.getInventory().deleteItem(23727, 1);
        } else if (itemId == 23731) {
            player.getSkills().addXp(Skills.RANGE, 4000);
            player.getInventory().deleteItem(23731, 1);
        } else if (itemId == 23735) {
            player.getSkills().addXp(Skills.MAGIC, 4000);
            player.getInventory().deleteItem(23735, 1);
        } else if (itemId == 23739) {
            player.getSkills().addXp(Skills.PRAYER, 4000);
            player.getInventory().deleteItem(23739, 1);
        } else if (itemId == 23743) {
            player.getSkills().addXp(Skills.RUNECRAFTING, 4000);
            player.getInventory().deleteItem(23743, 1);
        } else if (itemId == 23747) {
            player.getSkills().addXp(Skills.CONSTRUCTION, 4000);
            player.getInventory().deleteItem(23747, 1);
        } else if (itemId == 23751) {
            player.getSkills().addXp(Skills.DUNGEONEERING, 4000);
            player.getInventory().deleteItem(23751, 1);
        } else if (itemId == 23755) {
            player.getSkills().addXp(Skills.HITPOINTS, 4000);
            player.getInventory().deleteItem(23755, 1);
        } else if (itemId == 23759) {
            player.getSkills().addXp(Skills.AGILITY, 4000);
            player.getInventory().deleteItem(23759, 1);
        } else if (itemId == 23763) {
            player.getSkills().addXp(Skills.HERBLORE, 4000);
            player.getInventory().deleteItem(23763, 1);
        } else if (itemId == 23767) {
            player.getSkills().addXp(Skills.THIEVING, 4000);
            player.getInventory().deleteItem(23767, 1);
        } else if (itemId == 23771) {
            player.getSkills().addXp(Skills.CRAFTING, 4000);
            player.getInventory().deleteItem(23771, 1);
        } else if (itemId == 23776) {
            player.getSkills().addXp(Skills.FLETCHING, 4000);
            player.getInventory().deleteItem(23776, 1);
        } else if (itemId == 23780) {
            player.getSkills().addXp(Skills.SLAYER, 4000);
            player.getInventory().deleteItem(23780, 1);
        } else if (itemId == 23784) {
            player.getSkills().addXp(Skills.HUNTER, 4000);
            player.getInventory().deleteItem(23784, 1);
        } else if (itemId == 23788) {
            player.getSkills().addXp(Skills.MINING, 4000);
            player.getInventory().deleteItem(23788, 1);
        } else if (itemId == 23792) {
            player.getSkills().addXp(Skills.SMITHING, 4000);
            player.getInventory().deleteItem(23792, 1);
        } else if (itemId == 23796) {
            player.getSkills().addXp(Skills.FISHING, 4000);
            player.getInventory().deleteItem(23796, 1);
        } else if (itemId == 23800) {
            player.getSkills().addXp(Skills.COOKING, 4000);
            player.getInventory().deleteItem(23800, 1);
        } else if (itemId == 23804) {
            player.getSkills().addXp(Skills.FIREMAKING, 4000);
            player.getInventory().deleteItem(23804, 1);
        } else if (itemId == 23808) {
            player.getSkills().addXp(Skills.WOODCUTTING, 4000);
            player.getInventory().deleteItem(23808, 1);
        } else if (itemId == 23812) {
            player.getSkills().addXp(Skills.FARMING, 4000);
            player.getInventory().deleteItem(23812, 1);
        } else if (itemId == 23816) {
            player.getSkills().addXp(Skills.SUMMONING, 4000);
            player.getInventory().deleteItem(23816, 1);
        } else if (itemId == 23720) {
            player.getSkills().addXp(Skills.ATTACK, 7500);
            player.getInventory().deleteItem(23720, 1);
        } else if (itemId == 23724) {
            player.getSkills().addXp(Skills.STRENGTH, 7500);
            player.getInventory().deleteItem(23724, 1);
        } else if (itemId == 23728) {
            player.getSkills().addXp(Skills.DEFENCE, 7500);
            player.getInventory().deleteItem(23728, 1);
        } else if (itemId == 23732) {
            player.getSkills().addXp(Skills.RANGE, 7500);
            player.getInventory().deleteItem(23732, 1);
        } else if (itemId == 23736) {
            player.getSkills().addXp(Skills.MAGIC, 7500);
            player.getInventory().deleteItem(23736, 1);
        } else if (itemId == 23740) {
            player.getSkills().addXp(Skills.PRAYER, 7500);
            player.getInventory().deleteItem(23740, 1);
        } else if (itemId == 23744) {
            player.getSkills().addXp(Skills.RUNECRAFTING, 7500);
            player.getInventory().deleteItem(23744, 1);
        } else if (itemId == 23748) {
            player.getSkills().addXp(Skills.CONSTRUCTION, 7500);
            player.getInventory().deleteItem(23748, 1);
        } else if (itemId == 23752) {
            player.getSkills().addXp(Skills.DUNGEONEERING, 7500);
            player.getInventory().deleteItem(23752, 1);
        } else if (itemId == 23756) {
            player.getSkills().addXp(Skills.HITPOINTS, 7500);
            player.getInventory().deleteItem(23756, 1);
        } else if (itemId == 23760) {
            player.getSkills().addXp(Skills.AGILITY, 7500);
            player.getInventory().deleteItem(23760, 1);
        } else if (itemId == 23764) {
            player.getSkills().addXp(Skills.HERBLORE, 7500);
            player.getInventory().deleteItem(23764, 1);
        } else if (itemId == 23768) {
            player.getSkills().addXp(Skills.THIEVING, 7500);
            player.getInventory().deleteItem(23768, 1);
        } else if (itemId == 23772) {
            player.getSkills().addXp(Skills.CRAFTING, 7500);
            player.getInventory().deleteItem(23772, 1);
        } else if (itemId == 23777) {
            player.getSkills().addXp(Skills.FLETCHING, 7500);
            player.getInventory().deleteItem(23777, 1);
        } else if (itemId == 23781) {
            player.getSkills().addXp(Skills.SLAYER, 7500);
            player.getInventory().deleteItem(23781, 1);
        } else if (itemId == 23785) {
            player.getSkills().addXp(Skills.HUNTER, 7500);
            player.getInventory().deleteItem(23785, 1);
        } else if (itemId == 23789) {
            player.getSkills().addXp(Skills.MINING, 7500);
            player.getInventory().deleteItem(23789, 1);
        } else if (itemId == 23793) {
            player.getSkills().addXp(Skills.SMITHING, 7500);
            player.getInventory().deleteItem(23793, 1);
        } else if (itemId == 23797) {
            player.getSkills().addXp(Skills.FISHING, 7500);
            player.getInventory().deleteItem(23797, 1);
        } else if (itemId == 23801) {
            player.getSkills().addXp(Skills.COOKING, 7500);
            player.getInventory().deleteItem(23801, 1);
        } else if (itemId == 23805) {
            player.getSkills().addXp(Skills.FIREMAKING, 7500);
            player.getInventory().deleteItem(23805, 1);
        } else if (itemId == 23809) {
            player.getSkills().addXp(Skills.WOODCUTTING, 7500);
            player.getInventory().deleteItem(23809, 1);
        } else if (itemId == 23813) {
            player.getSkills().addXp(Skills.FARMING, 7500);
            player.getInventory().deleteItem(23813, 1);
        } else if (itemId == 23817) {
            player.getSkills().addXp(Skills.SUMMONING, 7500);
            player.getInventory().deleteItem(23817, 1);
        } else if (itemId == 24300) {
            player.getSkills().addXp(Skills.ATTACK, 2000000);
            player.getSkills().addXp(Skills.STRENGTH, 2000000);
            player.getSkills().addXp(Skills.DEFENCE, 2000000);
            player.getSkills().addXp(Skills.RANGE, 2000000);
            player.getSkills().addXp(Skills.MAGIC, 2000000);
            player.getSkills().addXp(Skills.PRAYER, 2000000);
            player.getSkills().addXp(Skills.RUNECRAFTING, 2000000);
            player.getSkills().addXp(Skills.CONSTRUCTION, 2000000);
            player.getSkills().addXp(Skills.DUNGEONEERING, 2000000);
            player.getSkills().addXp(Skills.HITPOINTS, 2000000);
            player.getSkills().addXp(Skills.AGILITY, 2000000);
            player.getSkills().addXp(Skills.HERBLORE, 2000000);
            player.getSkills().addXp(Skills.THIEVING, 2000000);
            player.getSkills().addXp(Skills.CRAFTING, 2000000);
            player.getSkills().addXp(Skills.FLETCHING, 2000000);
            player.getSkills().addXp(Skills.SLAYER, 2000000);
            player.getSkills().addXp(Skills.HUNTER, 2000000);
            player.getSkills().addXp(Skills.MINING, 2000000);
            player.getSkills().addXp(Skills.SMITHING, 2000000);
            player.getSkills().addXp(Skills.FISHING, 2000000);
            player.getSkills().addXp(Skills.COOKING, 2000000);
            player.getSkills().addXp(Skills.FIREMAKING, 2000000);
            player.getSkills().addXp(Skills.WOODCUTTING, 2000000);
            player.getSkills().addXp(Skills.FARMING, 2000000);
            player.getSkills().addXp(Skills.SUMMONING, 2000000);
            player.getInventory().deleteItem(24300, 1);
        } else if (itemId == 1856) {// Information Book
            //TODO information book
        } else if (itemId == BoxAction.HunterEquipment.BOX.getId()) {
            player.getActionManager().setAction(
                    new BoxAction(BoxAction.HunterEquipment.BOX));
        } else if (itemId == BoxAction.HunterEquipment.BRID_SNARE.getId()) {
            player.getActionManager().setAction(
                    new BoxAction(BoxAction.HunterEquipment.BRID_SNARE));
        } else if (item.getDefinitions().getName().startsWith("Burnt")) {
            player.getDialogueManager().startDialogue(SimplePlayerMessage.class,
                    "Ugh, this is inedible.");
        }
        if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            Logger.info("ItemHandler", "Item Select:" + itemId + ", Slot Id:"
                    + slotId);
        }
        return false;
    }
}
