package com.rs.server.net.decoders.impl;

import com.rs.server.Server;
import com.rs.core.cache.Cache;
import com.rs.server.net.Session;
import com.rs.server.net.decoders.Decoder;
import com.rs.server.net.io.InputStream;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;
import com.rs.utils.MachineInformation;
import com.rs.utils.Utils;
import com.rs.utils.file.Encrypt;
import com.rs.utils.net.IsaacKeyPair;
import com.rs.player.Player;
import com.rs.world.World;

import java.io.IOException;
import java.util.Optional;

public final class LoginPacketsDecoder extends Decoder {

	public LoginPacketsDecoder(final Session session) {
		super(session);
	}

	@Override
	public void decode(final InputStream stream) {
		session.setDecoder(-1);
		final int packetId = stream.readUnsignedByte();
		if (World.exiting_start != 0) {
			session.getLoginPackets().sendClientPacket(14);
			return;
		}
		final int packetSize = stream.readUnsignedShort();
		if (packetSize != stream.getRemaining()) {
			session.getChannel().close();
			return;
		}
		if (stream.readInt() != GameConstants.CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		if (packetId == 16 || packetId == 18) {
			decodeWorldLogin(stream);
		} else {
			if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
				Logger.info(this, "PacketId " + packetId);
			}
			session.getChannel().close();
		}
	}

	@SuppressWarnings("unused")
	public void decodeWorldLogin(final InputStream stream) {
		if (stream.readInt() != GameConstants.CUSTOM_CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		final boolean unknownEquals14 = stream.readUnsignedByte() == 1;
		final int rsaBlockSize = stream.readUnsignedShort();
		if (rsaBlockSize > stream.getRemaining()) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		final byte[] data = new byte[rsaBlockSize];
		stream.readBytes(data, 0, rsaBlockSize);
		final InputStream rsaStream = new InputStream(Utils.cryptRSA(data,
				GameConstants.PRIVATE_EXPONENT, GameConstants.MODULUS));
		if (rsaStream.readUnsignedByte() != 10) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		final int[] isaacKeys = new int[4];
		for (int i = 0; i < isaacKeys.length; i++) {
			isaacKeys[i] = rsaStream.readInt();
		}
		if (rsaStream.readLong() != 0L) { // rsa block check, pass part
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		String actualPassword = rsaStream.readString();
		if (actualPassword.length() > 30 || actualPassword.length() < 3) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		String password = Encrypt.encryptSHA1(actualPassword);
		final String unknown = Utils.longToString(rsaStream.readLong());
		rsaStream.readLong(); // random value
		rsaStream.readLong(); // random value
		stream.decodeXTEA(isaacKeys, stream.getOffset(), stream.getLength());
		final boolean stringUsername = stream.readUnsignedByte() == 1; // unknown
		final String username = Utils
				.formatPlayerNameForProtocol(stringUsername ? stream
						.readString() : Utils.longToString(stream.readLong()));
		final int displayMode = stream.readUnsignedByte();
		final int screenWidth = stream.readUnsignedShort();
		final int screenHeight = stream.readUnsignedShort();
		final int unknown2 = stream.readUnsignedByte();
		stream.skip(24); // 24bytes directly from a file, no idea whats there
		final String settings = stream.readString();
		final int affid = stream.readInt();
		stream.skip(stream.readUnsignedByte()); // useless settings
		/*
		 * if (stream.readUnsignedByte() != 6) { //personal data start
		 * session.getLoginPackets().sendClientPacket(10); return; } int os =
		 * stream.readUnsignedByte(); boolean x64Arch =
		 * stream.readUnsignedByte() == 1; int osVersion =
		 * stream.readUnsignedByte(); int osVendor = stream.readUnsignedByte();
		 * int javaVersion = stream.readUnsignedByte(); int javaVersionBuild =
		 * stream.readUnsignedByte(); int javaVersionBuild2 =
		 * stream.readUnsignedByte(); boolean hasApplet =
		 * stream.readUnsignedByte() == 1; int heap =
		 * stream.readUnsignedShort(); int availableProcessors =
		 * stream.readUnsignedByte(); int ram = stream.read24BitInt(); int
		 * cpuClockFrequency = stream.readUnsignedShort(); int cpuInfo3 =
		 * stream.readUnsignedByte(); int cpuInfo4 = stream.readUnsignedByte();
		 * int cpuInfo5 = stream.readUnsignedByte(); String empty1 =
		 * stream.readJagString(); String empty2 = stream.readJagString();
		 * String empty3 = stream.readJagString(); String empty4 =
		 * stream.readJagString(); int unused1 = stream.readUnsignedByte(); int
		 * unused2 = stream.readUnsignedShort(); MachineInformation mInformation
		 * = new MachineInformation(os, x64Arch, osVersion, osVendor,
		 * javaVersion, javaVersionBuild, javaVersionBuild2, hasApplet, heap,
		 * availableProcessors, ram, cpuClockFrequency, cpuInfo3, cpuInfo4,
		 * cpuInfo5);
		 */
		final MachineInformation mInformation = null;
		final int unknown3 = stream.readInt();
		final long userFlow = stream.readLong();
		final boolean hasAditionalInformation = stream.readUnsignedByte() == 1;
		if (hasAditionalInformation) {
			stream.readString(); // aditionalInformation
		}
		final boolean hasJagtheora = stream.readUnsignedByte() == 1;
		final boolean js = stream.readUnsignedByte() == 1;
		final boolean hc = stream.readUnsignedByte() == 1;
		final int unknown4 = stream.readByte();
		final int unknown5 = stream.readInt();
		final String unknown6 = stream.readString();
		final boolean unknown7 = stream.readUnsignedByte() == 1;
		for (int index = 0; index < Cache.STORE.getIndexes().length; index++) {
			final int crc = Cache.STORE.getIndexes()[index] == null ? -1011863738
					: Cache.STORE.getIndexes()[index].getCRC();
			final int receivedCRC = stream.readInt();
			if (crc != receivedCRC && index < 32) {
				/*
				 * Logger.log(this,
				 * "Invalid CRC at index: "+index+", "+receivedCRC+", "+crc);
				 */
				session.getLoginPackets().sendClientPacket(6);
				return;
			}
		}
		if (Utils.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (World.getPlayers().size() >= GameConstants.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if (World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		/*if () {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}*/
		Player player;
		if (!Server.getInstance().getPlayerFileManager().exists(username)) {
			player = new Player(password);
		} else {
			Optional<Player> playerOptional = Server.getInstance().getPlayerFileManager().load(username);
			if (!playerOptional.isPresent()) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			player = playerOptional.get();
			try {
                Server.getInstance().getPlayerFileManager().backupPlayer(username);
            } catch (IOException e) {
			    e.printStackTrace();
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!password.equals(player.getPassword())) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if (player.isPermBanned()
				|| player.getBanned() > Utils.currentTimeMillis()) {
			session.getLoginPackets().sendClientPacket(4);
			return;
		}
		player.setActualPassword(actualPassword);
		player.init(session, username, displayMode, screenWidth, screenHeight,
				mInformation, new IsaacKeyPair(isaacKeys));
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start();
	}

}
