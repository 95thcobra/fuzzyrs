package com.rs.core.net.handlers.object;

import com.rs.server.Server;
import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.crafting.FlaxCrafting;
import com.rs.content.actions.skills.thieving.Thieving;
import com.rs.content.dialogues.impl.*;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.player.PlayerRank;
import com.rs.content.spirittree.SpiritTree;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.net.handlers.PacketHandlerManager;
import com.rs.core.net.handlers.object.impl.DoorHandler;
import com.rs.core.net.handlers.object.impl.GateHandler;
import com.rs.core.net.handlers.object.impl.LadderHandler;
import com.rs.core.net.handlers.object.impl.StaircaseHandler;
import com.rs.core.utils.Logger;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.rs.player.content.ArtisanWorkshop;
import com.rs.player.content.LividFarm;
import com.rs.player.content.Magic;
import com.rs.player.content.PartyRoom;
import com.rs.player.controlers.FightKiln;
import com.rs.player.controlers.QueenBlackDragonController;
import com.rs.world.*;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ObjectOptionTwoHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        WorldObject object = (WorldObject) parameters[0];
        final ObjectDefinitions objectDef = object.getDefinitions();
        final int id = object.getId();
        player.setCoordsEvent(new CoordsEvent(object, () -> {
            player.stopAll();
            player.faceObject(object);
            if (!player.getControllerManager().processObjectClick2(object))
                return;
            else if (object.getDefinitions().name
                    .equalsIgnoreCase("furnace")) {
                player.getDialogueManager().startDialogue(SmeltingD.class,
                        object);
            } else if (object.getDefinitions().name
                    .equalsIgnoreCase("small furnace")) {
                player.getDialogueManager().startDialogue(SmeltingD.class,
                        object);
            } else if (id == 17010) {
                player.getDialogueManager().startDialogue(LunarAltar.class);
            } else if (id == 68974) {
                SpiritTree.openSpiritTree(player);
            } else if (id == 1317) {
                SpiritTree.openSpiritTree(player);
            } else if (id == 68973) {
                SpiritTree.openSpiritTree(player);
            }
            final String THIEVING_MESSAGE = "You successfully thieve from the stall";
            final Animation THIEVING_ANIMATION = new Animation(881);
            boolean thievingSuccess = false;
            if (id == 4875) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                    return;
                }
                if (player.getSkills().getLevel(Skills.THIEVING) >= 30) {
                    thievingSuccess = true;
                    player.getInventory().addItem(1739, 1);
                    player.getSkills().addXp(17, 70);
                    player.applyHit(new Hit(player, 5,
                            Hit.HitLook.POISON_DAMAGE));
                } else {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least 30 thieving to steal from this stall");
                }
            } else if (id == 20607) {
                player.getGeManager().openCollectionBox();
            } else if (id == 4874) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                    return;
                }
                if (player.getSkills().getLevel(Skills.THIEVING) >= 1) {
                    thievingSuccess = true;
                    player.getInventory().addItem(950, 1);
                    player.getSkills().addXp(17, 55);
                    player.applyHit(new Hit(player, 5,
                            Hit.HitLook.POISON_DAMAGE));
                } else {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least 1 thieving to steal from this stall");
                }
            } else if (id == 29394) {
                ArtisanWorkshop.DepositIngots(player);
            } else if (id == 6) {
                player.getDwarfCannon().pickUpDwarfCannon(id, object);
            } else if (id == 29395) {
                ArtisanWorkshop.DepositIngots(player);
            } else if (id == 4876) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                    return;
                }
                if (player.getSkills().getLevel(Skills.THIEVING) >= 50) {
                    thievingSuccess = true;
                    player.getInventory().addItem(1635, 1);
                    player.getSkills().addXp(17, 80);
                    player.applyHit(new Hit(player, 5,
                            Hit.HitLook.POISON_DAMAGE));
                } else {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least 50 thieving to steal from this stall");
                }
            } else if (id == 4708) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                    return;
                }
                if (player.getSkills().getLevel(Skills.THIEVING) >= 95) {
                    thievingSuccess = true;
                    player.getInventory().addItem(995, 75000);
                    player.getSkills().addXp(17, 1);
                    Magic.sendNormalTeleportSpell(player, 0, 0.0D,
                            new WorldTile(2993, 9677, 0));
                } else {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least 95 thieving to steal from this stall");
                }
            } else if (id == 4877) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                    return;
                }
                if (player.getSkills().getLevel(Skills.THIEVING) >= 75) {
                    thievingSuccess = true;
                    player.getInventory().addItem(7650, 1);
                    player.getSkills().addXp(17, 90);
                    player.applyHit(new Hit(player, 5,
                            Hit.HitLook.POISON_DAMAGE));
                } else {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least 75 thieving to steal from this stall");
                }
            } else if (id == 4878) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                    return;
                }
                if (player.getSkills().getLevel(Skills.THIEVING) >= 85) {
                    thievingSuccess = true;
                    player.getInventory().addItem(1662, 1);
                    player.getSkills().addXp(17, 100);
                    player.applyHit(new Hit(player, 5,
                            Hit.HitLook.POISON_DAMAGE));
                } else {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least 85 thieving to steal from this stall");
                }
            } else if (id == 26807 && player.getRank().isMinimumRank(PlayerRank.MOD)) {
                player.getDialogueManager().startDialogue(ModOptions.class);
            } else if (id == 62677) {
                player.getDominionTower().openRewards();
            } else if (id == 70812) {
                if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need an Summoning level of 60 to use this shorcut.");
                    return;
                }
                player.getControllerManager().startController(
                        QueenBlackDragonController.class);
            } else if (id == 12963) {
                player.setNextWorldTile(new WorldTile(2485, 3432, 3));
                player.getSkills().addXp(Skills.AGILITY, 1100);
            } else if (id == 29408) {
                player.getDwarfCannon().pickUpDwarfCannonRoyal(0, object);
            } else if (id == 70795) {
                player.setNextWorldTile(new WorldTile(1206, 6507, 0));
            } else if (id == 2644) {
                FlaxCrafting.make(player, FlaxCrafting.Orb.AIR_ORB);
            } else if (id == 40444) {
                LividFarm.takemoreLogs(player);
            } else if (id == 62688) {
                player.getDialogueManager().startDialogue(
                        SimpleMessage.class,
                        "You have a Dominion Factor of "
                                + player.getDominionTower()
                                .getDominionFactor() + ".");
            } else if (id == 68107) {
                FightKiln.enterFightKiln(player, true);
            } else if (id == 34384 || id == 34383 || id == 14011
                    || id == 7053 || id == 34387 || id == 34386
                    || id == 34385) {
                Thieving.handleStalls(player, object);
            } else if (id == 2418) {
                PartyRoom.openPartyChest(player);
            } else if (id == 2646) {
                World.removeTemporaryObject(object, 50000, true);
                player.getInventory().addItem(1779, 1);
                player.setNextAnimation(new Animation(827));
                player.lock(2);
                // crucible
            } else if (id == 67051) {
                player.getDialogueManager().startDialogue(Marv.class, true);
            } else {
                switch (objectDef.name.toLowerCase()) {
                    case "wheat":
                        if (objectDef.containsOption(1, "Pick")
                                && player.getInventory().addItem(1947, 1)) {
                            player.setNextAnimation(new Animation(827));
                            player.lock(2);
                            World.removeTemporaryObject(object, 50000, false);
                        }
                        break;
                    case "cabbage":
                        if (objectDef.containsOption(1, "Pick")
                                && player.getInventory().addItem(1965, 1)) {
                            player.setNextAnimation(new Animation(827));
                            player.lock(2);
                            World.removeTemporaryObject(object, 60000, false);
                        }
                        break;

                    case "bank":
                    case "bank chest":
                    case "bank booth":
                    case "counter":
                        if (objectDef.containsOption(1, "Bank")) {
                            player.getBank().openBank();
                        }
                        break;
                    case "gates":
                    case "gate":
                    case "metal door":
                        if (object.getType() == 0
                                && objectDef.containsOption(1, "Open")) {
                            PacketHandlerManager.get(GateHandler.class).process(player, object);
                        }
                        break;
                    case "door":
                        if (object.getType() == 0
                                && objectDef.containsOption(1, "Open")) {
                            PacketHandlerManager.get(DoorHandler.class).process(player, object);
                        }
                        break;
                    case "ladder":
                        PacketHandlerManager.get(LadderHandler.class).process(player, object, 2);
                        break;
                    case "staircase":
                        PacketHandlerManager.get(StaircaseHandler.class).process(player, object, 2);
                        break;
                    default:
                        player.getPackets().sendGameMessage(
                                "Nothing interesting happens.");
                        break;
                }
            }
            if (thievingSuccess) {
                player.lock(4);
                player.getPackets().sendGameMessage(THIEVING_MESSAGE);
                player.setNextAnimation(THIEVING_ANIMATION);
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info("ObjectHandler", "clicked 2 at object id : "
                        + id + ", " + object.getX() + ", " + object.getY()
                        + ", " + object.getPlane());
            }
        }, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
        return false;
    }
}
