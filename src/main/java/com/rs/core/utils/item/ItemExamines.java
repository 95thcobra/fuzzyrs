package com.rs.core.utils.item;

import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.world.item.Item;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public class ItemExamines {

    private final static HashMap<Integer, String> itemExamines = new HashMap<Integer, String>();
    private final static String PACKED_PATH = GameConstants.DATA_PATH + "/items/examines/packedExamines.e";
    private final static String UNPACKED_PATH = GameConstants.DATA_PATH + "/items/examines/unpackedExamines.txt";

    public static void init() {
        if (new File(PACKED_PATH).exists()) {
            loadPackedItemExamines();
        } else {
            loadUnpackedItemExamines();
        }
    }

    public static String getExamine(final Item item) {
        if (item.getAmount() >= 100000)
            return item.getAmount() + " x " + item.getDefinitions().getName()
                    + ".";
        if (item.getDefinitions().isNoted())
            return "Swap this note at any bank for the equivalent item.";
        final String examine = itemExamines.get(item.getId());
        if (examine != null)
            return examine;
        return "It's an " + item.getDefinitions().getName() + ".";
    }

    private static void loadPackedItemExamines() {
        try {
            final RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                itemExamines.put(buffer.getShort() & 0xffff,
                        readAlexString(buffer));
            }
            channel.close();
            in.close();
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    private static void loadUnpackedItemExamines() {
        Logger.info(ItemExamines.class, "Packing item examines...");
        try {
            final BufferedReader in = new BufferedReader(new FileReader(
                    UNPACKED_PATH));
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(PACKED_PATH));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("//")) {
                    continue;
                }
                line = line.replace("﻿", "");
                final String[] splitedLine = line.split(" - ", 2);
                if (splitedLine.length < 2)
                    throw new RuntimeException(
                            "Invalid list for item examine line: " + line);
                final int itemId = Integer.valueOf(splitedLine[0]);
                if (splitedLine[1].length() > 255) {
                    continue;
                }
                out.writeShort(itemId);
                writeAlexString(out, splitedLine[1]);
                itemExamines.put(itemId, splitedLine[1]);
            }
            in.close();
            out.flush();
            out.close();
        } catch (final IOException e) {
            Logger.handle(e);
        }

    }

    public static String readAlexString(final ByteBuffer buffer) {
        final int count = buffer.get() & 0xff;
        final byte[] bytes = new byte[count];
        buffer.get(bytes, 0, count);
        return new String(bytes);
    }

    public static void writeAlexString(final DataOutputStream out,
                                       final String string) throws IOException {
        final byte[] bytes = string.getBytes();
        out.writeByte(bytes.length);
        out.write(bytes);
    }
}
