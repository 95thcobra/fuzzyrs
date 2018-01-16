package com.rs.core.file.data.map;

import com.rs.core.file.FuzzyFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;

public final class ObjectSpawnsFileManager {

    private static final String UNPACKED_OBJECT_SPAWNS = GameConstants.DATA_PATH + "/map/objects/unpackedSpawns/";
    private static final String PACKED_OBJECT_SPAWNS = GameConstants.DATA_PATH + "/map/objects/packedSpawns/";

    public static void init() {
        if (!new File(PACKED_OBJECT_SPAWNS).exists()) {
            try {
                packObjectSpawns();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void packObjectSpawns() throws IOException {
        Logger.info("ObjectSpawnsFileManager", "Packing object spawns...");
        if (!new File(PACKED_OBJECT_SPAWNS).mkdir()) {
            throw new RuntimeException("Couldn't create packedSpawns directory.");
        }
        File[] files = new File(UNPACKED_OBJECT_SPAWNS).listFiles();
        if (files != null) {
            int objectId, type, rotation;
            WorldTile tile;
            String[] s1, s2, s3;
            for (File f : files) {
                if (f.isDirectory()) {
                    continue;
                }
                List<String> lines = FuzzyFileManager.getFileLines(f);
                for (String line : lines) {
                    s1 = line.split(" - ");
                    if (s1.length != 2)
                        throw new RuntimeException("Invalid Object Spawn line: " + line);
                    s2 = s1[0].split(" ");
                    s3 = s1[1].split(" ");
                    if (s2.length != 3 || s3.length != 4)
                        throw new RuntimeException("Invalid Object Spawn line: " + line);
                    objectId = Integer.parseInt(s2[0]);
                    type = Integer.parseInt(s2[1]);
                    rotation = Integer.parseInt(s2[2]);

                    tile = new WorldTile(Integer.parseInt(s3[0]), Integer.parseInt(s3[1]), Integer.parseInt(s3[2]));
                    addObjectSpawn(objectId, type, rotation, tile.getRegionId(),
                            tile, Boolean.parseBoolean(s3[3]));
                }
            }
        }
    }

    public static void loadObjectSpawns(final int regionId) {
        final File file = new File(PACKED_OBJECT_SPAWNS + regionId + ".os");
        if (!file.exists())
            return;
        try {
            final RandomAccessFile in = new RandomAccessFile(file, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                final int objectId = buffer.getShort() & 0xffff;
                final int type = buffer.get() & 0xff;
                final int rotation = buffer.get() & 0xff;
                final int plane = buffer.get() & 0xff;
                final int x = buffer.getShort() & 0xffff;
                final int y = buffer.getShort() & 0xffff;
                final boolean cliped = buffer.get() == 1;
                World.spawnObject(new WorldObject(objectId, type, rotation, x,
                        y, plane), cliped);
            }
            channel.close();
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void addObjectSpawn(final int objectId,
                                       final int type, final int rotation, final int regionId,
                                       final WorldTile tile, final boolean cliped) {
        try {
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(PACKED_OBJECT_SPAWNS + regionId
                            + ".os", true));
            out.writeShort(objectId);
            out.writeByte(type);
            out.writeByte(rotation);
            out.writeByte(tile.getPlane());
            out.writeShort(tile.getX());
            out.writeShort(tile.getY());
            out.writeBoolean(cliped);
            out.flush();
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
