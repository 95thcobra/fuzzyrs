package com.rs.player;

import com.rs.server.GameConstants;
import com.rs.utils.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Info is saved to data/starters.ini
 *
 * @author Emperial
 */
public class StarterMap {

    private static final String path = "./data/starters.ini";
    public static List<String> starters = new ArrayList<String>();
    private static File map = new File(path);

    public static void init() {
        try {
            Logger.info("StarterMap", "Loading Starters");
            final BufferedReader reader = new BufferedReader(
                    new FileReader(map));
            String s;
            while ((s = reader.readLine()) != null) {
                starters.add(s);
            }
            Logger.info("StarterMap", "Loaded Starter map, There are "
                    + starters.size() + " IP's in Configuration");
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        BufferedWriter bf;
        try {
            clearMapFile();
            bf = new BufferedWriter(new FileWriter(path, true));
            for (final String ip : starters) {
                bf.write(ip);
                bf.newLine();
            }
            bf.flush();
            bf.close();
        } catch (final IOException e) {
            System.err.println("Error saving starter map!");
        }
    }

    public static void clearMapFile() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(map);
            writer.print("");
            writer.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    public static void removeIP(final String ip) {
        if (starters.contains(ip)) {
            starters.remove(ip);
        }
        save();
    }

    public static int getCount(final String ip) {
        int count = 0;
        for (final String i : starters) {
            if (i.equals(ip)) {
                count++;
            }
        }
        return count;
    }

    public void addIP(final String ip) {
        if (getCount(ip) >= GameConstants.MAX_STARTER_AMOUNT)
            return;
        starters.add(ip);
        save();
    }

}
