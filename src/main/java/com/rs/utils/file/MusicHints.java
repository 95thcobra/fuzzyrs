package com.rs.utils.file;

import com.rs.server.GameConstants;
import com.rs.utils.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public class MusicHints {

    private final static HashMap<Integer, String> musicHints = new HashMap<Integer, String>();
    private final static String PACKED_PATH = GameConstants.DATA_PATH + "/musics/packedMusicHints.mh";
    private final static String UNPACKED_PATH = GameConstants.DATA_PATH + "/musics/unpackedMusicHints.txt";

    public static void init() {
        if (new File(PACKED_PATH).exists()) {
            loadPackedItemExamines();
        } else {
            loadUnpackedItemExamines();
        }
    }

    public static String getHint(final int musicId) {
        final String hint = musicHints.get(musicId);
        if (hint == null)
            return "somewhere.";
        return hint;
    }

    private static void loadPackedItemExamines() {
        try {
            final RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                musicHints.put(buffer.getShort() & 0xffff,
                        readAlexString(buffer));
            }
            channel.close();
            in.close();
        } catch (final Throwable e) {
            Logger.handle(e);
        }
    }

    private static void loadUnpackedItemExamines() {
        Logger.info("MusicHints", "Packing music hints...");
        try {
            final BufferedReader in = new BufferedReader(new FileReader(
                    UNPACKED_PATH));
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
                if (splitedLine.length < 2)
                    throw new RuntimeException(
                            "Invalid list for music hints line: " + line);
                final int musicId = Integer.valueOf(splitedLine[0]);
                if (splitedLine[1].length() > 255) {
                    continue;
                }
                out.writeShort(musicId);
                writeAlexString(out, splitedLine[1]);
                musicHints.put(musicId, splitedLine[1]);
            }
            in.close();
            out.flush();
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
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
