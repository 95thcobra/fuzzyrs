package com.rs.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AutoBackup {

    public static void init() {
        final File f1 = new File("./data/playersaves/characters/");
        final File f2 = new File(
                "./data/playersaves/charactersfullBackup/mainsave " + getDate()
                        + ".zip");
        if (!f2.exists()) {
            try {
                System.out
                        .println("[Auto-Backup] The mainsave has been automatically backed up.");
                zipDirectory(f1, f2);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out
                    .println("[Auto-Backup] The mainsave has already been backed up today.");
        }
    }

    public static void zipDirectory(final File f, final File zf)
            throws IOException {
        final ZipOutputStream z = new ZipOutputStream(new FileOutputStream(zf));
        zip(f, f, z);
        z.close();
    }

    private static void zip(final File directory, final File base,
                            final ZipOutputStream zos) throws IOException {
        final File[] files = directory.listFiles();
        final byte[] buffer = new byte[8192];
        int read;
        for (final File file : files) {
            if (file.isDirectory()) {
                zip(file, base, zos);
            } else {
                final FileInputStream in = new FileInputStream(file);
                final ZipEntry entry = new ZipEntry(file.getPath().substring(
                        base.getPath().length() + 1));
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
        }
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}