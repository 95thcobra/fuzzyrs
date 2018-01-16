package com.rs.core.file.data.map;

import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.world.WorldTile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class MapAreas {

    private final static HashMap<Integer, int[]> mapAreas = new HashMap<>();
    private final static String PACKED_PATH = GameConstants.DATA_PATH + "/map/packedMapAreas.ma";
    private final static Object lock = new Object();

    private MapAreas() {

    }

    public static void init() {
        if (new File(PACKED_PATH).exists()) {
            loadPackedMapAreas();
        } else {
            loadUnpackedMapAreas();
        }
    }

    public static boolean isAtArea(final String areaName,
                                   final WorldTile tile) {
        return isAtArea(Utils.getNameHash(areaName), tile);
    }

    public static boolean isAtArea(final int areaNameHash,
                                   final WorldTile tile) {
        final int[] coordsList = mapAreas.get(areaNameHash);
        if (coordsList == null)
            return false;
        int index = 0;
        while (index < coordsList.length) {
            if (tile.getPlane() == coordsList[index]
                    && tile.getX() >= coordsList[index + 1]
                    && tile.getX() <= coordsList[index + 2]
                    && tile.getY() >= coordsList[index + 3]
                    && tile.getY() <= coordsList[index + 4])
                return true;
            index += 5;
        }
        return false;
    }

    public static void removeArea(final int areaNameHash) {
        mapAreas.remove(areaNameHash);
    }

    public static void addArea(final int areaNameHash,
                               final int[] coordsList) {
        mapAreas.put(areaNameHash, coordsList);
    }

    public static int getRandomAreaHash() {
        synchronized (lock) {
            while (true) {
                long id = Utils.getRandom(Integer.MAX_VALUE)
                        + Utils.getRandom(Integer.MAX_VALUE);
                id -= Integer.MIN_VALUE;
                if (id != -1 && !mapAreas.containsKey((int) id))
                    return (int) id;
            }
        }
    }

    private static void loadUnpackedMapAreas() {
        Logger.info("MapAreas", "Packing map areas...");
        try {
            final BufferedReader in = new BufferedReader(new FileReader(
                    GameConstants.DATA_PATH + "/map/unpackedMapAreas.txt"));
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(PACKED_PATH));
            while (true) {
                final String line = in.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("//")) {
                    continue;
                }
                final String[] splitedLine = line.split(" - ", 2);
                final String areaName = splitedLine[0];
                final String[] splitedCoords = splitedLine[1].split(" ");
                final int[] coordsList = new int[splitedCoords.length];
                if (coordsList.length < 5)
                    throw new RuntimeException("Invalid list for area line: "
                            + line);
                for (int i = 0; i < coordsList.length; i++) {
                    coordsList[i] = Integer.parseInt(splitedCoords[i]);
                }
                final int areaNameHash = Utils.getNameHash(areaName);
                if (mapAreas.containsKey(areaNameHash)) {
                    continue;
                }
                out.writeInt(areaNameHash);
                out.writeByte(coordsList.length);
                for (final int element : coordsList) {
                    out.writeShort(element);
                }
                mapAreas.put(areaNameHash, coordsList);
            }
            in.close();
            out.flush();
            out.close();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
    }

    private static void loadPackedMapAreas() {
        try {
            final RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                final int areaNameHash = buffer.getInt();
                final int[] coordsList = new int[buffer.get() & 0xff];
                for (int i = 0; i < coordsList.length; i++) {
                    coordsList[i] = buffer.getShort() & 0xffff;
                }
                mapAreas.put(areaNameHash, coordsList);
            }
            channel.close();
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
