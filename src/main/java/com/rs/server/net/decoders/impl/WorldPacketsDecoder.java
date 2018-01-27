package com.rs.server.net.decoders.impl;

import com.rs.server.Server;
import com.rs.content.actions.impl.PlayerFollow;
import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.prayer.GildedAltar;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.christmas.snowballs.SnowBalls;
import com.rs.content.clans.ClansManager;
import com.rs.content.commands.CommandManager;
import com.rs.content.customskills.sailing.SailingController;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.economy.shops.Shop;
import com.rs.content.minigames.clanwars.ClanWars;
import com.rs.content.minigames.creations.StealingCreation;
import com.rs.content.staff.StaffInterface;
import com.rs.content.staff.StaffPanelHandler;
import com.rs.server.net.Session;
import com.rs.server.net.decoders.Decoder;
import com.rs.server.net.handlers.PacketHandlerManager;
import com.rs.server.net.handlers.button.ButtonHandler;
import com.rs.server.net.handlers.inventory.ItemOnItemHandler;
import com.rs.server.net.handlers.inventory.ItemOnNpcHandler;
import com.rs.server.net.handlers.inventory.ItemOnPlayerHandler;
import com.rs.server.net.handlers.npc.NPCHandler;
import com.rs.server.net.handlers.object.ItemOnObjectHandler;
import com.rs.server.net.handlers.object.ObjectHandler;
import com.rs.server.net.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;
import com.rs.player.*;
import com.rs.player.combat.PlayerCombat;
import com.rs.player.content.*;
import com.rs.world.*;
import com.rs.world.item.FloorItem;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.familiar.Familiar;

import java.text.DecimalFormat;

public final class WorldPacketsDecoder extends Decoder {

    public final static int ACTION_BUTTON1_PACKET = 14;
    public final static int ACTION_BUTTON2_PACKET = 67;
    public final static int ACTION_BUTTON3_PACKET = 5;
    public final static int ACTION_BUTTON4_PACKET = 55;
    public final static int ACTION_BUTTON5_PACKET = 68;
    public final static int ACTION_BUTTON6_PACKET = 90;
    public final static int ACTION_BUTTON7_PACKET = 6;
    public final static int ACTION_BUTTON8_PACKET = 32;
    public final static int ACTION_BUTTON9_PACKET = 27;
    public final static int WORLD_MAP_CLICK = 38;
    public final static int ACTION_BUTTON10_PACKET = 96;
    public final static int RECEIVE_PACKET_COUNT_PACKET = 33;
    public static final int PLAYER_OPTION_7_PACKET = 51;
    public static final int PLAYER_OPTION_8_PACKET = 85;
    private final static int PLAYER_OPTION_9_PACKET = 56;
    private final static int WALKING_PACKET = 8;
    private final static int MINI_WALKING_PACKET = 58;
    private final static int AFK_PACKET = -1;
    private final static int MAGIC_ON_ITEM_PACKET = 3;
    private final static int PLAYER_OPTION_4_PACKET = 17;
    private final static int MOVE_CAMERA_PACKET = 103;
    private final static int INTERFACE_ON_OBJECT = 37;
    private final static int CLICK_PACKET = -1;
    private final static int MOVE_MOUSE_PACKET = -1;
    private final static int KEY_TYPED_PACKET = -1;
    private final static int CLOSE_INTERFACE_PACKET = 54;
    private final static int COMMANDS_PACKET = 60;
    private final static int ITEM_ON_ITEM_PACKET = 3;
    private final static int IN_OUT_SCREEN_PACKET = -1;
    private final static int DONE_LOADING_REGION_PACKET = 30;
    private final static int PING_PACKET = 21;
    private final static int SCREEN_PACKET = 98;
    private final static int CHAT_TYPE_PACKET = 83;
    private final static int CHAT_PACKET = 53;
    private final static int PUBLIC_QUICK_CHAT_PACKET = 86;
    private final static int GRAND_EXCHANGE_ITEM_SELECT = 71;
    private final static int ADD_FRIEND_PACKET = 89;
    private final static int ADD_IGNORE_PACKET = 4;
    private final static int REMOVE_IGNORE_PACKET = 73;
    private final static int JOIN_FRIEND_CHAT_PACKET = 36;
    private final static int CHANGE_FRIEND_CHAT_PACKET = 22;
    private final static int KICK_FRIEND_CHAT_PACKET = 74;
    private final static int REMOVE_FRIEND_PACKET = 24;
    private final static int SEND_FRIEND_MESSAGE_PACKET = 82;
    private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 0;
    private final static int OBJECT_CLICK1_PACKET = 26;
    private final static int OBJECT_CLICK2_PACKET = 59;
    private final static int OBJECT_CLICK3_PACKET = 40;
    private final static int OBJECT_CLICK4_PACKET = 23;
    private final static int OBJECT_CLICK5_PACKET = 80;
    private final static int OBJECT_EXAMINE_PACKET = 25;
    private final static int NPC_CLICK1_PACKET = 31;
    private final static int NPC_CLICK2_PACKET = 101;
    private final static int NPC_CLICK3_PACKET = 34;
    private final static int NPC_CLICK4_PACKET = 65;
    private final static int ATTACK_NPC = 20;
    private final static int PLAYER_OPTION_1_PACKET = 42;
    private final static int PLAYER_OPTION_2_PACKET = 46;
    private final static int PLAYER_OPTION_6_PACKET = 49;
    private final static int ITEM_TAKE_PACKET = 57;
    private final static int DIALOGUE_CONTINUE_PACKET = 72;
    private final static int ENTER_INTEGER_PACKET = 81;
    private final static int ENTER_NAME_PACKET = 29;
    private final static int ENTER_STRING_PACKET = -1;
    private final static int SWITCH_INTERFACE_ITEM_PACKET = 76;
    private final static int INTERFACE_ON_PLAYER = 50;
    private final static int INTERFACE_ON_NPC = 66;
    private final static int COLOR_ID_PACKET = 97;
    private static final int NPC_EXAMINE_PACKET = 9;
    private final static int REPORT_ABUSE_PACKET = -1;
    private final static int ENTER_LONG_TEXT_PACKET = 48;

    private static final byte[] PACKET_SIZES = {-1, -2, -1, 16, -1, 8, 8, 3, -1, 3, -1, -1, -1, 7, 8, 6, 2, 3, -1, -2, 3, 0, -1, 9, -1, 9, 9, 8, 4, -1, 0, 3, 8, 4, 3, -1, -1, 17, 4, 4, 9, -1, 3, 7, -2, 7, 3, 4, -1, 3, 11, 3, -1, -1, 0, 8, 3, 7, -1, 9, -1, 7, 7, 12, 4, 3, 11, 8, 8, 15, 1, 2, 6, -1, -1, -2, 16, 3, 1, 3, 9, 4, -2, 1, 1, 3, -1, 4, 3, -1, 8, -2, -1, -1, 9, -2, 8, 2, 6, 2, -2, 3, 7, 4};

    private final Player player;
    private int chatType;

    public WorldPacketsDecoder(final Session session, final Player player) {
        super(session);
        this.player = player;
    }

