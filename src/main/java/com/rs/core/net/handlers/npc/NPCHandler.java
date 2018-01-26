package com.rs.core.net.handlers.npc;

import com.rs.Server;
import com.rs.content.actions.impl.Rest;
import com.rs.content.actions.skills.fishing.Fishing;
import com.rs.content.actions.skills.fishing.Fishing.FishingSpots;
import com.rs.content.actions.skills.hunter.Hunter;
import com.rs.content.actions.skills.mining.LivingMineralMining;
import com.rs.content.actions.skills.mining.MiningBase;
import com.rs.content.actions.skills.runecrafting.SiphonActionCreatures;
import com.rs.content.actions.skills.thieving.PickPocketAction;
import com.rs.content.actions.skills.thieving.PickPocketableNPC;
import com.rs.content.customskills.sailing.dialogues.JackSailsDialogue;
import com.rs.content.customskills.sailing.dialogues.CargoJobDialogue;
import com.rs.content.dialogues.impl.*;
import com.rs.content.dialogues.impl.home.ArmorExchange;
import com.rs.content.dialogues.impl.lumbridge.DukeHoracio;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.content.player.PlayerRank;
import com.rs.content.player.points.PlayerPoints;
import com.rs.content.prestige.PrestigeD;
import com.rs.core.net.io.InputStream;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.rs.player.content.LividFarm;
import com.rs.player.content.Magic;
import com.rs.player.content.PlayerLook;
import com.rs.player.controlers.SorceressGarden;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.familiar.Familiar;
import com.rs.world.npc.others.FireSpirit;
import com.rs.world.npc.others.LivingRock;
import com.rs.world.npc.pet.Pet;

public class NPCHandler {

