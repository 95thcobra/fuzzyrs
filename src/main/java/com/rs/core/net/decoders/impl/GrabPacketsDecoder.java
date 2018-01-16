package com.rs.core.net.decoders.impl;

import com.rs.core.cache.Cache;
import com.rs.core.net.Session;
import com.rs.core.net.decoders.Decoder;
import com.rs.core.net.io.InputStream;

import java.io.IOException;

public final class GrabPacketsDecoder extends Decoder {

	public GrabPacketsDecoder(final Session connection) {
		super(connection);
	}

	@Override
	public final void decode(final InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()) {
			final int packetId = stream.readUnsignedByte();
			if (packetId == 0 || packetId == 1) {
				decodeRequestCacheContainer(stream, packetId == 1);
			} else {
				decodeOtherPacket(stream, packetId);
			}
		}
	}

	private void decodeRequestCacheContainer(final InputStream stream,
			final boolean priority) {
		final int indexId = stream.readUnsignedByte();
		final int archiveId = stream.readInt();
		if (archiveId < 0)
			return;
		if (indexId != 255) {
			if (Cache.STORE.getIndexes().length <= indexId
					|| Cache.STORE.getIndexes()[indexId] == null
					|| !Cache.STORE.getIndexes()[indexId]
							.archiveExists(archiveId))
				return;
		} else if (archiveId != 255)
			if (Cache.STORE.getIndexes().length <= archiveId
			|| Cache.STORE.getIndexes()[archiveId] == null)
				return;
		try {
			session.getGrabPackets().sendCacheArchive(indexId, archiveId, priority);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void decodeOtherPacket(final InputStream stream,
			final int packetId) {
		if (packetId == 7) {
			session.getChannel().close();
			return;
		}
		if (packetId == 4) {
			session.getGrabPackets().setEncryptionValue(
					stream.readUnsignedByte());
			if (stream.readUnsignedShort() != 0) {
				session.getChannel().close();
			}
		} else {
			stream.skip(11); // for some reason 11bytes instead of 3 on 718+
								// protocol
		}
	}
}
