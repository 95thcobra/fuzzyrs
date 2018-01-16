package com.rs.core.net.encoders.impl;

import com.rs.content.clans.ClansManager;
import com.rs.content.economy.exchange.Offer;
import com.rs.content.player.PlayerRank;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.file.data.map.MapArchiveKeys;
import com.rs.core.net.Session;
import com.rs.core.net.encoders.Encoder;
import com.rs.core.net.io.OutputStream;
import com.rs.core.settings.GameConstants;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.core.utils.huffman.Huffman;
import com.rs.player.HintIcon;
import com.rs.player.Player;
import com.rs.player.PublicChatMessage;
import com.rs.player.QuickChatMessage;
import com.rs.player.content.FriendChatsManager;
import com.rs.world.*;
import com.rs.world.item.FloorItem;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;
import com.rs.world.npc.NPC;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public class WorldPacketsEncoder extends Encoder {

	private final Player player;

	public WorldPacketsEncoder(final Session session, final Player player) {
		super(session);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void sendPlayerUnderNPCPriority(final boolean priority) {
		final OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 6);
		stream.writeByteC(priority ? 1 : 0);
        session.write(stream);
    }

    public void sendWeight(double weight) {
        final OutputStream stream = new OutputStream();
        stream.writePacket(player, 98);
        stream.writeShort((short) weight);
        session.write(stream);
	}

	public void sendClanInviteMessage(Player p) {
		sendMessage(117, p.getDisplayName() + " is inviting you to join their clan.", p);
	}

	public void sendIComponentSprite(int interfaceId, int componentId, int spriteId) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(player, 121);
		stream.writeInt(spriteId);
		stream.writeIntV2(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendClanSettings(ClansManager manager, boolean myClan) {
		OutputStream stream = new OutputStream(manager == null ? 4 : manager.getClanSettingsDataBlock().length + 4);
		stream.writePacketVarShort(player, 133);
		stream.writeByte(myClan ? 1 : 0);
		if (manager != null)
			stream.writeBytes(manager.getClanSettingsDataBlock());
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendClanChannel(ClansManager manager, boolean myClan) {
		OutputStream stream = new OutputStream(manager == null ? 4 : manager.getClanChannelDataBlock().length + 4);
		stream.writePacketVarShort(player, 85);
		stream.writeByte(myClan ? 1 : 0);
		if (manager != null)
			stream.writeBytes(manager.getClanChannelDataBlock());
		stream.endPacketVarShort();
		session.write(stream);
	}


	public void receiveClanChatMessage(boolean myClan, String display, int rights, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 3);
		stream.writeByte(myClan ? 1 : 0);
		stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandom(255));
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receiveClanChatQuickMessage(boolean myClan, String display, int rights, QuickChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 1);
		stream.writeByte(myClan ? 1 : 0);
		stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandom(255));
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null)
			stream.writeBytes(message.getMessage().getBytes());
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendHintIcon(final HintIcon icon) {
		final OutputStream stream = new OutputStream(15);
		stream.writePacket(player, 79);
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0) {
			stream.skip(13);
		} else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(2500); // how often the arrow flashes, 2500
											// ideal, 0 never
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(icon.getPlane()); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(-1); // distance to start showing on minimap,
										// 0 doesnt show, -1 infinite
			}
			stream.writeInt(icon.getModelId());
		}
		session.write(stream);

	}

	public void sendCameraShake(final int slotId, final int b, final int c,
			final int d, final int e) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 44);
		stream.writeByte128(b);
		stream.writeByte128(slotId);
		stream.writeByte128(d);
		stream.writeByte128(c);
		stream.writeShortLE(e);
		session.write(stream);
	}

	public void sendStopCameraShake() {
		final OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 131);
		session.write(stream);
	}

	public void sendIComponentModel(final int interfaceId,
			final int componentId, final int modelId) {
		final OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 102);
		stream.writeIntV1(modelId);
		stream.writeIntV1(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendHideIComponent(final int interfaceId,
			final int componentId, final boolean hidden) {
		final OutputStream stream = new OutputStream(6);
		stream.writePacket(player, 112);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.writeByte(hidden ? 1 : 0);
		session.write(stream);
	}

	public void sendRemoveGroundItem(final FloorItem item) {
		final OutputStream stream = createWorldTileStream(item.getTile());
		final int localX = item.getTile().getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int localY = item.getTile().getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int offsetX = localX - ((localX >> 3) << 3);
		final int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 108);
		stream.writeShortLE(item.getId());
		stream.write128Byte((offsetX << 4) | offsetY);
		session.write(stream);

	}

	public void sendGroundItem(final FloorItem item) {
		final OutputStream stream = createWorldTileStream(item.getTile());
		final int localX = item.getTile().getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int localY = item.getTile().getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int offsetX = localX - ((localX >> 3) << 3);
		final int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 125);
		stream.writeByte128((offsetX << 4) | offsetY);
		stream.writeShortLE128(item.getAmount());
		stream.writeShortLE(item.getId());
		session.write(stream);
	}

	public void sendProjectile(final Entity receiver,
			final WorldTile startTile, final WorldTile endTile,
			final int gfxId, final int startHeight, final int endHeight,
			final int speed, final int delay, final int curve,
			final int startDistanceOffset, final int creatorSize) {
		final OutputStream stream = createWorldTileStream(startTile);
		stream.writePacket(player, 20);
		final int localX = startTile.getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int localY = startTile.getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int offsetX = localX - ((localX >> 3) << 3);
		final int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 3) | offsetY);
		stream.writeByte(endTile.getX() - startTile.getX());
		stream.writeByte(endTile.getY() - startTile.getY());
		stream.writeShort(receiver == null ? 0
				: (receiver instanceof Player ? -(receiver.getIndex() + 1)
						: receiver.getIndex() + 1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		final int duration = (Utils.getDistance(startTile.getX(),
				startTile.getY(), endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1
						: (speed / 10)))
						+ delay;
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeShort(creatorSize * 64 + startDistanceOffset * 64);
		session.write(stream);

	}

	public void sendUnlockIComponentOptionSlots(final int interfaceId,
			final int componentId, final int fromSlot, final int toSlot,
			final int... optionsSlots) {
		int settingsHash = 0;
		for (final int slot : optionsSlots) {
			settingsHash |= 2 << slot;
		}
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot,
				settingsHash);
	}

	public void sendIComponentSettings(final int interfaceId,
			final int componentId, final int fromSlot, final int toSlot,
			final int settingsHash) {
		final OutputStream stream = new OutputStream(13);
		stream.writePacket(player, 40);
		stream.writeIntV2(settingsHash);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShort128(fromSlot);
		stream.writeShortLE(toSlot);
		session.write(stream);
	}

	public void sendInterFlashScript(final int interfaceId,
			final int componentId, final int width, final int height,
			final int slot) {
		final Object[] parameters = new Object[4];
		int index = 0;
		parameters[index++] = slot;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(143, parameters);
	}

	public void sendInterSetItemsOptionsScript(final int interfaceId,
			final int componentId, final int key, final int width,
			final int height, final String... options) {
		sendInterSetItemsOptionsScript(interfaceId, componentId, key, false,
				width, height, options);
	}

	public void sendInterSetItemsOptionsScript(final int interfaceId,
			final int componentId, final int key, final boolean negativeKey,
			final int width, final int height, final String... options) {
		final Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--) {
			parameters[index++] = options[count];
		}
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this, maybe startslot?
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(negativeKey ? 695 : 150, parameters); // scriptid 150 does
															// that the method
		// name says*/
	}

	public void sendInputNameScript(final String message) {
		sendRunScript(109, message);
	}

	public void sendInputIntegerScript(final boolean integerEntryOnly,
			final String message) {
		sendRunScript(108, message);
	}

	public void sendInputLongTextScript(final String message) {
		sendRunScript(110, message);
	}

	public void sendRunScript(final int scriptId, final Object... params) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 119);
		String parameterTypes = "";
		if (params != null) {
			for (int count = params.length - 1; count >= 0; count--) {
				if (params[count] instanceof String) {
					parameterTypes += "s"; // string
				} else {
					parameterTypes += "i"; // integer
				}
			}
		}
		stream.writeString(parameterTypes);
		if (params != null) {
			int index = 0;
			for (int count = parameterTypes.length() - 1; count >= 0; count--) {
				if (parameterTypes.charAt(count) == 's') {
					stream.writeString((String) params[index++]);
				} else {
					stream.writeInt((Integer) params[index++]);
				}
			}
		}
		stream.writeInt(scriptId);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendGlobalConfig(final int id, final int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendGlobalConfig2(id, value);
		} else {
			sendGlobalConfig1(id, value);
		}
	}

	public void sendGlobalConfig1(final int id, final int value) {
		final OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 154);
		stream.writeByteC(value);
		stream.writeShort128(id);
		session.write(stream);
	}

	public void sendGlobalConfig2(final int id, final int value) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 63);
		stream.writeShort128(id);
		stream.writeInt(value);
		session.write(stream);
	}

	public void sendConfig(final int id, final int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendConfig2(id, value);
		} else {
			sendConfig1(id, value);
		}
	}

	public void sendConfigByFile(final int fileId, final int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendConfigByFile2(fileId, value);
		} else {
			sendConfigByFile1(fileId, value);
		}
	}

	public void sendConfig1(final int id, final int value) {
		final OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 110);
		stream.writeShortLE128(id);
		stream.writeByte128(value);
		session.write(stream);
	}

	public void sendConfig2(final int id, final int value) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 56);
		stream.writeShort128(id);
		stream.writeIntLE(value);
		session.write(stream);
	}

	public void sendConfigByFile1(final int fileId, final int value) {
		final OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 111);
		stream.writeShort128(fileId);
		stream.writeByteC(value);
		session.write(stream);
	}

	public void sendConfigByFile2(final int fileId, final int value) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 81);
		stream.writeIntV1(value);
		stream.writeShort128(fileId);
		session.write(stream);
	}

	public void sendRunEnergy() {
		final OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 25);
		stream.writeByte(player.getRunEnergy());
		session.write(stream);
	}

	public void sendIComponentText(final int interfaceId,
			final int componentId, final String text) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 135);
		stream.writeString(text);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.endPacketVarShort();
		session.write(stream);

	}

	public void sendIComponentAnimation(final int emoteId,
			final int interfaceId, final int componentId) {
		final OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 103);
		stream.writeIntV2(emoteId);
		stream.writeInt(interfaceId << 16 | componentId);
		session.write(stream);

	}

	public void sendItemOnIComponent(final int interfaceid,
			final int componentId, final int id, final int amount) {
		final OutputStream stream = new OutputStream(11);
		stream.writePacket(player, 152);
		stream.writeShort128(id);
		stream.writeIntV1(amount);
		stream.writeIntV2(interfaceid << 16 | componentId);
		session.write(stream);

	}

	public void sendExecMessage(final String command) {
		sendMessage(1337, command, null);
	}

	public void sendEntityOnIComponent(final boolean isPlayer,
			final int entityId, final int interfaceId, final int componentId) {
		if (isPlayer) {
			sendPlayerOnIComponent(interfaceId, componentId);
		} else {
			sendNPCOnIComponent(interfaceId, componentId, entityId);
		}
	}

	public void sendWorldTile(final WorldTile tile) {
		session.write(createWorldTileStream(tile));
	}

	public OutputStream createWorldTileStream(final WorldTile tile) {
		final OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 158);
		stream.writeByte128(tile.getLocalY(player.getLastLoadedMapRegionTile(),
				player.getMapSize()) >> 3);
		stream.writeByteC(tile.getPlane());
		stream.write128Byte(tile.getLocalX(player.getLastLoadedMapRegionTile(),
				player.getMapSize()) >> 3);
		return stream;
	}

	public void sendObjectAnimation(final WorldObject object,
			final Animation animation) {
		final OutputStream stream = new OutputStream(10);
		stream.writePacket(player, 76);
		stream.writeInt(animation.getIds()[0]);
		stream.writeByteC((object.getType() << 2)
				+ (object.getRotation() & 0x3));
		stream.writeIntLE(object.getTileHash());
		session.write(stream);
	}

	public void sendTileMessage(final String message, final WorldTile tile,
			final int color) {
		sendTileMessage(message, tile, 5000, 255, color);
	}

	public void sendTileMessage(final String message, final WorldTile tile,
			final int delay, final int height, final int color) {
		final OutputStream stream = createWorldTileStream(tile);
		stream.writePacketVarByte(player, 107);
		stream.skip(1);
		final int localX = tile.getLocalX(player.getLastLoadedMapRegionTile(),
				player.getMapSize());
		final int localY = tile.getLocalY(player.getLastLoadedMapRegionTile(),
				player.getMapSize());
		final int offsetX = localX - ((localX >> 3) << 3);
		final int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 4) | offsetY);
		stream.writeShort(delay / 30);
		stream.writeByte(height);
		stream.write24BitInteger(color);
		stream.writeString(message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendSpawnedObject(WorldObject object) {
		final int chunkRotation = World.getRotation(object.getPlane(),
				object.getX(), object.getY());
		if (chunkRotation == 1) {
			object = new WorldObject(object);
			final ObjectDefinitions defs = ObjectDefinitions
					.getObjectDefinitions(object.getId());
			object.moveLocation(0, -(defs.getSizeY() - 1), 0);
		} else if (chunkRotation == 2) {
			object = new WorldObject(object);
			final ObjectDefinitions defs = ObjectDefinitions
					.getObjectDefinitions(object.getId());
			object.moveLocation(-(defs.getSizeY() - 1), 0, 0);
		}
		final OutputStream stream = createWorldTileStream(object);
		final int localX = object.getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int localY = object.getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int offsetX = localX - ((localX >> 3) << 3);
		final int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 120);
		stream.writeByte((offsetX << 4) | offsetY); // the hash
		// for
		// coords,
		// useless
		stream.writeByte((object.getType() << 2) + (object.getRotation() & 0x3));
		stream.writeIntLE(object.getId());
		session.write(stream);

	}

	public void sendDestroyObject(WorldObject object) {
		final int chunkRotation = World.getRotation(object.getPlane(),
				object.getX(), object.getY());
		if (chunkRotation == 1) {
			object = new WorldObject(object);
			final ObjectDefinitions defs = ObjectDefinitions
					.getObjectDefinitions(object.getId());
			object.moveLocation(0, -(defs.getSizeY() - 1), 0);
		} else if (chunkRotation == 2) {
			object = new WorldObject(object);
			final ObjectDefinitions defs = ObjectDefinitions
					.getObjectDefinitions(object.getId());
			object.moveLocation(-(defs.getSizeY() - 1), 0, 0);
		}
		final OutputStream stream = createWorldTileStream(object);
		final int localX = object.getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int localY = object.getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
		final int offsetX = localX - ((localX >> 3) << 3);
		final int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 45);
		stream.writeByteC((object.getType() << 2)
				+ (object.getRotation() & 0x3));
		stream.writeByte128((offsetX << 4) | offsetY); // the hash for coords,
		// useless
		session.write(stream);

	}

	public void sendPlayerOnIComponent(final int interfaceId,
			final int componentId) {
		final OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 23);
		stream.writeIntV2(interfaceId << 16 | componentId);
		session.write(stream);

	}

	public void sendNPCOnIComponent(final int interfaceId,
			final int componentId, final int npcId) {
		final OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 31);
		stream.writeInt(npcId);
		stream.writeInt(interfaceId << 16 | componentId);
		session.write(stream);

	}

	public void sendRandomOnIComponent(final int interfaceId,
			final int componentId, final int id) {
		/*
		 * OutputStream stream = new OutputStream(); stream.writePacket(player,
		 * 235); stream.writeShort(id); stream.writeIntV1(interfaceId << 16 |
		 * componentId); stream.writeShort(interPacketsCount++);
		 * session.write(stream);
		 */
	}

	public void sendFaceOnIComponent(final int interfaceId,
			final int componentId, final int look1, final int look2,
			final int look3) {
		/*
		 * OutputStream stream = new OutputStream(); stream.writePacket(player,
		 * 192); stream.writeIntV2(interfaceId << 16 | componentId);
		 * stream.writeShortLE128(interPacketsCount++);
		 * stream.writeShortLE128(look1); stream.writeShortLE128(look2);
		 * stream.writeShort128(look2); session.write(stream);
		 */
	}

	public void sendFriendsChatChannel() {
		final FriendChatsManager manager = player.getCurrentFriendChat();
		final OutputStream stream = new OutputStream(manager == null ? 3
				: manager.getDataBlock().length + 3);
		stream.writePacketVarShort(player, 117);
		if (manager != null) {
			stream.writeBytes(manager.getDataBlock());
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFriends() {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 2);
		for (final String username : player.getFriendsIgnores().getFriends()) {
			String displayName;
			final Player p2 = World.getPlayerByDisplayName(username);
			if (p2 != null) {
				displayName = p2.getDisplayName();
			} else {
				displayName = Utils.formatPlayerNameForDisplay(username);
			}
			player.getPackets().sendFriend(
					Utils.formatPlayerNameForDisplay(username), displayName, 1,
					p2 != null && player.getFriendsIgnores().isOnline(p2),
					false, stream);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFriend(final String username, final String displayName,
			final int world, final boolean putOnline, final boolean warnMessage) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 2);
		sendFriend(username, displayName, world, putOnline, warnMessage, stream);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFriend(final String username, final String displayName,
			final int world, final boolean putOnline,
			final boolean warnMessage, final OutputStream stream) {
		stream.writeByte(warnMessage ? 0 : 1);
		stream.writeString(displayName);
		stream.writeString(displayName.equals(username) ? "" : username);
		stream.writeShort(putOnline ? world : 0);
		stream.writeByte(player.getFriendsIgnores().getRank(
				Utils.formatPlayerNameForProtocol(username)));
		stream.writeByte(0);
		if (putOnline) {
			stream.writeString(SettingsManager.getSettings().SERVER_NAME);
			stream.writeByte(0);
		}
	}

	public void sendIgnores() {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 55);
		stream.writeByte(player.getFriendsIgnores().getIgnores().size());
		for (final String username : player.getFriendsIgnores().getIgnores()) {
			String display;
			final Player p2 = World.getPlayerByDisplayName(username);
			if (p2 != null) {
				display = p2.getDisplayName();
			} else {
				display = Utils.formatPlayerNameForDisplay(username);
			}
			final String name = Utils.formatPlayerNameForDisplay(username);
			stream.writeString(display.equals(name) ? name : display);
			stream.writeString("");
			stream.writeString(display.equals(name) ? "" : name);
			stream.writeString("");
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendIgnore(final String name, final String display,
			final boolean updateName) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 128);
		stream.writeByte(0x2);
		stream.writeString(display.equals(name) ? name : display);
		stream.writeString("");
		stream.writeString(display.equals(name) ? "" : name);
		stream.writeString("");
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendPrivateMessage(final String username, final String message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 130);
		stream.writeString(username);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendGameBarStages() {
		sendConfig(1054, player.getClanStatus());
		sendConfig(1055, player.getAssistStatus());
		sendConfig(1056, player.isFilterGame() ? 1 : 0);
		sendConfig(2159, player.getFriendsIgnores().getFriendsChatStatus());
		sendOtherGameBarStages();
		sendPrivateGameBarStage();
	}

	public void sendOtherGameBarStages() {
		final OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 89);
		stream.write128Byte(player.getTradeStatus());
		stream.writeByte(player.getPublicStatus());
		session.write(stream);
	}

	public void sendPrivateGameBarStage() {
		final OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 75);
		stream.writeByte(player.getFriendsIgnores().getPrivateStatus());
		session.write(stream);
	}

	public void receivePrivateMessage(final String name, final String display,
			final int rights, final String message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 105);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Utils.getRandom(255));
		}
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarShort();
		session.write(stream);
	}

	// 131 clan chat quick message

	public void receivePrivateChatQuickMessage(final String name,
			final String display, final int rights,
			final QuickChatMessage message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 104);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Utils.getRandom(255));
		}
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null) {
			stream.writeBytes(message.getMessage().getBytes());
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendPrivateQuickMessageMessage(final String username,
			final QuickChatMessage message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 30);
		stream.writeString(username);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null) {
			stream.writeBytes(message.getMessage().getBytes());
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receiveFriendChatMessage(final String name,
			final String display, final int rights, final String chatName,
			final String message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 139);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		stream.writeLong(Utils.stringToLong(chatName));
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Utils.getRandom(255));
		}
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receiveFriendChatQuickMessage(final String name,
			final String display, final int rights, final String chatName,
			final QuickChatMessage message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 32);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		stream.writeLong(Utils.stringToLong(chatName));
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Utils.getRandom(255));
		}
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null) {
			stream.writeBytes(message.getMessage().getBytes());
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	/*
	 * useless, sending friends unlocks it
	 */
	public void sendUnlockIgnoreList() {
		final OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 18);
		session.write(stream);
	}

	/*
	 * dynamic map region
	 */
	public void sendDynamicMapRegion(final boolean sendLswp) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 144);
		if (sendLswp) {
			player.getLocalPlayerUpdate().init(stream);
		}
		final int regionX = player.getChunkX();
		final int regionY = player.getChunkY();
		stream.write128Byte(2);
		stream.writeShortLE(regionY);
		stream.writeShortLE128(regionX);
		stream.write128Byte(player.isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeByteC(player.getMapSize());
		stream.initBitAccess();
		final int mapHash = GameConstants.MAP_SIZES[player.getMapSize()] >> 4;
		final int[] realRegionIds = new int[4 * mapHash * mapHash];
		int realRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int thisRegionX = (regionX - mapHash); thisRegionX <= ((regionX + mapHash)); thisRegionX++) { // real
				// x
				// calcs
				for (int thisRegionY = (regionY - mapHash); thisRegionY <= ((regionY + mapHash)); thisRegionY++) { // real
					// y
					// calcs
					final int regionId = (((thisRegionX / 8) << 8) + (thisRegionY / 8));
					final Region region = World.getRegions().get(regionId);
					int realRegionX;
					int realRegionY;
					int realPlane;
					int rotation;
					if (region instanceof DynamicRegion) { // generated map
						final DynamicRegion dynamicRegion = (DynamicRegion) region;
						final int[] regionCoords = dynamicRegion
								.getRegionCoords()[plane][thisRegionX
						                                                                  - ((thisRegionX / 8) * 8)][thisRegionY
						                                                                                             - ((thisRegionY / 8) * 8)];
						realRegionX = regionCoords[0];
						realRegionY = regionCoords[1];
						realPlane = regionCoords[2];
						rotation = regionCoords[3];
					} else { // real map
						// base region + difference * 8 so gets real region
						// coords
						realRegionX = thisRegionX;
						realRegionY = thisRegionY;
						realPlane = plane;
						rotation = 0;// no rotation
					}
					// invalid region, not built region
					if (realRegionX == 0 || realRegionY == 0) {
						stream.writeBits(1, 0);
					} else {
						stream.writeBits(1, 1);
						stream.writeBits(26, (rotation << 1)
								| (realPlane << 24) | (realRegionX << 14)
								| (realRegionY << 3));
						final int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
						boolean found = false;
						for (int index = 0; index < realRegionIdsCount; index++)
							if (realRegionIds[index] == realRegionId) {
								found = true;
								break;
							}
						if (!found) {
							realRegionIds[realRegionIdsCount++] = realRegionId;
						}
					}

				}
			}
		}
		stream.finishBitAccess();
		for (int index = 0; index < realRegionIdsCount; index++) {
			int[] xteas = MapArchiveKeys.getMapKeys(realRegionIds[index]);
			if (xteas == null) {
				xteas = new int[4];
			}
			for (int keyIndex = 0; keyIndex < 4; keyIndex++) {
				stream.writeInt(xteas[keyIndex]);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	/*
	 * normal map region
	 */
	public void sendMapRegion(final boolean sendLswp) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 42);
		if (sendLswp) {
			player.getLocalPlayerUpdate().init(stream);
		}
		stream.writeByteC(player.getMapSize());
		stream.writeByte(player.isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeShort(player.getChunkX());
		stream.writeShort(player.getChunkY());
		for (final int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapArchiveKeys.getMapKeys(regionId);
			if (xteas == null) {
				xteas = new int[4];
			}
			for (int index = 0; index < 4; index++) {
				stream.writeInt(xteas[index]);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendCutscene(final int id) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 142);
		stream.writeShort(id);
		stream.writeShort(20); // xteas count
		for (int count = 0; count < 20; count++) {
			// xteas
			for (int i = 0; i < 4; i++) {
				stream.writeInt(0);
			}
		}
		final byte[] appearence = player.getAppearance().getAppeareanceData();
		stream.writeByte(appearence.length);
		stream.writeBytes(appearence);
		stream.endPacketVarShort();
		session.write(stream);
	}

	/*
	 * sets the pane interface
	 */
	public void sendWindowsPane(final int id, final int type) {
		final int[] xteas = new int[4];
		player.getInterfaceManager().setWindowsPane(id);
		final OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 39);
		stream.write128Byte(type);
		stream.writeShort128(id);
		stream.writeIntLE(xteas[1]);
		stream.writeIntV2(xteas[0]);
		stream.writeInt(xteas[3]);
		stream.writeInt(xteas[2]);
		session.write(stream);
	}

	public void sendPlayerOption(final String option, final int slot,
			final boolean top) {
		sendPlayerOption(option, slot, top, -1);
	}

	public void sendPublicMessage(final Player p,
			final PublicChatMessage message) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 106);
		stream.writeShort(p.getIndex());
		stream.writeShort(message.getEffects());
		stream.writeByte(p.getRank().getMessageIcon());
		if (message instanceof QuickChatMessage) {
			final QuickChatMessage qcMessage = (QuickChatMessage) message;
			/*
			 * stream.writeShort(qcMessage.getFileId()); if
			 * (qcMessage.getMessage() != null)
			 * stream.writeBytes(message.getMessage().getBytes());
			 */
		} else {
			final byte[] chatStr = new byte[250];
			chatStr[0] = (byte) message.getMessage().length();
			final int offset = 1 + Huffman.encryptMessage(1, message
					.getMessage().length(), chatStr, 0, message.getMessage()
					.getBytes());
			stream.writeBytes(chatStr, 0, offset);
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendPlayerOption(final String option, final int slot,
			final boolean top, final int cursor) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 118);
		stream.writeByte128(slot);
		stream.writeString(option);
		stream.writeShortLE128(cursor);
		stream.writeByteC(top ? 1 : 0);
		stream.endPacketVarByte();
		session.write(stream);
	}

	/*
	 * sends local players update
	 */
	public void sendLocalPlayersUpdate() {
		session.write(player.getLocalPlayerUpdate().createPacketAndProcess());
	}

	/*
	 * sends local npcs update
	 */
	public void sendLocalNPCsUpdate() {
		session.write(player.getLocalNPCUpdate().createPacketAndProcess());
	}

	public void sendGraphics(final Graphics graphics, final Object target) {
		final OutputStream stream = new OutputStream(13);
		int hash = 0;
		if (target instanceof Player) {
			final Player p = (Player) target;
			hash = p.getIndex() & 0xffff | 1 << 28;
		} else if (target instanceof NPC) {
			final NPC n = (NPC) target;
			hash = n.getIndex() & 0xffff | 1 << 29;
		} else {
			final WorldTile tile = (WorldTile) target;
			hash = tile.getPlane() << 28 | tile.getX() << 14 | tile.getY()
					& 0x3fff | 1 << 30;
		}
		stream.writePacket(player, 90);
		stream.writeShort(graphics.getId());
		stream.writeByte128(0); // slot id used for entitys
		stream.writeShort(graphics.getSpeed());
		stream.writeByte128(graphics.getSettings2Hash());
		stream.writeShort(graphics.getHeight());
		stream.writeIntLE(hash);
		session.write(stream);
	}

	public void sendDelayedGraphics(final Graphics graphics, final int delay,
			final WorldTile tile) {

	}

	public void closeInterface(final int windowComponentId) {
		closeInterface(
				player.getInterfaceManager().getTabWindow(windowComponentId),
				windowComponentId);
		player.getInterfaceManager().removeTab(windowComponentId);
	}

	public void closeInterface(final int windowId, final int windowComponentId) {
		final OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 5);
		stream.writeIntLE(windowId << 16 | windowComponentId);
		session.write(stream);
	}

	public void sendInterface(final boolean nocliped, final int windowId,
			final int windowComponentId, final int interfaceId) {
		// currently fixes the inter engine.. not ready for same component
		// ids(tabs), different inters
		if (!(windowId == 752 && (windowComponentId == 9 || windowComponentId == 12))) { // if
			// chatbox
			if (player.getInterfaceManager().containsInterface(
					windowComponentId, interfaceId)) {
				closeInterface(windowComponentId);
			}
			if (!player.getInterfaceManager().addInterface(windowId,
					windowComponentId, interfaceId)) {
				Logger.info(this, "Error adding interface: " + windowId + " , "
						+ windowComponentId + " , " + interfaceId);
				return;
			}
		}
		final int[] xteas = new int[4];
		final OutputStream stream = new OutputStream(24);
		stream.writePacket(player, 14);
		stream.writeShort(interfaceId);
		stream.writeInt(xteas[0]);
		stream.writeIntV2(xteas[1]);
		stream.writeIntV1(windowId << 16 | windowComponentId);
		stream.writeByte(nocliped ? 1 : 0);
		stream.writeIntV1(xteas[3]);
		stream.writeIntV2(xteas[2]);
		session.write(stream);
	}

	public void sendSystemUpdate(final int delay) {
		final OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 141);
		stream.writeShort((int) (delay * 1.6));
		session.write(stream);
	}

	public void sendUpdateItems(final int key,
			final ItemsContainer<Item> items, final int... slots) {
		sendUpdateItems(key, items.getItems(), slots);
	}

	public void sendUpdateItems(final int key, final Item[] items,
			final int... slots) {
		sendUpdateItems(key, key < 0, items, slots);
	}

	public void sendUpdateItems(final int key, final boolean negativeKey,
			final Item[] items, final int... slots) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 138);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		for (final int slotId : slots) {
			if (slotId >= items.length) {
				continue;
			}
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			final Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255) {
					stream.writeInt(amount);
				}
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendGlobalString(final int id, final String string) {
		final OutputStream stream = new OutputStream();
		if (string.length() > 253) {
			stream.writePacketVarShort(player, 34);
			stream.writeString(string);
			stream.writeShort(id);
			stream.endPacketVarShort();
		} else {
			stream.writePacketVarByte(player, 134);
			stream.writeShort(id);
			stream.writeString(string);
			stream.endPacketVarByte();
		}
		session.write(stream);
	}

	public void sendItems(final int key, final ItemsContainer<Item> items) {
		sendItems(key, key < 0, items);
	}

	public void sendItems(final int key, final boolean negativeKey,
			final ItemsContainer<Item> items) {
		sendItems(key, negativeKey, items.getItems());
	}

	public void sendItems(final int key, final Item[] items) {
		sendItems(key, key < 0, items);
	}

	public void resetItems(final int key, final boolean negativeKey,
			final int size) {
		sendItems(key, negativeKey, new Item[size]);
	}

	public void sendItems(final int key, final boolean negativeKey,
			final Item[] items) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 77);
		stream.writeShort(key); // negativeKey ? -key : key
		stream.writeByte(negativeKey ? 1 : 0);
		stream.writeShort(items.length);
		for (final Item item : items) {
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShortLE128(id + 1);
			stream.writeByte128(amount >= 255 ? 255 : amount);
			if (amount >= 255) {
				stream.writeIntV1(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFilteredGameMessage(boolean filter, String text, Object... args) {
		sendMessage(filter ? 109 : 0, String.format(text, args), null);
	}

	public void sendLogout(final boolean lobby) {
		final OutputStream stream = new OutputStream();
		stream.writePacket(player, lobby ? 59 : 60);
		final ChannelFuture future = session.write(stream);
        if (player.getDwarfCannon().hasCannon()) {
            player.getDwarfCannon().pickUpDwarfCannon(0, player.getDwarfCannon().getObject());
        }
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}

	public void sendInventoryMessage(final int border, final int slotId,
			final String message) {
		sendGameMessage(message);
		sendRunScript(948, border, slotId, message);
	}

	public void sendNPCMessage(final int border, final NPC npc,
			final String message) {
		sendGameMessage(message);
	}

	public void sendGameMessage(final String text) {
		sendGameMessage(text, false);
	}

	public void sendGameMessage(final String text, final boolean filter) {
		sendMessage(filter ? 109 : 0, text, null);
	}

	public void sendPanelBoxMessage(final String text) {
		sendMessage(player.getRank().isMinimumRank(PlayerRank.ADMIN) ? 99 : 0, text, null);
	}

	public void sendTradeRequestMessage(final Player p) {
		sendMessage(100, "wishes to trade with you.", p);
	}

	public void sendClanWarsRequestMessage(final Player p) {
		sendMessage(101, "wishes to challenge your clan to a clan war.", p);
	}

	public void sendDuelChallengeRequestMessage(final Player p,
			final boolean friendly) {
		sendMessage(101, "wishes to duel with you("
				+ (friendly ? "friendly" : "stake") + ").", p);
	}

	public void sendMessage(final int type, final String text, final Player p) {
		int maskData = 0;
		if (p != null) {
			maskData |= 0x1;
			if (p.hasDisplayName()) {
				maskData |= 0x2;
			}
		}
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 136);
		stream.writeSmart(type);
		stream.writeInt(player.getTileHash()); // junk, not used by client
		stream.writeByte(maskData);
		if ((maskData & 0x1) != 0) {
			stream.writeString(p.getDisplayName());
			if (p.hasDisplayName()) {
				stream.writeString(Utils.formatPlayerNameForDisplay(p
						.getUsername()));
			}
		}
		stream.writeString(text);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendGrandExchangeOffer(Offer offer) {
		OutputStream stream = new OutputStream(21);
		stream.writePacket(player, 53);
		stream.writeByte(offer.getSlot());
		stream.writeByte(offer.getStage());
		if (offer.forceRemove())
			stream.skip(18);
		else {
			stream.writeShort(offer.getId());
			stream.writeInt(offer.getPrice());
			stream.writeInt(offer.getAmount());
			stream.writeInt(offer.getTotalAmmountSoFar());
			stream.writeInt(offer.getTotalPriceSoFar());
		}
		session.write(stream);
	}

	public void sendVar(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendVar2(id, value);
		else
			sendVar1(id, value);
	}

	public void sendVarBit(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendVarBit2(id, value);
		else
			sendVarBit1(id, value);
	}

	public void sendVar1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 110);
		stream.writeShortLE128(id);
		stream.writeByte128(value);
		session.write(stream);
	}

	public void sendVar2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 56);
		stream.writeShort128(id);
		stream.writeIntLE(value);
		session.write(stream);
	}

	public void sendVarBit1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 111);
		stream.writeShort128(id);
		stream.writeByteC(value);
		session.write(stream);
	}

	public void sendVarBit2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 81);
		stream.writeIntV1(value);
		stream.writeShort128(id);
		session.write(stream);
	}

	private void writeVarBitsToStream(int outputStreamId, int packetId, int id, int value) {
		OutputStream stream = new OutputStream(outputStreamId);
		stream.writePacket(player, packetId);
		stream.writeIntV1(value);
		stream.writeShort128(id);
		session.write(stream);
	}
	// effect type 1 or 2(index4 or index14 format, index15 format unusused by
	// jagex for now)
	public void sendSound(final int id, final int delay, final int effectType) {
		if (effectType == 1) {
			sendIndex14Sound(id, delay);
		} else if (effectType == 2) {
			sendIndex15Sound(id, delay);
		}
	}

	public void sendVoice(final int id) {
		resetSounds();
		sendSound(id, 0, 2);
	}

	public void resetSounds() {
		final OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 145);
		session.write(stream);
	}

	public void sendIndex14Sound(final int id, final int delay) {
		final OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 26);
		stream.writeShort(id);
		stream.writeByte(1);// repeated amount
		stream.writeShort(delay);
		stream.writeByte(255);
		stream.writeShort(256);
		session.write(stream);
	}

	public void sendIndex15Sound(final int id, final int delay) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 70);
		stream.writeShort(id);
		stream.writeByte(1); // amt of times it repeats
		stream.writeShort(delay);
		stream.writeByte(255); // volume
		session.write(stream);
	}

	public void sendMusicEffect(final int id) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 9);
		stream.write128Byte(255); // volume
		stream.write24BitIntegerV2(0);
		stream.writeShort(id);
		session.write(stream);
	}

	public void sendMusic(final int id) {
		sendMusic(id, 100, 255);
	}

	public void sendMusic(final int id, final int delay, final int volume) {
		final OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 129);
		stream.writeByte(delay);
		stream.writeShortLE128(id);
		stream.writeByte128(volume);
		session.write(stream);
	}

	public void sendSkillLevel(final int skill) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 146);
		stream.write128Byte(skill);
		stream.writeInt((int) player.getSkills().getXp(skill));
		stream.writeByte128(player.getSkills().getLevel(skill));
		session.write(stream);
	}

	// CUTSCENE PACKETS START

	/**
	 * This will blackout specified area.
	 */
	public void sendBlackOut(final int area) {
		final OutputStream out = new OutputStream(2);
		out.writePacket(player, 69);
		out.writeByte(area);
		session.write(out);
	}

	// instant
	public void sendCameraLook(final int viewLocalX, final int viewLocalY,
			final int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
	}

	public void sendCameraLook(final int viewLocalX, final int viewLocalY,
			final int viewZ, final int speed1, final int speed2) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 116);
		stream.writeByte128(viewLocalY);
		stream.writeByte(speed1);
		stream.writeByteC(viewLocalX);
		stream.writeByte(speed2);
		stream.writeShort128(viewZ >> 2);
		session.write(stream);
	}

	public void sendResetCamera() {
		final OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 95);
		session.write(stream);
	}

	public void sendCameraRotation(final int unknown1, final int unknown2) {
		final OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 123);
		stream.writeShort(unknown1);
		stream.writeShortLE(unknown1);
		session.write(stream);
	}

	public void sendCameraPos(final int moveLocalX, final int moveLocalY,
			final int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
	}

	public void sendClientConsoleCommand(final String command) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 61);
		stream.writeString(command);
		stream.endPacketVarByte();
	}

	public void sendOpenURL(final String url) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 17);
		stream.writeByte(0);
		stream.writeString(url);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendSetMouse(final String walkHereReplace, final int cursor) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 10);
		stream.writeString(walkHereReplace);
		stream.writeShort(cursor);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendItemsLook() {
		final OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 159);
		stream.writeByte(player.isOldItemsLook() ? 1 : 0);
		session.write(stream);
	}

	public void sendCameraPos(final int moveLocalX, final int moveLocalY,
			final int moveZ, final int speed1, final int speed2) {
		final OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 74);
		stream.writeByte128(speed2);
		stream.writeByte128(speed1);
		stream.writeByte(moveLocalY);
		stream.writeShort(moveZ >> 2);
		stream.writeByte(moveLocalX);
		session.write(stream);
	}

}