    public static void handleExamine(final Player player,
                                     final InputStream stream) {
        final int npcIndex = stream.readUnsignedShort128();
        final boolean forceRun = stream.read128Byte() == 1;
        if (forceRun) {
            player.setRun(forceRun);
        }
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId()))
            return;
        if (player.getRank().isMinimumRank(PlayerRank.MOD)) {
            player.getPackets().sendGameMessage(
                    "NPC - [id=" + npc.getId() + ", loc=[" + npc.getX() + ", "
                            + npc.getY() + ", " + npc.getPlane() + "]].");
        }
        player.getPackets().sendNPCMessage(0, npc,
                "It's a " + npc.getDefinitions().name + ".");
        if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            Logger.info("NPCHandler",
                    "examined npc: " + npcIndex + ", " + npc.getId());
        }
    }

    public static void handleOption1(final Player player,
                                     final InputStream stream) {
        final int npcIndex = stream.readUnsignedShort128();
        final boolean forceRun = stream.read128Byte() == 1;
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.isCantInteract() || npc.isDead()
                || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId()))
            return;
        player.stopAll(false);
        if (forceRun) {
            player.setRun(forceRun);
        }
        if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
            player.setCoordsEvent(new CoordsEvent(npc, () -> {
                player.faceEntity(npc);
                if (!player.withinDistance(npc, 5))
                    return;
                npc.faceEntity(player);
                player.getDialogueManager().startDialogue(Banker.class, npc.getId());
            }, npc.getSize()));
            return;
        }
        if (npc.getDefinitions().name.contains("Musician")) {
            player.faceEntity(npc);
            if (!player.withinDistance(npc, 2))
                return;
            long currentTime = Utils.currentTimeMillis();
            if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
                player.getPackets().sendGameMessage("You can't rest while performing an emote.");
                return;
            }
            if (player.getLockDelay() >= currentTime) {
                player.getPackets().sendGameMessage("You can't rest while performing an action.");
                return;
            }
            player.stopAll();
            player.getActionManager().setAction(new Rest());
            return;
        }
        if (npc.getId() == 2593 ||npc.getDefinitions().name.contains("Grand Exchange clerk") || npc.getDefinitions().name.contains("Grand_Exchange_clerk")) {
            player.setCoordsEvent(new CoordsEvent(npc, () -> {
                if (!player.withinDistance(npc, 3))
                    return;
                npc.resetWalkSteps();
                npc.faceEntity(player);
                player.faceEntity(npc);
                player.getGeManager().openGrandExchange();
            }, npc.getSize()));
            return;
        }
        player.setCoordsEvent(new CoordsEvent(npc, () -> {
            npc.resetWalkSteps();
            player.faceEntity(npc);
            if (!player.getControllerManager().processNPCClick1(npc))
                return;
            final FishingSpots spot = FishingSpots.forId(npc.getId()
                    | 1 << 24);
            if (spot != null) {
                player.getActionManager().setAction(new Fishing(spot, npc));
                return; // its a spot, they wont face us
            } else if (npc.getId() >= 8837 && npc.getId() <= 8839) {
                player.getActionManager().setAction(
                        new LivingMineralMining((LivingRock) npc));
                return;
            }
            npc.faceEntity(player);
            if (npc.getDefinitions().name.contains("Slave")) {
                player.getDialogueManager().startDialogue(SlaveTalk.class, npc.getId());
                return;
            }
            if (npc.getDefinitions().name.contains("Grand Exchange Tutor")) {
                player.getDialogueManager().startDialogue(GrandExchangeTutor.class,
                        npc.getId());
                return;
            }
            if (SiphonActionCreatures.siphon(player, npc) || ShopsManager.handleShopNpc(player, npc.getId())) {
                return;
            }
            if (npc.getId() == 3709) {
                player.getDialogueManager().startDialogue(MrEx.class, npc.getId());
            } else if (npc.getId() == JackSailsDialogue.NPC_ID) {
                player.getDialogueManager().startDialogue(JackSailsDialogue.class);
            } else if (npc.getId() == Mandrith.MANDRITH_ID) {
                player.getDialogueManager().startDialogue(Mandrith.class);
            } else if (npc.getId() == 2253) {
                player.getDialogueManager().startDialogue(PrestigeD.class);
            } else if (npc.getId() == 949) {
                player.getDialogueManager().startDialogue(QuestGuide.class,
                        npc.getId(), null);
            } else if (npc.getId() == 15451 && npc instanceof FireSpirit) {
                final FireSpirit spirit = (FireSpirit) npc;
                spirit.giveReward(player);
            } else if (npc.getId() == 943) {
                player.getInterfaceManager().sendInterface(473);
                /**
                 * Top Part / menu buttons / Tabs
                 */
                player.getPackets().sendIComponentText(473, 31,
                        "Dragon Hunter Shop");
                player.getPackets().sendIComponentText(473, 227,
                        "<col=B00000><shad=FF0000>Dragon Items");
                player.getPackets().sendIComponentText(473, 224,
                        "<col=B00000><shad=FF0000>Void");
                player.getPackets().sendIComponentText(473, 221,
                        "<col=B00000><shad=FF0000>Elite Void");
                player.getPackets().sendIComponentText(473, 36,
                        "<col=B00000><shad=FF0000>Not added yet");
                /**
                 * First Tab
                 */
                /*
                 * Button box 1
                 */
                player.getPackets().sendItemOnIComponent(473, 43, 24315, 1);
                player.getPackets().sendItemOnIComponent(473, 44, 24315, 1);
                player.getPackets().sendIComponentText(473, 46,
                        " +15 Str Bonus");
                player.getPackets().sendIComponentText(473, 45,
                        "  <shad=FF0000>600 Dragon Points");
                /*
                 * Button box 2
                 */
                player.getPackets().sendItemOnIComponent(473, 57, 11283, 1);
                player.getPackets().sendItemOnIComponent(473, 58, 11283, 1);
                player.getPackets().sendIComponentText(473, 60,
                        " +7 Str Bonus");
                player.getPackets().sendIComponentText(473, 59,
                        "  <shad=FF0000>400 Dragon Points");
                /*
                 * Button box 3
                 */
                player.getPackets().sendItemOnIComponent(473, 50, 20072, 1);
                player.getPackets().sendItemOnIComponent(473, 51, 20072, 1);
                player.getPackets().sendIComponentText(473, 53,
                        " +7 Str Bonus");
                player.getPackets().sendIComponentText(473, 52,
                        "  <shad=FF0000>500 Dragon Points");
                /*
                 * Button box 4
                 */
                player.getPackets().sendItemOnIComponent(473, 64, 19287, 1);
                player.getPackets().sendItemOnIComponent(473, 65, 19287, 1);
                player.getPackets().sendIComponentText(473, 67,
                        "Purely Cosmetic");
                player.getPackets().sendIComponentText(473, 66,
                        "  <shad=FF0000>75 Dragon Points");
                /**
                 * First Tab 2
                 */
                /*
                 * Button Box 1
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 126, 11665, 1);
                player.getPackets().sendIComponentText(473, 127,
                        "Void Melee Helm");
                player.getPackets().sendIComponentText(473, 128,
                        "100 Dragon Points");
                player.getPackets().sendIComponentText(473, 130, "");
                /*
                 * Button Box 2
                 */
                player.getPackets().sendItemOnIComponent(473, 98, 11664, 1);
                player.getPackets().sendIComponentText(473, 99,
                        "Void Range Helm");
                player.getPackets().sendIComponentText(473, 100,
                        "100 Dragon Points");
                player.getPackets().sendIComponentText(473, 102, "");
                /*
                 * Button Box 3
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 119, 11663, 1);
                player.getPackets().sendIComponentText(473, 120,
                        "Void Mage Helm");
                player.getPackets().sendIComponentText(473, 121,
                        "100 Dragon Points");
                player.getPackets().sendIComponentText(473, 123, "");
                /*
                 * Button Box 4
                 */
                player.getPackets().sendItemOnIComponent(473, 91, 8839, 1);
                player.getPackets().sendIComponentText(473, 92,
                        "Void Melee Top");
                player.getPackets().sendIComponentText(473, 93,
                        "200 Dragon Points");
                player.getPackets().sendIComponentText(473, 95, "");
                /*
                 * Button box 5
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 112, 28783, 1);
                player.getPackets().sendIComponentText(473, 113,
                        "Greater Demon Armor Set");
                player.getPackets().sendIComponentText(473, 115,
                        "<col=FF0000>3 Tasks completed + 30 Pvp Kills");
                player.getPackets().sendIComponentText(473, 114,
                        "20 PvP Points + 550k Pvp Tokens");
                player.getPackets().sendIComponentText(473, 116, "");
                /*
                 * Button box 6
                 */
                player.getPackets().sendItemOnIComponent(473, 85, 25386, 1);
                player.getPackets().sendIComponentText(473, 86,
                        "K'ril's Godcrusher Armor Set");
                player.getPackets().sendIComponentText(473, 88,
                        "<col=FF0000>5 Tasks completed + 40 Pvp Kills");
                player.getPackets().sendIComponentText(473, 87,
                        "38 PvP Points + 550k Pvp Tokens");
                player.getPackets().sendIComponentText(473, 116, "");
                /*
                 * Button box 7
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 105, 26182, 1);
                player.getPackets().sendIComponentText(473, 106,
                        "TokHaar Warlord Armor Set");
                player.getPackets().sendIComponentText(473, 108,
                        "<col=FF0000>6 Tasks completed + 50 Pvp Kills");
                player.getPackets().sendIComponentText(473, 107,
                        "45 PvP Points + 500k Pvp Tokens");
                player.getPackets().sendIComponentText(473, 109, "");
                /*
                 * Button box 8
                 */
                player.getPackets().sendItemOnIComponent(473, 79, 28792, 1);
                player.getPackets()
                        .sendIComponentText(473, 80,
                                "Kalphite Armor Set(<col=FF00FF>NOT OUT YET</col>)");
                player.getPackets().sendIComponentText(473, 82,
                        "<col=FF0000>10 Tasks completed + 60 Pvp Kills");
                player.getPackets().sendIComponentText(473, 81,
                        "50 PvP Points + 500k Pvp Tokens");
                /**
                 * First Tab 3
                 */
                /*
                 * Button box 1
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 144, 28813, 1);
                player.getPackets()
                        .sendItemOnIComponent(473, 145, 28813, 1);
                player.getPackets().sendIComponentText(473, 147,
                        "+153 Str Bonus");
                player.getPackets().sendIComponentText(473, 146,
                        "<shad=FF0000>UNKNOWN PvP Points");
                /*
                 * Button box 2
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 136, 28818, 1);
                player.getPackets()
                        .sendItemOnIComponent(473, 139, 28818, 1);
                player.getPackets().sendIComponentText(473, 141,
                        "+165 Str Bonus");
                player.getPackets().sendIComponentText(473, 140,
                        "<shad=FF0000>UNKNOWN PvP Points");

                /**
                 * First Tab 4
                 */
                /*
                 * Button box 1
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 217, 13883, 1);
                player.getPackets().sendItemOnIComponent(473, 218, 13883,
                        20);
                player.getPackets().sendIComponentText(473, 220, "");
                player.getPackets().sendIComponentText(473, 219,
                        "<col=B00000>7 PvP Points");
                /*
                 * Button box 2
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 181, 13879, 1);
                player.getPackets().sendItemOnIComponent(473, 182, 26783,
                        20);
                player.getPackets().sendIComponentText(473, 185, "");
                player.getPackets().sendIComponentText(473, 184, "");
                player.getPackets().sendIComponentText(473, 183,
                        "<col=B00000>7 PvP Points");
                /*
                 * Button box 3
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 204, 13887, 1);
                player.getPackets().sendIComponentText(473, 205,
                        "Vesta's Armor Set");
                player.getPackets().sendIComponentText(473, 207, "");
                player.getPackets().sendIComponentText(473, 206,
                        "35 PvP Points");
                /*
                 * Button box 4
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 210, 13884, 1);
                player.getPackets().sendIComponentText(473, 211,
                        "Statius's Armor Set");
                player.getPackets().sendIComponentText(473, 213, "");
                player.getPackets().sendIComponentText(473, 214, "");
                player.getPackets().sendIComponentText(473, 212,
                        "35 PvP Points");
                /*
                 * Button box 5
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 198, 13870, 1);
                player.getPackets().sendIComponentText(473, 199,
                        "Morrigan's Armor Set");
                player.getPackets().sendIComponentText(473, 201, "");
                player.getPackets().sendIComponentText(473, 200,
                        "20 PvP Points");
                /*
                 * Button box 6
                 */
                player.getPackets()
                        .sendItemOnIComponent(473, 192, 13858, 1);
                player.getPackets().sendIComponentText(473, 193,
                        "Zuriel's Armor Set");
                player.getPackets().sendIComponentText(473, 195, "");
                player.getPackets().sendIComponentText(473, 194,
                        "20 PvP Points");
                /**
                 * Bottem Part
                 */
                // player.getPackets().sendIComponentText(473, 194,
                // "you have <col=B00000><shad=FF0000>" +player.getPlayerPoints().getPoints(PlayerPoints.PK_POINTS)
                // + "</col> Dragon Points.");
                player.getPackets().sendIComponentText(
                        473,
                        5,
                        "                                            You have <col=B00000>"
                                + player.getPlayerPoints().getPoints(PlayerPoints.PK_POINTS)
                                + "</col></shad> Dragon Points.");
                player.getPackets().sendIComponentText(473, 6,
                        "                                             ");
                player.getPackets().sendIComponentText(473, 7, "");
                player.getPackets().sendIComponentText(473, 8, "");
                player.getPackets().sendIComponentText(473, 9, "");
                player.getPackets().sendIComponentText(473, 10, "");
                player.getPackets().sendIComponentText(473, 11, "");
                player.getPackets().sendIComponentText(473, 12, "");
            } else if (npc.getId() == 3005) {
                player.getInterfaceManager().sendInterface(72);
                player.setGambleNumber(Utils.random(9) + 1);
                player.getPackets().sendGameMessage("I wish you the best of luck.");
                player.getPackets().sendIComponentText(72, 31, "1");
                player.getPackets().sendIComponentText(72, 32, "2");
                player.getPackets().sendIComponentText(72, 33, "3");
                player.getPackets().sendIComponentText(72, 34, "4");
                player.getPackets().sendIComponentText(72, 35, "5");
                player.getPackets().sendIComponentText(72, 36, "6");
                player.getPackets().sendIComponentText(72, 37, "7");
                player.getPackets().sendIComponentText(72, 38, "8");
                player.getPackets().sendIComponentText(72, 39, "9");
                player.getPackets().sendIComponentText(72, 40, "10");
                player.getPackets().sendIComponentText(72, 55, "Guess the number!");
            } else if (npc.getId() == 470) {
                player.getInterfaceManager().sendInterface(1312);
                player.getPackets().sendIComponentText(1312, 27,
                        "Pvm Points: " + player.getPlayerPoints().getPoints(PlayerPoints.PVM_POINTS) + ".");
                player.getPackets().sendIComponentText(1312, 38,
                        "Ornate Katana (500 points)");
                player.getPackets().sendIComponentText(1312, 46,
                        "Auspicious Katana (1000 points)");
                player.getPackets().sendIComponentText(1312, 54,
                        "Herculean Gold Ring +12 strength (800 points)");
                player.getPackets().sendIComponentText(1312, 62,
                        "Fire Cape +16 strength (1000 points)");
                player.getPackets().sendIComponentText(1312, 70,
                        "Sagittarian Coif (5000 points)");
                player.getPackets().sendIComponentText(1312, 78,
                        "Sagittarian Gloves (5000 points)");
                player.getPackets().sendIComponentText(1312, 86,
                        "Sagittarian Body (10.000 points)");
                player.getPackets().sendIComponentText(1312, 94,
                        "Sagittarian Chaps (10.000 points)");
                player.getPackets().sendIComponentText(1312, 102,
                        "Sagittarian Boots (5000 points)");
            } else if (npc.getId() == 3805) {
                player.getDialogueManager().startDialogue(PostiePete.class,
                        npc.getId());
            } else if (npc.getId() == 560) {
                player.getDialogueManager().startDialogue(CargoJobDialogue.class, npc.getId());
            } else if (npc.getId() == 418) {
                player.getDialogueManager().startDialogue(FarmingLady.class,
                        npc.getId());
            } else if (npc.getId() == 14915) {
                player.getDialogueManager().startDialogue(SkillGone.class,
                        npc.getId());
            } else if (npc.getId() == 815) {
                player.getDialogueManager().startDialogue(RepairNex.class,
                        npc.getId());
            } else if (npc.getId() == 15612) {
                player.getDialogueManager().startDialogue(BorkDia.class,
                        npc.getId());
            } else if (npc.getId() == 7909) {
                player.getDialogueManager().startDialogue(Roddeck.class,
                        npc.getId());
            } else if (npc.getId() == 8091) {
                player.getDialogueManager().startDialogue(StarSprite.class);
            } else if (npc.getId() == 1886) {
                Magic.pushLeverTeleport(player, new WorldTile(3505, 9494, 0));
            } else if (npc.getId() == 7935) {
                player.getDialogueManager().startDialogue(HansShop.class,
                        npc.getId());
            } else if (npc.getId() == 8556) {
                player.getDialogueManager().startDialogue(DTRewards.class,
                        npc.getId());
            } else if (npc.getId() == 14712) {
                player.getDialogueManager().startDialogue(ShopsTeleport.class,
                        npc.getId());
            } else if (npc.getId() == 14) {
                //player.getDialogueManager().startDialogue(AmazingShop.class,npc.getId());
            } else if (npc.getId() == 5566) {
                player.getDialogueManager().startDialogue(LucienMystery.class,
                        npc.getId());
            } else if (npc.getId() == 13955) {
                player.getDialogueManager().startDialogue(TheRaptor.class,
                        npc.getId());
            } else if (npc.getId() == 14714) {
                player.getDialogueManager().startDialogue(RunespanPortalD.class, npc.getId());
            } else if (npc.getId() == 1701) {
                player.getDialogueManager().startDialogue(FoodExchange.class,
                        npc.getId());
            } else if (npc.getId() == 1703) {
                player.getDialogueManager().startDialogue(SailorFood.class,
                        npc.getId());
            } else if (npc.getId() == 2998) {
                player.getDialogueManager().startDialogue(ClassicCape.class, npc.getId());
            } else if (npc.getId() == 13698) {
                player.getDialogueManager().startDialogue(Trolly.class,
                        npc.getId());
            } else if (npc.getId() == 9634) {
                player.getDialogueManager().startDialogue(ChooseXenia.class,
                        npc.getId());
            } else if (npc.getId() == 14343) {
                player.getDialogueManager().startDialogue(Barrelchest.class,
                        npc.getId());
            } else if (npc.getId() == 15534) {
                player.getDialogueManager().startDialogue(AllShops1.class,
                        npc.getId());
            } else if (npc.getId() == 15532) {
                player.getDialogueManager().startDialogue(AllShops.class,
                        npc.getId());
            } else if (npc.getId() == 15533) {
                player.getDialogueManager().startDialogue(AllShops2.class,
                        npc.getId());
            } else if (npc.getId() == 6334) {
                player.getDialogueManager().startDialogue(QuestChild.class,
                        npc.getId());
            } else if (npc.getId() == 15019) {
                player.getDialogueManager().startDialogue(QuestSoldier.class,
                        npc.getId());
            } else if (npc.getId() == 410) {
                player.getDialogueManager().startDialogue(VIPCape.class,
                        npc.getId());
            } else if (npc.getId() == 6361) {
                player.getDialogueManager().startDialogue(KorasiShop.class,
                        npc.getId());
            } else if (npc.getId() == 3006) {
                player.getDialogueManager().startDialogue(Gambler.class,
                        npc.getId());
            } else if (npc.getId() == 7531) {
                player.getDialogueManager().startDialogue(Niles.class,
                        npc.getId());
            } else if (npc.getId() == 13462) {
                player.getDialogueManager().startDialogue(WolfHunter.class,
                        npc.getId());
            } else if (npc.getId() == 7530) {
                LividFarm.CheckforLogs(player);
            } else if (npc.getId() == 11460) {
                player.getDialogueManager().startDialogue(MoneyVault.class,
                        npc.getId());
            } else if (npc.getId() == 9684) {
                player.getDialogueManager().startDialogue(EasterRing.class,
                        npc.getId());
            } else if (npc.getId() == 3705) {
                player.getDialogueManager().startDialogue(MaxShop.class,
                        npc.getId());
            } else if (npc.getId() == 1576) {
                //player.getDialogueManager().startDialogue(XtremeShop.class, npc.getId());
            } else if (npc.getId() == 9434) {
                player.getDialogueManager().startDialogue(MrEx.class,
                        npc.getId());
            } else if (npc.getId() == 918) {
                player.getDialogueManager().startDialogue(Sailor.class,
                        npc.getId());
            } else if (npc.getId() == 2274) {
                player.getDialogueManager().startDialogue(PrisonPete.class,
                        npc.getId());
            } else if (npc.getId() == 2139) {
                player.getDialogueManager().startDialogue(GnomeTalk.class,
                        npc.getId());
            } else if (npc.getId() == 278) {
                player.getDialogueManager().startDialogue(CooksAssistant.class,
                        npc.getId());
            } else if (npc.getId() == 758) {
                player.getDialogueManager().startDialogue(Fred.class,
                        npc.getId());
            } else if (npc.getId() == 4250) {
                player.getDialogueManager().startDialogue(SawmillOperator.class, npc.getId());
            } else if (npc.getId() == 659) {
                player.getDialogueManager().startDialogue(Bday.class,
                        npc.getId());
            } else if (npc.getId() == 13768) {
                //player.getDialogueManager().startDialogue(VoteShop.class, npc.getId());
            } else if (npc.getId() == 3807) {
                player.getDialogueManager().startDialogue(MilkBucket.class,
                        npc.getId());
            } else if (npc.getId() == 1430) {
                player.getDialogueManager().startDialogue(CookBro.class,
                        npc.getId());
            } else if (npc.getId() == 7892) {
                player.getDialogueManager().startDialogue(OtherTeleports.class,
                        npc.getId());
            } else if (npc.getId() == 14722) {
                //player.getDialogueManager().startDialogue(XtremeFlasksShop.class, npc.getId());
            } else if (npc.getId() == 1835) {
                player.getDialogueManager().startDialogue(EasterBunny.class,
                        npc.getId());
            } else if (npc.getId() == 568) {
                //player.getDialogueManager().startDialogue(VipShopper.class, npc.getId());
            } else if (npc.getId() == 15548) {
                player.getDialogueManager().startDialogue(LevelUpShop.class,
                        npc.getId());
            } else if (npc.getId() == 10) {
                //player.getDialogueManager().startDialogue(ZombieMonk.class, npc.getId());
            } else if (npc.getId() == 9085) {
                player.getDialogueManager().startDialogue(Kuradal.class, false);
            } else if (npc.getId() == 8461) {
                player.getDialogueManager().startDialogue(Kuradal.class, false);
            } else if (npc.getId() == 7892) {
                player.getDialogueManager().startDialogue(OtherTeleports.class);
            } else if (npc.getId() == 3374) {
                player.getDialogueManager().startDialogue(Max.class, npc.getId(), null);
            } else if (npc.getId() == 9462) {
                player.setNextAnimation(new Animation(4278));
                World.spawnNPC(9463, new WorldTile(3422, 5665, 0), -1, true);
                npc.transformIntoNPC(-1);
            } else if (npc.getId() == 6053) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6054) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6055) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6056) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6057) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6058) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6059) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6060) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6061) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6063) {
                Hunter.openJar(player);
            } else if (npc.getId() == 6064) {
                Hunter.openJar(player);
            } else if (npc.getId() == 9707) {
                player.getDialogueManager().startDialogue(FremennikShipmaster.class, npc.getId(), true);
            } else if (npc.getId() == 9708) {
                player.getDialogueManager().startDialogue(FremennikShipmaster.class, npc.getId(), false);
            } else if (npc.getId() == 9711) {
                player.getDialogueManager().startDialogue(DungeonMaster.class,
                        npc.getId());
            } else if (npc.getId() == 13727) {
                player.getDialogueManager().startDialogue(Xuans.class,
                        npc.getId());
            } else if (npc.getId() == 1918) {
                player.getDialogueManager().startDialogue(Mandrith.class,
                        npc.getId());
            } else if (npc.getId() == 6970) {
                player.getDialogueManager().startDialogue(SummoningShop.class,
                        npc.getId(), false);
            } else if (npc.getId() == 598) {
                player.getDialogueManager().startDialogue(Hairdresser.class,
                        npc.getId());
            } else if (npc.getId() == 548) {
                player.getDialogueManager().startDialogue(Thessalia.class,
                        npc.getId());
            } else if (npc.getId() == 2417) {
                player.setNextAnimation(new Animation(4278));
                World.spawnNPC(3334, new WorldTile(3084, 3648, 0), -1, true);
            } else if (npc.getId() == 4250) {
                player.getInterfaceManager().sendInterface(403);
            } else if (npc.getId() == 598) {
                PlayerLook.openHairdresserSalon(player);
            } else if (npc.getId() == 548) {
                player.getDialogueManager().startDialogue(SkillcapeChoice.class, npc.getId());
            } else if (npc instanceof Pet) {
                final Pet pet = (Pet) npc;
                if (pet != player.getPet()) {
                    player.getPackets().sendGameMessage(
                            "This isn't your pet.");
                    return;
                }
                player.setNextAnimation(new Animation(827));
                pet.pickup();
            } else if (npc.getId() == 6970) {
                player.getDialogueManager().startDialogue(SummoningShop.class,
                        npc.getId(), false);
            } else if (npc.getId() == 6892) {
                //player.getDialogueManager().startDialogue(PetShop.class, npc.getId());
            } else if (npc.getId() == 15501) {
                player.getDialogueManager().startDialogue(QuestStart.class,
                        npc.getId(), 0);
            } else {
                player.getPackets().sendGameMessage(
                        "Nothing interesting happens.");
                for (final Player players : World.getPlayers())
                    if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                        if (players.getUsername().equalsIgnoreCase("Ben")) {
                            System.out.println("cliked 1 at npc id : "
                                    + npc.getId() + ", " + npc.getX()
                                    + ", " + npc.getY() + ", "
                                    + npc.getPlane());
                        }
                    }
            }
        }, npc.getSize()));
    }

    public static void handleOption2(final Player player,
                                     final InputStream stream) {
        final int npcIndex = stream.readUnsignedShort128();
        final boolean forceRun = stream.read128Byte() == 1;
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.isCantInteract() || npc.isDead()
                || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId()))
            return;
        player.stopAll(false);
        if (forceRun) {
            player.setRun(forceRun);
        }
        if (npc.getDefinitions().name.contains("Musician")) {
            player.faceEntity(npc);
            if (!player.withinDistance(npc, 2))
                return;
            player.getDialogueManager().startDialogue(MusicianD.class, npc.getId());
            return;
        }
        if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
            player.setCoordsEvent(new CoordsEvent(npc, () -> {
                player.faceEntity(npc);
                if (!player.withinDistance(npc, 5))
                    return;
                npc.faceEntity(player);
                player.getBank().openBank();
                //player.getDialogueManager().startDialogue(Banker.class, npc.getId());
            }, npc.getSize()));
            return;
        }
        if (npc.getId() == 2593 ||npc.getDefinitions().name.contains("Grand Exchange clerk") || npc.getDefinitions().name.contains("Grand_Exchange_clerk")) {
            player.setCoordsEvent(new CoordsEvent(npc, () -> {
                if (!player.withinDistance(npc, 3))
                    return;
                npc.resetWalkSteps();
                npc.faceEntity(player);
                player.faceEntity(npc);
                player.getGeManager().openGrandExchange();
            }, npc.getSize()));
            return;
        }
        player.setCoordsEvent(new CoordsEvent(npc, () -> {
            npc.resetWalkSteps();
            player.faceEntity(npc);
            if (!player.getControllerManager().processNPCClick2(npc))
                return;
            final FishingSpots spot = FishingSpots.forId(npc.getId()
                    | (2 << 24));
            if (spot != null) {
                player.getActionManager().setAction(new Fishing(spot, npc));
                return;
            }
            npc.faceEntity(player);
            final PickPocketableNPC pocket = PickPocketableNPC.get(npc
                    .getId());
            if (pocket != null) {
                player.getActionManager().setAction(
                        new PickPocketAction(npc, pocket));
                return;
            }
            if (npc instanceof Familiar) {
                if (npc.getDefinitions().hasOption("store")) {
                    if (player.getFamiliar() != npc) {
                        player.getPackets().sendGameMessage(
                                "That isn't your familiar.");
                        return;
                    }
                    player.getFamiliar().store();
                } else if (npc.getDefinitions().hasOption("cure")) {
                    if (player.getFamiliar() != npc) {
                        player.getPackets().sendGameMessage(
                                "That isn't your familiar.");
                        return;
                    }
                    if (!player.getPoison().isPoisoned()) {
                        player.getPackets().sendGameMessage(
                                "Your arent poisoned or diseased.");
                        return;
                    } else {
                        player.getFamiliar().drainSpecial(2);
                        player.addPoisonImmune(120);
                    }
                }
                return;
            }
            if (ShopsManager.handleShopNpc(player, npc.getId())) {
                return;
            }
            if (npc.getId() == 9707) {
                FremennikShipmaster.sail(player, true);
            } else if (npc.getId() == 9708) {
                FremennikShipmaster.sail(player, false);
            } else if (npc.getId() == 13455) {
                player.getBank().openBank();
            } else if (npc.getId() == 598) {
                PlayerLook.openHairdresserSalon(player);
            } else if (npc.getId() == 741) {
                player.getDialogueManager().startDialogue(DukeHoracio.class);
            } else if (npc.getId() == 548) {
                player.getDialogueManager().startDialogue(SkillcapeChoice.class, npc.getId());
            } else if (npc instanceof Pet) {
                final Pet pet = (Pet) npc;
                if (pet != player.getPet()) {
                    player.getPackets().sendGameMessage(
                            "This isn't your pet.");
                    return;
                }
                player.setNextAnimation(new Animation(827));
                pet.pickup();
            } else if (npc.getId() == 6970) {
                player.getDialogueManager().startDialogue(SummoningShop.class,
                        npc.getId(), false);
            } else if (npc.getId() == 15501) {
                PlayerLook.openMageMakeOver(player);
            } else {
                player.getPackets().sendGameMessage(
                        "Nothing interesting happens.");
                if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                    System.out.println("clicked 2 at npc id : "
                            + npc.getId() + ", " + npc.getX() + ", "
                            + npc.getY() + ", " + npc.getPlane());
                }
            }
        }, npc.getSize()));
    }

    public static void handleOption3(final Player player,
                                     final InputStream stream) {
        final int npcIndex = stream.readUnsignedShort128();
        final boolean forceRun = stream.read128Byte() == 1;
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.isCantInteract() || npc.isDead()
                || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId()))
            return;
        player.stopAll(false);
        if (forceRun) {
            player.setRun(forceRun);
        }
        if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
            player.setCoordsEvent(new CoordsEvent(npc, () -> {
                if (!player.withinDistance(npc, 2))
                    return;
                npc.resetWalkSteps();
                npc.setNextFaceWorldTile(new WorldTile(player.getX(),
                        player.getY(), player.getPlane()));
                player.faceEntity(npc);
                player.getGeManager().openCollectionBox();
            }, npc.getSize()));
            return;
        }
        if (npc.getId() == 2593 || npc.getDefinitions().name.contains("Grand Exchange clerk") || npc.getDefinitions().name.contains("Grand_Exchange_clerk")) {
            player.setCoordsEvent(new CoordsEvent(npc, () -> {
                if (!player.withinDistance(npc, 2))
                    return;
                npc.resetWalkSteps();
                npc.setNextFaceWorldTile(new WorldTile(player.getX(),
                        player.getY(), player.getPlane()));
                player.faceEntity(npc);
                player.getGeManager().openHistory();
            }, npc.getSize()));
            return;
        }
        player.setCoordsEvent(new CoordsEvent(npc, () -> {
            npc.resetWalkSteps();
            if (!player.getControllerManager().processNPCClick3(npc))
                return;
            player.faceEntity(npc);
            if (npc.getId() >= 8837 && npc.getId() <= 8839) {
                MiningBase.propect(player, "You examine the remains...",
                        "The remains contain traces of living minerals.");
                return;
            }
            if (npc.getId() == 13727) {
                player.getPackets().sendGameMessage("Title cleared.");
                player.getAppearance().setTitle(0);
                player.getDisplayName();
                player.getAppearance().generateAppearenceData();
            }
            npc.faceEntity(player);
            if (npc.getId() == 548) {
                PlayerLook.openThessaliasMakeOver(player);
            }
            if (npc.getId() == 5532) {
                npc.setNextForceTalk(new ForceTalk(
                        "Senventior Disthinte Molesko!"));
                player.getControllerManager().startController(
                        SorceressGarden.class);
            }
        }, npc.getSize()));
        if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            System.out.println("clicked 3 at npc id : " + npc.getId() + ", "
                    + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
        }
    }

    public static void handleOption4(final Player player,
                                     final InputStream stream) {
        final int npcIndex = stream.readUnsignedShort128();
        final boolean forceRun = stream.read128Byte() == 1;
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.isCantInteract() || npc.isDead()
                || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId()))
            return;
        player.stopAll(false);
        if (forceRun) {
            player.setRun(forceRun);
        }
        player.setCoordsEvent(new CoordsEvent(npc, () -> {
            npc.resetWalkSteps();
            if (!player.getControllerManager().processNPCClick4(npc))
                return;
            player.faceEntity(npc);
            if (npc.getId() == 2593 || npc.getDefinitions().name.contains("Grand Exchange clerk") || npc.getDefinitions().name.contains("Grand_Exchange_clerk")) {
                player.getDialogueManager().startDialogue(ArmorExchange.class, npc.getId());
            }
            if (npc.getId() == 9085 || npc.getId() == 8461
                    || npc.getId() == 8464 || npc.getId() == 1597
                    || npc.getId() == 1598 || npc.getId() == 7780
                    || npc.getId() == 8466) {
                player.getInterfaceManager().sendInterface(164);
                player.getPackets().sendIComponentText(164, 20,
                        "" + player.getPlayerPoints().getPoints(PlayerPoints.SLAYER_POINTS) + "");
                player.getPackets().sendIComponentText(164, 32,
                        "(20 points)");
                player.getPackets().sendIComponentText(164, 33,
                        "(1500 points)");
                player.getPackets().sendIComponentText(164, 34,
                        "(35 points)");
                player.getPackets().sendIComponentText(164, 35,
                        "(35 points)");
                player.getPackets().sendIComponentText(164, 36,
                        "(35 points)");
            }
        }, npc.getSize()));
        if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            System.out.println("clicked 4 at npc id : " + npc.getId() + ", "
                    + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
        }
    }
}
