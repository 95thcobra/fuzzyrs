package com.rs.core.net.decoders.impl;

import com.rs.core.net.Session;
import com.rs.core.net.decoders.Decoder;
import com.rs.core.net.io.InputStream;
import com.rs.core.settings.GameConstants;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;

public final class ClientPacketsDecoder extends Decoder {

	public ClientPacketsDecoder(final Session connection) {
		super(connection);
	}

	@Override
	public final void decode(final InputStream stream) {
		session.setDecoder(-1);
		final int packetId = stream.readUnsignedByte();
		switch (packetId) {
		case 14:
			decodeLogin(stream);
			break;
		case 15:
			decodeGrab(stream);
			break;
		default:
			if (SettingsManager.getSettings().DEBUG) {
				Logger.info(this, "PacketId " + packetId);
			}
			session.getChannel().close();
			break;
		}
	}

	private void decodeLogin(final InputStream stream) {
		if (stream.getRemaining() != 0) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(2);
		session.setEncoder(1);
		session.getLoginPackets().sendStartUpPacket();
	}

	private void decodeGrab(final InputStream stream) {
		final int size = stream.readUnsignedByte();
		if (stream.getRemaining() < size) {
			session.getChannel().close();
			return;
		}
		session.setEncoder(0);
		if (stream.readInt() != GameConstants.CLIENT_BUILD
				|| stream.readInt() != GameConstants.CUSTOM_CLIENT_BUILD) {
			session.setDecoder(-1);
			session.getGrabPackets().sendOutdatedClientPacket();
			return;
		}
		if (!stream.readString().equals(GameConstants.GRAB_SERVER_TOKEN)) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(1);
		session.getGrabPackets().sendStartUpPacket();
	}
}
