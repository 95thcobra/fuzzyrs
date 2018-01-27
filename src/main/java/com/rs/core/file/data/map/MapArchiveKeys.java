package com.rs.core.file.data.map;

import com.rs.server.GameConstants;
import com.rs.utils.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class MapArchiveKeys {

    private final static HashMap<Integer, int[]> keys = new HashMap<Integer, int[]>();
    private final static String PACKED_PATH = GameConstants.DATA_PATH + "/map/archiveKeys/packed.mcx";

    public static int[] getMapKeys(final int regionId) {
        return keys.get(regionId);
    }

    public static void init() {
        if (new File(PACKED_PATH).exists()) {
            loadPackedKeys();
        } else {
            loadUnpackedKeys();
        }
    }

    private static void loadPackedKeys() {
        try {
            final RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                final int regionId = buffer.getShort() & 0xffff;
                final int[] xteas = new int[4];
                for (int index = 0; index < 4; index++) {
                    xteas[index] = buffer.getInt();
                }
                keys.put(regionId, xteas);
            }
            channel.close();
            in.close();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
    }

    public static void loadUnpackedKeys() {
        Logger.info("MapArchiveKeys", "Packing map containers xteas...");
        try {
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(PACKED_PATH));
            final File unpacked = new File(GameConstants.DATA_PATH + "/map/archiveKeys/unpacked/");
            final File[] xteasFiles = unpacked.listFiles();
            for (final File region : xteasFiles) {
                final String name = region.getName();
                if (!name.contains(".txt")) {
                    region.delete();
                    continue;
                }
                final int regionId = Short.parseShort(name.replace(".txt", ""));
                if (regionId <= 0) {
                    region.delete();
                    continue;
                }
                final BufferedReader in = new BufferedReader(new FileReader(
                        region));
                out.writeShort(regionId);
                final int[] xteas = new int[4];
                for (int index = 0; index < 4; index++) {
                    xteas[index] = Integer.parseInt(in.readLine());
                    out.writeInt(xteas[index]);
                }
                keys.put(regionId, xteas);
                in.close();
            }
            out.flush();
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
