package com.rs.core.net.handlers.object;

import com.rs.content.CryptHandler;
import com.rs.content.WhirlPoolHandler;
import com.rs.content.actions.impl.CowMilkingAction;
import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.agility.*;
import com.rs.content.actions.skills.crafting.spinning.SpinningD;
import com.rs.content.actions.skills.dungeoneering.DungeonPartyManager;
import com.rs.content.actions.skills.hunter.Hunter;
import com.rs.content.actions.skills.mining.EssenceMining;
import com.rs.content.actions.skills.mining.Mining;
import com.rs.content.actions.skills.runecrafting.Runecrafting;
import com.rs.content.actions.skills.smithing.Smithing;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.actions.skills.woodcutting.Woodcutting;
import com.rs.content.christmas.snowballs.SnowBalls;
import com.rs.content.dialogues.impl.*;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.content.ectofuntus.Ectofuntus;
import com.rs.content.minigames.*;
import com.rs.content.minigames.castlewars.CastleWarsObjectHandler;
import com.rs.content.minigames.creations.StealingCreation;
import com.rs.content.minigames.creations.StealingCreationLobby;
import com.rs.content.minigames.rfd.RecipeforDisaster;
import com.rs.content.minigames.soulwars.SoulWarsAreaController;
import com.rs.content.spirittree.SpiritTreeDialogue;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.file.managers.PkRankFileManager;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.net.handlers.PacketHandlerManager;
import com.rs.core.net.handlers.inventory.InventoryOptionsHandler;
import com.rs.core.net.handlers.object.impl.DoorHandler;
import com.rs.core.net.handlers.object.impl.GateHandler;
import com.rs.core.net.handlers.object.impl.LadderHandler;
import com.rs.core.net.handlers.object.impl.StaircaseHandler;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.*;
import com.rs.player.combat.PlayerCombat;
import com.rs.player.content.*;
import com.rs.player.controlers.FightCaves;
import com.rs.player.controlers.FightKiln;
import com.rs.player.controlers.NomadsRequiem;
import com.rs.player.controlers.Wilderness;
import com.rs.player.controlers.fightpits.FightPitsLobby;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ObjectOptionOneHandler implements PacketHandler {

    //chaos alter ids
    //id == 61 || id == 411 || id == 412 || id == 32079 || id == 37990 || id == 65371

    @Override
    public boolean process(Player player, Object... parameters) {
        WorldObject object = (WorldObject) parameters[0];
        final ObjectDefinitions objectDef = object.getDefinitions();
        final int objectId = object.getId();
        final int x = object.getX();
        final int y = object.getY();
        player.setCoordsEvent(new CoordsEvent(object, () -> {
            player.stopAll();
            player.faceObject(object);
            if (!player.getControllerManager().processObjectClick1(object))
                return;
            if (Ectofuntus.handleObjects(player, object.getId()) || CastleWarsObjectHandler.handleObjects(player, objectId) ||
                    ClueScrolls.objectSpot(player, object) || ResourceDungeons.handleDungeons(player, object) || CryptHandler.handleCrypt(player, objectId, x, y)) {
                return;
            }
            if (object.getDefinitions().name.toLowerCase().contains("spinning")) {
                player.getDialogueManager().startDialogue(SpinningD.class);
                return;
            }
            switch (objectId) {

                case 19205:
                    Hunter.createLoggedObject(player, object, true);
                    break;

                case 28297:
                case 28296:
                    SnowBalls.handleObjectClick(player, object);
                    break;

                case 25213:
                    if (player.getRegionId() == 11414) {
                        player.setNextAnimation(new Animation(828));
                        WorldTasksManager.schedule(new WorldTask() {
                            @Override
                            public void run() {
                                player.setNextWorldTile(new WorldTile(2834, 3258, 0));
                            }
                        }, 1);
                    }
                    break;

                case 2606:
                    if (player.getY() == 9600) {
                        player.setNextWorldTile(new WorldTile(new WorldTile(2836, 9599, 0)));
                    } else if (player.getY() == 9599) {
                        player.setNextWorldTile(new WorldTile(new WorldTile(2836, 9600, 0)));
                    }
                    break;

                case 25154:
                    if (player.getRegionId() == 11314) {
                        player.setNextAnimation(new Animation(11042));
                        WorldTasksManager.schedule(new WorldTask() {
                            @Override
                            public void run() {
                                player.setNextWorldTile(new WorldTile(2833, 9656, 0));
                                player.setNextAnimation(new Animation(-1));
                            }
                        }, 1);
                    }
                    break;

                case 47237:
                    if (player.getSkills().getLevel(Skills.AGILITY) < 90) {
                        player.getPackets().sendGameMessage(
                                "You need 90 agility to use this shortcut.");
                        return;
                    }
                    if (player.getX() == 1641 && player.getY() == 5260
                            || player.getX() == 1641 && player.getY() == 5259
                            || player.getX() == 1640 && player.getY() == 5259) {
                        player.setNextWorldTile(new WorldTile(1641, 5268, 0));
                    } else {
                        player.setNextWorldTile(new WorldTile(1641, 5260, 0));
                    }
                    break;

                case 47232:
                    if (player.getSkills().getLevel(Skills.SLAYER) < 75) {
                        player.getPackets()
                                .sendGameMessage(
                                        "You need 75 slayer to enter Kuradal's dungeon.");
                        return;
                    }
                    player.setNextWorldTile(new WorldTile(1661, 5257, 0));
                    break;

                case 39508:
                    if (player.getControllerManager().getController() != null) {
                        if (player.getControllerManager().getController().getClass().getSimpleName().equals(StealingCreationLobby.class.getSimpleName())) {
                            StealingCreation.leaveTeamLobby(player, true);
                            return;
                        }
                    }
                    StealingCreation.enterTeamLobby(player, true);
                    break;

                case 39509:
                    if (player.getControllerManager().getController() != null) {
                        if (player.getControllerManager().getController().getClass().getSimpleName().equals(StealingCreationLobby.class.getSimpleName())) {
                            StealingCreation.leaveTeamLobby(player, false);
                            return;
                        }
                    }
                    StealingCreation.enterTeamLobby(player, false);
                    break;

                case 2562:
                    player.getPackets().sendGameMessage("You investigate...");
                    if (player.isMaxed == 0) {
                        MaxedUser.CheckMaxed(player);
                        MaxedUser.CheckCompletionist(player);
                        player.lock(2);
                    }
                    break;

                case 9312:
                case 9311:
                    AgilityShortcuts.doGETunnel(player, x, y);
                    break;

                case 48496:
                    new DungeonPartyManager(player);
                    player.dungtime = 800;
                    break;

                case 29395:
                    player.getInterfaceManager().sendInterface(ArtisanWorkshop.INGOTWITH);
                    break;

                case 47231:
                    player.setNextWorldTile(new WorldTile(1685, 5287, 1));
                    break;

                case 29386:
                    player.setNextWorldTile(new WorldTile(3067, 9710, 0));
                    break;

                case 28515:
                    player.setNextWorldTile(new WorldTile(3420, 2803, 1));
                    break;

                case 40760:
                    player.getDialogueManager().startDialogue(Noticeboard.class);
                    break;

                case 6:
                case 29408:
                    player.getDwarfCannon().preRotationSetup(object);
                    break;

                case 6282:
                    player.getDialogueManager().startDialogue(PkPortal.class);
                    break;

                case 12963:
                    player.setNextWorldTile(new WorldTile(2485, 3432, 3));
                    player.getSkills().addXp(Skills.AGILITY, 1100);
                    break;

                case 70812:
                    player.getDialogueManager().startDialogue(SummoningPortal.class);
                    break;

                case 40443:
                    LividFarm.deposit(player);
                    break;

                case 40492:
                    LividFarm.MakePlants(player);
                    break;

                case 40486:
                    LividFarm.MakePlants(player);
                    break;

                case 40505:
                    LividFarm.MakePlants(player);
                    break;

                case 40534:
                    LividFarm.MakePlants(player);
                    break;

                case 40464:
                    LividFarm.MakePlants(player);
                    break;

                case 40489:
                    LividFarm.MakePlants(player);
                    break;

                case 40487:
                    LividFarm.MakePlants(player);
                    break;

                case 40532:
                    LividFarm.MakePlants(player);
                    break;

                case 40499:
                    LividFarm.MakePlants(player);
                    break;

                case 40533:
                    LividFarm.MakePlants(player);
                    break;

                case 40504:
                    LividFarm.MakePlants(player);
                    break;

                case 40444:
                    LividFarm.TakeLogs(player);
                    break;

                case 29385:
                    player.setNextWorldTile(new WorldTile(3035, 9713, 0));
                    break;

                case 47236:
                    if (player.getX() == 1650 && player.getY() == 5281 || player.getX() == 1651 && player.getY() == 5281 || player.getX() == 1650 && player.getY() == 5281) {
                        player.addWalkSteps(1651, 5280, 1, false);
                    }
                    if (player.getX() == 1652 && player.getY() == 5280 || player.getX() == 1651 && player.getY() == 5280 || player.getX() == 1653 && player.getY() == 5280) {
                        player.addWalkSteps(1651, 5281, 1, false);
                    }
                    if (player.getX() == 1650 && player.getY() == 5301 || player.getX() == 1650 && player.getY() == 5302 || player.getX() == 1650 && player.getY() == 5303) {
                        player.addWalkSteps(1649, 5302, 1, false);
                    }
                    if (player.getX() == 1649 && player.getY() == 5303 || player.getX() == 1649 && player.getY() == 5302 || player.getX() == 1649 && player.getY() == 5301) {
                        player.addWalkSteps(1650, 5302, 1, false);
                    }
                    if (player.getX() == 1626 && player.getY() == 5301 || player.getX() == 1626 && player.getY() == 5302 || player.getX() == 1626 && player.getY() == 5303) {
                        player.addWalkSteps(1625, 5302, 1, false);
                    }
                    if (player.getX() == 1625 && player.getY() == 5301 || player.getX() == 1625 && player.getY() == 5302 || player.getX() == 1625 && player.getY() == 5303) {
                        player.addWalkSteps(1626, 5302, 1, false);
                    }
                    if (player.getX() == 1609 && player.getY() == 5289 || player.getX() == 1610 && player.getY() == 5289 || player.getX() == 1611 && player.getY() == 5289) {
                        player.addWalkSteps(1610, 5288, 1, false);
                    }
                    if (player.getX() == 1609 && player.getY() == 5288 || player.getX() == 1610 && player.getY() == 5288 || player.getX() == 1611 && player.getY() == 5288) {
                        player.addWalkSteps(1610, 5289, 1, false);
                    }
                    if (player.getX() == 1606 && player.getY() == 5265 || player.getX() == 1605 && player.getY() == 5265 || player.getX() == 1604 && player.getY() == 5265) {
                        player.addWalkSteps(1605, 5264, 1, false);
                    }
                    if (player.getX() == 1606 && player.getY() == 5264 || player.getX() == 1605 && player.getY() == 5264 || player.getX() == 1604 && player.getY() == 5264) {
                        player.addWalkSteps(1605, 5265, 1, false);
                    }
                    if (player.getX() == 1634 && player.getY() == 5254 || player.getX() == 1634 && player.getY() == 5253 || player.getX() == 1634 && player.getY() == 5252) {
                        player.addWalkSteps(1635, 5253, 1, false);
                    }
                    if (player.getX() == 1635 && player.getY() == 5254 || player.getX() == 1635 && player.getY() == 5253 || player.getX() == 1635 && player.getY() == 5252) {
                        player.addWalkSteps(1634, 5253, 1, false);
                    }
                    break;

                case 28716:
                    Summoning.sendInterface(player);
                    player.setNextFaceWorldTile(object);
                    break;

                case 47233:
                    if (player.getSkills().getLevel(Skills.AGILITY) < 80) {
                        player.getPackets().sendGameMessage("You need 80 agility to use this shortcut.");
                        return;
                    }
                    if (player.getX() == 1633 && player.getY() == 5294) {
                        player.getPackets().sendGameMessage("That not safe!");
                        return;
                    }
                    player.lock(3);
                    player.setNextAnimation(new Animation(4853));
                    final WorldTile toTile = new WorldTile(object.getX(), object.getY() + 1, object.getPlane());
                    player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.EAST));
                    WorldTasksManager.schedule(new WorldTask() {
                        @Override
                        public void run() {
                            player.setNextWorldTile(toTile);
                        }
                    }, 1);
                    break;

                case 68974:
                case 1317:
                case 68973:
                    player.getDialogueManager().startDialogue(SpiritTreeDialogue.class);
                    break;

                case 67966:
                    WhirlPoolHandler.handleWirlPool(player, object);
                    break;
                case 29958:
                case 4019:
                case 50205:
                case 50206:
                case 50207:
                case 53883:
                case 54650:
                case 55605:
                case 56083:
                case 56084:
                case 56085:
                case 56086:
                    final int maxSummoning = player.getSkills().getLevelForXp(
                            23);
                    if (player.getSkills().getLevel(23) < maxSummoning) {
                        player.lock(5);
                        player.getPackets().sendGameMessage(
                                "You feel the obelisk", true);
                        player.setNextAnimation(new Animation(8502));
                        player.setNextGraphics(new Graphics(1308));
                        WorldTasksManager.schedule(new WorldTask() {

                            @Override
                            public void run() {
                                player.getSkills().restoreSummoning();
                                player.getPackets().sendGameMessage(
                                        "...and recharge all your skills.",
                                        true);
                            }
                        }, 2);
                    } else {
                        player.getPackets().sendGameMessage(
                                "You already have full summoning.", true);
                    }
                    break;

                default:
                    if (objectId == 11554 || objectId == 11552) {
                        player.getPackets().sendGameMessage(
                                "That rock is currently unavailable.");
                    } else if (objectId == 38279 || objectId == 15482) {
                        player.getDialogueManager().startDialogue(RunespanPortalD.class);
                    } else if (objectId == 6) {
                        player.getDwarfCannon().fireDwarfCannon(object);
                        player.getDwarfCannon().preRotationSetup(object);
                    } else if (objectId == 2491) {
                        player.getActionManager().setAction(new EssenceMining(object, player.getSkills().getLevel(Skills.MINING) < 30 ? EssenceMining.EssenceDefinitions.Rune_Essence : EssenceMining.EssenceDefinitions.Pure_Essence));
                    } else if (objectId == 70794) {
                        player.setNextWorldTile(new WorldTile(1340, 6488, 0));
                    } else if (objectId == 70796) {
                        player.setNextWorldTile(new WorldTile(1090, 6360, 0));
                    } else if (objectId == 70797) {
                        player.setNextWorldTile(new WorldTile(1090, 6497, 0));
                    } else if (objectId == 70798) {
                        player.setNextWorldTile(new WorldTile(1340, 6497, 0));
                    } else if (objectId == 2478) {
                        Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2,
                                22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 88, 9, 99,
                                10);
                    } else if (objectId == 2479) {
                        Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14,
                                2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98, 8);
                    } else if (objectId == 2480) {
                        Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2,
                                38, 3, 57, 4, 76, 5, 95, 6);
                    } else if (objectId == 2481) {
                        Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26,
                                2, 52, 3, 78, 4);
                    } else if (objectId == 2482) {
                        Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2,
                                70, 3);
                    } else if (objectId == 2483) {
                        Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46,
                                2, 92, 3);
                    } else if (objectId == 2484) {
                        Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
                    } else if (objectId == 2487) {
                        Runecrafting
                                .craftEssence(player, 562, 35, 8.5, true, 74, 2);
                    } else if (objectId == 17010) {
                        Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82,
                                2);
                    } else if (objectId == 2486) {
                        Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
                    } else if (objectId == 2485) {
                        Runecrafting.craftEssence(player, 563, 50, 9.5, true);
                    } else if (objectId == 2488) {
                        Runecrafting.craftEssence(player, 560, 65, 10, true);
                    } else if (objectId == 30624) {
                        Runecrafting.craftEssence(player, 565, 77, 10.5, true);
                    } else if (objectId == 2452) {
                        final int hatId = player.getEquipment().getHatId();
                        if (hatId == Runecrafting.AIR_TIARA
                                || hatId == Runecrafting.OMNI_TIARA
                                || player.getInventory().containsItem(1438, 1)) {
                            Runecrafting.enterAirAltar(player);
                        }
                    } else if (objectId == 2455) {
                        final int hatId = player.getEquipment().getHatId();
                        if (hatId == Runecrafting.EARTH_TIARA
                                || hatId == Runecrafting.OMNI_TIARA
                                || player.getInventory().containsItem(1440, 1)) {
                            Runecrafting.enterEarthAltar(player);
                        }
                    } else if (objectId == 2456) {
                        final int hatId = player.getEquipment().getHatId();
                        if (hatId == Runecrafting.FIRE_TIARA
                                || hatId == Runecrafting.OMNI_TIARA
                                || player.getInventory().containsItem(1442, 1)) {
                            Runecrafting.enterFireAltar(player);
                        }
                    } else if (objectId == 2454) {
                        final int hatId = player.getEquipment().getHatId();
                        if (hatId == Runecrafting.WATER_TIARA
                                || hatId == Runecrafting.OMNI_TIARA
                                || player.getInventory().containsItem(1444, 1)) {
                            Runecrafting.enterWaterAltar(player);
                        }
                    } else if (objectId == 2457) {
                        final int hatId = player.getEquipment().getHatId();
                        if (hatId == Runecrafting.BODY_TIARA
                                || hatId == Runecrafting.OMNI_TIARA
                                || player.getInventory().containsItem(1446, 1)) {
                            Runecrafting.enterBodyAltar(player);
                        }
                    } else if (objectId == 2453) {
                        final int hatId = player.getEquipment().getHatId();
                        if (hatId == Runecrafting.MIND_TIARA
                                || hatId == Runecrafting.OMNI_TIARA
                                || player.getInventory().containsItem(1448, 1)) {
                            Runecrafting.enterMindAltar(player);
                        } else if (object.getId() == 13715) {
                            final int itemId = ((Integer) player
                                    .getTemporaryAttributtes().remove("Ritem"))
                                    .intValue();
                            final RepairItems.BrokenItems brokenitems = RepairItems.BrokenItems
                                    .forId(itemId);
                            final Item item = new Item(itemId);
                            if (RepairItems.BrokenItems.forId(itemId) == null) {
                                player.getDialogueManager().startDialogue(
                                        SimpleMessage.class,
                                        "You cant repair this item.");
                                return;
                            }
                            player.getDialogueManager().startDialogue(Repair.class,
                                    945, itemId);
                            return;
                        }
                        // staffzone noticeboard
                    } else if (objectId == 47120) { // zaros altar
                        // recharge if needed
                        if (player.getPrayer().getPrayerpoints() < player
                                .getSkills().getLevelForXp(Skills.PRAYER) * 10) {
                            player.lock(12);
                            player.setNextAnimation(new Animation(12563));
                            player.getPrayer().setPrayerpoints(
                                    (int) ((player.getSkills().getLevelForXp(
                                            Skills.PRAYER) * 10) * 1.15));
                            player.getPrayer().refreshPrayerPoints();
                        }
                        player.getDialogueManager().startDialogue(Switcher.class);
                    } else if (objectId == 7131) {
                        player.setNextWorldTile(new WorldTile(2527, 4833, 0));

                    } else if (objectId == 7140) {
                        player.setNextWorldTile(new WorldTile(2796, 4818, 0));

                    } else if (objectId == 7139) {
                        player.setNextWorldTile(new WorldTile(2845, 4832, 0));

                    } else if (objectId == 7138) {
                        player.getPackets().sendGameMessage(
                                "A strange power blocks your entrance.");

                    } else if (objectId == 7137) {
                        player.setNextWorldTile(new WorldTile(3491, 4834, 0));

                    } else if (objectId == 7136) {
                        player.setNextWorldTile(new WorldTile(2207, 4836, 0));

                    } else if (objectId == 7135) {
                        player.setNextWorldTile(new WorldTile(2471, 4838, 0));

                    } else if (objectId == 7134) {
                        player.setNextWorldTile(new WorldTile(2269, 4843, 0));

                    } else if (objectId == 7133) {
                        player.setNextWorldTile(new WorldTile(2398, 4841, 0));

                    } else if (objectId == 7132) {
                        player.setNextWorldTile(new WorldTile(2162, 4833, 0));

                    } else if (objectId == 7141) {
                        player.setNextWorldTile(new WorldTile(2468, 4890, 1));

                    } else if (objectId == 7129) {
                        player.setNextWorldTile(new WorldTile(2584, 4836, 0));

                    } else if (objectId == 7130) {
                        player.setNextWorldTile(new WorldTile(2660, 4839, 0));
                        // end of abyss

                        // barrows stair
                    } else if (objectId == 29589) {
                        player.setNextWorldTile(new WorldTile(3566, 3289, 0));
                        // Pk box slide
                    } else if (objectId == 44379) {
                        player.setNextWorldTile(new WorldTile(3380, 3513, 0));
                        player.sendMessage("The only way out is by teleporting.");
                        player.getControllerManager().startController(Wilderness.class);

                        // coins troll
                    } else if (objectId == 15462) {
                        player.getDialogueManager().startDialogue(
                                SimpleNPCMessage.class, 186,
                                "Dont even think bout it nigga");

                    } else if (objectId == 46300) {
                        player.getInterfaceManager().sendInterface(397);
                    } else if (objectId == 19222) {
                        Falconry.beginFalconry(player);
                    } else if (objectId == 36786) {
                        player.getDialogueManager().startDialogue(Banker.class, 4907);
                    } else if (objectId == 42377 || objectId == 42378) {
                        player.getDialogueManager().startDialogue(Banker.class, 2759);
                    } else if (objectId == 42217 || objectId == 782 || objectId == 34752) {
                        player.getDialogueManager().startDialogue(Banker.class, 553);
                    } else if (objectId == 57437) {
                        player.getBank().openBank();
                    } else if (objectId == 42425 && object.getX() == 3220
                            && object.getY() == 3222) { // zaros portal
                        player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5,
                                "And you find yourself into a digsite.");
                        player.addWalkSteps(3222, 3223, -1, false);
                        player.getPackets().sendGameMessage(
                                "You examine portal and it aborves you...");
                    } else if (objectId == 25161) {
                        player.setNextWorldTile(new WorldTile(2847, 9636, 0));
                        player.getPackets()
                                .sendGameMessage(
                                        "Goodluck, he is a though one without safespotting.");
                    } else if (objectId == 23117) {
                        player.setNextWorldTile(new WorldTile(2516, 5355, 0));
                        player.getPackets().sendGameMessage(
                                "You enter the rabbit hole.");
                    } else if (objectId == 9356) {
                        FightCaves.enterFightCaves(player);
                    } else if (objectId == 68107) {
                        FightKiln.enterFightKiln(player, false);
                    } else if (objectId == 50350) {
                        player.setNextWorldTile(new WorldTile(199, 5006, 0));
                    } else if (objectId == 2679) {
                        player.setNextWorldTile(new WorldTile(3284, 3034, 0));
                    } else if (objectId == 2689) {
                        player.setNextWorldTile(new WorldTile(3288, 3034, 0));
                    } else if (objectId == 1528) {
                        player.sendMessage("It seems to be locked, try an other way.");
                    } else if (objectId == 76211) {
                        player.setNextWorldTile(new WorldTile(3299, 3123, 0));
                    } else if (objectId == 10553) {
                        World.spawnNPC(8993, new WorldTile(3171, 9765, 0), -1, true);
                    } else if (objectId == 12356) {
                        player.getControllerManager().startController(
                                RecipeforDisaster.class, 1);
                    } else if (objectId == 68223) {
                        FightPits.enterLobby(player, false);
                    } else if (objectId == 70799) {
                        if (player.getSkills().getLevelForXp(Skills.AGILITY) < 60) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You need an Agility level of 60 to use this shorcut.");
                            return;
                        }
                        player.setNextWorldTile(new WorldTile(1178, 6356, 0));
                    } else if (objectId == 46500 && object.getX() == 3351
                            && object.getY() == 3415) { // zaros portal
                        player.useStairs(-1, new WorldTile(
                                        SettingsManager.getSettings().RESPAWN_PLAYER_LOCATION.getX(),
                                        SettingsManager.getSettings().RESPAWN_PLAYER_LOCATION.getY(),
                                        SettingsManager.getSettings().RESPAWN_PLAYER_LOCATION.getPlane()), 2,
                                3, "You found your way back to home.");
                        player.addWalkSteps(3351, 3415, -1, false);
                    } else if (objectId == 9293) {
                        if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You need an agility level of 70 to use this obstacle.",
                                            true);
                            return;
                        }
                        final int x1 = player.getX() == 2886 ? 2892 : 2886;
                        WorldTasksManager.schedule(new WorldTask() {
                            int count = 0;

                            @Override
                            public void run() {
                                player.setNextAnimation(new Animation(844));
                                if (count++ == 1) {
                                    stop();
                                }
                            }

                        }, 0, 0);
                        player.setNextForceMovement(new ForceMovement(
                                new WorldTile(x1, 9799, 0), 3,
                                player.getX() == 2886 ? 1 : 3));
                        player.useStairs(-1, new WorldTile(x1, 9799, 0), 3, 4);
                    } else if (objectId == 29370
                            && (object.getX() == 3150 || object.getX() == 3153)
                            && object.getY() == 9906) { // edgeville dungeon cut
                        if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You need an agility level of 53 to use this obstacle.");
                            return;
                        }
                        final boolean running = player.getRun();
                        player.setRunHidden(false);
                        player.lock(8);
                        player.addWalkSteps(x == 3150 ? 3155 : 3149, 9906, -1,
                                false);
                        player.getPackets().sendGameMessage(
                                "You pulled yourself through the pipes.", true);
                        WorldTasksManager.schedule(new WorldTask() {
                            boolean secondloop;

                            @Override
                            public void run() {
                                if (!secondloop) {
                                    secondloop = true;
                                    player.getAppearance().setRenderEmote(295);
                                } else {
                                    player.getAppearance().setRenderEmote(-1);
                                    player.setRunHidden(running);
                                    player.getSkills().addXp(Skills.AGILITY, 7);
                                    stop();
                                }
                            }
                        }, 0, 5);
                    }
                    // start forinthry dungeon
                    else if (objectId == 18341 && object.getX() == 3036
                            && object.getY() == 10172) {
                        player.useStairs(-1, new WorldTile(3039, 3765, 0), 0, 1);
                    } else if (objectId == 20599 && object.getX() == 3038
                            && object.getY() == 3761) {
                        player.useStairs(-1, new WorldTile(3037, 10171, 0), 0, 1);
                    } else if (objectId == 18342 && object.getX() == 3075
                            && object.getY() == 10057) {
                        player.useStairs(-1, new WorldTile(3071, 3649, 0), 0, 1);
                    } else if (objectId == 20600 && object.getX() == 3072
                            && object.getY() == 3648) {
                        player.useStairs(-1, new WorldTile(3077, 10058, 0), 0, 1);
                    } else if (objectId == 18425
                            && !player.getQuestManager().completedQuest(
                            QuestManager.Quests.NOMADS_REQUIEM)) {
                        NomadsRequiem.enterNomadsRequiem(player);
                    } else if (objectId == 42219) {
                        player.useStairs(-1, new WorldTile(1886, 3178, 0), 0, 1);
                        if (player.getQuestManager().getQuestStage(
                                QuestManager.Quests.NOMADS_REQUIEM) == -2) {
                            player.getQuestManager().setQuestStageAndRefresh(
                                    QuestManager.Quests.NOMADS_REQUIEM, 0);
                        }
                        player.getControllerManager().startController(SoulWarsAreaController.class);
                    } else if (objectId == 8689) {
                        player.getActionManager().setAction(new CowMilkingAction());
                    } else if (objectId == 42220) {
                        player.useStairs(-1, new WorldTile(3082, 3475, 0), 0, 1);
                    } else if (objectId == 30942 && object.getX() == 3019
                            && object.getY() == 3450) {
                        player.useStairs(828, new WorldTile(3020, 9850, 0), 1, 2);
                    } else if (objectId == 6226 && object.getX() == 3019
                            && object.getY() == 9850) {
                        player.useStairs(833, new WorldTile(3018, 3450, 0), 1, 2);
                    } else if (objectId == 31002
                            && player.getQuestManager().completedQuest(
                            QuestManager.Quests.PERIL_OF_ICE_MONTAINS)) {
                        player.useStairs(833, new WorldTile(2998, 3452, 0), 1, 2);
                    } else if (objectId == 31012
                            && player.getQuestManager().completedQuest(
                            QuestManager.Quests.PERIL_OF_ICE_MONTAINS)) {
                        player.useStairs(828, new WorldTile(2996, 9845, 0), 1, 2);
                    } else if (objectId == 30943 && object.getX() == 3059
                            && object.getY() == 9776) {
                        player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
                    } else if (objectId == 30944 && object.getX() == 3059
                            && object.getY() == 3376) {
                        player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
                    } else if (objectId == 2112 && object.getX() == 3046
                            && object.getY() == 9756) {
                        if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
                            player.getDialogueManager()
                                    .startDialogue(
                                            SimpleNPCMessage.class,
                                            MiningGuildDwarf
                                                    .getClosestDwarfID(player),
                                            "Sorry, but you need level 60 Mining to go in there.");
                            return;
                        }
                        final WorldObject openedDoor = new WorldObject(object
                                .getId(), object.getType(),
                                object.getRotation() - 1, object.getX(), object
                                .getY() + 1, object.getPlane());
                        if (World.removeTemporaryObject(object, 1200, false)) {
                            World.spawnTemporaryObject(openedDoor, 1200, false);
                            player.lock(2);
                            player.stopAll();
                            player.addWalkSteps(3046,
                                    player.getY() > object.getY() ? object.getY()
                                            : object.getY() + 1, -1, false);
                        }
                    } else if (objectId == 2113) {
                        if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
                            player.getDialogueManager()
                                    .startDialogue(
                                            SimpleNPCMessage.class,
                                            MiningGuildDwarf
                                                    .getClosestDwarfID(player),
                                            "Sorry, but you need level 60 Mining to go in there.");
                            return;
                        }
                        player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
                    } else if (objectId == 6226 && object.getX() == 3019
                            && object.getY() == 9740) {
                        player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
                    } else if (objectId == 6226 && object.getX() == 3019
                            && object.getY() == 9738) {
                        player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
                    } else if (objectId == 6226 && object.getX() == 3018
                            && object.getY() == 9739) {
                        player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
                    } else if (objectId == 6226 && object.getX() == 3020
                            && object.getY() == 9739) {
                        player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
                    } else if (objectId == 30963) {
                        player.getBank().openBank();
                    } else if (objectId == 6045) {
                        player.getPackets().sendGameMessage(
                                "You search the cart but find nothing.");
                    } else if (objectId == 5906) {
                        if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You need an agility level of 42 to use this obstacle.");
                            return;
                        }
                        player.lock();
                        WorldTasksManager.schedule(new WorldTask() {
                            int count = 0;

                            @Override
                            public void run() {
                                if (count == 0) {
                                    player.setNextAnimation(new Animation(2594));
                                    final WorldTile tile = new WorldTile(
                                            object.getX()
                                                    + (object.getRotation() == 2 ? -2
                                                    : +2), object.getY(), 0);
                                    player.setNextForceMovement(new ForceMovement(
                                            tile, 4, Utils.getMoveDirection(
                                            tile.getX() - player.getX(),
                                            tile.getY() - player.getY())));
                                } else if (count == 2) {
                                    final WorldTile tile = new WorldTile(
                                            object.getX()
                                                    + (object.getRotation() == 2 ? -2
                                                    : +2), object.getY(), 0);
                                    player.setNextWorldTile(tile);
                                } else if (count == 5) {
                                    player.setNextAnimation(new Animation(2590));
                                    final WorldTile tile = new WorldTile(
                                            object.getX()
                                                    + (object.getRotation() == 2 ? -5
                                                    : +5), object.getY(), 0);
                                    player.setNextForceMovement(new ForceMovement(
                                            tile, 4, Utils.getMoveDirection(
                                            tile.getX() - player.getX(),
                                            tile.getY() - player.getY())));
                                } else if (count == 7) {
                                    final WorldTile tile = new WorldTile(
                                            object.getX()
                                                    + (object.getRotation() == 2 ? -5
                                                    : +5), object.getY(), 0);
                                    player.setNextWorldTile(tile);
                                } else if (count == 10) {
                                    player.setNextAnimation(new Animation(2595));
                                    final WorldTile tile = new WorldTile(
                                            object.getX()
                                                    + (object.getRotation() == 2 ? -6
                                                    : +6), object.getY(), 0);
                                    player.setNextForceMovement(new ForceMovement(
                                            tile, 4, Utils.getMoveDirection(
                                            tile.getX() - player.getX(),
                                            tile.getY() - player.getY())));
                                } else if (count == 12) {
                                    final WorldTile tile = new WorldTile(
                                            object.getX()
                                                    + (object.getRotation() == 2 ? -6
                                                    : +6), object.getY(), 0);
                                    player.setNextWorldTile(tile);
                                } else if (count == 14) {
                                    stop();
                                    player.unlock();
                                }
                                count++;
                            }

                        }, 0, 0);
                        // BarbarianOutpostAgility start
                    } else if (objectId == 20210) {
                        BarbarianOutpostAgility.enterObstaclePipe(player, object);
                    } else if (objectId == 43526) {
                        BarbarianOutpostAgility.swingOnRopeSwing(player, object);
                    } else if (objectId == 43595 && x == 2550 && y == 3546) {
                        BarbarianOutpostAgility
                                .walkAcrossLogBalance(player, object);
                    } else if (objectId == 2286 && x == 2538 && y == 3545) {
                        BarbarianOutpostAgility.climbObstacleNet(player, object);
                    } else if (objectId == 2302 && x == 2535 && y == 3547) {
                        BarbarianOutpostAgility.walkAcrossBalancingLedge(player,
                                object);
                    } else if (objectId == 1948) {
                        BarbarianOutpostAgility.climbOverCrumblingWall(player,
                                object);
                    } else if (objectId == 43533) {
                        BarbarianOutpostAgility.runUpWall(player, object);
                    } else if (objectId == 43597) {
                        BarbarianOutpostAgility.climbUpWall(player, object);
                    } else if (objectId == 43587) {
                        BarbarianOutpostAgility.fireSpringDevice(player, object);
                    } else if (objectId == 43527) {
                        BarbarianOutpostAgility.crossBalanceBeam(player, object);
                    } else if (objectId == 43531) {
                        BarbarianOutpostAgility.jumpOverGap(player, object);
                    } else if (objectId == 43532) {
                        BarbarianOutpostAgility.slideDownRoof(player, object);
                    } else if (objectId == 45077) {
                        player.lock();
                        if (player.getX() != object.getX()
                                || player.getY() != object.getY()) {
                            player.addWalkSteps(object.getX(), object.getY(), -1,
                                    false);
                        }
                        WorldTasksManager.schedule(new WorldTask() {

                            private int count;

                            @Override
                            public void run() {
                                if (count == 0) {
                                    player.setNextFaceWorldTile(new WorldTile(
                                            object.getX() - 1, object.getY(), 0));
                                    player.setNextAnimation(new Animation(12216));
                                    player.unlock();
                                } else if (count == 2) {
                                    player.setNextWorldTile(new WorldTile(3651,
                                            5122, 0));
                                    player.setNextFaceWorldTile(new WorldTile(3651,
                                            5121, 0));
                                    player.setNextAnimation(new Animation(12217));
                                } else if (count == 3) {
                                    // TODO find emote
                                    // player.getPackets().sendObjectAnimation(new
                                    // WorldObject(45078, 0, 3, 3651, 5123, 0), new
                                    // Animation(12220));
                                } else if (count == 5) {
                                    player.unlock();
                                    stop();
                                }
                                count++;
                            }

                        }, 1, 0);
                    } else if (objectId == 45076) {
                        player.getActionManager().setAction(
                                new Mining(object, Mining.RockDefinitions.LRC_Gold_Ore));
                    } else if (objectId == 5999) {
                        player.getActionManager().setAction(
                                new Mining(object, Mining.RockDefinitions.LRC_Coal_Ore));
                    } else if (objectId == 45078) {
                        player.useStairs(2413, new WorldTile(3012, 9832, 0), 2, 2);
                    } else if (objectId == 45079) {
                        player.getBank().openDepositBox();
                    } else if (objectId == 24357 && object.getX() == 3188
                            && object.getY() == 3355) {
                        player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
                    } else if (objectId == 24359 && object.getX() == 3188
                            && object.getY() == 3355) {
                        player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
                    } else if (objectId == 1805 && object.getX() == 3191
                            && object.getY() == 3363) {
                        final WorldObject openedDoor = new WorldObject(object
                                .getId(), object.getType(),
                                object.getRotation() - 1, object.getX(), object
                                .getY(), object.getPlane());
                        if (World.removeTemporaryObject(object, 1200, false)) {
                            World.spawnTemporaryObject(openedDoor, 1200, false);
                            player.lock(2);
                            player.stopAll();
                            player.addWalkSteps(3191, player.getY() >= object
                                            .getY() ? object.getY() - 1 : object.getY(),
                                    -1, false);
                            if (player.getY() >= object.getY()) {
                                player.getDialogueManager()
                                        .startDialogue(
                                                SimpleNPCMessage.class,
                                                198,
                                                "Greetings bolt adventurer. Welcome to the guild of",
                                                "Champions.");
                            }
                        }
                    }
                    // start of varrock dungeon
                    else if (objectId == 29355 && object.getX() == 3230
                            && object.getY() == 9904) {
                        player.useStairs(828, new WorldTile(3229, 3503, 0), 1, 2);
                    } else if (objectId == 24264) {
                        player.useStairs(833, new WorldTile(3229, 9904, 0), 1, 2);
                    } else if (objectId == 24366) {
                        player.useStairs(828, new WorldTile(3237, 3459, 0), 1, 2);
                    } else if (objectId == 882 && object.getX() == 3237
                            && object.getY() == 3458) {
                        player.useStairs(833, new WorldTile(3237, 9858, 0), 1, 2);
                    } else if (objectId == 29355 && object.getX() == 3097
                            && object.getY() == 9867) {
                        player.useStairs(828, new WorldTile(3096, 3468, 0), 1, 2);
                    } else if (objectId == 26934) {
                        player.useStairs(833, new WorldTile(3097, 9868, 0), 1, 2);
                    } else if (objectId == 29355 && object.getX() == 3088
                            && object.getY() == 9971) {
                        player.useStairs(828, new WorldTile(3087, 3571, 0), 1, 2);
                    } else if (objectId == 65453) {
                        player.useStairs(833, new WorldTile(3089, 9971, 0), 1, 2);
                    } else if (objectId == 12389 && object.getX() == 3116
                            && object.getY() == 3452) {
                        player.useStairs(833, new WorldTile(3117, 9852, 0), 1, 2);
                    } else if (objectId == 29355 && object.getX() == 3116
                            && object.getY() == 9852) {
                        player.useStairs(833, new WorldTile(3115, 3452, 0), 1, 2);
                    } else if (objectId == 65365) {
                        WildernessAgility.GateWalk(player);
                    } else if (objectId == 65367) {
                        WildernessAgility.GateWalkBack(player);
                    } else if (objectId == 65734) {
                        WildernessAgility.climbCliff(player, object);
                    } else if (objectId == 65362) {
                        WildernessAgility.enterObstaclePipe(player, object.getX(),
                                object.getY());
                    } else if (objectId == 64696) {
                        WildernessAgility.swingOnRopeSwing(player, object);
                    } else if (objectId == 64698) {
                        WildernessAgility.walkLog(player);
                    } else if (objectId == 64699) {
                        WildernessAgility.crossSteppingPalletes(player, object);
                    } else if (objectId == 69526) {
                        GnomeAgility.walkGnomeLog(player);
                    } else if (objectId == 69506) {
                        AdvancedGnomeAgility.climbUpGnomeTreeBranch(player);
                    } else if (objectId == 69514) {
                        AdvancedGnomeAgility.RunGnomeBoard(player, object);
                    } else if (objectId == 69381) {
                        AdvancedGnomeAgility.JumpDown(player, object);
                    } else if (objectId == 69383) {
                        GnomeAgility.climbGnomeObstacleNet(player);
                    } else if (objectId == 69508) {
                        GnomeAgility.climbUpGnomeTreeBranch(player);
                    } else if (objectId == 2312) {
                        GnomeAgility.walkGnomeRope(player);
                    } else if (objectId == 4059) {
                        GnomeAgility.walkBackGnomeRope(player);
                    } else if (objectId == 69507) {
                        GnomeAgility.climbDownGnomeTreeBranch(player);
                    } else if (objectId == 69384) {
                        GnomeAgility.climbGnomeObstacleNet2(player);
                    } else if (objectId == 69377 || objectId == 69378) {
                        GnomeAgility.enterGnomePipe(player, object.getX(),
                                object.getY());
                    } else if (Wilderness.isDitch(objectId)) {// wild ditch
                        player.getDialogueManager().startDialogue(
                                WildernessDitch.class, object);
                    } else if (objectId == 42611) {// Magic Portal
                        player.getDialogueManager().startDialogue(MagicPortal.class);
                    } else if (object.getDefinitions().name.equalsIgnoreCase("Obelisk") && object.getY() > 3525) {
                        // Who the fuck removed the controler class and the code
                        // from SONIC!!!!!!!!!!
                        // That was an hour of collecting coords :fp: Now ima kill
                        // myself.
                    } else if (objectId == 27254) {// Edgeville portal
                        player.getPackets().sendGameMessage(
                                "You enter the portal...");
                        player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3,
                                "..and are transported to Edgeville.");
                        player.addWalkSteps(1598, 4506, -1, false);
                    } else if (objectId == 12202) {// mole entrance
                        if (!player.getInventory().containsItem(952, 1)) {
                            player.getPackets().sendGameMessage(
                                    "You need a spade to dig this.");
                            return;
                        }
                        if (player.getX() != object.getX()
                                || player.getY() != object.getY()) {
                            player.lock();
                            player.addWalkSteps(object.getX(), object.getY());
                            WorldTasksManager.schedule(new WorldTask() {
                                @Override
                                public void run() {
                                    InventoryOptionsHandler.dig(player);
                                }

                            }, 1);
                        } else {
                            InventoryOptionsHandler.dig(player);
                        }
                    } else if (objectId == 12230 && object.getX() == 1752
                            && object.getY() == 5136) {// mole exit
                        player.setNextWorldTile(new WorldTile(2986, 3316, 0));
                    } else if (objectId == 15522) {// portal sign
                        if (player.withinDistance(new WorldTile(1598, 4504, 0), 1)) {// PORTAL
                            // 1
                            player.getInterfaceManager().sendInterface(327);
                            player.getPackets().sendIComponentText(327, 13,
                                    "Edgeville");
                            player.getPackets()
                                    .sendIComponentText(
                                            327,
                                            14,
                                            "This portal will take you to edgeville. There "
                                                    + "you can multi pk once past the wilderness ditch.");
                        }
                        if (player.withinDistance(new WorldTile(1598, 4508, 0), 1)) {// PORTAL
                            // 2
                            player.getInterfaceManager().sendInterface(327);
                            player.getPackets().sendIComponentText(327, 13,
                                    "Mage Bank");
                            player.getPackets()
                                    .sendIComponentText(
                                            327,
                                            14,
                                            "This portal will take you to the mage bank. "
                                                    + "The mage bank is a 1v1 deep wilderness area.");
                        }
                        if (player.withinDistance(new WorldTile(1598, 4513, 0), 1)) {// PORTAL
                            // 3
                            player.getInterfaceManager().sendInterface(327);
                            player.getPackets().sendIComponentText(327, 13,
                                    "Magic's Portal");
                            player.getPackets()
                                    .sendIComponentText(
                                            327,
                                            14,
                                            "This portal will allow you to teleport to areas that "
                                                    + "will allow you to change your magic spell book.");
                        }
                    } else if (objectId == 38811 || objectId == 37929) {// corp beast
                        if (object.getX() == 2971 && object.getY() == 4382) {
                            player.getInterfaceManager().sendInterface(650);
                        } else if (object.getX() == 2918 && object.getY() == 4382) {
                            player.stopAll();
                            player.setNextWorldTile(new WorldTile(
                                    player.getX() == 2921 ? 2917 : 2921, player
                                    .getY(), player.getPlane()));
                        }
                    } else if (objectId == 37928 && object.getX() == 2883
                            && object.getY() == 4370) {
                        player.stopAll();
                        player.setNextWorldTile(new WorldTile(3214, 3782, 0));
                        player.getControllerManager().startController(Wilderness.class);
                    } else if (objectId == 38815 && object.getX() == 3209
                            && object.getY() == 3780 && object.getPlane() == 0) {
                        if (player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 37
                                || player.getSkills().getLevelForXp(Skills.MINING) < 45
                                || player.getSkills().getLevelForXp(
                                Skills.SUMMONING) < 23
                                || player.getSkills().getLevelForXp(
                                Skills.FIREMAKING) < 47
                                || player.getSkills().getLevelForXp(Skills.PRAYER) < 55) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
                            return;
                        }
                        player.stopAll();
                        player.setNextWorldTile(new WorldTile(2885, 4372, 0));
                        player.getControllerManager().forceStop();
                        // TODO all reqs, skills not added
                    } else if (objectId == 9369) {
                        player.getControllerManager().startController(FightPitsLobby.class);
                    } else if (objectId == 2693) {
                        QuestChest.searchChest(player);
                    } else if (objectId == 2079) {
                        CrystalChest.searchChest(player);
                    } else if (objectId == 59731) {
                        LucienChest.searchChest(player);
                    } else if (objectId == 29577) {
                        TreasureChest.searchChest(player);
                    } else if (objectId == 20600) {
                        player.setNextWorldTile(new WorldTile(3077, 10058, 0));
                    } else if (objectId == 18342) {
                        player.setNextWorldTile(new WorldTile(3071, 3649, 0));
                    } else if (objectId == 52859) {
                        Magic.sendNormalTeleportSpell(player, 0, 0.0D,
                                new WorldTile(1297, 4510, 0));
                        player.getPackets().sendGameMessage(
                                "Have fun hunting Frost Dragons.", true);
                    } else if (objectId == 4493) {
                        player.setNextWorldTile(new WorldTile(3433, 3538, 1));
                    } else if (objectId == 4494) {
                        player.setNextWorldTile(new WorldTile(3438, 3538, 0));
                    } else if (objectId == 4496) {
                        player.setNextWorldTile(new WorldTile(3412, 3541, 1));
                    } else if (objectId == 4495) {
                        player.setNextWorldTile(new WorldTile(3417, 3541, 2));
                    } else if (objectId == 9319) {
                        player.setNextAnimation(new Animation(828));
                        if (object.getX() == 3447 && object.getY() == 3576
                                && object.getPlane() == 1) {
                            player.setNextWorldTile(new WorldTile(3446, 3576, 2));
                        }
                        if (object.getX() == 3422 && object.getY() == 3550
                                && object.getPlane() == 0) {
                            player.setNextWorldTile(new WorldTile(3422, 3551, 1));
                        }
                        player.stopAll();
                    } else if (objectId == 9320) {
                        player.setNextAnimation(new Animation(828));
                        if (object.getX() == 3447 && object.getY() == 3576
                                && object.getPlane() == 2) {
                            player.setNextWorldTile(new WorldTile(3446, 3576, 1));
                        }
                        if (object.getX() == 3422 && object.getY() == 3550
                                && object.getPlane() == 1) {
                            player.setNextWorldTile(new WorldTile(3422, 3551, 0));
                        }
                        player.stopAll();
                    } else if (objectId == 52875) {
                        Magic.sendNormalTeleportSpell(player, 0, 0.0D,
                                new WorldTile(3033, 9598, 0));
                    } else if (objectId == 54019 || objectId == 54020 || objectId == 55301) {
                        PkRankFileManager.showRanks(player);
                    } else if (objectId == 1817 && object.getX() == 2273
                            && object.getY() == 4680) { // kbd lever
                        Magic.pushLeverTeleport(player, new WorldTile(3067, 10254,
                                0));
                    } else if (objectId == 23113 && object.getX() == 3359
                            && object.getY() == 3271) { // easter crate 1
                        player.getInventory().addItem(12638, 1);
                    } else if (objectId == 23114 && object.getX() == 3166
                            && object.getY() == 3489) { // easter crate 2
                        player.getInventory().addItem(12639, 1);
                    } else if (objectId == 49126 && object.getX() == 2533
                            && object.getY() == 5321) { // easter crate 3
                        player.getInventory().addItem(12640, 1);
                    } else if (objectId == 1816 && object.getX() == 3067
                            && object.getY() == 10252) { // kbd out lever
                        Magic.pushLeverTeleport(player,
                                new WorldTile(2273, 4681, 0));
                    } else if (objectId == 3827 && object.getX() == 1610
                            && object.getY() == 4500) { // kalph
                        Magic.pushLeverTeleport(player,
                                new WorldTile(3505, 9494, 0));
                    } else if (objectId == 32015 && object.getX() == 3069
                            && object.getY() == 10256) { // kbd stairs
                        player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
                        player.getControllerManager().startController(Wilderness.class);
                    } else if (objectId == 1765 && object.getX() == 3017
                            && object.getY() == 3849) { // kbd out stairs
                        player.stopAll();
                        player.setNextWorldTile(new WorldTile(3069, 10255, 0));
                        player.getControllerManager().forceStop();
                    } else if (objectId == 14315) {
                        //TODO pest control
                        //player.getControllerManager().startController(PestC.class, 1);
                    } else if (objectId == 5959) {
                        Magic.pushLeverTeleport(player,
                                new WorldTile(2539, 4712, 0));
                    } else if (objectId == 5960) {
                        Magic.pushLeverTeleport(player,
                                new WorldTile(3089, 3957, 0));
                    } else if (objectId == 1814) {
                        Magic.pushLeverTeleport(player,
                                new WorldTile(3155, 3923, 0));
                    } else if (objectId == 1815) {
                        Magic.pushLeverTeleport(player,
                                new WorldTile(2561, 3311, 0));
                    } else if (objectId == 62675) {
                        player.getCutscenesManager().play("DTPreview");
                    } else if (objectId == 62681) {
                        player.getDominionTower().viewScoreBoard();
                    } else if (objectId == 43767) {
                        PkRankFileManager.showRanks(player);
                    } else if (objectId == 62678 || objectId == 62679) {
                        player.getDominionTower().openModes();
                    } else if (objectId == 8150) {
                        FarmingManager.useRake3(player, 780);
                    } else if (objectId == 7847) {
                        FarmingManager.useRake2(player, 728);
                    } else if (objectId == 8550) {
                        FarmingManager.useRake(player, 708);
                    } else if (objectId == 8551) {
                        FarmingManager.useRake(player, 709);
                    } else if (objectId == 7836) {
                        FarmingManager.makeCompost(player, 709);
                    }
                    // End Raking
                    else if (objectId == 62688) {
                        player.getDialogueManager().startDialogue(DTClaimRewards.class);
                    } else if (objectId == 62677) {
                        player.getDominionTower().talkToFace();
                    } else if (objectId == 62680) {
                        player.getDominionTower().openBankChest();
                    } else if (objectId == 48797) {
                        player.useStairs(-1, new WorldTile(3877, 5526, 1), 0, 1);
                    } else if (objectId == 48798) {
                        player.useStairs(-1, new WorldTile(3246, 3198, 0), 0, 1);
                    } else if (objectId == 48678 && x == 3858 && y == 5533) {
                        player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
                    } else if (objectId == 48678 && x == 3858 && y == 5543) {
                        player.useStairs(-1, new WorldTile(3861, 5543, 0), 0, 1);
                    } else if (objectId == 48677 && x == 3858 && y == 5543) {
                        player.useStairs(-1, new WorldTile(3856, 5543, 1), 0, 1);
                    } else if (objectId == 48677 && x == 3858 && y == 5533) {
                        player.useStairs(-1, new WorldTile(3856, 5533, 1), 0, 1);
                    } else if (objectId == 48679) {
                        player.useStairs(-1, new WorldTile(3875, 5527, 1), 0, 1);
                    } else if (objectId == 48688) {
                        player.useStairs(-1, new WorldTile(3972, 5565, 0), 0, 1);
                    } else if (objectId == 48683) {
                        player.useStairs(-1, new WorldTile(3868, 5524, 0), 0, 1);
                    } else if (objectId == 48682) {
                        player.useStairs(-1, new WorldTile(3869, 5524, 0), 0, 1);
                    } else if (objectId == 62676) { // dominion exit
                        player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
                    } else if (objectId == 62674) { // dominion entrance
                        player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
                    } else if (objectId == 3192) {
                        PkRankFileManager.showRanks(player);
                    } else if (objectId == 65349) {
                        player.useStairs(-1, new WorldTile(3044, 10325, 0), 0, 1);
                    } else if (objectId == 32048 && object.getX() == 3043
                            && object.getY() == 10328) {
                        player.useStairs(-1, new WorldTile(3045, 3927, 0), 0, 1);
                    } else if (objectId == 26194) {
                        player.getDialogueManager().startDialogue(PartyRoomLever.class);
                    } else if (objectId == 61190 || objectId == 61191 || objectId == 61192
                            || objectId == 61193) {
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager()
                                    .setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.NORMAL));
                        }
                    } else if (objectId == 20573) {
                        player.getControllerManager().startController(RefugeOfFear.class);
                    } else if (objectId == 67050) {
                        player.useStairs(-1, new WorldTile(3359, 6110, 0), 0, 1);
                    } else if (objectId == 67053) {
                        player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
                    } else if (objectId == 67051) {
                        player.getDialogueManager().startDialogue(Marv.class, false);
                    } else if (objectId == 67052) {
                        Crucible.enterCrucibleEntrance(player);
                    } else {
                        switch (objectDef.name.toLowerCase()) {
                            case "trapdoor":
                            case "manhole":
                                if (objectDef.containsOption(0, "Open")) {
                                    final WorldObject openedHole = new WorldObject(
                                            object.getId() + 1, object.getType(),
                                            object.getRotation(), object.getX(), object
                                            .getY(), object.getPlane());
                                    // if (World.removeTemporaryObject(object, 60000,
                                    // true)) {
                                    player.faceObject(openedHole);
                                    World.spawnTemporaryObject(openedHole, 60000, true);
                                    // }
                                }
                                break;
                            case "closed chest":
                                if (objectDef.containsOption(0, "Open")) {
                                    player.setNextAnimation(new Animation(536));
                                    player.lock(2);
                                    final WorldObject openedChest = new WorldObject(
                                            object.getId() + 1, object.getType(),
                                            object.getRotation(), object.getX(), object
                                            .getY(), object.getPlane());
                                    // if (World.removeTemporaryObject(object, 60000,
                                    // true)) {
                                    player.faceObject(openedChest);
                                    World.spawnTemporaryObject(openedChest, 60000, true);
                                    // }
                                }
                                break;
                            case "open chest":
                                if (objectDef.containsOption(0, "Search")) {
                                    player.getPackets().sendGameMessage(
                                            "You search the chest but find nothing.");
                                }
                                break;
                            case "spiderweb":
                                if (object.getRotation() == 2) {
                                    player.lock(2);
                                    if (Utils.getRandom(1) == 0) {
                                        player.addWalkSteps(player.getX(), player
                                                .getY() < y ? object.getY() + 2
                                                : object.getY() - 1, -1, false);
                                        player.getPackets().sendGameMessage(
                                                "You squeeze though the web.");
                                    } else {
                                        player.getPackets()
                                                .sendGameMessage(
                                                        "You fail to squeeze though the web; perhaps you should try again.");
                                    }
                                }
                                break;
                            case "web":
                                if (objectDef.containsOption(0, "Slash")) {
                                    player.setNextAnimation(new Animation(PlayerCombat
                                            .getWeaponAttackEmote(player.getEquipment()
                                                    .getWeaponId(), player
                                                    .getCombatDefinitions()
                                                    .getAttackStyle())));
                                    ObjectHandler.slashWeb(player, object);
                                }
                                break;
                            case "anvil":
                                if (objectDef.containsOption(0, "Smith")) {
                                    final Smithing.ForgingBar bar = Smithing.ForgingBar.getBar(player);
                                    if (bar != null) {
                                        Smithing.ForgingInterface.sendSmithingInterface(player,
                                                bar);
                                    } else {
                                        player.getPackets()
                                                .sendGameMessage(
                                                        "You have no bars which you have smithing level to use.");
                                    }
                                }
                                break;
                            case "tin ore rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Tin_Ore));
                                break;
                            case "gold ore rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Gold_Ore));
                                break;
                            case "iron ore rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Iron_Ore));
                                break;
                            case "silver ore rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Silver_Ore));
                                break;
                            case "coal rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Coal_Ore));
                                break;
                            case "clay rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Clay_Ore));
                                break;
                            case "copper ore rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Copper_Ore));
                                break;
                            case "adamantite ore rocks":
                                player.getActionManager()
                                        .setAction(
                                                new Mining(object,
                                                        Mining.RockDefinitions.Adamant_Ore));
                                break;
                            case "runite ore rocks":
                                player.getActionManager().setAction(
                                        new Mining(object, Mining.RockDefinitions.Runite_Ore));
                                break;
                            case "granite rocks":
                                player.getActionManager()
                                        .setAction(
                                                new Mining(object,
                                                        Mining.RockDefinitions.Granite_Ore));
                                break;
                            case "sandstone rocks":
                                player.getActionManager().setAction(
                                        new Mining(object,
                                                Mining.RockDefinitions.Sandstone_Ore));
                                break;
                            case "crashed star":
                                player.getActionManager()
                                        .setAction(
                                                new Mining(object,
                                                        Mining.RockDefinitions.CRASHED_STAR));
                                break;
                            case "mithril ore rocks":
                                player.getActionManager()
                                        .setAction(
                                                new Mining(object,
                                                        Mining.RockDefinitions.Mithril_Ore));
                                break;
                            case "bank deposit box":
                                if (objectDef.containsOption(0, "Deposit")) {
                                    player.getBank().openDepositBox();
                                }
                                break;
                            case "bank":
                            case "bank chest":
                            case "bank booth":
                            case "counter":
                                if (objectDef.containsOption(0, "Bank")
                                        || objectDef.containsOption(0, "Use")) {
                                    player.getBank().openBank();
                                }
                                break;
                            // Woodcutting start
                            case "tree":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.NORMAL));
                                }
                                break;
                            case "evergreen":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.EVERGREEN));
                                }
                                break;
                            case "dead tree":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.DEAD));
                                }
                                break;
                            case "oak":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager()
                                            .setAction(
                                                    new Woodcutting(object,
                                                            Woodcutting.TreeDefinitions.OAK));
                                }
                                break;
                            case "willow":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.WILLOW));
                                }
                                break;
                            case "maple tree":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.MAPLE));
                                }
                                break;
                            case "ivy":
                                if (objectDef.containsOption(0, "Chop")) {
                                    player.getActionManager()
                                            .setAction(
                                                    new Woodcutting(object,
                                                            Woodcutting.TreeDefinitions.IVY));
                                }
                                break;
                            case "yew":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager()
                                            .setAction(
                                                    new Woodcutting(object,
                                                            Woodcutting.TreeDefinitions.YEW));
                                }
                                break;
                            case "magic tree":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.MAGIC));
                                }
                                break;
                            case "cursed magic tree":
                                if (objectDef.containsOption(0, "Chop down")) {
                                    player.getActionManager().setAction(
                                            new Woodcutting(object,
                                                    Woodcutting.TreeDefinitions.CURSED_MAGIC));
                                }
                                break;
                            // Woodcutting end
                            case "gate":
                            case "large door":
                            case "metal door":
                            case "long hall door":
                            case "main entrance":
                                if (object.getType() == 0
                                        && objectDef.containsOption(0, "Open"))
                                    if (!PacketHandlerManager.get(GateHandler.class).process(player, object)) {
                                        PacketHandlerManager.get(DoorHandler.class).process(player, object);
                                    }
                                break;
                            case "officer tower door":
                                if (object.getType() == 0
                                        && objectDef.containsOption(0, "Enter"))
                                    if (!PacketHandlerManager.get(GateHandler.class).process(player, object)) {
                                        PacketHandlerManager.get(DoorHandler.class).process(player, object);
                                    }
                                break;
                            case "door":
                                if (object.getType() == 0
                                        && (objectDef.containsOption(0, "Open") || objectDef
                                        .containsOption(0, "Unlock"))) {
                                    PacketHandlerManager.get(DoorHandler.class).process(player, object);
                                }
                                break;
                            case "ladder":
                                PacketHandlerManager.get(LadderHandler.class).process(player, object, 1);
                                break;
                            case "staircase":
                                PacketHandlerManager.get(StaircaseHandler.class).process(player, object, 1);
                                break;
                            case "small obelisk":
                                if (objectDef.containsOption(0, "Renew-points")) {
                                    final int summonLevel = player.getSkills()
                                            .getLevelForXp(Skills.SUMMONING);
                                    if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
                                        player.lock(3);
                                        player.setNextAnimation(new Animation(8502));
                                        player.getSkills().set(Skills.SUMMONING,
                                                summonLevel);
                                        player.getPackets()
                                                .sendGameMessage(
                                                        "You have recharged your Summoning points.",
                                                        true);
                                    } else {
                                        player.getPackets()
                                                .sendGameMessage(
                                                        "You already have full Summoning points.");
                                    }
                                }
                                break;
                            case "altar":
                                if (objectDef.containsOption(0, "Pray")
                                        || objectDef.containsOption(0, "Pray-at")) {
                                    final int maxPrayer = player.getSkills()
                                            .getLevelForXp(Skills.PRAYER) * 10;
                                    if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                                        player.lock(5);
                                        player.getPackets().sendGameMessage(
                                                "You pray to the gods...", true);
                                        player.setNextAnimation(new Animation(645));
                                        WorldTasksManager.schedule(new WorldTask() {
                                            @Override
                                            public void run() {
                                                player.getPrayer().restorePrayer(
                                                        maxPrayer);
                                                player.getPackets()
                                                        .sendGameMessage(
                                                                "...and recharged your prayer.",
                                                                true);
                                            }
                                        }, 2);
                                    } else {
                                        player.getPackets().sendGameMessage(
                                                "You already have full prayer.");
                                    }
                                    if (objectId == 6552) {
                                        player.getDialogueManager().startDialogue(
                                                AncientAltar.class);
                                    }
                                }
                                break;
                            case "bandos altar":
                                if (objectDef.containsOption(0, "Pray")
                                        || objectDef.containsOption(0, "Pray-at")) {
                                    final int maxPrayer = player.getSkills()
                                            .getLevelForXp(Skills.PRAYER) * 10;
                                    if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                                        player.lock(5);
                                        player.getPackets().sendGameMessage(
                                                "You pray to the gods...", true);
                                        player.setNextAnimation(new Animation(645));
                                        WorldTasksManager.schedule(new WorldTask() {
                                            @Override
                                            public void run() {
                                                player.getPrayer().restorePrayer(
                                                        maxPrayer);
                                                player.getPackets()
                                                        .sendGameMessage(
                                                                "...and recharged your prayer.",
                                                                true);
                                            }
                                        }, 2);
                                    } else {
                                        player.getPackets().sendGameMessage(
                                                "You already have full prayer.");
                                    }
                                    if (objectId == 6552) {
                                        player.getDialogueManager().startDialogue(
                                                AncientAltar.class);
                                    }
                                }
                                break;
                            case "armadyl altar":
                                if (objectDef.containsOption(0, "Pray")
                                        || objectDef.containsOption(0, "Pray-at")) {
                                    final int maxPrayer = player.getSkills()
                                            .getLevelForXp(Skills.PRAYER) * 10;
                                    if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                                        player.lock(5);
                                        player.getPackets().sendGameMessage(
                                                "You pray to the gods...", true);
                                        player.setNextAnimation(new Animation(645));
                                        WorldTasksManager.schedule(new WorldTask() {
                                            @Override
                                            public void run() {
                                                player.getPrayer().restorePrayer(
                                                        maxPrayer);
                                                player.getPackets()
                                                        .sendGameMessage(
                                                                "...and recharged your prayer.",
                                                                true);
                                            }
                                        }, 2);
                                    } else {
                                        player.getPackets().sendGameMessage(
                                                "You already have full prayer.");
                                    }
                                    if (objectId == 6552) {
                                        player.getDialogueManager().startDialogue(
                                                AncientAltar.class);
                                    }
                                }
                                break;
                            case "saradomin altar":
                                if (objectDef.containsOption(0, "Pray")
                                        || objectDef.containsOption(0, "Pray-at")) {
                                    final int maxPrayer = player.getSkills()
                                            .getLevelForXp(Skills.PRAYER) * 10;
                                    if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                                        player.lock(5);
                                        player.getPackets().sendGameMessage(
                                                "You pray to the gods...", true);
                                        player.setNextAnimation(new Animation(645));
                                        WorldTasksManager.schedule(new WorldTask() {
                                            @Override
                                            public void run() {
                                                player.getPrayer().restorePrayer(
                                                        maxPrayer);
                                                player.getPackets()
                                                        .sendGameMessage(
                                                                "...and recharged your prayer.",
                                                                true);
                                            }
                                        }, 2);
                                    } else {
                                        player.getPackets().sendGameMessage(
                                                "You already have full prayer.");
                                    }
                                    if (objectId == 6552) {
                                        player.getDialogueManager().startDialogue(
                                                AncientAltar.class);
                                    }
                                }
                                break;
                            case "zamorak altar":
                                if (objectDef.containsOption(0, "Pray")
                                        || objectDef.containsOption(0, "Pray-at")) {
                                    final int maxPrayer = player.getSkills()
                                            .getLevelForXp(Skills.PRAYER) * 10;
                                    if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                                        player.lock(5);
                                        player.getPackets().sendGameMessage(
                                                "You pray to the gods...", true);
                                        player.setNextAnimation(new Animation(645));
                                        WorldTasksManager.schedule(new WorldTask() {
                                            @Override
                                            public void run() {
                                                player.getPrayer().restorePrayer(
                                                        maxPrayer);
                                                player.getPackets()
                                                        .sendGameMessage(
                                                                "...and recharged your prayer.",
                                                                true);
                                            }
                                        }, 2);
                                    } else {
                                        player.getPackets().sendGameMessage(
                                                "You already have full prayer.");
                                    }
                                    if (objectId == 6552) {
                                        player.getDialogueManager().startDialogue(
                                                AncientAltar.class);
                                    }
                                }
                                break;
                            default:
                                player.getPackets().sendGameMessage(
                                        "Nothing interesting happens.");
                                break;
                        }
                    }
                    break;

            }
            if (SettingsManager.getSettings().DEBUG) {
                Logger.info("ObjectHandler", "clicked 1 at object id : " + objectId + ", " + object.getX() + ", " + object.getY() + ", " + object.getPlane() + ", " + object.getType() + ", " + object.getRotation() + ", " + object.getDefinitions().name);
            }
        }, objectDef.getSizeX(), Wilderness.isDitch(objectId) ? 4 : objectDef.getSizeY(), object.getRotation()));
        return false;
    }
}
