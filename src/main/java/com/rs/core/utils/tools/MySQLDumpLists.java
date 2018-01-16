package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MySQLDumpLists {

    public static void main(final String[] args) {
        try {
            System.out.println("Dumping...");
            Cache.init();
            dumpItems();
            dumpNPCs();
            dumpObjects();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void dumpItems() throws IOException {
        final BufferedWriter writer = new BufferedWriter(
                new FileWriter(
                        "C:/Users/Nick Hartskeerl/Desktop/RsPsCoding V2.0/includes/data/itemdb.sql",
                        true));
        for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
            final String name = ItemDefinitions.getItemDefinitions(i).getName();
            if (name == null) {
                continue;
            }
            System.out.println("Item: " + i + ", name: " + name + "");
            writer.write("INSERT INTO `itemdb` (`id`, `name`) VALUES (" + i
                    + ", '" + name.replaceAll("'", "") + "');");
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static void dumpNPCs() throws IOException {
        final BufferedWriter writer = new BufferedWriter(
                new FileWriter(
                        "C:/Users/Nick Hartskeerl/Desktop/RsPsCoding V2.0/includes/data/npcdb.sql",
                        true));
        for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
            final String name = NPCDefinitions.getNPCDefinitions(i).name;
            if (name == null) {
                continue;
            }
            System.out.println("NPC: " + i + ", name: " + name + "");
            writer.write("INSERT INTO `npcdb` (`id`, `name`) VALUES (" + i
                    + ", '" + name.replaceAll("'", "") + "');");
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static void dumpObjects() throws IOException {
        final BufferedWriter writer = new BufferedWriter(
                new FileWriter(
                        "C:/Users/Nick Hartskeerl/Desktop/RsPsCoding V2.0/includes/data/objectdb.sql",
                        true));
        for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
            final String name = ObjectDefinitions.getObjectDefinitions(i).name;
            if (name == null) {
                continue;
            }
            System.out.println("Object: " + i + ", name: " + name + "");
            writer.write("INSERT INTO `objectdb` (`id`, `name`) VALUES (" + i
                    + ", '" + name.replaceAll("'", "") + "');");
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
