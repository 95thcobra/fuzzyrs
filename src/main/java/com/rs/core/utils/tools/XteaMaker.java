package com.rs.core.utils.tools;

import java.io.*;

public class XteaMaker {

    public static void main(final String[] args) {
        try {
            final BufferedReader stream = new BufferedReader(
                    new InputStreamReader(new FileInputStream("xtea650.txt")));
            while (true) {
                final String line = stream.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("--")) {
                    continue;
                }
                final String[] spaceSplitLine = line.split(" ");
                final int regionId = Integer.valueOf(spaceSplitLine[0]);
                final String[] xteaSplit = spaceSplitLine[3].split("\\.");
                /*
				 * for(byte c : spaceSplitLine[3].getBytes()) {
				 * System.out.println(c); System.out.println((char) c); }
				 */

                if (xteaSplit[0].equals("0") && xteaSplit[1].equals("0")
                        && xteaSplit[2].equals("0") && xteaSplit[3].equals("0")) {
                    continue;
                }
                final BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(
                                "convertedXtea/" + regionId + ".txt")));
                for (final String xtea : xteaSplit) {
                    writer.append(xtea);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
