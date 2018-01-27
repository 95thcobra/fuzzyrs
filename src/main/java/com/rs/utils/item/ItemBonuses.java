package com.rs.utils.item;

import com.rs.server.GameConstants;
import com.rs.utils.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class ItemBonuses {

    private final static String PACKED_PATH = GameConstants.DATA_PATH + "/items/bonuses.ib";
    private static HashMap<Integer, int[]> itemBonuses;

    private ItemBonuses() {

    }

    public static void init() {
        if (new File(PACKED_PATH).exists()) {
            loadItemBonuses();
        } else
            throw new RuntimeException("Missing item bonuses.");
    }

    public static int[] getItemBonuses(final int itemId) {
        return itemBonuses.get(itemId);
    }

    private static void loadItemBonuses() {
        try {
            final RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            itemBonuses = new HashMap<>(buffer.remaining() / 38);
            while (buffer.hasRemaining()) {
                final int itemId = buffer.getShort() & 0xffff;
                final int[] bonuses = new int[18];
                for (int index = 0; index < bonuses.length; index++) {
                    bonuses[index] = buffer.getShort();
                }
                itemBonuses.put(itemId, bonuses);
            }
            channel.close();
            in.close();
        } catch (final Throwable e) {
            Logger.handle(e);
        }

    }

}
