package com.rs.core.file.data.npc;

import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.world.npc.Drop;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.*;

public class NPCDropsFileManager {

    private final static String PACKED_PATH = GameConstants.DATA_PATH + "/npcs/drops/packedDrops.d";
    private final static String UNPACKED_PATH = GameConstants.DATA_PATH + "/npcs/drops/unpackedDrops.txt";

    private static HashMap<Integer, Drop[]> npcDrops;
    private Map<Integer, ArrayList<Drop>> dropMapx = null;

    public static void init() {
        loadPackedNPCDrops();
    }

    public static Drop[] getDrops(final int npcId) {
        return npcDrops.get(npcId);
    }

    private static void loadPackedNPCDrops() {
        try {
            final RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            final int dropSize = buffer.getShort() & 0xffff;
            npcDrops = new HashMap<>(dropSize);
            for (int i = 0; i < dropSize; i++) {
                final int npcId = buffer.getShort() & 0xffff;
                final Drop[] drops = new Drop[buffer.getShort() & 0xffff];
                for (int d = 0; d < drops.length; d++) {
                    if (buffer.get() == 0) {
                        drops[d] = new Drop(buffer.getShort() & 0xffff,
                                buffer.getDouble(), buffer.getInt(),
                                buffer.getInt(), false);
                    } else {
                        drops[d] = new Drop(0, 0, 0, 0, true);
                    }

                }
                npcDrops.put(npcId, drops);
            }
            channel.close();
            in.close();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
    }

    public Map<Integer, ArrayList<Drop>> getDropArray() {

        if (dropMapx == null) {
            dropMapx = new LinkedHashMap<>();
        }
        for (final int i : npcDrops.keySet()) {
            final ArrayList<Drop> temp = new ArrayList<>();
            Collections.addAll(temp, npcDrops.get(i));
            dropMapx.put(i, temp);
        }

        return dropMapx;
    }

    public void insertDrop(final int npcID, final Drop d) {
        loadPackedNPCDrops();
        final Drop[] oldDrop = npcDrops.get(npcID);
        if (oldDrop == null) {
            npcDrops.put(npcID, new Drop[]{d});
        } else {
            final int length = oldDrop.length;
            final Drop destination[] = new Drop[length + 1];
            System.arraycopy(oldDrop, 0, destination, 0, length);
            destination[length] = d;
            npcDrops.put(npcID, destination);
        }
    }

    public HashMap<Integer, Drop[]> getDropMap() {
        return npcDrops;
    }
}