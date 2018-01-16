package com.rs.core.file.data.npc;

import com.rs.core.file.FuzzyFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * @author John (FuzzyAvacado) on 1/1/2016.
 */
public class NPCSpawnsFileManager {

    private static final String UNPACKED_NPC_SPAWNS_PATH = GameConstants.DATA_PATH + "/npcs/spawns/unpackedSpawns/";
    private static final String PACKED_NPC_SPAWNS_PATH = GameConstants.DATA_PATH + "/npcs/spawns/packedSpawns/";

    public static void init() {
        if (!new File(PACKED_NPC_SPAWNS_PATH).exists()) {
            try {
                packNPCSpawns();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void packNPCSpawns() throws IOException {
        Logger.info("NPCSpawns", "Packing npc spawns...");
        if (!new File(PACKED_NPC_SPAWNS_PATH).mkdir()) {
            throw new RuntimeException("Couldn't create packedSpawns directory.");
        }
        File[] files = new File(UNPACKED_NPC_SPAWNS_PATH).listFiles();
        if (files != null) {
            int npcId, mapAreaNameHash;
            WorldTile tile;
            String[] s1, s2;
            boolean canBeAttackFromOutOfArea;
            for (File f : files) {
                if (f.isDirectory()) {
                    continue;
                }
                List<String> lines = FuzzyFileManager.getFileLines(f);
                for (String line : lines) {
                    s1 = line.split(" - ", 2);
                    if (s1.length != 2) {
                        throw new RuntimeException("Invalid NPC Spawn line: " + line + "File: " + f.getName());
                    }
                    npcId = Integer.parseInt(s1[0]);
                    s2 = s1[1].split(" ", 5);
                    if (s2.length != 3 && s2.length != 5) {
                        throw new RuntimeException("Invalid NPC Spawn line: " + line + "File: " + f.getName());
                    }
                    tile = new WorldTile(Integer.parseInt(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2]));
                    mapAreaNameHash = -1;
                    canBeAttackFromOutOfArea = true;
                    if (s2.length == 5) {
                        mapAreaNameHash = Utils.getNameHash(s2[3]);
                        canBeAttackFromOutOfArea = Boolean.parseBoolean(s2[4]);
                    }
                    addNPCSpawn(npcId, tile.getRegionId(), tile, mapAreaNameHash, canBeAttackFromOutOfArea);
                }
            }
        }
    }

    public static void loadNPCSpawns(final int regionId) {
        final File file = new File(PACKED_NPC_SPAWNS_PATH + regionId + ".ns");
        if (!file.exists())
            return;
        try {
            final RandomAccessFile in = new RandomAccessFile(file, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                final int npcId = buffer.getShort() & 0xffff;
                final int plane = buffer.get() & 0xff;
                final int x = buffer.getShort() & 0xffff;
                final int y = buffer.getShort() & 0xffff;
                final boolean hashExtraInformation = buffer.get() == 1;
                int mapAreaNameHash = -1;
                boolean canBeAttackFromOutOfArea = true;
                if (hashExtraInformation) {
                    mapAreaNameHash = buffer.getInt();
                    canBeAttackFromOutOfArea = buffer.get() == 1;
                }
                NPC npc = World.spawnNPC(npcId, new WorldTile(x, y, plane), mapAreaNameHash, canBeAttackFromOutOfArea);
            }
            channel.close();
            in.close();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
    }

    private static void addNPCSpawn(final int npcId, final int regionId, final WorldTile tile, final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_NPC_SPAWNS_PATH + regionId + ".ns", true))) {
            out.writeShort(npcId);
            out.writeByte(tile.getPlane());
            out.writeShort(tile.getX());
            out.writeShort(tile.getY());
            out.writeBoolean(mapAreaNameHash != -1);
            if (mapAreaNameHash != -1) {
                out.writeInt(mapAreaNameHash);
                out.writeBoolean(canBeAttackFromOutOfArea);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

}
