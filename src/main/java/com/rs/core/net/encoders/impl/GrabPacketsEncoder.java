package com.rs.core.net.encoders.impl;

import com.rs.server.Server;
import com.rs.core.cache.Cache;
import com.rs.core.net.Session;
import com.rs.core.net.encoders.Encoder;
import com.rs.core.net.io.OutputStream;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import java.io.IOException;

public final class GrabPacketsEncoder extends Encoder {

    private static byte[] UKEYS_FILE;

    private int encryptionValue;

    public GrabPacketsEncoder(final Session connection) {
        super(connection);
    }

    public static OutputStream getUkeysFile() {
        if (UKEYS_FILE == null) {
            UKEYS_FILE = Cache.generateUkeysFile();
        }
        return getContainerPacketData(255, 255, UKEYS_FILE);
    }

    /*
     * only using for ukeys atm, doesnt allow keys encode
     */
    public static OutputStream getContainerPacketData(
            final int indexFileId, final int containerId, final byte[] archive) {
        final OutputStream stream = new OutputStream(archive.length + 4);
        stream.writeByte(indexFileId);
        stream.writeInt(containerId);
        stream.writeByte(0);
        stream.writeInt(archive.length);
        int offset = 10;
        for (final byte element : archive) {
            if (offset == 512) {
                stream.writeByte(255);
                offset = 1;
            }
            stream.writeByte(element);
            offset++;
        }
        return stream;
    }

    public final void sendOutdatedClientPacket() {
        final OutputStream stream = new OutputStream(1);
        stream.writeByte(6);
        final ChannelFuture future = session.write(stream);
        if (future != null) {
            future.addListener(ChannelFutureListener.CLOSE);
        } else {
            session.getChannel().close();
        }
    }

    public final void sendStartUpPacket() {
        final OutputStream stream = new OutputStream(
                1 + GameConstants.GRAB_SERVER_KEYS.length * 4);
        stream.writeByte(0);
        for (final int key : GameConstants.GRAB_SERVER_KEYS) {
            stream.writeInt(key);
        }
        session.write(stream);
    }

    public void sendCacheArchive(int indexId, int containerId, boolean priority) throws IOException {
        int amount = 1;
        if (getRequests().containsKey(indexId + (containerId << 8))) {
            amount = getRequests().get(indexId + (containerId << 8));
            if (++amount > Server.getInstance().getSettingsManager().getSettings().getMaxConnections() * 5) {
                Logger.info(this.getClass(), "Multiple requests for index " + indexId + " from " + session.getIP() + "!");
                return;
            }
        }
        getRequests().put(indexId + (containerId << 8), amount);
        if (indexId == 255 && containerId == 255) {
            OutputStream buffer = getUkeysFile();
            if (buffer == null) {
                Logger.info(this.getClass(), "Buffer is null! IndexId: " + indexId);
                return;
            }
            session.write(buffer);
        } else {
            ChannelBuffer buffer = getArchivePacketData(indexId, containerId, priority);
            if (buffer == null) {
                Logger.info(this.getClass(), "Buffer is null! IndexId: " + indexId);
                return;
            }
            session.write(buffer);
        }
    }

    public final ChannelBuffer getArchivePacketData(final int indexId,
                                                    final int archiveId, final boolean priority) {
        final byte[] archive = indexId == 255 ? Cache.STORE.getIndex255()
                .getArchiveData(archiveId) : Cache.STORE.getIndexes()[indexId]
                .getMainFile().getArchiveData(archiveId);
        if (archive == null)
            return null;
        final int compression = archive[0] & 0xff;
        final int length = ((archive[1] & 0xff) << 24)
                + ((archive[2] & 0xff) << 16) + ((archive[3] & 0xff) << 8)
                + (archive[4] & 0xff);
        int settings = compression;
        if (!priority) {
            settings |= 0x80;
        }
        final ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeByte(indexId);
        buffer.writeInt(archiveId);
        buffer.writeByte(settings);
        buffer.writeInt(length);
        final int realLength = compression != 0 ? length + 4 : length;
        for (int index = 5; index < realLength + 5; index++) {
            if (buffer.writerIndex() % 512 == 0) {
                buffer.writeByte(255);
            }
            buffer.writeByte(archive[index]);
        }
        final int v = encryptionValue;
        if (v != 0) {
            for (int i = 0; i < buffer.arrayOffset(); i++) {
                buffer.setByte(i, buffer.getByte(i) ^ v);
            }
        }
        return buffer;
    }

    public void setEncryptionValue(final int encryptionValue) {
        this.encryptionValue = encryptionValue;
    }

}
