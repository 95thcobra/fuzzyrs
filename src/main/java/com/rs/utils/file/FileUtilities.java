package com.rs.utils.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 'Mystic Flow
 */
public class FileUtilities {

    public static final int BUFFER = 1024;

    public static boolean exists(final String name) {
        final File file = new File(name);
        return file.exists();
    }

    public static ByteBuffer fileBuffer(final String name) throws IOException {
        final File file = new File(name);
        if (!file.exists())
            return null;
        FileInputStream in = new FileInputStream(name);

        final byte[] data = new byte[BUFFER];
        int read;
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(in.available() + 1);
            while ((read = in.read(data, 0, BUFFER)) != -1) {
                buffer.put(data, 0, read);
            }
            buffer.flip();
            return buffer;
        } finally {
            if (in != null) {
                in.close();
            }
            in = null;
        }
    }

    public static void writeBufferToFile(final String name,
                                         final ByteBuffer buffer) throws IOException {
        final File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
        }
        final FileOutputStream out = new FileOutputStream(name);
        out.write(buffer.array(), 0, buffer.remaining());
        out.flush();
        out.close();
    }

    public static List<String> readLines(String location) {
        final List<String> fileLines = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//"))
                    continue;
                fileLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLines;
    }

}