    public static void decodeLogicPacket(final Player player,
                                         final LogicPacket packet) {
        final int packetId = packet.getId();
        final InputStream stream = new InputStream(packet.getData());
        if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            final long currentTime = Utils.currentTimeMillis();
            if (player.getLockDelay() > currentTime)
                return;
            if (player.getFreezeDelay() >= currentTime) {
                player.getPackets().sendGameMessage(
                        "A magical force prevents you from moving.");
                return;
            }
            GildedAltar.bonestoOffer.stopOfferGod = true;
            final int length = stream.getLength();
            /*
             * if (packetId == MINI_WALKING_PACKET) length -= 13;
             */
            final int baseX = stream.readUnsignedShort128();
            final boolean forceRun = stream.readUnsigned128Byte() == 1;
            final int baseY = stream.readUnsignedShort128();
            int steps = (length - 5) / 2;
            if (steps > 25) {
                steps = 25;
            }
            player.stopAll();
            if (forceRun) {
                player.setRun(forceRun);
            }
            for (int step = 0; step < steps; step++)
                if (!player.addWalkSteps(baseX + stream.readUnsignedByte(),
                        baseY + stream.readUnsignedByte(), 25, true)) {
                    break;
                }
        } else if (packetId == PLAYER_OPTION_7_PACKET) {
            boolean unknown = stream.readByte() == 1;
            int playerIndex = stream.readUnsignedShortLE128();
            Player target = World.getPlayers().get(playerIndex);
            if (target != null) {
                SnowBalls.handleSnowBallPacket(player, target);
            }
        } else if (packetId == INTERFACE_ON_OBJECT) {
            final boolean forceRun = stream.readByte128() == 1;
            final int itemId = stream.readShortLE128();
            final int y = stream.readShortLE128();
            final int objectId = stream.readIntV2();
            final int interfaceHash = stream.readInt();
            final int interfaceId = interfaceHash >> 16;
            final int slot = stream.readShortLE();
            final int x = stream.readShort128();
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            final long currentTime = Utils.currentTimeMillis();
            if (player.getLockDelay() >= currentTime
                    || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
                return;
            final WorldTile tile = new WorldTile(x, y, player.getPlane());
            final int regionId = tile.getRegionId();
            if (!player.getMapRegionsIds().contains(regionId))
                return;
            final WorldObject mapObject = World.getRegion(regionId).getObject(
                    objectId, tile);
            if (mapObject == null || mapObject.getId() != objectId)
                return;
            final WorldObject object = !player.isAtDynamicRegion() ? mapObject
                    : new WorldObject(objectId, mapObject.getType(),
                    mapObject.getRotation(), x, y, player.getPlane());
            final Item item = player.getInventory().getItem(slot);
            if (player.isDead()
                    || Utils.getInterfaceDefinitionsSize() <= interfaceId)
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            if (!player.getInterfaceManager().containsInterface(interfaceId))
                return;
            if (item == null || item.getId() != itemId)
                return;
            player.stopAll(false); // false
            if (forceRun) {
                player.setRun(forceRun);
            }
            switch (interfaceId) {
                case Inventory.INVENTORY_INTERFACE: // inventory
                    PacketHandlerManager.get(ItemOnObjectHandler.class).process(player, object, interfaceId, item);
                    break;
            }
        } else if (packetId == PLAYER_OPTION_2_PACKET) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            @SuppressWarnings("unused") final boolean unknown = stream.readByte() == 1;
            final int playerIndex = stream.readUnsignedShortLE128();
            final Player p2 = World.getPlayers().get(playerIndex);
            if (p2 == null || p2.isDead() || p2.hasFinished()
                    || !player.getMapRegionsIds().contains(p2.getRegionId()))
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            player.stopAll(false);
            player.getActionManager().setAction(new PlayerFollow(p2));
        } else if (packetId == PLAYER_OPTION_4_PACKET) {
            @SuppressWarnings("unused") final boolean unknown = stream.readByte() == 1;
            final int playerIndex = stream.readUnsignedShortLE128();
            final Player p2 = World.getPlayers().get(playerIndex);
            if (p2 == null || p2.isDead() || p2.hasFinished()
                    || !player.getMapRegionsIds().contains(p2.getRegionId()))
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            player.stopAll(false);
            if (player.isCantTrade()) {
                player.getPackets().sendGameMessage("You are busy.");
                return;
            }
            if (p2.getInterfaceManager().containsScreenInter()
                    || p2.isCantTrade()) {
                player.getPackets()
                        .sendGameMessage("The other player is busy.");
                return;
            }
            if (!p2.withinDistance(player, 14)) {
                player.getPackets().sendGameMessage(
                        "Unable to find target " + p2.getDisplayName());
                return;
            }
            int total = 0;
            final int reqTotal = 400;
            for (int i = 0; i < 25; i++) {
                total += player.getSkills().getLevel(i);
            }
            if (total < reqTotal) {
                player.sendMessage("You must have a total level of 400 to trade!");
                return;
            }

            if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
                p2.getTemporaryAttributtes().remove("TradeTarget");
                player.getTrade().openTrade(p2);
                p2.getTrade().openTrade(player);
                return;
            }
            player.getTemporaryAttributtes().put("TradeTarget", p2);
            player.getPackets().sendGameMessage(
                    "Sending " + p2.getDisplayName() + " a request...");
            p2.getPackets().sendTradeRequestMessage(player);
        } else if (packetId == PLAYER_OPTION_6_PACKET) {
            @SuppressWarnings("unused") final boolean unknown = stream.readByte() == 1;
            final int playerIndex = stream.readUnsignedShortLE128();
            final Player other = World.getPlayers().get(playerIndex);
            if (other == null || other.isDead() || other.hasFinished()
                    || !player.getMapRegionsIds().contains(other.getRegionId()))
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            if (player.getInterfaceManager().containsScreenInter()) {
                player.getPackets()
                        .sendGameMessage("The other player is busy.");
                return;
            }
            if (!other.withinDistance(player, 14)) {
                player.getPackets().sendGameMessage(
                        "Unable to find target " + other.getDisplayName());
                return;
            }
            if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
                player.getPackets()
                        .sendGameMessage(
                                "<col=B00000>You view "
                                        + other.getDisplayName()
                                        + " Stats's until 10 seconds after the end of combat.");
                return;
            }
            player.getInterfaceManager().sendInterface(1314);
            player.getPackets().sendIComponentText(1314, 91,
                    "" + other.getDisplayName() + "'s stats");
            player.getPackets().sendIComponentText(1314, 90, "");
            player.getPackets().sendIComponentText(1314, 61,
                    "" + other.getSkills().getLevel(0));// attack
            player.getPackets().sendIComponentText(1314, 62,
                    "" + other.getSkills().getLevel(2)); // str
            player.getPackets().sendIComponentText(1314, 63,
                    "" + other.getSkills().getLevel(1)); // def
            player.getPackets().sendIComponentText(1314, 65,
                    "" + other.getSkills().getLevel(4)); // range
            player.getPackets().sendIComponentText(1314, 66,
                    "" + other.getSkills().getLevel(5)); // prayer
            player.getPackets().sendIComponentText(1314, 64,
                    "" + other.getSkills().getLevel(6)); // mage
            player.getPackets().sendIComponentText(1314, 78,
                    "" + other.getSkills().getLevel(20)); // rc
            player.getPackets().sendIComponentText(1314, 81,
                    "" + other.getSkills().getLevel(22)); // construction
            player.getPackets().sendIComponentText(1314, 76,
                    "" + other.getSkills().getLevel(24)); // dung
            player.getPackets().sendIComponentText(1314, 82,
                    "" + other.getSkills().getLevel(3)); // hitpoints
            player.getPackets().sendIComponentText(1314, 83,
                    "" + other.getSkills().getLevel(16)); // agiality
            player.getPackets().sendIComponentText(1314, 84,
                    "" + other.getSkills().getLevel(15)); // herblore
            player.getPackets().sendIComponentText(1314, 80,
                    "" + other.getSkills().getLevel(17)); // thiving
            player.getPackets().sendIComponentText(1314, 70,
                    "" + other.getSkills().getLevel(12)); // crafting
            player.getPackets().sendIComponentText(1314, 85,
                    "" + other.getSkills().getLevel(9)); // fletching
            player.getPackets().sendIComponentText(1314, 77,
                    "" + other.getSkills().getLevel(18)); // slayer
            player.getPackets().sendIComponentText(1314, 79,
                    "" + other.getSkills().getLevel(21)); // hunter
            player.getPackets().sendIComponentText(1314, 68,
                    "" + other.getSkills().getLevel(14)); // mining
            player.getPackets().sendIComponentText(1314, 69,
                    "" + other.getSkills().getLevel(13)); // smithing
            player.getPackets().sendIComponentText(1314, 74,
                    "" + other.getSkills().getLevel(10)); // fishing
            player.getPackets().sendIComponentText(1314, 75,
                    "" + other.getSkills().getLevel(7)); // cooking
            player.getPackets().sendIComponentText(1314, 73,
                    "" + other.getSkills().getLevel(11)); // firemaking
            player.getPackets().sendIComponentText(1314, 71,
                    "" + other.getSkills().getLevel(8)); // wc
            player.getPackets().sendIComponentText(1314, 72,
                    "" + other.getSkills().getLevel(19)); // farming
            player.getPackets().sendIComponentText(1314, 67,
                    "" + other.getSkills().getLevel(23)); // summining
            player.getPackets()
                    .sendIComponentText(1314, 30, "Domion tower KC:"); // boss
            player.getPackets().sendIComponentText(1314, 60,
                    "" + other.getDominionTower().getKilledBossesCount()); // boss
            player.getPackets().sendIComponentText(1314, 87,
                    "" + other.getMaxHitpoints()); // hitpoints
            player.getPackets().sendIComponentText(1314, 86,
                    "" + other.getSkills().getCombatLevelWithSummoning()); // combatlevel
            player.getPackets().sendIComponentText(1314, 88,
                    "" + other.getSkills().getTotalLevel()); // total level
            player.getPackets().sendIComponentText(1314, 89,
                    "" + other.getSkills().getTotalXpString()); // total level
            player.getTemporaryAttributtes().put("finding_player",
                    Boolean.FALSE);
        } else if (packetId == PLAYER_OPTION_1_PACKET) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            @SuppressWarnings("unused") final boolean unknown = stream.readByte() == 1;
            final int playerIndex = stream.readUnsignedShortLE128();
            final Player p2 = World.getPlayers().get(playerIndex);
            if (p2 == null || p2.isDead() || p2.hasFinished()
                    || !player.getMapRegionsIds().contains(p2.getRegionId()))
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis()
                    || !player.getControllerManager().canPlayerOption1(p2))
                return;
            if (!player.isCanPvp())
                return;
            if (!player.getControllerManager().canAttack(p2))
                return;

            if (!player.isCanPvp() || !p2.isCanPvp()) {
                player.getPackets()
                        .sendGameMessage(
                                "You can only attack players in a player-vs-player area.");
                return;
            }
            if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
                if (player.getAttackedBy() != p2
                        && player.getAttackedByDelay() > Utils
                        .currentTimeMillis()) {
                    player.getPackets().sendGameMessage(
                            "You are already in combat.");
                    return;
                }
                if (p2.getAttackedBy() != player
                        && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
                    if (p2.getAttackedBy() instanceof NPC) {
                        p2.setAttackedBy(player); // changes enemy to player,
                        // player has priority over
                        // npc on single areas
                    } else {
                        player.getPackets().sendGameMessage(
                                "That player is already in combat.");
                        return;
                    }
                }
            }
            player.stopAll(false);
            player.getActionManager().setAction(new PlayerCombat(p2));
        } else if (packetId == PLAYER_OPTION_9_PACKET) {
            boolean forceRun = stream.readUnsignedByte() == 1;
            int playerIndex = stream.readUnsignedShortLE128();
            Player p2 = World.getPlayers().get(playerIndex);
            if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
                    || !player.getMapRegionsIds().contains(p2.getRegionId()))
                return;
            if (player.isLocked())
                return;
            if (forceRun)
                player.setRun(forceRun);
            player.stopAll();
            ClansManager.viewInvite(player, p2);
        } else if (packetId == PLAYER_OPTION_8_PACKET) {
            boolean unknown = stream.readUnsignedByte() == 1;
            int playerIndex = stream.readUnsignedShortLE128();
            Player target = World.getPlayers().get(playerIndex);
            StaffInterface.sendInterface(player, target);
        } else if (packetId == ATTACK_NPC) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            final int npcIndex = stream.readUnsignedShort128();
            final boolean forceRun = stream.read128Byte() == 1;
            if (forceRun) {
                player.setRun(forceRun);
            }
            final NPC npc = World.getNPCs().get(npcIndex);
            if (npc == null || npc.isDead() || npc.hasFinished()
                    || !player.getMapRegionsIds().contains(npc.getRegionId())
                    || !npc.getDefinitions().hasAttackOption())
                return;
            if (!player.getControllerManager().canAttack(npc))
                return;
            if (npc instanceof Familiar) {
                final Familiar familiar = (Familiar) npc;
                if (familiar == player.getFamiliar()) {
                    player.getPackets().sendGameMessage(
                            "You can't attack your own familiar.");
                    return;
                }
                if (!familiar.canAttack(player)) {
                    player.getPackets().sendGameMessage(
                            "You can't attack this npc.");
                    return;
                }
            } else if (!npc.isForceMultiAttacked()) {
                if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
                    if (player.getAttackedBy() != npc
                            && player.getAttackedByDelay() > Utils
                            .currentTimeMillis()) {
                        player.getPackets().sendGameMessage(
                                "You are already in combat.");
                        return;
                    }
                    if (npc.getAttackedBy() != player
                            && npc.getAttackedByDelay() > Utils
                            .currentTimeMillis()) {
                        player.getPackets().sendGameMessage(
                                "This npc is already in combat.");
                        return;
                    }
                }
            }
            player.stopAll(false);
            player.getActionManager().setAction(new PlayerCombat(npc));
        } else if (packetId == INTERFACE_ON_PLAYER) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            @SuppressWarnings("unused") final int junk1 = stream.readUnsignedShort();
            final int playerIndex = stream.readUnsignedShort();
            final int interfaceHash = stream.readIntV2();
            @SuppressWarnings("unused") final int junk2 = stream.readUnsignedShortLE128();
            @SuppressWarnings("unused") final boolean unknown = stream.read128Byte() == 1;
            final int interfaceId = interfaceHash >> 16;
            int componentId = interfaceHash - (interfaceId << 16);
            if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
                return;
            if (!player.getInterfaceManager().containsInterface(interfaceId))
                return;
            if (componentId == 65535) {
                componentId = -1;
            }
            if (componentId != -1
                    && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
                return;
            final Player p2 = World.getPlayers().get(playerIndex);
            if (p2 == null || p2.isDead() || p2.hasFinished()
                    || !player.getMapRegionsIds().contains(p2.getRegionId()))
                return;
            player.stopAll(false);
            switch (interfaceId) {
                case 1110:
                    if (componentId == 87)
                        ClansManager.invite(player, p2);
                    break;
                case Inventory.INVENTORY_INTERFACE:// Item on player
                    PacketHandlerManager.get(ItemOnPlayerHandler.class).process(player, p2, junk1);
                    break;
                case 662:
                case 747:
                    if (player.getFamiliar() == null)
                        return;
                    player.resetWalkSteps();
                    if ((interfaceId == 747 && componentId == 15)
                            || (interfaceId == 662 && componentId == 65)
                            || (interfaceId == 662 && componentId == 74)
                            || interfaceId == 747 && componentId == 18) {
                        if ((interfaceId == 662 && componentId == 74
                                || interfaceId == 747 && componentId == 24 || interfaceId == 747
                                && componentId == 18)) {
                            if (player.getFamiliar().getSpecialAttack() != Familiar.SpecialAttack.ENTITY)
                                return;
                        }
                        if (!player.isCanPvp() || !p2.isCanPvp()) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You can only attack players in a player-vs-player area.");
                            return;
                        }
                        if (!player.getFamiliar().canAttack(p2)) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You can only use your familiar in a multi-zone area.");
                            return;
                        } else {
                            player.getFamiliar().setSpecial(
                                    interfaceId == 662 && componentId == 74
                                            || interfaceId == 747
                                            && componentId == 18);
                            player.getFamiliar().setTarget(p2);
                        }
                    }
                    break;
                case 193:
                    switch (componentId) {
                        case 28:
                        case 32:
                        case 24:
                        case 20:
                        case 30:
                        case 34:
                        case 26:
                        case 22:
                        case 29:
                        case 33:
                        case 25:
                        case 21:
                        case 31:
                        case 35:
                        case 27:
                        case 23:
                            if (Magic.checkCombatSpell(player, componentId, 1, false)) {
                                player.setNextFaceWorldTile(new WorldTile(p2
                                        .getCoordFaceX(p2.getSize()), p2
                                        .getCoordFaceY(p2.getSize()), p2.getPlane()));
                                if (!player.getControllerManager().canAttack(p2))
                                    return;
                                if (!player.isCanPvp() || !p2.isCanPvp()) {
                                    player.getPackets()
                                            .sendGameMessage(
                                                    "You can only attack players in a player-vs-player area.");
                                    return;
                                }
                                if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
                                    if (player.getAttackedBy() != p2
                                            && player.getAttackedByDelay() > Utils
                                            .currentTimeMillis()) {
                                        player.getPackets()
                                                .sendGameMessage(
                                                        "That "
                                                                + (player
                                                                .getAttackedBy() instanceof Player ? "player"
                                                                : "npc")
                                                                + " is already in combat.");
                                        return;
                                    }
                                    if (p2.getAttackedBy() != player
                                            && p2.getAttackedByDelay() > Utils
                                            .currentTimeMillis()) {
                                        if (p2.getAttackedBy() instanceof NPC) {
                                            p2.setAttackedBy(player); // changes enemy
                                            // to player,
                                            // player has
                                            // priority over
                                            // npc on single
                                            // areas
                                        } else {
                                            player.getPackets()
                                                    .sendGameMessage(
                                                            "That player is already in combat.");
                                            return;
                                        }
                                    }
                                }
                                player.getActionManager().setAction(
                                        new PlayerCombat(p2));
                            }
                            break;
                    }
                case 192:
                    switch (componentId) {
                        case 25: // air strike
                        case 28: // water strike
                        case 30: // earth strike
                        case 32: // fire strike
                        case 34: // air bolt
                        case 39: // water bolt
                        case 42: // earth bolt
                        case 45: // fire bolt
                        case 49: // air blast
                        case 52: // water blast
                        case 58: // earth blast
                        case 63: // fire blast
                        case 70: // air wave
                        case 73: // water wave
                        case 77: // earth wave
                        case 80: // fire wave
                        case 86: // teleblock
                        case 84: // air surge
                        case 87: // water surge
                        case 89: // earth surge
                        case 91: // fire surge
                        case 99: // storm of armadyl
                        case 36: // bind
                        case 66: // Sara Strike
                        case 67: // Guthix Claws
                        case 68: // Flame of Zammy
                        case 55: // snare
                        case 81: // entangle
                            if (Magic.checkCombatSpell(player, componentId, 1, false)) {
                                player.setNextFaceWorldTile(new WorldTile(p2
                                        .getCoordFaceX(p2.getSize()), p2
                                        .getCoordFaceY(p2.getSize()), p2.getPlane()));
                                if (!player.getControllerManager().canAttack(p2))
                                    return;
                                if (!player.isCanPvp() || !p2.isCanPvp()) {
                                    player.getPackets()
                                            .sendGameMessage(
                                                    "You can only attack players in a player-vs-player area.");
                                    return;
                                }
                                if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
                                    if (player.getAttackedBy() != p2
                                            && player.getAttackedByDelay() > Utils
                                            .currentTimeMillis()) {
                                        player.getPackets()
                                                .sendGameMessage(
                                                        "That "
                                                                + (player
                                                                .getAttackedBy() instanceof Player ? "player"
                                                                : "npc")
                                                                + " is already in combat.");
                                        return;
                                    }
                                    if (p2.getAttackedBy() != player
                                            && p2.getAttackedByDelay() > Utils
                                            .currentTimeMillis()) {
                                        if (p2.getAttackedBy() instanceof NPC) {
                                            p2.setAttackedBy(player); // changes enemy
                                            // to player,
                                            // player has
                                            // priority over
                                            // npc on single
                                            // areas
                                        } else {
                                            player.getPackets()
                                                    .sendGameMessage(
                                                            "That player is already in combat.");
                                            return;
                                        }
                                    }
                                }
                                player.getActionManager().setAction(
                                        new PlayerCombat(p2));
                            }
                            break;
                    }
                    break;
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                System.out.println("Spell:" + componentId);
            }
        } else if (packetId == INTERFACE_ON_NPC) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            if (player.getLockDelay() > Utils.currentTimeMillis())
                return;
            @SuppressWarnings("unused") final boolean unknown = stream.readByte() == 1;
            final int interfaceHash = stream.readInt();
            final int npcIndex = stream.readUnsignedShortLE();
            final int interfaceSlot = stream.readUnsignedShortLE128();
            @SuppressWarnings("unused") final int junk2 = stream.readUnsignedShortLE();
            final int interfaceId = interfaceHash >> 16;
            int componentId = interfaceHash - (interfaceId << 16);
            if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
                return;
            if (!player.getInterfaceManager().containsInterface(interfaceId))
                return;
            if (componentId == 65535) {
                componentId = -1;
            }
            if (componentId != -1
                    && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
                return;
            final NPC npc = World.getNPCs().get(npcIndex);
            if (npc == null || npc.isDead() || npc.hasFinished()
                    || !player.getMapRegionsIds().contains(npc.getRegionId()))
                return;
            player.stopAll(false);
            if (interfaceId != Inventory.INVENTORY_INTERFACE) {
                if (!npc.getDefinitions().hasAttackOption()) {
                    player.getPackets().sendGameMessage(
                            "You can't attack this npc.");
                    return;
                }
            }
            switch (interfaceId) {
                case Inventory.INVENTORY_INTERFACE:
                    final Item item = player.getInventory().getItem(interfaceSlot);
                    if (item == null
                            || !player.getControllerManager().processItemOnNPC(npc,
                            item))
                        return;
                    PacketHandlerManager.get(ItemOnNpcHandler.class).process(player, npc, item);
                    break;
                case 1165:
                    Summoning.attackDreadnipTarget(npc, player);
                    break;
                case 662:
                case 747:
                    if (player.getFamiliar() == null)
                        return;
                    player.resetWalkSteps();
                    if ((interfaceId == 747 && componentId == 15)
                            || (interfaceId == 662 && componentId == 65)
                            || (interfaceId == 662 && componentId == 74)
                            || interfaceId == 747 && componentId == 18
                            || interfaceId == 747 && componentId == 24) {
                        if ((interfaceId == 662 && componentId == 74 || interfaceId == 747
                                && componentId == 18)) {
                            if (player.getFamiliar().getSpecialAttack() != Familiar.SpecialAttack.ENTITY)
                                return;
                        }
                        if (npc instanceof Familiar) {
                            final Familiar familiar = (Familiar) npc;
                            if (familiar == player.getFamiliar()) {
                                player.getPackets().sendGameMessage(
                                        "You can't attack your own familiar.");
                                return;
                            }
                            if (!player.getFamiliar()
                                    .canAttack(familiar.getOwner())) {
                                player.getPackets()
                                        .sendGameMessage(
                                                "You can only attack players in a player-vs-player area.");
                                return;
                            }
                        }
                        if (!player.getFamiliar().canAttack(npc)) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You can only use your familiar in a multi-zone area.");
                            return;
                        } else {
                            player.getFamiliar().setSpecial(
                                    interfaceId == 662 && componentId == 74
                                            || interfaceId == 747
                                            && componentId == 18);
                            player.getFamiliar().setTarget(npc);
                        }
                    }
                    break;
                case 193:
                    switch (componentId) {
                        case 28:
                        case 32:
                        case 24:
                        case 20:
                        case 30:
                        case 34:
                        case 26:
                        case 22:
                        case 29:
                        case 33:
                        case 25:
                        case 21:
                        case 31:
                        case 35:
                        case 27:
                        case 23:
                            if (Magic.checkCombatSpell(player, componentId, 1, false)) {
                                player.setNextFaceWorldTile(new WorldTile(npc
                                        .getCoordFaceX(npc.getSize()), npc
                                        .getCoordFaceY(npc.getSize()), npc.getPlane()));
                                if (!player.getControllerManager().canAttack(npc))
                                    return;
                                if (npc instanceof Familiar) {
                                    final Familiar familiar = (Familiar) npc;
                                    if (familiar == player.getFamiliar()) {
                                        player.getPackets().sendGameMessage(
                                                "You can't attack your own familiar.");
                                        return;
                                    }
                                    if (!familiar.canAttack(player)) {
                                        player.getPackets().sendGameMessage(
                                                "You can't attack this npc.");
                                        return;
                                    }
                                } else if (!npc.isForceMultiAttacked()) {
                                    if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
                                        if (player.getAttackedBy() != npc
                                                && player.getAttackedByDelay() > Utils
                                                .currentTimeMillis()) {
                                            player.getPackets().sendGameMessage(
                                                    "You are already in combat.");
                                            return;
                                        }
                                        if (npc.getAttackedBy() != player
                                                && npc.getAttackedByDelay() > Utils
                                                .currentTimeMillis()) {
                                            player.getPackets().sendGameMessage(
                                                    "This npc is already in combat.");
                                            return;
                                        }
                                    }
                                }
                                player.getActionManager().setAction(
                                        new PlayerCombat(npc));
                            }
                            break;
                    }
                case 192:
                    switch (componentId) {
                        case 25: // air strike
                        case 28: // water strike
                        case 30: // earth strike
                        case 32: // fire strike
                        case 34: // air bolt
                        case 39: // water bolt
                        case 42: // earth bolt
                        case 45: // fire bolt
                        case 49: // air blast
                        case 52: // water blast
                        case 58: // earth blast
                        case 63: // fire blast
                        case 70: // air wave
                        case 73: // water wave
                        case 77: // earth wave
                        case 80: // fire wave
                        case 84: // air surge
                        case 87: // water surge
                        case 89: // earth surge
                        case 66: // Sara Strike
                        case 67: // Guthix Claws
                        case 68: // Flame of Zammy
                        case 93:
                        case 91: // fire surge
                        case 99: // storm of Armadyl
                        case 36: // bind
                        case 55: // snare
                        case 81: // entangle
                            if (Magic.checkCombatSpell(player, componentId, 1, false)) {
                                player.setNextFaceWorldTile(new WorldTile(npc
                                        .getCoordFaceX(npc.getSize()), npc
                                        .getCoordFaceY(npc.getSize()), npc.getPlane()));
                                if (!player.getControllerManager().canAttack(npc))
                                    return;
                                if (npc instanceof Familiar) {
                                    final Familiar familiar = (Familiar) npc;
                                    if (familiar == player.getFamiliar()) {
                                        player.getPackets().sendGameMessage(
                                                "You can't attack your own familiar.");
                                        return;
                                    }
                                    if (!familiar.canAttack(player)) {
                                        player.getPackets().sendGameMessage(
                                                "You can't attack this npc.");
                                        return;
                                    }
                                } else if (!npc.isForceMultiAttacked()) {
                                    if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
                                        if (player.getAttackedBy() != npc
                                                && player.getAttackedByDelay() > Utils
                                                .currentTimeMillis()) {
                                            player.getPackets().sendGameMessage(
                                                    "You are already in combat.");
                                            return;
                                        }
                                        if (npc.getAttackedBy() != player
                                                && npc.getAttackedByDelay() > Utils
                                                .currentTimeMillis()) {
                                            player.getPackets().sendGameMessage(
                                                    "This npc is already in combat.");
                                            return;
                                        }
                                    }
                                }
                                player.getActionManager().setAction(
                                        new PlayerCombat(npc));
                            }
                            break;
                    }
                    break;
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                System.out.println("Spell:" + componentId);
            }
        } else if (packetId == NPC_CLICK1_PACKET) {
            NPCHandler.handleOption1(player, stream);
        } else if (packetId == NPC_CLICK2_PACKET) {
            NPCHandler.handleOption2(player, stream);
        } else if (packetId == NPC_CLICK3_PACKET) {
            NPCHandler.handleOption3(player, stream);
        } else if (packetId == NPC_CLICK4_PACKET) {
            NPCHandler.handleOption4(player, stream);
        } else if (packetId == OBJECT_CLICK1_PACKET) {
            ObjectHandler.handleOption(player, stream, 1);
        } else if (packetId == OBJECT_CLICK2_PACKET) {
            ObjectHandler.handleOption(player, stream, 2);
        } else if (packetId == OBJECT_CLICK3_PACKET) {
            ObjectHandler.handleOption(player, stream, 3);
        } else if (packetId == OBJECT_CLICK4_PACKET) {
            ObjectHandler.handleOption(player, stream, 4);
        } else if (packetId == OBJECT_CLICK5_PACKET) {
            ObjectHandler.handleOption(player, stream, 5);
        } else if (packetId == ITEM_TAKE_PACKET) {
            if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
                    || player.isDead())
                return;
            final long currentTime = Utils.currentTimeMillis();
            if (player.getLockDelay() > currentTime)
                // || player.getFreezeDelay() >= currentTime)
                return;
            final int y = stream.readUnsignedShort();
            final int x = stream.readUnsignedShortLE();
            final int id = stream.readUnsignedShort();
            final boolean forceRun = stream.read128Byte() == 1;
            final WorldTile tile = new WorldTile(x, y, player.getPlane());
            final int regionId = tile.getRegionId();
            if (!player.getMapRegionsIds().contains(regionId))
                return;
            final FloorItem item = World.getRegion(regionId).getGroundItem(id,
                    tile, player);
            if (item == null)
                return;
            player.stopAll(false);
            if (forceRun) {
                player.setRun(forceRun);
            }
            player.setCoordsEvent(new CoordsEvent(tile, () -> {
                final FloorItem item1 = World.getRegion(regionId).getGroundItem(id, tile, player);
                if (item1 == null)
                    return;
                player.setNextFaceWorldTile(tile);
                player.addWalkSteps(tile.getX(), tile.getY(), 1);
                World.removeGroundItem(player, item1);
            }, 1, 1));
        }
    }

    private String getFormattedNumber(final int amount) {
        return new DecimalFormat("#,###,##0").format(amount);
    }

    @Override
    public void decode(final InputStream stream) {
        while (stream.getRemaining() > 0 && session.getChannel().isConnected()
                && !player.hasFinished()) {
            final int packetId = stream.readPacket(player);
            if (packetId >= PACKET_SIZES.length || packetId < 0) {
                if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                    System.out.println("PacketId " + packetId
                            + " has fake packet id.");
                }
                break;
            }
            int length = PACKET_SIZES[packetId];
            if (length == -1) {
                length = stream.readUnsignedByte();
            } else if (length == -2) {
                length = stream.readUnsignedShort();
            } else if (length == -3) {
                length = stream.readInt();
            } else if (length == -4) {
                length = stream.getRemaining();
                if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                    System.out.println("Invalid size for PacketId " + packetId
                            + ". Size guessed to be " + length);
                }
            }
            if (length > stream.getRemaining()) {
                length = stream.getRemaining();
                if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                    System.out.println("PacketId " + packetId
                            + " has fake size. - expected size " + length);
                    // break;
                }

            }
            /*
             * System.out.println("PacketId " +packetId+
             * " has . - expected size " +length);
             */
            final int startOffset = stream.getOffset();
            processPackets(packetId, stream, length);
            stream.setOffset(startOffset + length);
        }
    }

    public void processPackets(final int packetId, final InputStream stream,
                               final int length) {
        player.setPacketsDecoderPing(Utils.currentTimeMillis());
        if (packetId == PING_PACKET) {
            // kk we ping :)
        } else if (packetId == MOVE_MOUSE_PACKET) {
            // USELESS PACKET
        } else if (packetId == KEY_TYPED_PACKET) {
            // USELESS PACKET
        } else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
            // interface packets
            stream.readInt();
        } else if (packetId == ITEM_ON_ITEM_PACKET) {
            PacketHandlerManager.get(ItemOnItemHandler.class).process(player, stream);
        } else if (packetId == MAGIC_ON_ITEM_PACKET) {
            final int inventoryInter = stream.readInt() >> 16;
            final int itemId = stream.readShort128();
            @SuppressWarnings("unused") final int junk = stream.readShort();
            @SuppressWarnings("unused") final int itemSlot = stream.readShortLE();
            final int interfaceSet = stream.readIntV1();
            final int spellId = interfaceSet & 0xFFF;
            final int magicInter = interfaceSet >> 16;
            if (inventoryInter == 149 && magicInter == 192) {
                switch (spellId) {
                    case 59:// High Alch
                        if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "You do not have the required level to cast this spell.");
                            return;
                        }
                        if (itemId == 995) {
                            player.getPackets().sendGameMessage(
                                    "You can't alch this!");
                            return;
                        }
                        if (player.getEquipment().getWeaponId() == 1401
                                || player.getEquipment().getWeaponId() == 3054
                                || player.getEquipment().getWeaponId() == 19323) {
                            if (!player.getInventory().containsItem(561, 1)) {
                                player.getPackets()
                                        .sendGameMessage(
                                                "You do not have the required runes to cast this spell.");
                                return;
                            }
                            player.setNextAnimation(new Animation(9633));
                            player.setNextGraphics(new Graphics(112));
                            player.getInventory().deleteItem(561, 1);
                            player.getInventory().deleteItem(itemId, 1);
                            player.getInventory()
                                    .addItem(
                                            995,
                                            new Item(itemId, 1).getDefinitions()
                                                    .getGEPrice() >> 6);
                        } else {
                            if (!player.getInventory().containsItem(561, 1)
                                    || !player.getInventory().containsItem(554, 5)) {
                                player.getPackets()
                                        .sendGameMessage(
                                                "You do not have the required runes to cast this spell.");
                                return;
                            }
                            player.setNextAnimation(new Animation(713));
                            player.setNextGraphics(new Graphics(113));
                            player.getInventory().deleteItem(561, 1);
                            player.getInventory().deleteItem(554, 5);
                            player.getInventory().deleteItem(itemId, 1);
                            player.getInventory()
                                    .addItem(
                                            995,
                                            new Item(itemId, 1).getDefinitions()
                                                    .getGEPrice() >> 6);
                        }
                        break;
                    default:
                        System.out.println("Spell:" + spellId + ", Item:" + itemId);
                }
                System.out.println("Spell:" + spellId + ", Item:" + itemId);
            }
        } else if (packetId == AFK_PACKET) {
            player.getSession().getChannel().close();
        } else if (packetId == CLOSE_INTERFACE_PACKET) {
            if (player.hasStarted() && !player.hasFinished()
                    && !player.isRunning()) { // used for old welcome screen
                player.run();
                return;
            }
            player.stopAll();
        } else if (packetId == GRAND_EXCHANGE_ITEM_SELECT) {
            int itemId = stream.readUnsignedShort();
            player.getGeManager().chooseItem(itemId);
        } else if (packetId == MOVE_CAMERA_PACKET) {
            // not using it atm
            stream.readUnsignedShort();
            stream.readUnsignedShort();
        } else if (packetId == IN_OUT_SCREEN_PACKET) {
            // not using this check because not 100% efficient
            @SuppressWarnings("unused") final boolean inScreen = stream.readByte() == 1;
        } else if (packetId == SCREEN_PACKET) {
            final int displayMode = stream.readUnsignedByte();
            player.setScreenWidth(stream.readUnsignedShort());
            player.setScreenHeight(stream.readUnsignedShort());
            @SuppressWarnings("unused") final boolean switchScreenMode = stream.readUnsignedByte() == 1;
            if (!player.hasStarted() || player.hasFinished()
                    || displayMode == player.getDisplayMode()
                    || !player.getInterfaceManager().containsInterface(742))
                return;
            player.setDisplayMode(displayMode);
            player.getInterfaceManager().removeAll();
            player.getInterfaceManager().sendInterfaces();
            player.getInterfaceManager().sendInterface(742);
        } else if (packetId == CLICK_PACKET) {
            final int mouseHash = stream.readShortLE128();
            final int mouseButton = mouseHash >> 15;
            final int time = mouseHash - (mouseButton << 15); // time
            final int positionHash = stream.readIntV1();
            final int y = positionHash >> 16; // y;
            final int x = positionHash - (y << 16); // x
            @SuppressWarnings("unused")
            boolean clicked;
            // mass click or stupid autoclicker, lets stop lagg
            if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0
                    || y > player.getScreenHeight()) {
                // player.getSession().getChannel().close();
                clicked = false;
                return;
            }
            clicked = true;
        } else if (packetId == DIALOGUE_CONTINUE_PACKET) {
            final int interfaceHash = stream.readInt();
            final int junk = stream.readShort128();
            final int interfaceId = interfaceHash >> 16;
            final int buttonId = (interfaceHash & 0xFF);
            if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
                // hack, or server error or client error
                // player.getSession().getChannel().close();
                return;
            if (!player.isRunning()
                    || !player.getInterfaceManager().containsInterface(
                    interfaceId))
                return;
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info(this, "Dialogue: " + interfaceId + ", " + buttonId
                        + ", " + junk);
            }
            final int componentId = interfaceHash - (interfaceId << 16);
            player.getDialogueManager().continueDialogue(interfaceId,
                    componentId);
        } else if (packetId == WORLD_MAP_CLICK) {
            final int coordinateHash = stream.readInt();
            final int x = coordinateHash >> 14;
            final int y = coordinateHash & 0x3fff;
            final int plane = coordinateHash >> 28;
            final Integer hash = (Integer) player.getTemporaryAttributtes()
                    .get("worldHash");
            if (hash == null || coordinateHash != hash) {
                player.getTemporaryAttributtes().put("worldHash",
                        coordinateHash);
            } else {
                player.getTemporaryAttributtes().remove("worldHash");
                player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2,
                        -1, true);
                player.getPackets().sendConfig(1159, coordinateHash);
            }
        } else if (packetId == ACTION_BUTTON1_PACKET
                || packetId == ACTION_BUTTON2_PACKET
                || packetId == ACTION_BUTTON4_PACKET
                || packetId == ACTION_BUTTON5_PACKET
                || packetId == ACTION_BUTTON6_PACKET
                || packetId == ACTION_BUTTON7_PACKET
                || packetId == ACTION_BUTTON8_PACKET
                || packetId == ACTION_BUTTON3_PACKET
                || packetId == ACTION_BUTTON9_PACKET
                || packetId == ACTION_BUTTON10_PACKET) {
            ButtonHandler.handleButtons(player, stream, packetId);
        } else if (packetId == ENTER_NAME_PACKET) {
            if (!player.isRunning() || player.isDead())
                return;
            final String value = stream.readString();
            if (value.equals(""))
                return;
            if (player.getInterfaceManager().containsInterface(1108)) {
                player.getFriendsIgnores().setChatPrefix(value);
            } else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
                if (value.length() != 6) {
                    player.getDialogueManager()
                            .startDialogue(SimpleMessage.class,
                                    "The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
                } else if (Utils.containsInvalidCharacter(value)
                        || value.contains("_")) {
                    player.getDialogueManager()
                            .startDialogue(SimpleMessage.class,
                                    "The requested yell color can only contain numeric and regular characters.");
                } else {
                    player.setYellColor(value);
                    player.getDialogueManager().startDialogue(
                            SimpleMessage.class,
                            "Your yell color has been changed to <col="
                                    + player.getYellColor() + ">"
                                    + player.getYellColor() + "</col>.");
                }
                player.getTemporaryAttributtes()
                        .put("yellcolor", Boolean.FALSE);
            } else if (player.getTemporaryAttributtes().get("using_cp") == Boolean.TRUE) {
                System.out.println("Handled staff action! Value: " + value);
                StaffPanelHandler.handle(player, value);
                player.getTemporaryAttributtes().put("using_cp", Boolean.FALSE);
            } else if (player.getTemporaryAttributtes().get("LocationCrystal") != null) {
                final int index = (Integer) player.getTemporaryAttributtes()
                        .remove("LocationCrystal");
                player.getLocationCrystal().saveLocation(value, index);
                return;
            } else if (player.getTemporaryAttributtes().remove("setclan") != null) {
                ClansManager.createClan(player, value);
            } else if (player.getTemporaryAttributtes().remove("joinguestclan") != null) {
                ClansManager.connectToClan(player, value, true);
            } else if (player.getTemporaryAttributtes().remove("banclanplayer") != null) {
                ClansManager.banPlayer(player, value);
            } else if (player.getTemporaryAttributtes().remove("unbanclanplayer") != null) {
                ClansManager.unbanPlayer(player, value);
            } else if (player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
                player.getTemporaryAttributtes().remove("view_name");
                final Player other = World.getPlayerByDisplayName(value);
                if (other == null) {
                    player.getPackets()
                            .sendGameMessage("Couldn't find player.");
                    return;
                }
                final ClanWars clan = other.getCurrentFriendChat() != null ? other
                        .getClanManager().getClanWars() : null;
                if (clan == null) {
                    player.getPackets().sendGameMessage(
                            "This player's clan is not in war.");
                    return;
                }
                if (player.getTemporaryAttributtes().get("RingNPC") == Boolean.TRUE) {
                    player.unlock();
                    player.getAppearance().transformIntoNPC(-1);
                    player.getTemporaryAttributtes().remove("RingNPC");
                    player.setNextAnimation(new Animation(14884));
                }
                if (!clan.getSecondTeam().getLeader().getUsername().equalsIgnoreCase(other.getClanManager().getLeader().getUsername())) {
                    player.getTemporaryAttributtes().put("view_prefix", 1);
                }
                player.getTemporaryAttributtes().put("view_clan", clan);
                ClanWars.enter(player);
            } else if (player.getTemporaryAttributtes().remove("setdisplay") != null) {
                if (Utils.invalidAccountName(Utils
                        .formatPlayerNameForProtocol(value))) {
                    player.getPackets()
                            .sendGameMessage("This name is invalid.");
                    return;
                }
                if (!Server.getInstance().getDisplayNamesFileManager().setDisplayName(player, value)) {
                    player.getPackets().sendGameMessage(
                            "This name is already in use by someone else.");
                    return;
                }
                player.getPackets().sendGameMessage(
                        "You have changed your display name.");
                player.getInventory().deleteItem(995, 50000000);
            } else if (player.getTemporaryAttributtes()
                    .remove("checkvoteinput") != null) {
                player.getPackets().sendGameMessage("Unknown action.");
            }
        } else if (packetId == ENTER_STRING_PACKET) {
            if (!player.isRunning() || player.isDead())
                return;
            final String value = stream.readString();
            if (value.equals(""))
                return;
        } else if (packetId == ENTER_LONG_TEXT_PACKET) {
            if (!player.isRunning() || player.isDead())
                return;
            String value = stream.readString();
            if (value.equals(""))
                return;
            if (player.getTemporaryAttributtes().get("using_cp") == Boolean.TRUE) {
                System.out.println("Handled staff action! Value: " + value);
                StaffPanelHandler.handle(player, value);
                player.getTemporaryAttributtes().put("using_cp", Boolean.FALSE);
            } else if (player.getTemporaryAttributtes().get("editing_note") == Boolean.TRUE) {
                final Notes.Note note = (Notes.Note) player.getTemporaryAttributtes().get("curNote");
                player.getNotes().set(new Notes.Note(value, 1), player.getNotes().getNotes().indexOf(note));
                player.getNotes().refresh();
                player.getTemporaryAttributtes().put("editing_note", Boolean.FALSE);
            } else if (player.getTemporaryAttributtes().get("entering_note") == Boolean.TRUE) {
                player.getNotes().add(new Notes.Note(value, 1));
                player.getNotes().refresh();
                player.getTemporaryAttributtes().put("entering_note", Boolean.FALSE);
            } else if (player.getInterfaceManager().containsInterface(1103))
                ClansManager.setClanMottoInterface(player, value);
        } else if (packetId == ENTER_INTEGER_PACKET) {
            if (!player.isRunning() || player.isDead())
                return;
            int value = stream.readInt();
            final long value1 = stream.readInt();
            if (player.getTemporaryAttributtes().remove("add_Money_Pouch_To_Trade") != null) {
                if (value <= 0)
                    return;
                if (player.getTemporaryAttributtes().remove("add_money_pouch_trade") != null) {
                    if (value > player.getMoneyPouchValue()) {
                        player.sendMessage("You do not have enough coins in your money pouch.");
                        return;
                    } else if (value <= player.getMoneyPouchValue()) {
                        player.getMoneyPouch().takeMoneyFromPouch(value);
                        player.getPackets().sendRunScript(5561, 0, value);
                        player.getMoneyPouch().refresh();
                        player.setMoneyPouchTrade(value);
                        player.getTrade().addMoneyPouch(value);
                    }
                }
            }
            if (player.getInterfaceManager().containsInterface(548)
                    || player.getInterfaceManager().containsInterface(746)) {
                if (player.getTemporaryAttributtes().remove("remove_money") != null) {
                    final Integer remove_X_money = (Integer) player
                            .getTemporaryAttributtes().remove("remove_X_money");
                    final int amount = player.getInventory().getItems()
                            .getNumberOf(remove_X_money);
                    if (value < 0)
                        return;
                    if (remove_X_money == null)
                        return;
                    if (value <= player.getMoneyPouchValue()) {
                        if (amount + value > 0) {
                            if (player.getMoneyPouchValue() == 0) {
                                player.getPackets().sendGameMessage(
                                        "You can't withdraw null coins.");
                                return;
                            }
                            if (!player.canSpawn()) {
                                player.getPackets()
                                        .sendGameMessage(
                                                "Money Pouch is disabled in this Area.");
                                return;
                            }
                            if (player.getInventory().getFreeSlots() < 1) {
                                player.getPackets()
                                        .sendGameMessage(
                                                "Get some space before you do this action again.");
                                return;
                            }
                            player.getInventory().addItem(remove_X_money, value);
                            player.getPackets().sendRunScript(5561, 0, value);
                            player.getMoneyPouch().takeMoneyFromPouch(value);
                            //player.refreshMoneyPouch();
                        } else {
                            player.getPackets().sendGameMessage(
                                    "This shouldn't happen...");
                        }
                    } else {
                        if (player.getMoneyPouchValue() == 0) {
                            player.getPackets().sendGameMessage(
                                    "You can't withdraw null coins.");
                            return;
                        }
                        if (!player.canSpawn()) {
                            player.getPackets().sendGameMessage(
                                    "Money Pouch is disabled in this Area.");
                            return;
                        }
                        if (player.getInventory().getFreeSlots() < 1) {
                            player.getPackets()
                                    .sendGameMessage(
                                            "Get some space before you do this action again.");
                            return;
                        }
                        player.getInventory().addItem(remove_X_money,
                                player.getMoneyPouchValue());
                        player.getPackets().sendRunScript(5561, 0, player.getMoneyPouchValue());
                        player.getPackets().sendGameMessage(getFormattedNumber(player.getMoneyPouchValue()) + " coins have been removed from your money pouch.");
                        player.setMoneyPouchValue(0);
                        player.refreshMoneyPouch();
                    }
                }
            }
            if (player.getTemporaryAttributtes().get("using_cp") == Boolean.TRUE) {
                System.out.println("Handled staff action! Value: " + value);
                StaffPanelHandler.handle(player, Integer.toString(value));
                player.getTemporaryAttributtes().put("using_cp", Boolean.FALSE);
            }
            if ((player.getInterfaceManager().containsInterface(762) && player.getInterfaceManager().containsInterface(763)) || player.getInterfaceManager().containsInterface(11)) {
                if (value < 0)
                    return;
                final Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
                if (bank_item_X_Slot == null)
                    return;
                player.getBank().setLastX(value);
                player.getBank().refreshLastX();
                if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null) {
                    player.getBank().withdrawItem(bank_item_X_Slot, value);
                } else {
                    player.getBank().depositItem(bank_item_X_Slot, value, !player.getInterfaceManager().containsInterface(11));
                }
            } else if (player.getTemporaryAttributtes().get("GEPRICESET") != null) {
                if (value == 0)
                    return;
                player.getTemporaryAttributtes().remove("GEQUANTITYSET");
                player.getTemporaryAttributtes().remove("GEPRICESET");
                player.getGeManager().setPricePerItem(value);
            } else if (player.getTemporaryAttributtes().get("GEQUANTITYSET") != null) {
                player.getTemporaryAttributtes().remove("GEPRICESET");
                player.getTemporaryAttributtes().remove("GEQUANTITYSET");
                player.getGeManager().setAmount(value);
            } else if (player.getTemporaryAttributtes().get("bank_pin") == Boolean.TRUE) {
                if (value < 0)
                    return;
                player.setBankPin(value);
                player.setHasBankPin(true);
                player.getAppearance().generateAppearenceData();
                player.getDialogueManager().startDialogue(SimpleMessage.class, "Your Bank Pin Is... <col=FF0000>" + player.getBankPin() + " </col>Remember it!");
                player.getTemporaryAttributtes().put("bank_pin", Boolean.FALSE);
            } else if (player.getTemporaryAttributtes().get("bank_pin1") == Boolean.TRUE) {
                if (value < 0)
                    return;
                if (player.getPin() != value) {
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "Wrong Pin please try again.");
                } else {
                    player.getAppearance().generateAppearenceData();
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "You have entered your bank pin, Thank You");
                    player.setHasEnteredPin(true);
                    player.getBank().openBank();
                }
                player.getTemporaryAttributtes()
                        .put("bank_pin1", Boolean.FALSE);
            } else if (player.getTemporaryAttributtes().get("Repair") != null) {
                final int amount = value;
                final int itemId = (Integer) player.getTemporaryAttributtes()
                        .remove("Ritem");
                final RepairItems.BrokenItems brokenitems = RepairItems.BrokenItems.forId(itemId);
                final Item item = new Item(itemId);
                if (brokenitems == null)
                    return;
                if (amount == 0) {
                    player.getDialogueManager().startDialogue(
                            SimpleMessage.class,
                            "You cant repair <col=ff0000>" + "ZERO"
                                    + "</col> items...");
                    return;
                }
                if (player.getInventory().containsItem(brokenitems.getId(),
                        amount)) {
                    RepairItems.Repair(player, itemId, amount);
                } else {
                    player.getDialogueManager().startDialogue(
                            SimpleMessage.class,
                            "You dont have " + amount + " of " + item.getName()
                                    + ".");
                }
            } else if (player.getInterfaceManager().containsInterface(206)
                    && player.getInterfaceManager().containsInterface(207)) {
                if (value < 0)
                    return;
                final Integer pc_item_X_Slot = (Integer) player
                        .getTemporaryAttributtes().remove("pc_item_X_Slot");
                if (pc_item_X_Slot == null)
                    return;
                if (player.getTemporaryAttributtes().remove("pc_isRemove") != null) {
                    player.getPriceCheckManager().removeItem(pc_item_X_Slot,
                            value);
                } else {
                    player.getPriceCheckManager()
                            .addItem(pc_item_X_Slot, value);
                }
            } else if (player.getInterfaceManager().containsInterface(671) && player.getInterfaceManager().containsInterface(665)) {
                if (player.getControllerManager().getController() instanceof SailingController) {
                    if (value < 0)
                        return;
                    final Integer storage_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("storage_item_X_Slot");
                    if (storage_item_X_Slot == null)
                        return;
                    if (player.getTemporaryAttributtes().remove("storage_isRemove") != null) {
                        ((SailingController) player.getControllerManager().getController()).getStorage().removeItem(storage_item_X_Slot, value);
                    } else {
                        ((SailingController) player.getControllerManager().getController()).getStorage().addItem(storage_item_X_Slot, value);
                    }
                } else {
                    if (player.getFamiliar() == null || player.getFamiliar().getBob() == null)
                        return;
                    if (value < 0)
                        return;
                    final Integer bob_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bob_item_X_Slot");
                    if (bob_item_X_Slot == null)
                        return;
                    if (player.getTemporaryAttributtes().remove("bob_isRemove") != null) {
                        player.getFamiliar().getBob().removeItem(bob_item_X_Slot, value);
                    } else {
                        player.getFamiliar().getBob().addItem(bob_item_X_Slot, value);
                    }
                }
            } else if (player.getInterfaceManager().containsInterface(335)
                    && player.getInterfaceManager().containsInterface(336)) {
                if (value < 0)
                    return;
                final Integer trade_item_X_Slot = (Integer) player
                        .getTemporaryAttributtes().remove("trade_item_X_Slot");
                if (trade_item_X_Slot == null)
                    return;
                if (player.getTemporaryAttributtes().remove("trade_isRemove") != null) {
                    player.getTrade().removeItem(trade_item_X_Slot, value);
                } else {
                    player.getTrade().addItem(trade_item_X_Slot, value);
                }
            } else if (player.getTemporaryAttributtes().get("skillId") != null) {
                if (player.getEquipment().wearingArmour()) {
                    player.getDialogueManager().finishDialogue();
                    player.getDialogueManager().startDialogue(SimpleMessage.class,
                            "You cannot do this while having armour on!");
                    return;
                }
                final int skillId = (Integer) player.getTemporaryAttributtes()
                        .remove("skillId");
                if (skillId == Skills.HITPOINTS && value <= 9) {
                    value = 10;
                } else if (value < 1) {
                    value = 1;
                } else if (value > 99) {
                    value = 99;
                }
                player.getSkills().set(skillId, value);
                player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
                player.getAppearance().generateAppearenceData();
                player.getDialogueManager().finishDialogue();
            } else if (player.getTemporaryAttributtes().get("kilnX") != null) {
                final int index = (Integer) player.getTemporaryAttributtes()
                        .get("scIndex");
                final int componentId = (Integer) player
                        .getTemporaryAttributtes().get("scComponentId");
                final int itemId = (Integer) player.getTemporaryAttributtes()
                        .get("scItemId");
                player.getTemporaryAttributtes().remove("kilnX");
                if (StealingCreation.proccessKilnItems(player, componentId,
                        index, itemId, value))
                    return;
            }
        } else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
            stream.readShortLE128();
            final int fromInterfaceHash = stream.readIntV1();
            final int toInterfaceHash = stream.readInt();
            final int fromSlot = stream.readUnsignedShort();
            int toSlot = stream.readUnsignedShortLE128();
            stream.readUnsignedShortLE();

            final int toInterfaceId = toInterfaceHash >> 16;
            final int toComponentId = toInterfaceHash - (toInterfaceId << 16);
            final int fromInterfaceId = fromInterfaceHash >> 16;
            final int fromComponentId = fromInterfaceHash
                    - (fromInterfaceId << 16);
            if (fromInterfaceId == 1265 && toSlot == 65535) { //Shop drag item to inventory
                Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
                if (player.isBuying) {
                    shop.buy(player, fromSlot, shop.quantity);
                }
            }
            if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
                    || Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
                return;
            if (!player.getInterfaceManager()
                    .containsInterface(fromInterfaceId)
                    || !player.getInterfaceManager().containsInterface(
                    toInterfaceId))
                return;
            if (fromComponentId != -1
                    && Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
                return;
            if (toComponentId != -1
                    && Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
                return;
            if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
                    && fromComponentId == 0
                    && toInterfaceId == Inventory.INVENTORY_INTERFACE
                    && toComponentId == 0) {
                toSlot -= 28;
                if (toSlot < 0
                        || toSlot >= player.getInventory()
                        .getItemsContainerSize()
                        || fromSlot >= player.getInventory()
                        .getItemsContainerSize())
                    return;
                player.getInventory().switchItem(fromSlot, toSlot);
            } else if (fromInterfaceId == 763 && fromComponentId == 0
                    && toInterfaceId == 763 && toComponentId == 0) {
                if (toSlot >= player.getInventory().getItemsContainerSize()
                        || fromSlot >= player.getInventory()
                        .getItemsContainerSize())
                    return;
                player.getInventory().switchItem(fromSlot, toSlot);
            } else if (fromInterfaceId == 762 && toInterfaceId == 762) {
                player.getBank().switchItem(fromSlot, toSlot, fromComponentId,
                        toComponentId);
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                System.out.println("Switch item " + fromInterfaceId + ", "
                        + fromSlot + ", " + toSlot);
            }
        } else if (packetId == DONE_LOADING_REGION_PACKET) {
            /*
             * if(!player.clientHasLoadedMapRegion()) { //load objects and items
             * here player.setClientHasLoadedMapRegion(); }
             * //player.refreshSpawnedObjects(); //player.refreshSpawnedItems();
             */
        } else if (packetId == WALKING_PACKET
                || packetId == PLAYER_OPTION_7_PACKET
                || packetId == PLAYER_OPTION_8_PACKET
                || packetId == MINI_WALKING_PACKET
                || packetId == ITEM_TAKE_PACKET
                || packetId == PLAYER_OPTION_2_PACKET
                || packetId == PLAYER_OPTION_4_PACKET
                || packetId == PLAYER_OPTION_6_PACKET
                || packetId == PLAYER_OPTION_1_PACKET
                || packetId == ATTACK_NPC
                || packetId == INTERFACE_ON_PLAYER
                || packetId == INTERFACE_ON_NPC
                || packetId == NPC_CLICK1_PACKET
                || packetId == NPC_CLICK2_PACKET
                || packetId == NPC_CLICK3_PACKET
                || packetId == NPC_CLICK4_PACKET
                || packetId == OBJECT_CLICK1_PACKET
                || packetId == SWITCH_INTERFACE_ITEM_PACKET
                || packetId == OBJECT_CLICK2_PACKET
                || packetId == OBJECT_CLICK3_PACKET
                || packetId == OBJECT_CLICK4_PACKET
                || packetId == OBJECT_CLICK5_PACKET
                || packetId == INTERFACE_ON_OBJECT
                || packetId == PLAYER_OPTION_9_PACKET) {
            player.addLogicPacketToQueue(new LogicPacket(packetId, length, stream));
        } else if (packetId == OBJECT_EXAMINE_PACKET) {
            ObjectHandler.handleOption(player, stream, -1);
        } else if (packetId == NPC_EXAMINE_PACKET) {
            NPCHandler.handleExamine(player, stream);
        } else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
            if (!player.hasStarted())
                return;
            FriendChatsManager.joinChat(stream.readString(), player);
        } else if (packetId == KICK_FRIEND_CHAT_PACKET) {
            if (!player.hasStarted())
                return;
            player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
            // message
            // appearing
            player.kickPlayerFromFriendsChannel(stream.readString());
        } else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
            if (!player.hasStarted()
                    || !player.getInterfaceManager().containsInterface(1108))
                return;
            player.getFriendsIgnores().changeRank(stream.readString(),
                    stream.readUnsignedByte128());
        } else if (packetId == ADD_FRIEND_PACKET) {
            if (!player.hasStarted())
                return;
            player.getFriendsIgnores().addFriend(stream.readString());
        } else if (packetId == REMOVE_FRIEND_PACKET) {
            if (!player.hasStarted())
                return;
            player.getFriendsIgnores().removeFriend(stream.readString());
        } else if (packetId == ADD_IGNORE_PACKET) {
            if (!player.hasStarted())
                return;
            player.getFriendsIgnores().addIgnore(stream.readString(),
                    stream.readUnsignedByte() == 1);
        } else if (packetId == REMOVE_IGNORE_PACKET) {
            if (!player.hasStarted())
                return;
            player.getFriendsIgnores().removeIgnore(stream.readString());
        } else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {
            if (!player.hasStarted())
                return;
            if (player.getMuted() > Utils.currentTimeMillis()) {
                player.getPackets().sendGameMessage(
                        "You temporary muted. Recheck in 48 hours.");
                return;
            }
            final String username = stream.readString();
            final Player p2 = World.getPlayerByDisplayName(username);
            if (p2 == null)
                return;

            player.getFriendsIgnores().sendMessage(
                    p2,
                    Utils.fixChatMessage(Huffman.readEncryptedMessage(150,
                            stream)));
        } else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
            /*
             * if (!player.hasStarted()) return; String username =
             * stream.readString(); int fileId = stream.readUnsignedShort();
             * byte[] data = null; if (length > 3 + username.length()) { data =
             * new byte[length - (3 + username.length())];
             * stream.readBytes(data); } data =
             * Utils.completeQuickMessage(player, fileId, data); Player p2 =
             * World.getPlayerByDisplayName(username); if (p2 == null) return;
             * player.getFriendsIgnores().sendQuickChatMessage(p2, new
             * QuickChatMessage(fileId, data));
             */
        } else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
            if (!player.hasStarted())
                return;
            if (player.getLastPublicMessage() > Utils.currentTimeMillis())
                return;
            player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
            // just tells you which client script created packet
            @SuppressWarnings("unused") final boolean secondClientScript = stream.readByte() == 1;// script
            // 5059
            // or 5061
            final int fileId = stream.readUnsignedShort();
            byte[] data = null;
            if (length > 3) {
                data = new byte[length - 3];
                stream.readBytes(data);
            }
            final String message = Huffman.readEncryptedMessage(200, stream);
            if (message.contains("0hdr2ufufl9ljlzlyla")
                    || message.contains("0hdr"))
                return;
            data = Utils.completeQuickMessage(player, fileId, data);
            if (chatType == 0) {
                // player.sendPublicChatMessage(new QuickChatMessage(fileId,
                // data));
                player.getPackets().sendGameMessage("Not a chance.");
            } else if (chatType == 1) {
                // player.sendFriendsChannelQuickMessage(new QuickChatMessage(
                // fileId, data));
                player.getPackets().sendGameMessage("Not a chance.");
            } else if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info(this, "Unknown chat type: " + chatType);
            }
        } else if (packetId == CHAT_TYPE_PACKET) {
            chatType = stream.readUnsignedByte();
        } else if (packetId == CHAT_PACKET) {
            if (!player.hasStarted())
                return;
            if (player.getLastPublicMessage() > Utils.currentTimeMillis())
                return;
            player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
            final int colorEffect = stream.readUnsignedByte();
            final int moveEffect = stream.readUnsignedByte();
            final String message = Huffman.readEncryptedMessage(200, stream);
            if (message == null || message.replaceAll(" ", "").equals(""))
                return;
            if (colorEffect > 11 || moveEffect > 5 || colorEffect < 0 || moveEffect < 0)
                return;
            if (message.startsWith("::") || message.startsWith(";;")) {
                // if command exists and processed wont send message as public
                // message
                Server.getInstance().getCommandManager().processCommand(player, message.replace("::", "")
                        .replace(";;", ""), false, false);
                return;
            }
            if (message.contains("0hdr2ufufl9ljlzlyla")
                    || message.contains("0hdr"))
                return;
            if (player.getMuted() > Utils.currentTimeMillis()) {
                player.getPackets().sendGameMessage(
                        "You temporary muted. Recheck in 48 hours.");
                return;
            }
            final int effects = (colorEffect << 8) | (moveEffect & 0xff);
            if (chatType == 1) {
                player.sendFriendsChannelMessage(Utils.fixChatMessage(message));
            } else if (chatType == 2) {
                player.sendClanChannelMessage(Utils.fixChatMessage(message));
            } else if (chatType == 3) {
                player.sendGuestClanChannelMessage(Utils.fixChatMessage(message));
            } else {
                player.sendPublicChatMessage(new PublicChatMessage(Utils.fixChatMessage(message), effects));
            }
            player.logThis(message);
            player.setLastMsg(message);
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info(this, "Chat type: " + chatType);
            }
        } else if (packetId == COMMANDS_PACKET) {
            if (!player.isRunning())
                return;
            final boolean clientCommand = stream.readUnsignedByte() == 1;
            @SuppressWarnings("unused") final boolean unknown = stream.readUnsignedByte() == 1;
            final String command = stream.readString();
            if (!Server.getInstance().getCommandManager().processCommand(player, command, true, clientCommand)
                    && Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info(this, "Command: " + command);
            }
        } else if (packetId == COLOR_ID_PACKET) {
            if (!player.hasStarted())
                return;
            final int colorId = stream.readUnsignedShort();
            if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null) {
                SkillCapeCustomizer.handleSkillCapeCustomizerColor(player,
                        colorId);
            } else if (player.getTemporaryAttributtes().get("MottifCustomize") != null) {
                ClansManager.setMottifColor(player, colorId);
            }
        } else if (packetId == REPORT_ABUSE_PACKET) {
            if (!player.hasStarted())
                return;
            @SuppressWarnings("unused") final String username = stream.readString();
            @SuppressWarnings("unused") final int type = stream.readUnsignedByte();
            @SuppressWarnings("unused") final boolean mute = stream.readUnsignedByte() == 1;
            @SuppressWarnings("unused") final String unknown2 = stream.readString();
        } else {
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info(this, "Missing packet " + packetId
                        + ", expected size: " + length + ", actual size: "
                        + PACKET_SIZES[packetId]);
            }
        }
    }

}
